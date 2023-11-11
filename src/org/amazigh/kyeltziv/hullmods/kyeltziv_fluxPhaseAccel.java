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

public class kyeltziv_fluxPhaseAccel extends BaseHullMod {
	
	public static final float DAM_BONUS = 40f;
	public static final float VEL_BONUS = 20f;
	public static final float RoF_MALUS = 10f;
	public static final float RANGE_BONUS = 100f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

		stats.getBallisticWeaponRangeBonus().modifyFlat(id, RANGE_BONUS);
		stats.getBallisticAmmoRegenMult().modifyMult(id, 1f - (RoF_MALUS * 0.01f));
		stats.getBallisticRoFMult().modifyMult(id, 1f - (RoF_MALUS * 0.01f));
	}
		
	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		MutableShipStatsAPI stats = ship.getMutableStats();
		
		float FLUX_USAGE = 0f;
		if (ship.getHardFluxLevel() > 0.75f) {
			FLUX_USAGE = 0.75f;
		} else {
			FLUX_USAGE = ship.getHardFluxLevel();
		}
		float FLUX_RATIO = FLUX_USAGE / 0.75f;
		
		stats.getBallisticWeaponDamageMult().modifyMult(spec.getId(), 1.0f + (0.1f + ((DAM_BONUS * 0.01f) * FLUX_RATIO)));
		stats.getBallisticProjectileSpeedMult().modifyMult(spec.getId(), 1.0f + (0.05f + ((VEL_BONUS * 0.01f) * FLUX_RATIO)));
		
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			float FLUX_DISPLAY = FLUX_RATIO * 100f;
			
			Global.getCombatEngine().maintainStatusForPlayerShip("KYEL-PACCEL", "graphics/icons/hullsys/ammo_feeder.png", "Flux-Phase Bonus Level ", Math.round(FLUX_DISPLAY) + "%", false);
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
		
		float TRUE_ROF = DAM_BONUS + 10f;
		float TRUE_FLUX = VEL_BONUS + 5f;
		
		LabelAPI label = tooltip.addPara("A special autoloader system found on the Khoronit, provides amplified ballistic weapon performance as Hard Flux levels increase.", opad);
		
		label = tooltip.addPara("Ballistic weapon range increased by %s.", opad, h, "" + (int)RANGE_BONUS + "");
		label.setHighlight("" + (int)RANGE_BONUS + "");
		label.setHighlightColors(h);
		label = tooltip.addPara("Ballistic weapon rate of Fire and ammunition regeneration rate reduced by %s.", pad, bad, "" + (int)RoF_MALUS + "%");
		label.setHighlight("" + (int)RoF_MALUS + "%");
		label.setHighlightColors(bad);
		
		label = tooltip.addPara("Ballistic weapons recieve bonuses scaling with current Hard Flux level.", opad);
		label = tooltip.addPara("They will always recieve the minimum bonus, and it scales up to the maximum bonus when at %s Hard Flux or higher:", pad, h, "75%");
		label.setHighlight("75%");
		label.setHighlightColors(h);
		
		label = tooltip.addPara("Damage increased by %s to %s.", pad, h, "10%", "" + (int)TRUE_ROF + "%");
		label.setHighlight("10%", "" + (int)TRUE_ROF + "%");
		label.setHighlightColors(h, h);
		label = tooltip.addPara("Projectile velocity increased by %s to %s.", pad, h, "5%", "" + (int)TRUE_FLUX + "%");
		label.setHighlight("5%", "" + (int)TRUE_FLUX + "%");
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
