package org.amazigh.kyeltziv.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class kyeltziv_VariableBoosterStats extends BaseShipSystemScript {

	public static final float FLUX_BONUS = 10f;
	public static final float RECOIL_BONUS = 0.2f;
	public static final float DAM_BONUS = 0.1f;
	
	public static final float ASS_FLUX_BONUS = 40f;
	public static final float ASS_ROF_BONUS = 0.6f;
	
	public static final float SIEGE_D_BONUS = 0.3f;
	public static final float SIEGE_V_BONUS = 0.2f;
	
	public static final float SUP_FLUX_BONUS = 30f;
	public static final float SUP_RECOIL_BONUS = 0.5f;
	public static final float SUP_REGEN_BONUS = 0.5f;
	public static final float SUP_SECRET_SAUCE = 0.1f;
	
	private boolean LOADER = false;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		
		ShipAPI ship = (ShipAPI)stats.getEntity();
		ShipVariantAPI variant = ship.getVariant();
		
    	if (variant.getHullMods().contains("kyeltziv_loader_ass")) {
    		LOADER = true;

			float multf = ASS_FLUX_BONUS * effectLevel;
			stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -multf);

			float mult = 1f + (ASS_ROF_BONUS * effectLevel);
			stats.getBallisticRoFMult().modifyMult(id, mult);
			stats.getBallisticAmmoRegenMult().modifyMult(id, mult);
		}

		if (variant.getHullMods().contains("kyeltziv_loader_siege")) {
    		LOADER = true;
    		
    		float multSD = 1f + (SIEGE_D_BONUS * effectLevel);
    		stats.getBallisticWeaponDamageMult().modifyMult(id, multSD);
    		
    		float multSV = 1f + (SIEGE_V_BONUS * effectLevel);
    		stats.getBallisticProjectileSpeedMult().modifyMult(id, multSV);
    		
		}

		if (variant.getHullMods().contains("kyeltziv_loader_supp")) {
    		LOADER = true;

			float multf = SUP_FLUX_BONUS * effectLevel;
			stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -multf);
			stats.getMissileWeaponFluxCostMod().modifyPercent(id, -multf);
			
			float multr = 1f - (SUP_RECOIL_BONUS * effectLevel);
			stats.getMaxRecoilMult().modifyMult(id, multr);
			stats.getRecoilPerShotMult().modifyMult(id, multr);
			stats.getRecoilDecayMult().modifyMult(id, multr);
			
			float multg = 1f + (SUP_REGEN_BONUS * effectLevel);
			stats.getBallisticAmmoRegenMult().modifyMult(id, multg);
			
			float multSSS = SUP_SECRET_SAUCE * effectLevel;
			stats.getHitStrengthBonus().modifyPercent(id, multSSS);
			
		}
		
		if (LOADER = false) {
			float multf = FLUX_BONUS * effectLevel;
			stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -multf);
			stats.getMissileWeaponFluxCostMod().modifyPercent(id, -multf);
			stats.getEnergyWeaponFluxCostMod().modifyPercent(id, -multf);
			
			float multr = 1f - (RECOIL_BONUS * effectLevel);
			stats.getMaxRecoilMult().modifyMult(id, multr);
			stats.getRecoilPerShotMult().modifyMult(id, multr);
			stats.getRecoilDecayMult().modifyMult(id, multr);
			
			float mult = 1f + (DAM_BONUS * effectLevel);
			stats.getBallisticWeaponDamageMult().modifyMult(id, mult);
		}
		
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getBallisticRoFMult().unmodify(id);
		stats.getBallisticAmmoRegenMult().unmodify(id);
		
		stats.getBallisticProjectileSpeedMult().unmodify(id);
		stats.getBallisticWeaponDamageMult().unmodify(id);
		
		stats.getMissileAmmoRegenMult().unmodify(id);
		stats.getHitStrengthBonus().unmodify();
		
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
		stats.getMissileWeaponFluxCostMod().unmodify(id);
		stats.getEnergyWeaponFluxCostMod().unmodify(id);
		
		stats.getMaxRecoilMult().unmodify(id);
		stats.getRecoilPerShotMult().unmodify(id);
		stats.getRecoilDecayMult().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("weaponry boosted", false);
		}
		return null;
	}
}
