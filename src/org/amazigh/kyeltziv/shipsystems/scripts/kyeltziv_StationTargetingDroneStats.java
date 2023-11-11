package org.amazigh.kyeltziv.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class kyeltziv_StationTargetingDroneStats extends BaseShipSystemScript {

	public static final float BONUS_PERCENT = 15f;
	public static final float MSSL_PERCENT = 5f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float hitStrengthPercent = BONUS_PERCENT * effectLevel;
		float msslPercent = MSSL_PERCENT * effectLevel;
		
		stats.getHitStrengthBonus().modifyPercent(id, hitStrengthPercent);
		stats.getDamageToTargetShieldsMult().modifyPercent(id, hitStrengthPercent);
		
		stats.getMissileAccelerationBonus().modifyPercent(id, msslPercent);
		stats.getMissileMaxTurnRateBonus().modifyPercent(id, msslPercent);
		stats.getMissileTurnAccelerationBonus().modifyPercent(id, msslPercent);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		
		stats.getHitStrengthBonus().unmodify(id);
		stats.getDamageToTargetShieldsMult().unmodify(id);
		
		stats.getMissileAccelerationBonus().unmodify(id);
		stats.getMissileMaxTurnRateBonus().unmodify(id);
		stats.getMissileTurnAccelerationBonus().unmodify(id);
	}
}
