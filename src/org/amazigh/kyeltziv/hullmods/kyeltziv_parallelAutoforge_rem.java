package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;

public class kyeltziv_parallelAutoforge_rem extends BaseHullMod {
	
	public static final float REFIT_MULT = 25f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getFighterRefitTimeMult().modifyMult(id, 1f - (REFIT_MULT * 0.01f));
	}
    
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) REFIT_MULT + "%";
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
