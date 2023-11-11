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

public class kyeltziv_DronePlatform extends BaseHullMod {

	public static final float HEALTH_BONUS = 100f;
	public static final float REPAIR_RATE = 50f;
	
	public static final float EN_RANGE_BONUS = 200f;
	public static final float PD_DAMAGE_BONUS = 40f;
	
	public static final float EMP_DAMAGE_BONUS = 20f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS);
		stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - (REPAIR_RATE * 0.01f));
		
		stats.getEnergyWeaponRangeBonus().modifyFlat(id, EN_RANGE_BONUS);
		stats.getDamageToFighters().modifyPercent(id, PD_DAMAGE_BONUS);
		
		stats.getDamageToTargetEnginesMult().modifyPercent(id, EMP_DAMAGE_BONUS);
		stats.getDamageToTargetWeaponsMult().modifyPercent(id, EMP_DAMAGE_BONUS);
	}
	
	public void advanceInCombat(ShipAPI ship, float amount){
		if (Global.getCombatEngine().isPaused() || !ship.isAlive() || ship.isPiece()) {
			return;
		}
        ShipSpecificData info = (ShipSpecificData) Global.getCombatEngine().getCustomData().get("KOHTA_DATA_KEY" + ship.getId());
        if (info == null) {
            info = new ShipSpecificData();
        }
        
        // SPEEEN
        ship.giveCommand(ShipCommand.TURN_LEFT, null, 0);
        
        
        // the self destruct timer, to prevent meme cheesery with stacking infinite platforms.
        info.L_TIMER -= amount;
        
        // do some "sparking" fx as lifetime gets low, with rate increasing as timer gets to zero.
        if (info.L_TIMER <= 4f) {
        	info.SPK_TIMER += amount;
        	if (info.SPK_TIMER > Math.max(info.L_TIMER / 3f, 0.25f)) {
            	Global.getSoundPlayer().playSound("disabled_small", MathUtils.getRandomNumberInRange(0.9f, 1.2f), MathUtils.getRandomNumberInRange(0.3f, 0.6f), ship.getLocation(), ship.getVelocity());
            	
            	float distanceRandom1 = MathUtils.getRandomNumberInRange(10f, 24f);
        		float angleRandom1 = MathUtils.getRandomNumberInRange(0, 360);
                Vector2f arcPoint1 = MathUtils.getPointOnCircumference(ship.getLocation(), distanceRandom1, angleRandom1);
                
                float distanceRandom2 = MathUtils.getRandomNumberInRange(10f, 24f);
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
        			1600f,
        			100f,
        			1000f,
        			"disabled_small_crit",
        			6f,
        			new Color(5,10,15,1),
        			new Color(5,5,5,1));
        }
        
		Global.getCombatEngine().getCustomData().put("KOHTA_DATA_KEY" + ship.getId(), info);
	}
	

    private class ShipSpecificData {
        private float L_TIMER = 40f;
        private float SPK_TIMER = 0f;
    }
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HEALTH_BONUS + "%";
		if (index == 1) return "" + (int) REPAIR_RATE + "%";
		return null;
	}

}
