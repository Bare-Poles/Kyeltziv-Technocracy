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
		
		int dam = (int) (100f * (DAMAGE_BONUS -1f));
		
		if (index == 0) return "" + (int) AMMO_BONUS + "%";
		if (index == 1) return "" + (int) REGEN_BONUS + "%";
		if (index == 2) return "" + dam + "%";
		return null;
	}
}
