package org.amazigh.kyeltziv.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class kyeltziv_HeavyTargetingDroneStats extends BaseShipSystemScript {

	public static final float ARMOUR_DAMAGE_PERCENT = 10f;
	public static final float BALL_VEL_PERCENT = 5f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float hitStrengthPercent = ARMOUR_DAMAGE_PERCENT * effectLevel;
		float velPercent = BALL_VEL_PERCENT * effectLevel;
		
		stats.getHitStrengthBonus().modifyPercent(id, hitStrengthPercent);
		stats.getBallisticProjectileSpeedMult().modifyPercent(id, velPercent);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		
		stats.getHitStrengthBonus().unmodify(id);
		stats.getBallisticProjectileSpeedMult().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float hitStrengthPercent = ARMOUR_DAMAGE_PERCENT * effectLevel;
		float velPercent = BALL_VEL_PERCENT * effectLevel;
		
		if (index == 0) {
			return new StatusData("ballistic projectile velocity +" + (int) velPercent + "%, and all weapon hit strength +" + (int) hitStrengthPercent + "%", false);
		}
		return null;
	}
}
