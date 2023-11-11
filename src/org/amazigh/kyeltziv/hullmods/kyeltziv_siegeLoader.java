package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_siegeLoader extends BaseHullMod {
	
	public static final float ROF_MALUS = 25f;
	public static final float DAMAGE_BONUS = 40f;
	public static final float FLUX_MALUS = 50f;
	public static final float VELOCITY_BONUS = 15f;
	public static final float REGEN_MALUS = 15f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		stats.getBallisticRoFMult().modifyMult(id, 1f - (ROF_MALUS * 0.01f));
		stats.getBallisticWeaponDamageMult().modifyMult(id, 1f + (DAMAGE_BONUS * 0.01f));
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f + (FLUX_MALUS * 0.01f));
		stats.getBallisticProjectileSpeedMult().modifyMult(id, 1f + (VELOCITY_BONUS * 0.01f));
		stats.getBallisticAmmoRegenMult().modifyMult(id, 1f - (REGEN_MALUS * 0.01f));
		
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
		
		LabelAPI label = tooltip.addPara("A customised ballistic autoloader exclusive to Kyeltziv Technocracy vessels that increases ballistic range, per shot damage and velocity in exchange for flux efficiency and rate of fire.", opad);
		
		label = tooltip.addPara("Ballistic weapons recieve the following bonuses:", opad);
		label = tooltip.addPara("Rate of Fire reduced by %s.", pad, bad, "" + (int)ROF_MALUS + "%");
		label.setHighlight("" + (int)ROF_MALUS + "%");
		label.setHighlightColors(bad);
		label = tooltip.addPara("Damage increased by %s.", pad, h, "" + (int)DAMAGE_BONUS + "%");
		label.setHighlight("" + (int)DAMAGE_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Flux cost to fire increased by %s.", pad, bad, "" + (int)FLUX_MALUS + "%");
		label.setHighlight("" + (int)FLUX_MALUS + "%");
		label.setHighlightColors(bad);
		label = tooltip.addPara("Projectile velocity increased by %s.", pad, h, "" + (int)VELOCITY_BONUS + "%");
		label.setHighlight("" + (int)VELOCITY_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Ammunition regeneration rate reduced by %s.", pad, bad, "" + (int)REGEN_MALUS + "%");
		label.setHighlight("" + (int)REGEN_MALUS + "%");
		label.setHighlightColors(bad);
		
		label = tooltip.addPara("Doubles the effect of the range bonus provided by %s.", opad, h, "Kyeltziv Rangefinders");
		label.setHighlight("Kyeltziv Rangefinders");
		label.setHighlightColors(h);
		
		label = tooltip.addPara("May only be installed on Kyeltziv Technocracy combat vessels, and only one loader refit may be installed at a time.", opad);
		
	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot") && !ship.getVariant().getHullMods().contains("kyeltziv_loader_ass") && !ship.getVariant().getHullMods().contains("kyeltziv_loader_supp");
	}
	
	public boolean showInRefitScreenModPickerFor(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot");
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("kyeltziv_loader_ass") || ship.getVariant().getHullMods().contains("kyeltziv_loader_supp")) {
			return "May only install one loader refit at a time.";
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
