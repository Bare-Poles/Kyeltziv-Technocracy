package org.amazigh.kyeltziv.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_rangefinders extends BaseHullMod {

	public static final float RANGE_BONUS = 100f;

	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		MutableShipStatsAPI stats = ship.getMutableStats();
		
		float FLUX_USAGE = 1 - ship.getFluxLevel();
		
		if (ship.getVariant().getHullMods().contains("kyeltziv_loader_siege")) {
			FLUX_USAGE *= 2f;
		}
		
		if (ship.getVariant().getHullMods().contains("kyeltziv_pack_ass")) {
			FLUX_USAGE *= 0.5f;
		}
		
		stats.getBallisticWeaponRangeBonus().modifyFlat(spec.getId(), RANGE_BONUS * FLUX_USAGE);
		
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			Global.getCombatEngine().maintainStatusForPlayerShip("KRANGE", "graphics/icons/hullsys/kyeltziv_rangefinder.png", "Rangefinder Bonus: ", Math.round(RANGE_BONUS * FLUX_USAGE) + "", false);
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
		
		float opad = 10f;
		
		float rangeBonus = RANGE_BONUS;
		
		boolean siege = false;
		boolean assault = false;
		

		if (!Global.CODEX_TOOLTIP_MODE) {
			if (ship.getVariant().getHullMods().contains("kyeltziv_loader_siege")) {
				rangeBonus *= 2f;
				siege = true;
			}
			
			if (ship.getVariant().getHullMods().contains("kyeltziv_pack_ass")) {
				rangeBonus *= 0.5f;
				 assault = true;
			}
		}
		
		Color h = Misc.getHighlightColor();
		
		LabelAPI label = tooltip.addPara("Advanced rangefinding systems are mounted on this vessel increasing the range of ballistic weaponry by %s, but these rangefinders suffer interference from flux buildup reducing this bonus as flux levels increase, weapon range bonus drops off completely at full flux.", opad, h, "" + (int)rangeBonus + "");
		label.setHighlight("" + (int)rangeBonus + "");
		label.setHighlightColors(h);
		
		if (siege) {
			if (assault) {
				label = tooltip.addPara("Both %s and %s have been isntalled, resulting in the standard Range Bonus.", opad, h, "Kyeltziv Siege Loader", "Kyeltziv Assault Package");
				label.setHighlight("Kyeltziv Siege Loader", "Kyeltziv Assault Package");
				label.setHighlightColors(h);
			} else {
				label = tooltip.addPara("Range Bonus has been doubled due to the installation of a %s.", opad, h, "Kyeltziv Siege Loader");
				label.setHighlight("Kyeltziv Siege Loader");
				label.setHighlightColors(h);
			}
		} else if (assault) {
			label = tooltip.addPara("Range Bonus has been halved due to the installation of a %s.", opad, h, "Kyeltziv Assault Package");
			label.setHighlight("Kyeltziv Assault Package");
			label.setHighlightColors(h);
		}
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
