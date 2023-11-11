package org.amazigh.kyeltziv.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class kyeltziv_PiperDroneStats extends BaseShipSystemScript {

	public static final float ECM_VALUE = 1f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float ecmScaled = ECM_VALUE * effectLevel;
		
		stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).modifyFlat(id, (Float) ecmScaled);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		
		stats.getHitStrengthBonus().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float ecmScaled = ECM_VALUE * effectLevel;
		if (index == 0) {
			return new StatusData("ECM Rating increased by " + (int) ecmScaled + "%", false);
		}
		return null;
	}
}
