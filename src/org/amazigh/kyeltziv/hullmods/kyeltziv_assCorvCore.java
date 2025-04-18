package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.util.IntervalUtil;

public class kyeltziv_assCorvCore extends BaseHullMod {

	public static final float DAMAGE_BONUS = 40f;
	private final IntervalUtil interval = new IntervalUtil(0.1f, 0.1f);
	private static final float HULL_REPAIR = 15f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getDamageToTargetWeaponsMult().modifyPercent(id, DAMAGE_BONUS);
	}

	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		
		float hullRatio = (ship.getHitpoints() / ship.getMaxHitpoints());
		
		if (hullRatio <= 0.5f) {
			interval.advance(amount);
	        if (interval.intervalElapsed()) {
	            ship.setHitpoints(Math.min(ship.getHitpoints() + (interval.getIntervalDuration() * HULL_REPAIR * (1f - hullRatio)), ship.getMaxHitpoints()));
	        }
		}
	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) DAMAGE_BONUS + "%";
		if (index == 1) return "50%";
		if (index == 2) return "150";
		return null;
	}
}
