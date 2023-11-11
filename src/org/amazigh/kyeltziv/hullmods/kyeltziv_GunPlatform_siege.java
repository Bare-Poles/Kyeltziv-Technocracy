package org.amazigh.kyeltziv.hullmods;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipCommand;

public class kyeltziv_GunPlatform_siege extends BaseHullMod {

	public static final float HEALTH_BONUS = 20f;
	public static final float REPAIR_RATE = 20f;
	
	public static final float KE_RESIST = 0.80f;
	
	public static final float BASE_RANGE_BONUS = 100f;
	public static final float FLUX_RANGE_BONUS = 80f;
	public static final float PD_RANGE_MALUS = 20f;
	public static final float PD_FLUX_RANGE_MALUS = 80f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS);
		stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - (REPAIR_RATE * 0.01f));
		
		stats.getKineticDamageTakenMult().modifyMult(id, KE_RESIST);
		
	}
	
	public void advanceInCombat(ShipAPI ship, float amount){
		if (Global.getCombatEngine().isPaused() || !ship.isAlive() || ship.isPiece()) {
			return;
		}
        ShipSpecificData info = (ShipSpecificData) Global.getCombatEngine().getCustomData().get("KYEL_PLAT_BASE_DATA_KEY" + ship.getId());
        if (info == null) {
            info = new ShipSpecificData();
        }
        
        
        // SPEEEN
        ship.giveCommand(ShipCommand.TURN_RIGHT, null, 0);
        
        // the self destruct timer, to prevent meme cheesery with stacking infinite platforms.
        info.L_TIMER -= amount;
        
        // do some "sparking" fx as lifetime gets low, with rate increasing as timer gets to zero.
        if (info.L_TIMER <= 7f) {
        	info.SPK_TIMER += amount;
        	if (info.SPK_TIMER > Math.max(info.L_TIMER / 3f, 0.25f)) {
            	Global.getSoundPlayer().playSound("disabled_small", MathUtils.getRandomNumberInRange(0.9f, 1.2f), MathUtils.getRandomNumberInRange(0.3f, 0.6f), ship.getLocation(), ship.getVelocity());
            	
            	float radius = ship.getCollisionRadius();
            	
            	float distanceRandom1 = MathUtils.getRandomNumberInRange(radius * 0.4f, radius * 0.9f);
        		float angleRandom1 = MathUtils.getRandomNumberInRange(0, 360);
                Vector2f arcPoint1 = MathUtils.getPointOnCircumference(ship.getLocation(), distanceRandom1, angleRandom1);
                
                float distanceRandom2 = MathUtils.getRandomNumberInRange(radius * 0.4f, radius * 0.9f);
                float angleRandom2 = angleRandom1 + MathUtils.getRandomNumberInRange(140, 220);
                Vector2f arcPoint2 = MathUtils.getPointOnCircumference(ship.getLocation(), distanceRandom2, angleRandom2);
                
                Global.getCombatEngine().spawnEmpArcVisual(arcPoint1, ship, arcPoint2, ship, 8f,
        				new Color(25,100,155,45),
        				new Color(175,220,255,50));
            	
            	info.SPK_TIMER = 0f;
        	}
        }
        
        // when timer hits zero, explode the platform
        if (info.L_TIMER <= 0f) {	
        	ship.setHitpoints(1f);
        	
        	Global.getCombatEngine().spawnEmpArcPierceShields(ship,
        			ship.getLocation(),
        			ship,
        			ship,
        			DamageType.ENERGY,
        			4000f,
        			100f,
        			1000f,
        			"disabled_small_crit",
        			6f,
        			new Color(5,10,15,1),
        			new Color(5,5,5,1));
        }
        
        
        // platform weapon range boost
		MutableShipStatsAPI stats = ship.getMutableStats();
        float FLUX_USAGE = 1 - ship.getFluxLevel();
        
        if (ship.getVariant().getHullMods().contains("kyeltziv_loader_siege")) {
        	stats.getBallisticWeaponRangeBonus().modifyFlat(spec.getId(), (100f * FLUX_USAGE));
		}
        
		stats.getBallisticWeaponRangeBonus().modifyPercent(spec.getId(), BASE_RANGE_BONUS + (FLUX_RANGE_BONUS * FLUX_USAGE));
		stats.getEnergyWeaponRangeBonus().modifyPercent(spec.getId(), BASE_RANGE_BONUS + (FLUX_RANGE_BONUS * FLUX_USAGE));
		
		stats.getNonBeamPDWeaponRangeBonus().modifyPercent(spec.getId(), -(PD_RANGE_MALUS + (PD_FLUX_RANGE_MALUS * FLUX_USAGE)));
		stats.getBeamPDWeaponRangeBonus().modifyPercent(spec.getId(), -(PD_RANGE_MALUS + (PD_FLUX_RANGE_MALUS * FLUX_USAGE)));
		
		
		Global.getCombatEngine().getCustomData().put("KYEL_PLAT_BASE_DATA_KEY" + ship.getId(), info);
	}
	

    private class ShipSpecificData {
        private float L_TIMER = 70f;
        private float SPK_TIMER = 0f;
    }
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		return null;
	}

}
