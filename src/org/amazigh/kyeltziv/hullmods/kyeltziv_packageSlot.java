package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class kyeltziv_packageSlot extends BaseHullMod {
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		// so this is just a hidden dummy hullmod that does nothing, but it is checked for by the package hullmods to determine if the ship is valid to mount them
		// not managing the packages by rangefinders, because the phaeton/valkyrie have rangefinders and shouldn't have access to packages.
	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		return null;
	}	
}
