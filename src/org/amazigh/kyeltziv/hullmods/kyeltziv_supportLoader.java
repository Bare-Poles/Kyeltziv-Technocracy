package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_supportLoader extends BaseHullMod {
	
	public static final float RECHARGE_BONUS = 25f;
	
	public static final float MISSILE_RATE_BONUS = 20f;
	public static final float MISSILE_D_BONUS = 10f;
	public static final float MISSILE_H_BONUS = 20f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		stats.getSystemRegenBonus().modifyPercent(id, RECHARGE_BONUS);
		
		stats.getMissileRoFMult().modifyMult(id, 1f + (MISSILE_RATE_BONUS * 0.01f));
		stats.getMissileAmmoRegenMult().modifyMult(id, 1f + (MISSILE_RATE_BONUS * 0.01f));
		
		stats.getMissileWeaponDamageMult().modifyMult(id, 1f + (MISSILE_D_BONUS * 0.01f));
		stats.getMissileHealthBonus().modifyMult(id, 1f + (MISSILE_H_BONUS * 0.01f));
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
		
		LabelAPI label = tooltip.addPara("A customised autoforge loading system exclusive to Kyeltziv Technocracy vessels that improves drone rebuild rate and overall missile performance.", opad);
		
		label = tooltip.addPara("Increases shipsystem drone rebuild rate by %s.", opad, h, "" + (int)RECHARGE_BONUS + "%");
		label.setHighlight("" + (int)RECHARGE_BONUS + "%");
		label.setHighlightColors(h);
		
		label = tooltip.addPara("Missile weapons recieve the following bonuses:", opad);
		label = tooltip.addPara("Rate of Fire and ammunition regeneration rate increased by %s.", pad, h, "" + (int)MISSILE_RATE_BONUS + "%");
		label.setHighlight("" + (int)MISSILE_RATE_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Damage increased by %s.", pad, h, "" + (int)MISSILE_D_BONUS + "%");
		label.setHighlight("" + (int)MISSILE_D_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Missile hitpoints increased by %s.", pad, h, "" + (int)MISSILE_H_BONUS + "%");
		label.setHighlight("" + (int)MISSILE_H_BONUS + "%");
		label.setHighlightColors(h);
		
		label = tooltip.addPara("May only be installed on Kyeltziv Technocracy combat vessels, and only one loader refit may be installed at a time.", opad);
		
	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot") && !ship.getVariant().getHullMods().contains("kyeltziv_loader_ass") && !ship.getVariant().getHullMods().contains("kyeltziv_loader_siege");
	}
	
	public boolean showInRefitScreenModPickerFor(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot");
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("kyeltziv_loader_ass") || ship.getVariant().getHullMods().contains("kyeltziv_loader_siege")) {
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
