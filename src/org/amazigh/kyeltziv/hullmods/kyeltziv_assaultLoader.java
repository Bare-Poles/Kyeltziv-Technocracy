package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_assaultLoader extends BaseHullMod {
	
	public static final float ROF_BONUS = 60f;
	public static final float DAMAGE_MALUS = 25f;
	public static final float FLUX_BONUS = 30f;
	public static final float REGEN_BONUS = 40f;
	
	public static final float RECOIL_MALUS = 35f; // rather than being a pussy with 20%, let's go sicko mode and make this 35% lol.
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getBallisticRoFMult().modifyMult(id, 1f + (ROF_BONUS * 0.01f));
		stats.getBallisticWeaponDamageMult().modifyMult(id, 1f - (DAMAGE_MALUS * 0.01f));
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_BONUS * 0.01f));
		stats.getBallisticAmmoRegenMult().modifyMult(id, 1f + (REGEN_BONUS * 0.01f));

		stats.getMaxRecoilMult().modifyMult(id, 1f + (0.01f * RECOIL_MALUS));
		stats.getRecoilPerShotMult().modifyMult(id, 1f + (0.01f * RECOIL_MALUS));
		stats.getRecoilDecayMult().modifyMult(id, 1f + (0.01f * RECOIL_MALUS));
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
		
		LabelAPI label = tooltip.addPara("A customised ballistic autoloader exclusive to Kyeltziv Technocracy vessels that improves fire rate and flux efficiency in exchange for per-shot damage and increased shot spread.", opad);

		label = tooltip.addPara("Recoil of all weapons increased by %s.", opad, bad, "" + (int)RECOIL_MALUS + "%");
		label.setHighlight("" + (int)RECOIL_MALUS + "%");
		label.setHighlightColors(bad);
		
		label = tooltip.addPara("Ballistic weapons recieve the following bonuses:", opad);
		
		label = tooltip.addPara("Rate of Fire increased by %s.", pad, h, "" + (int)ROF_BONUS + "%");
		label.setHighlight("" + (int)ROF_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Damage decreased by %s.", pad, bad, "" + (int)DAMAGE_MALUS + "%");
		label.setHighlight("" + (int)DAMAGE_MALUS + "%");
		label.setHighlightColors(bad);
		label = tooltip.addPara("Flux cost to fire reduced by %s.", pad, h, "" + (int)FLUX_BONUS + "%");
		label.setHighlight("" + (int)FLUX_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Ammunition regeneration rate increased by %s.", pad, h, "" + (int)REGEN_BONUS + "%");
		label.setHighlight("" + (int)REGEN_BONUS + "%");
		label.setHighlightColors(h);
		
		label = tooltip.addPara("May only be installed on Kyeltziv Technocracy combat vessels, and only one loader refit may be installed at a time.", opad);
		
	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot") && !ship.getVariant().getHullMods().contains("kyeltziv_loader_siege") && !ship.getVariant().getHullMods().contains("kyeltziv_loader_supp");
	}
	
	public boolean showInRefitScreenModPickerFor(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot");
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("kyeltziv_loader_siege") || ship.getVariant().getHullMods().contains("kyeltziv_loader_supp")) {
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
