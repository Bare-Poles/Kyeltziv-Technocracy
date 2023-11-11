package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_fluxPhaseLoader extends BaseHullMod {
	
	public static final float ROF_BONUS = 60f;
	public static final float FLUX_BONUS = 40f;
	
	public static final float RATE_MALUS = 35f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		stats.getMissileAmmoRegenMult().modifyMult(id, 1f - (RATE_MALUS * 0.01f));
		stats.getMissileRoFMult().modifyMult(id, 1f - (RATE_MALUS * 0.01f));
	}
		
	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		MutableShipStatsAPI stats = ship.getMutableStats();
		
		float FLUX_USAGE = 0f;
		if (ship.getHardFluxLevel() > 0.8f) {
			FLUX_USAGE = 0.8f;
		} else {
			FLUX_USAGE = ship.getHardFluxLevel();
		}
		float FLUX_RATIO = FLUX_USAGE / 0.8f;
		
		stats.getBallisticAmmoRegenMult().modifyMult(spec.getId(), 1.0f + (0.15f + ((ROF_BONUS * 0.01f) * FLUX_RATIO)));
		stats.getBallisticRoFMult().modifyMult(spec.getId(), 1.0f + (0.15f + ((ROF_BONUS * 0.01f) * FLUX_RATIO)));
		
		stats.getBallisticWeaponFluxCostMod().modifyMult(spec.getId(), 1.0f - (0.1f + ((FLUX_BONUS * 0.01f) * FLUX_RATIO)));
		
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			float FLUX_DISPLAY = FLUX_RATIO * 100f;
			
			Global.getCombatEngine().maintainStatusForPlayerShip("KYEL-PRATE", "graphics/icons/hullsys/ammo_feeder.png", "Flux-Phase Bonus Level ", Math.round(FLUX_DISPLAY) + "%", false);
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
		
		float TRUE_ROF = ROF_BONUS + 15f;
		float TRUE_FLUX = FLUX_BONUS + 10f;
		
		LabelAPI label = tooltip.addPara("A special autoloader system found on Kyeltziv phase vessels, provides amplified ballistic weapon performance as Hard Flux levels increase in exchange for reduced missile weapon rate of fire.", opad);
		
		label = tooltip.addPara("Missile weapon rate of Fire and ammunition regeneration rate reduced by %s.", opad, bad, "" + (int)RATE_MALUS + "%");
		label.setHighlight("" + (int)RATE_MALUS + "%");
		label.setHighlightColors(bad);
		
		label = tooltip.addPara("Ballistic weapons recieve bonuses scaling with current Hard Flux level.", opad);
		label = tooltip.addPara("They will always recieve the minimum bonus, and it scales up to the maximum bonus when at %s Hard Flux or higher:", pad, h, "80%");
		label.setHighlight("80%");
		label.setHighlightColors(h);
		
		label = tooltip.addPara("Rate of Fire and ammunition regeneration rate increased by %s to %s.", pad, h, "15%", "" + (int)TRUE_ROF + "%");
		label.setHighlight("15%", "" + (int)TRUE_ROF + "%");
		label.setHighlightColors(h, h);
		label = tooltip.addPara("Flux cost to fire reduced by %s to %s.", pad, h, "10%", "" + (int)TRUE_FLUX + "%");
		label.setHighlight("10%", "" + (int)TRUE_FLUX + "%");
		label.setHighlightColors(h, h);
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
