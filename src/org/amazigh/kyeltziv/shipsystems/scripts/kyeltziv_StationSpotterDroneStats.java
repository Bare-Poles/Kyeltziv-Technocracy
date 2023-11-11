package org.amazigh.kyeltziv.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class kyeltziv_StationSpotterDroneStats extends BaseShipSystemScript {

	public static final float SENSOR_RANGE_PERCENT = 30f;
	public static final float WEAPON_RANGE_PERCENT = 10f;
	public static final float BALL_VEL_PERCENT = 10f;
	public static final float BALL_FLAT_BONUS = 50f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float sensorRangePercent = SENSOR_RANGE_PERCENT * effectLevel;
		float weaponRangePercent = WEAPON_RANGE_PERCENT * effectLevel;
		float velPercent = BALL_VEL_PERCENT * effectLevel;
		float flatRange = BALL_FLAT_BONUS * effectLevel;
		
		stats.getSightRadiusMod().modifyPercent(id, sensorRangePercent);
		
		stats.getBallisticWeaponRangeBonus().modifyPercent(id, weaponRangePercent);
		stats.getEnergyWeaponRangeBonus().modifyPercent(id, weaponRangePercent);
		stats.getBallisticProjectileSpeedMult().modifyPercent(id, velPercent);
		
		stats.getBallisticWeaponRangeBonus().modifyFlat(id, flatRange);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getSightRadiusMod().unmodify(id);
		
		stats.getBallisticWeaponRangeBonus().unmodify(id);
		stats.getEnergyWeaponRangeBonus().unmodify(id);
		stats.getBallisticProjectileSpeedMult().unmodify(id);
	}
}
