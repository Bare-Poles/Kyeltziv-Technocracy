package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_siegePackage extends BaseHullMod {

	public static final float SPEED_MALUS = 10f;
	public static final float TURN_MALUS = 10f;

	public static final float KE_RESIST = 0.80f;
	
	public static final float VENT_BONUS = 50f;
	
	public static final float MANEUVER_BONUS = 60f;
	public static final float VENT_ARMOUR = 35f;

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		stats.getMaxSpeed().modifyPercent(id, -SPEED_MALUS);
		stats.getMaxTurnRate().modifyPercent(id, -TURN_MALUS);

		stats.getKineticDamageTakenMult().modifyMult(id, KE_RESIST);
		stats.getKineticShieldDamageTakenMult().modifyMult(id, KE_RESIST);

		stats.getVentRateMult().modifyPercent(id, VENT_BONUS);
	}
	
	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		MutableShipStatsAPI stats = ship.getMutableStats();
		FluxTrackerAPI fluxTracker = ship.getFluxTracker();
		
        if (fluxTracker.isVenting()) {
        	float DAM_VENT = 1f - (fluxTracker.getFluxLevel() * VENT_ARMOUR * 0.01f);
        	
        	stats.getAcceleration().modifyPercent(spec.getId(), + (MANEUVER_BONUS * fluxTracker.getFluxLevel()));
        	stats.getDeceleration().modifyPercent(spec.getId(), + (MANEUVER_BONUS * fluxTracker.getFluxLevel()));
        	stats.getTurnAcceleration().modifyPercent(spec.getId(), + (MANEUVER_BONUS * fluxTracker.getFluxLevel()));
        	stats.getMaxTurnRate().modifyPercent(spec.getId(), + (MANEUVER_BONUS * fluxTracker.getFluxLevel()));
        	stats.getArmorDamageTakenMult().modifyMult(spec.getId(), DAM_VENT);
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
		
		float KE_DESC = 20f; //(1f - KE_RESIST) * 100f;
		
		Color h = Misc.getHighlightColor();
		Color bad = Misc.getNegativeHighlightColor();
		
		LabelAPI label = tooltip.addPara("A series of modifications exclusive to Kyeltziv Technocracy vessels that optimise the ship for extreme range combat.", opad);
		
		label = tooltip.addPara("Reduces the ship's top speed in combat by %s.", opad, h, "" + (int)SPEED_MALUS + "%");
		label.setHighlight("" + (int)SPEED_MALUS + "%");
		label.setHighlightColors(bad);
		label = tooltip.addPara("Reduces the ship's turn rate by %s.", pad, h, "" + (int)TURN_MALUS + "%");
		label.setHighlight("" + (int)TURN_MALUS + "%");
		label.setHighlightColors(bad);
		label = tooltip.addPara("Reduces Kinetic damage taken by %s.", pad, h, "" + (int)KE_DESC + "%");
		label.setHighlight("" + (int)KE_DESC + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Increases active flux vent rate by %s.", pad, h, "" + (int)VENT_BONUS + "%");
		label.setHighlight("" + (int)VENT_BONUS + "%");
		label.setHighlightColors(h);
		
		label = tooltip.addPara("While venting, you also recieve the following bonuses that scale based on current flux level.", opad);
		label = tooltip.addPara("Increases the ship's maneuverability by up to %s.", pad, h, "" + (int)MANEUVER_BONUS + "%");
		label.setHighlight("" + (int)MANEUVER_BONUS + "%");
		label.setHighlightColors(h);
		label = tooltip.addPara("Reduces damage taken by armour by up to %s.", pad, h, "" + (int)VENT_ARMOUR + "%");
		label.setHighlight("" + (int)VENT_ARMOUR + "%");
		label.setHighlightColors(h);
		
		label = tooltip.addPara("May only be installed on Kyeltziv Technocracy combat vessels, and only one modification package may be installed at a time.", opad);
		
	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot") && !ship.getVariant().getHullMods().contains("kyeltziv_pack_ass") && !ship.getVariant().getHullMods().contains("kyeltziv_pack_supp");
	}
	
	public boolean showInRefitScreenModPickerFor(ShipAPI ship) {
		return ship.getVariant().getHullMods().contains("kyeltziv_pack_slot");
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("kyeltziv_pack_ass") || ship.getVariant().getHullMods().contains("kyeltziv_pack_supp")) {
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
