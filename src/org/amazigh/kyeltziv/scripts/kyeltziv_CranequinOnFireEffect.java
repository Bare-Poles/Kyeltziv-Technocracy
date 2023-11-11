package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;

public class kyeltziv_CranequinOnFireEffect implements OnFireEffectPlugin {
    
    private static final Color FLASH_COLOR = new Color(150,165,230,200);
    
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
    	
            ShipAPI ship = weapon.getShip();
            Vector2f ship_velocity = ship.getVelocity();
            Vector2f proj_location = projectile.getLocation();
            
            engine.addHitParticle(proj_location, ship_velocity, 75f, 1f, 0.1f, FLASH_COLOR.brighter());
            
        	for (int i=0; i < 30; i++) {
        		
        		float angle = projectile.getFacing() + MathUtils.getRandomNumberInRange(-30f, 30f);
        		Vector2f sparkVel = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(18f, 130f), angle);
        		
        		Global.getCombatEngine().addSmoothParticle(MathUtils.getRandomPointInCircle(proj_location, 3f),
        				sparkVel,
        				MathUtils.getRandomNumberInRange(3f, 6f), //size
        				1f, //brightness
        				MathUtils.getRandomNumberInRange(0.35f, 0.5f), //duration
        				FLASH_COLOR);
        	}
        	
    }
  }