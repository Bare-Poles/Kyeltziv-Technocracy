package org.amazigh.kyeltziv.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_variableBooster extends BaseHullMod {

	public static final float FLUX_BONUS = 10f;
	public static final float RECOIL_BONUS = 20f;
	public static final float DAM_BONUS = 10f;
	
	public static final float ASS_FLUX_BONUS = 40f;
	public static final float ASS_ROF_BONUS = 60f;
	
	public static final float SIEGE_D_BONUS = 30f;
	public static final float SIEGE_V_BONUS = 20f;
	
	public static final float SUP_FLUX_BONUS = 30f;
	public static final float SUP_RECOIL_BONUS = 50f;
	public static final float SUP_REGEN_BONUS = 50f;
	
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
		float hpad = 14f;
		float hopad = 6f;
		float pad = 2f;
		
		boolean loader = false;
		Color h = Misc.getHighlightColor();
		
		LabelAPI label = tooltip.addPara("This vessel features a modular Weapon Boosting System that provides enhancements to weapons performance while active, the nature of which varies based on the currently installed %s.", opad, h, "Loader Refit");
		label.setHighlight("Loader Refit");
		label.setHighlightColors(h);

		if (!Global.CODEX_TOOLTIP_MODE) {
			if (ship.getVariant().getHullMods().contains("kyeltziv_loader_ass")) {
				tooltip.addSectionHeading("Installed Loader Refit:", Alignment.MID, hpad);
				label = tooltip.addPara("%s", opad, h, "Assault Loader");
				label.setHighlight("Assault Loader");
				label.setHighlightColors(h);
				
				label = tooltip.addPara("Ballistic weapon Rate of Fire increased by %s.", hopad, h, "" + (int)ASS_ROF_BONUS + "%");
				label.setHighlight("" + (int)ASS_ROF_BONUS + "%");
				label.setHighlightColors(h);
				label = tooltip.addPara("Ballistic weapon Flux Cost to fire reduced by %s.", pad, h, "" + (int)ASS_FLUX_BONUS + "%");
				label.setHighlight("" + (int)ASS_FLUX_BONUS + "%");
				label.setHighlightColors(h);
				
				loader = true;
			}
			
			if (ship.getVariant().getHullMods().contains("kyeltziv_loader_siege")) {
				tooltip.addSectionHeading("Installed Loader Refit:", Alignment.MID, hpad);
				label = tooltip.addPara("%s", opad, h, "Siege Loader");
				label.setHighlight("Siege Loader");
				label.setHighlightColors(h);

				label = tooltip.addPara("Ballistic weapon Damage increased by %s.", hopad, h, "" + (int)SIEGE_D_BONUS + "%");
				label.setHighlight("" + (int)SIEGE_D_BONUS + "%");
				label.setHighlightColors(h);
				label = tooltip.addPara("Ballistic weapon Projectile Velocity increased by %s.", pad, h, "" + (int)SIEGE_V_BONUS + "%");
				label.setHighlight("" + (int)SIEGE_V_BONUS + "%");
				label.setHighlightColors(h);
				
				loader = true;
			}
			
			if (ship.getVariant().getHullMods().contains("kyeltziv_loader_supp")) {
				tooltip.addSectionHeading("Installed Loader Refit:", Alignment.MID, hpad);
				label = tooltip.addPara("%s", opad, h, "Autoforge Loader");
				label.setHighlight("Autoforge Loader");
				label.setHighlightColors(h);

				label = tooltip.addPara("Ballistic and Missile weapon Flux Cost to fire reduced by %s.", hopad, h, "" + (int)SUP_FLUX_BONUS + "%");
				label.setHighlight("" + (int)SUP_FLUX_BONUS + "%");
				label.setHighlightColors(h);
				label = tooltip.addPara("Weapon recoil reduced by %s.", pad, h, "" + (int)SUP_RECOIL_BONUS + "%");
				label.setHighlight("" + (int)SUP_RECOIL_BONUS + "%");
				label.setHighlightColors(h);
				label = tooltip.addPara("Missile weapon Ammunition Regeneration rate increased by %s.", pad, h, "" + (int)SUP_REGEN_BONUS + "%");
				label.setHighlight("" + (int)SUP_REGEN_BONUS + "%");
				label.setHighlightColors(h);
				
				loader = true;
			}
			
			if (!loader) {
				tooltip.addSectionHeading("Installed Loader Refit:", Alignment.MID, hpad);
				label = tooltip.addPara("%s", opad, h, "None");
				label.setHighlight("None");
				label.setHighlightColors(h);

				label = tooltip.addPara("Ballistic weapon Damage increased by %s.", hopad, h, "" + (int)DAM_BONUS + "%");
				label.setHighlight("" + (int)DAM_BONUS + "%");
				label.setHighlightColors(h);
				label = tooltip.addPara("All Weapons Flux Cost to fire reduced by %s.", pad, h, "" + (int)FLUX_BONUS + "%");
				label.setHighlight("" + (int)FLUX_BONUS + "%");
				label.setHighlightColors(h);
				label = tooltip.addPara("Weapon recoil reduced by %s.", pad, h, "" + (int)RECOIL_BONUS + "%");
				label.setHighlight("" + (int)RECOIL_BONUS + "%");
				label.setHighlightColors(h);
			}
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
