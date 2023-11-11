package org.amazigh.kyeltziv.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;

public class CHM_kyeltziv extends BaseHullMod {
    private static final Map coom = new HashMap();
    static {
        coom.put(HullSize.FIGHTER, 12f);
        coom.put(HullSize.FRIGATE, 12f);
        coom.put(HullSize.DESTROYER, 10f);
        coom.put(HullSize.CRUISER, 8f);
        coom.put(HullSize.CAPITAL_SHIP, 6f);
        coom.put(HullSize.DEFAULT, 10f);
    }
	//This above is kinda important, you have to define HullSize.FIGHTER and HullSize.DEFAULT because for some reason people are spawning old precursor fighters and the mod is randomly summoning these cringe gargoyles and CTDing the game. If you don't want them to get the bonus, I would just set it to 0f or something...
    public static final float EMP_MALUS = 15f;

    @Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
    	stats.getEmpDamageTakenMult().modifyMult(id, 1f + EMP_MALUS * 0.01f);
	}
    
	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		
		MutableShipStatsAPI stats = ship.getMutableStats();
		
		float DISS_BONUS = (Float) coom.get(ship.getHullSize());
		float RATIO = 1f / 0.9f;
		float DISS_RATE = 0f;
		
		if (ship.getFluxLevel() * RATIO >= 1f) {
			DISS_RATE = 1f;
		} else {
			DISS_RATE = ship.getFluxLevel() * RATIO;
		}
		
		stats.getFluxDissipation().modifyMult(spec.getId(), 1f + DISS_RATE * DISS_BONUS * 0.01f);
	}
    
    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
    	if (index == 0) return "90%";
        if (index == 1) return "" + ((Float) coom.get(HullSize.FRIGATE)).intValue() + "%";
        if (index == 2) return "" + ((Float) coom.get(HullSize.DESTROYER)).intValue() + "%";
        if (index == 3) return "" + ((Float) coom.get(HullSize.CRUISER)).intValue() + "%";
        if (index == 4) return "" + ((Float) coom.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
        if (index == 5) return "" + (int) EMP_MALUS + "%";
        return null;
    }

	//Oh these are cool colors below introduced in 0.95a, to match with your tech type and stuff. Just nice to have!

    @Override
    public Color getBorderColor() {
        return new Color(197,207,169,200); //189,42,30,150
    }

    @Override
    public Color getNameColor() {
        return new Color(197,207,169,255);
    }
}
