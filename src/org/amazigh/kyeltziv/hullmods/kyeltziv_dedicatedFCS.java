package org.amazigh.kyeltziv.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class kyeltziv_dedicatedFCS extends BaseHullMod {

	public static final float DAMAGE_MALUS = 30f;
	public static final float FLUX_BONUS = 15f;
	
public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		stats.getDamageToCapital().modifyMult(id, 1f - (DAMAGE_MALUS / 100f));
		stats.getDamageToCruisers().modifyMult(id, 1f - (DAMAGE_MALUS / 100f));
		stats.getDamageToDestroyers().modifyMult(id, 1f - (DAMAGE_MALUS / 100f));
		stats.getDamageToFrigates().modifyMult(id, 1f - (DAMAGE_MALUS / 100f));
		
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_BONUS / 100f));
		stats.getEnergyWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_BONUS / 100f));
		stats.getMissileWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_BONUS / 100f));
	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) DAMAGE_MALUS + "%";
		if (index == 1) return "" + (int) FLUX_BONUS + "%";
		return null;
	}
	
    @Override
    public Color getBorderColor() {
        return new Color(197,207,169,200); //189,42,30,150
    }

    @Override
    public Color getNameColor() {
        return new Color(197,207,169,255);
    }
	
}
