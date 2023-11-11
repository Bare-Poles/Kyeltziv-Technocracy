package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;

import org.lazywizard.lazylib.combat.DefenseUtils;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_hevSuit extends BaseHullMod {

	public static final float KE_RESIST = 0.75f;
	public static final float WEAPON_REPAIR = 40f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

		stats.getKineticDamageTakenMult().modifyMult(id, KE_RESIST);
		stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - WEAPON_REPAIR * 0.01f);
	}
		
	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		
			// repair armour if it is damaged
			if (DefenseUtils.hasArmorDamage(ship)) {
	        	ArmorGridAPI armorGrid = ship.getArmorGrid();
		        final float[][] grid = armorGrid.getGrid();
		        final float max = armorGrid.getMaxArmorInCell();
		        
		        float baseCell = armorGrid.getMaxArmorInCell() * Math.min(ship.getHullSpec().getArmorRating(), 1350f) / armorGrid.getArmorRating();
		        float repairAmount = baseCell * 0.02f * amount * (1f - (ship.getFluxLevel() * 0.75f));
		        // base of 2% armour/sec, scaling as flux goes up.
		        
				if (ship.isPhased()) {
					repairAmount *= 0.5f;
			        // if phased, then rate is halved
				}
				
				for (int x = 0; x < grid.length; x++) {
		            for (int y = 0; y < grid[0].length; y++) {
		                if (grid[x][y] < max) {
		                    float regen = grid[x][y] + repairAmount;
		                    armorGrid.setArmorValue(x, y, regen);
		                }
		            }
		        }
		        ship.syncWithArmorGridState();
	            ship.syncWeaponDecalsWithArmorDamage();
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
		
		float KE_DESC = 25f; //(1f - KE_RESIST) * 100f;
		
		LabelAPI label = tooltip.addPara("A specialised suite of defensive measures are built into the structure of the Khoronit, they give it impressive resistance to weapons fire as well as protection from 'Uncoperative Samples' as per the documentation.", opad);
		
		label = tooltip.addPara("Advanced plating reduces kinetic damage taken by %s.", opad, h, "" + (int)KE_DESC + "%");
		label.setHighlight("" + (int)KE_DESC + "%");
		label = tooltip.addPara("Autorepair systems reduce the time taken to repair disabled weapons by %s.", pad, h, "" + (int)WEAPON_REPAIR + "%");
		label.setHighlight("" + (int)WEAPON_REPAIR + "%");
		
		label = tooltip.addPara("This ship will repair up to %s armour a second with the repair rate scaling down as the ship builds up flux, repair rate is halved when phased.", opad, h, "2%");
		label.setHighlight("2%");
		label.setHighlightColors(h);
		
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
