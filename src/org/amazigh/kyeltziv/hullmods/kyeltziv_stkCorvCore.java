package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class kyeltziv_stkCorvCore extends BaseHullMod {

	public static final float DAMAGE_BONUS = 25f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getDamageToTargetShieldsMult().modifyPercent(id, DAMAGE_BONUS);
		stats.getHitStrengthBonus().modifyPercent(id, DAMAGE_BONUS);
	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		return null;
	}
}
