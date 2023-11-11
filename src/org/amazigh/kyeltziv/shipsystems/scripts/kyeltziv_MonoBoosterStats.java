package org.amazigh.kyeltziv.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class kyeltziv_MonoBoosterStats extends BaseShipSystemScript {

	public static final float FLUX_BONUS = 20f;
	public static final float RECOIL_BONUS = 0.3f;
	public static final float DAM_BONUS = 0.1f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		
		float multf = FLUX_BONUS * effectLevel;
		stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -multf);
		stats.getEnergyWeaponFluxCostMod().modifyPercent(id, -multf);
		
		float multr = 1f - (RECOIL_BONUS * effectLevel);
		stats.getMaxRecoilMult().modifyMult(id, multr);
		stats.getRecoilPerShotMult().modifyMult(id, multr);
		stats.getRecoilDecayMult().modifyMult(id, multr);
		
		float mult = 1f + (DAM_BONUS * effectLevel);
		stats.getBallisticWeaponDamageMult().modifyMult(id, mult);
		stats.getEnergyWeaponDamageMult().modifyMult(id, mult);
		
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getBallisticWeaponDamageMult().unmodify(id);
		stats.getEnergyWeaponDamageMult().unmodify(id);
		
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
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
