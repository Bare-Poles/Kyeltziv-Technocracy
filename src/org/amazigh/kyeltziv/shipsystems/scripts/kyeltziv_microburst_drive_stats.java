package org.amazigh.kyeltziv.shipsystems.scripts;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

public class kyeltziv_microburst_drive_stats extends BaseShipSystemScript {

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
	  
	  ShipAPI ship0 = (ShipAPI)stats.getEntity();
	  
		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getMaxSpeed().modifyFlat(id, 100f * effectLevel);
			stats.getAcceleration().modifyPercent(id, 200f * effectLevel);
			stats.getAcceleration().modifyPercent(id, 50f * effectLevel);
			stats.getDeceleration().modifyPercent(id, 20f * effectLevel);
			stats.getTurnAcceleration().modifyPercent(id, 400f * effectLevel);
			stats.getMaxTurnRate().modifyPercent(id, 200f);
			
			// this is to make the craft "jitter" when the system is active
			float jitterStrength = effectLevel * 3.5f;
			Vector2f randomJitter = MathUtils.getRandomPointInCircle(null, jitterStrength);
			Vector2f jitterMeme = ship0.getLocation();
			jitterMeme.x += randomJitter.x;
			jitterMeme.y += randomJitter.y;
			
		}
		
		if (stats.getEntity() instanceof ShipAPI && false) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			String key = ship.getId() + "_" + id;
			Object test = Global.getCombatEngine().getCustomData().get(key);
			if (state == State.IN) {
				if (test == null && effectLevel > 0.2f) {
					Global.getCombatEngine().getCustomData().put(key, new Object());
					ship.getEngineController().getExtendLengthFraction().advance(1f);
					for (ShipEngineAPI engine : ship.getEngineController().getShipEngines()) {
						if (engine.isSystemActivated()) {
							ship.getEngineController().setFlameLevel(engine.getEngineSlot(), 1f);
						}
					}
				}
			} else {
				Global.getCombatEngine().getCustomData().remove(key);
			}
		}
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getDeceleration().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("improved maneuverability", false);
		} else if (index == 1) {
			return new StatusData("+100 top speed", false);
		}
		return null;
	}
}
