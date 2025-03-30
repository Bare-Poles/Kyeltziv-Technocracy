package org.amazigh.kyeltziv.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class kyeltziv_NavigationDroneStats extends BaseShipSystemScript {

	public static final float NAV_VALUE = 1f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float navScaled = NAV_VALUE * effectLevel;
		
		stats.getDynamic().getMod(Stats.COORDINATED_MANEUVERS_FLAT).modifyFlat(id, (Float) navScaled);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		
		stats.getHitStrengthBonus().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float ecmScaled = NAV_VALUE * effectLevel;
		if (index == 0) {
			return new StatusData("Nav Rating increased by " + (int) ecmScaled + "%", false);
		}
		return null;
	}
}