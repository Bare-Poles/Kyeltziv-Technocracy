package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

import java.awt.Color;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_supportPackage extends BaseHullMod {

	public static final float ARC_BONUS = 20f;
	public static final float HARD_VENT = 10f;
	
	public static final float PD_DAMAGE_BONUS = 25f;
	
	public static final float PD_RANGE_BONUS = 200f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		stats.getShieldArcBonus().modifyFlat(id, ARC_BONUS);
		stats.getHardFluxDissipationFraction().modifyFlat(id, (float)HARD_VENT * 0.01f);
		stats.getDamageToMissiles().modifyPercent(id, PD_DAMAGE_BONUS);
		stats.getDamageToFighters().modifyPercent(id, PD_DAMAGE_BONUS);
		
		stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 1f);
	}

	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		MutableShipStatsAPI stats = ship.getMutableStats();
		
		float FLUX_USAGE = 1 - ship.getFluxLevel();
		
		stats.getEnergyWeaponRangeBonus().modifyFlat(spec.getId(), PD_RANGE_BONUS * FLUX_USAGE);
		
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			Global.getCombatEngine().maintainStatusForPlayerShip("KPDRANGE", "graphics/icons/hullsys/kyeltziv_rangefinder.png", "Support Package Bonus ", Math.round(PD_RANGE_BONUS * FLUX_USAGE) + "", false);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		return null;
	}
	
	@Override
	public boolean shouldAddDescriptionToTooltip(HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
		return false;
	}

	@Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		float pad = 2f;
		float opad = 10f;
		
		Color h = Misc.getHighlightColor();
		Color bad = Misc.getNegativeHighlightColor();
		
		LabelAPI label = tooltip.addPara("A series of modifications exclusive to Kyeltziv Technocracy vessels that optimise the ship for a supporting role.", opad);
		
		label = tooltip.addPara("Increases the ship's shield arc by %s.", pad, h, "" + (int)ARC_BONUS);
		label.setHighlight("" + (int)ARC_BONUS);
		label.setHighlightColors(h);
		label = tooltip.addPara("Allows the ship to dissipate hard flux at %s of the normal rate while shields are on.", pad, h, "" + (int)HARD_VENT + "%");
		label.setHighlight("" + (int)HARD_VENT + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Increases damage dealt to missiles and fighters by %s.", pad, h, "" + (int)PD_DAMAGE_BONUS + "%");
		label.setHighlight("" + (int)PD_DAMAGE_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("All point-defense weapons get the best possible target leading, regardless of combat readiness.", pad);
		
		label = tooltip.addPara("Energy weapons recieve the following bonuses:", opad);
		label = tooltip.addPara("Range increased by %s when at 0 flux, this bonus drops off as flux levels increase, and reaches %s when flux is full.", pad, h, "" + (int)PD_RANGE_BONUS, "0");
		label.setHighlight("" + (int)PD_RANGE_BONUS, "0");
		label.setHighlightColors(h, bad);
		
		label = tooltip.addPara("May only be installed on Kyeltziv Technocracy combat vessels, and only one modification package may be installed at a time.", opad);
		
	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot") && !ship.getVariant().getHullMods().contains("kyeltziv_pack_ass") && !ship.getVariant().getHullMods().contains("kyeltziv_pack_siege");
	}
	
	public boolean showInRefitScreenModPickerFor(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot");
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("kyeltziv_pack_ass") || ship.getVariant().getHullMods().contains("kyeltziv_pack_siege")) {
			return "May only install one modification package at a time.";
		}
		if (!ship.getVariant().getHullMods().contains("kyeltziv_pack_slot")) {
			return "Only compatible with Kyeltziv Technocracy non-phase combat vessels.";
		}
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
