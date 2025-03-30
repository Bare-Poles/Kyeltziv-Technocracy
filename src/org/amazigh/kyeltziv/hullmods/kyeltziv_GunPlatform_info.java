package org.amazigh.kyeltziv.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_GunPlatform_info extends BaseHullMod {
	
	public String LOADER = "None";
	public String PACKAGE = "None";
	
	public String PRIMARY = "Lorentz Rifle";
	public String SECONDARY = "Particle Discharger";
	public String TERTIARY = "Twin Tappi Autoguns";
	public String DRONES = "4x Kiusa Drones.";
	public boolean BONUS = false;
	public String BONUS1a = "";
	public String BONUS1b = "";
	public String BONUS2a = "";
	public String BONUS2b = "";
	public String BONUS2c = "";	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		// gottem
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
		
		LOADER = "None";
		PACKAGE = "None";
		
		PRIMARY = "Lorentz Rifle";
		SECONDARY = "Particle Discharger";
		TERTIARY = "Twin Tappi Autoguns";
		DRONES = "4x Kiusa Drones.";
		BONUS = false;
		
		if (!Global.CODEX_TOOLTIP_MODE) {
			if (ship.getVariant().getHullMods().contains("kyeltziv_pack_ass")) {
				PACKAGE = "Assault Package";
				SECONDARY = "Perennis Shock Missile Pods";
				DRONES = "4x Bellis SRM armed Missile Drones.";
				BONUS1a = "Platform armour increased by";
				BONUS1b = "10%";
				BONUS2a = "Platform takes";
				BONUS2b = "10%";
				BONUS2c = "reduced High Explosive damage.";
				BONUS = true;
			}
			if (ship.getVariant().getHullMods().contains("kyeltziv_pack_siege")) {
				PACKAGE = "Siege Package";
				SECONDARY = "Light Galvanic Howitzers";
				DRONES = "2x Shock Culverin armed Gun Drones.";
				BONUS1a = "Non-PD weapon range increased by";
				BONUS1b = "20%";
				BONUS2a = "Platform takes";
				BONUS2b = "20%";
				BONUS2c = "reduced Kinetic damage.";
				BONUS = true;
			}
			if (ship.getVariant().getHullMods().contains("kyeltziv_pack_supp")) {
				PACKAGE = "Support Package";
				SECONDARY = "Light Killcloud Projector";
				DRONES = "8x Kiusa Drones.";
				BONUS1a = "Point Defense weapon range increased by";
				BONUS1b = "60%";
				BONUS2a = "Damage dealt to missiles and fighters increased by";
				BONUS2b = "25%";
				BONUS2c = "";
				BONUS = true;
			}
			
			if (ship.getVariant().getHullMods().contains("kyeltziv_loader_ass")) {
				LOADER = "Assault Loader";
				PRIMARY =  "Fusion Houfnice";
				TERTIARY = "Shock Avtomats";
			}
			if (ship.getVariant().getHullMods().contains("kyeltziv_loader_siege")) {
				LOADER = "Siege Loader";
				PRIMARY =  "Compact Shock-Driver";
				TERTIARY = "Piikki Burst Rifles";
			}
			if (ship.getVariant().getHullMods().contains("kyeltziv_loader_supp")) {
				LOADER = "Autoforge Loader";
				PRIMARY =  "Vedha Pulse Rocket Pod";
				TERTIARY = "Micro Stutter Beams";
			}
		}
		
		float pad = 2f;
		float opad = 10f;
		float hpad = 14f;
		float hopad = 6f;
		
		Color n = Misc.getTextColor();
		Color h = Misc.getHighlightColor();
		
		LabelAPI label = tooltip.addPara("This ship can deploy Linnake Gun Platforms, these platforms are modified by the installation of Kyeltziv Modification Packages and Loader Refits on the lauching vessel.", opad);
		label = tooltip.addPara("Modification Packages will apply a small bonus to the platform, alter what drones the platform deploys and which secondary weapons are mounted.", opad);
		label = tooltip.addPara("Loader Refits will install the Loader Refit on the platform and alter both the primary weapon and the tertiary point defense weapons that are mounted.", pad);

		if (!Global.CODEX_TOOLTIP_MODE) {
			tooltip.addSectionHeading("Installed Packages:", Alignment.MID, hpad);
			label = tooltip.addPara("Modification Package: %s", hopad, h, PACKAGE);
			label.setHighlight(PACKAGE);
			label.setHighlightColors(h);
			label = tooltip.addPara("Loader Refit: %s", pad, h, "" + LOADER);
			label.setHighlight(LOADER);
			label.setHighlightColors(h);
			
			tooltip.addSectionHeading("Weapons:", Alignment.MID, hpad);
			label = tooltip.addPara("Primary: %s", hopad, h, PRIMARY);
			label.setHighlight(PRIMARY);
			label.setHighlightColors(h);
			label = tooltip.addPara("Secondary: %s", pad, h, SECONDARY);
			label.setHighlight(SECONDARY);
			label.setHighlightColors(h);
			label = tooltip.addPara("Tertiary Point Defense: %s", pad, h, TERTIARY);
			label.setHighlight(TERTIARY);
			label.setHighlightColors(h);
			
			tooltip.addSectionHeading("Other:", Alignment.MID, hpad);
			label = tooltip.addPara("Drones: %s", hopad, h, DRONES);
			label.setHighlight(DRONES);
			label.setHighlightColors(h);
			
			if (BONUS) {
				label = tooltip.addPara("%s %s", hopad, h, BONUS1a, BONUS1b);
				label.setHighlight(BONUS1a, BONUS1b);
				label.setHighlightColors(n, h);
				label = tooltip.addPara("%s %s %s", pad, h, BONUS2a, BONUS2b, BONUS2c);
				label.setHighlight(BONUS2a, BONUS2b, BONUS2c);
				label.setHighlightColors(n, h, n);
			}
		}
	}
	
    @Override
    public Color getBorderColor() {
        return new Color(197,207,169,200);
    }

    @Override
    public Color getNameColor() {
        return new Color(197,207,169,255);
    }

}
