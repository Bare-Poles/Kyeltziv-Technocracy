package org.amazigh.kyeltziv.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class kyeltziv_symDroneStats extends BaseShipSystemScript {

	public static final float DAMAGE_PERCENT = 3f;
	public static final float SENSOR_RANGE_PERCENT = 8f;
	public static final float WEAPON_RANGE_PERCENT = 3f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float damagePercent = DAMAGE_PERCENT * effectLevel;
		float sensorRangePercent = SENSOR_RANGE_PERCENT * effectLevel;
		float weaponRangePercent = WEAPON_RANGE_PERCENT * effectLevel;
		
		stats.getHitStrengthBonus().modifyPercent(id, damagePercent);
		stats.getDamageToTargetShieldsMult().modifyPercent(id, damagePercent);
		
		stats.getSightRadiusMod().modifyPercent(id, sensorRangePercent);
		
		stats.getBallisticWeaponRangeBonus().modifyPercent(id, weaponRangePercent);
		stats.getEnergyWeaponRangeBonus().modifyPercent(id, weaponRangePercent);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		
		stats.getHitStrengthBonus().unmodify(id);
		stats.getSightRadiusMod().unmodify(id);
		stats.getBallisticWeaponRangeBonus().unmodify(id);
		stats.getEnergyWeaponRangeBonus().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float hitStrengthPercent = DAMAGE_PERCENT * effectLevel;
		float sensorRangePercent = SENSOR_RANGE_PERCENT * effectLevel;
		float weaponRangePercent = WEAPON_RANGE_PERCENT * effectLevel;
		if (index == 0) {
			return new StatusData("weapon hit strength and shield damage +" + (int) hitStrengthPercent + "%", false);
		} else if (index == 1) {
			return new StatusData("sensor range +" + (int) sensorRangePercent + "%", false);
		} else if (index == 2) {
			return new StatusData("weapon range +" + (int) weaponRangePercent + "%", false);
		}
		return null;
	}
}
