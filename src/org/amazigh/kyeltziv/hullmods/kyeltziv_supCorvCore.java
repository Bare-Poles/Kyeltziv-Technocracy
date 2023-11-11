package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class kyeltziv_supCorvCore extends BaseHullMod {

	public static final float AMMO_BONUS = 50f;
	public static final float REGEN_BONUS = 25f;
	public static final float DAMAGE_BONUS = 1.2f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getEnergyAmmoBonus().modifyPercent(id, AMMO_BONUS);
		stats.getEnergyAmmoRegenMult().modifyPercent(id, REGEN_BONUS);
		stats.getEnergyWeaponDamageMult().modifyMult(id, DAMAGE_BONUS);
	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		return null;
	}
}
