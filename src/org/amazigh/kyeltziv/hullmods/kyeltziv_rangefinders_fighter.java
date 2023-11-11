package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class kyeltziv_rangefinders_fighter extends BaseHullMod {

	public static final float RANGE_BONUS = 45f;

	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		MutableShipStatsAPI stats = ship.getMutableStats();
		
		float FLUX_USAGE = 1 - ship.getFluxLevel();
		
		stats.getBallisticWeaponRangeBonus().modifyMult(spec.getId(), 1.0f + ((RANGE_BONUS * 0.01f) * FLUX_USAGE));
		
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			Global.getCombatEngine().maintainStatusForPlayerShip("KRANGEF", "graphics/icons/hullsys/kyeltziv_rangefinder.png", "Rangefinder Bonus ", Math.round(RANGE_BONUS * FLUX_USAGE) + "%", false);
		}
	}


	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) RANGE_BONUS + "%";
		return null;
	}
	
}
