package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_assaultPackage extends BaseHullMod {
	
	public static final float FLUX_BONUS = 8f;
	public static final float SPEED_BONUS = 5f;
	public static final float MANEUVER_BONUS = 20f;
	
	public static final float ARMOR_BONUS = 10f;
	public static final float HEALTH_BONUS = 20f;
	public static final float HE_RESIST = 0.9f;

	public static final float RANGE_MALUS = 10f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		stats.getFluxDissipation().modifyPercent(id, FLUX_BONUS);
		
		stats.getMaxSpeed().modifyFlat(id, SPEED_BONUS);
		stats.getAcceleration().modifyPercent(id, MANEUVER_BONUS);
		stats.getDeceleration().modifyPercent(id, MANEUVER_BONUS);
		stats.getTurnAcceleration().modifyPercent(id, MANEUVER_BONUS);
		
		stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
		stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS);
		stats.getHighExplosiveDamageTakenMult().modifyMult(id, HE_RESIST);
		stats.getHighExplosiveShieldDamageTakenMult().modifyMult(id, HE_RESIST);
		
		stats.getBallisticWeaponRangeBonus().modifyMult(id, 1f - (RANGE_MALUS / 100f));
		stats.getEnergyWeaponRangeBonus().modifyMult(id, 1f - (RANGE_MALUS / 100f));
		
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
		
		float HE_DESC = (1f - HE_RESIST) * 100f;
		
		Color h = Misc.getHighlightColor();
		Color bad = Misc.getNegativeHighlightColor();
		
		LabelAPI label = tooltip.addPara("A series of modifications exclusive to Kyeltziv Technocracy vessels that optimise the ship for short ranged combat.", opad);
		
		label = tooltip.addPara("Increases the ship's base flux dissipation by %s.", opad, h, "" + (int)FLUX_BONUS + "%");
		label.setHighlight("" + (int)FLUX_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Increases the ship's top speed in combat by %s.", pad, h, "" + (int)SPEED_BONUS + "%");
		label.setHighlight("" + (int)SPEED_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Increases the ship's maneuverability by %s.", pad, h, "" + (int)MANEUVER_BONUS + "%");
		label.setHighlight("" + (int)MANEUVER_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Increases the ship's armor by %s.", pad, h, "" + (int)ARMOR_BONUS + "%");
		label.setHighlight("" + (int)ARMOR_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Increases the durability of all weapons by %s.", pad, h, "" + (int)HEALTH_BONUS + "%");
		label.setHighlight("" + (int)HEALTH_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Reduces High Explosive damage taken by %s.", pad, h, "" + (int)HE_DESC + "%");
		label.setHighlight("" + (int)HE_DESC + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Weapon range is reduced by %s.", pad, h, "" + (int)RANGE_MALUS + "%");
		label.setHighlight("" + (int)RANGE_MALUS + "%");
		label.setHighlightColors(bad);

		label = tooltip.addPara("Halves the effect of the range bonus provided by %s.", opad, h, "Kyeltziv Rangefinders");
		label.setHighlight("Kyeltziv Rangefinders");
		label.setHighlightColors(h);
		
		/*
		label = tooltip.addPara(
				"Increases the ship's top speed in combat by %s."
				+ "Increases the ship's maneuverability by %s."
				+ "Increases the ship's armor by %s."
				+ "Increases the durability of all weapons by %s."
				+ "Reduces High Explosive damage taken by %s."
				+ "Weapon range is reduced by %s.", opad, h,
				"" + (int)SPEED_BONUS + "%",
				"" + (int)MANEUVER_BONUS + "%",
				"" + (int)ARMOR_BONUS + "%",
				"" + (int)HEALTH_BONUS + "%",
				"" + (int)HE_DESC + "%",
				"" + (int)RANGE_DESC + "%");
		label.setHighlight(
				"" + (int)SPEED_BONUS + "%",
				"" + (int)MANEUVER_BONUS + "%",
				"" + (int)ARMOR_BONUS + "%",
				"" + (int)HEALTH_BONUS + "%",
				"" + (int)HE_DESC + "%",
				"" + (int)RANGE_DESC + "%");
		label.setHighlightColors(h, h, h, h, h, bad);
		*/
		
		label = tooltip.addPara("May only be installed on Kyeltziv Technocracy combat vessels, and only one modification package may be installed at a time.", opad);
		
	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot") && !ship.getVariant().getHullMods().contains("kyeltziv_pack_siege") && !ship.getVariant().getHullMods().contains("kyeltziv_pack_supp");
	}

	public boolean showInRefitScreenModPickerFor(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot");
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("kyeltziv_pack_siege") || ship.getVariant().getHullMods().contains("kyeltziv_pack_supp")) {
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
