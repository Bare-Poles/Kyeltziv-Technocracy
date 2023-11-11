package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class kyeltziv_ShockDriverLightOnFire implements OnFireEffectPlugin {
    
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
    	
    	Vector2f ship_velocity = weapon.getShip().getVelocity();
    	Vector2f proj_location = projectile.getLocation();
    	
    	for (int ex=0; ex < 3; ex++) {
            float angleEx = projectile.getFacing() + (-50 + (ex * 25));
            Vector2f offsetVelEx = MathUtils.getPointOnCircumference(ship_velocity, MathUtils.getRandomNumberInRange(3f, 7.5f), angleEx);
            
            Vector2f pointEx = MathUtils.getPointOnCircumference(proj_location, MathUtils.getRandomNumberInRange(3f, 9f), angleEx);
        	
        	engine.addNebulaSmokeParticle(MathUtils.getRandomPointInCircle(pointEx, 10f),
        		offsetVelEx,
        		MathUtils.getRandomNumberInRange(10f, 13.5f), //size
        		MathUtils.getRandomNumberInRange(1.5f, 1.8f), //end mult
        		0.4f, //ramp fraction
        		0.5f, //full bright fraction
        		MathUtils.getRandomNumberInRange(0.9f, 1.1f), //duration
        		new Color(90,100,125,75));
        }
    	
        for (int j=0; j < 8; j++) {
        	float angle1 = projectile.getFacing() + MathUtils.getRandomNumberInRange(-5f, 5f);
            Vector2f offsetVel1 = MathUtils.getPointOnCircumference(ship_velocity, MathUtils.getRandomNumberInRange(3f, 40f), angle1);
            
            Vector2f point1 = MathUtils.getPointOnCircumference(proj_location, MathUtils.getRandomNumberInRange(2f, 15f), angle1);
            
            engine.addNebulaSmokeParticle(MathUtils.getRandomPointInCircle(point1, 3f),
            		offsetVel1,
            		MathUtils.getRandomNumberInRange(9f, 12f), //size
            		MathUtils.getRandomNumberInRange(1.4f, 1.7f), //end mult
            		0.4f, //ramp fraction
            		0.5f, //full bright fraction
            		MathUtils.getRandomNumberInRange(0.7f, 0.9f), //duration
            		new Color(90,105,135,75));
            
            for (int k=0; k < 3; k++) {
                float angle2 = projectile.getFacing() + MathUtils.getRandomNumberInRange(-8f, 8f);
                Vector2f offsetVel2 = MathUtils.getPointOnCircumference(ship_velocity, MathUtils.getRandomNumberInRange(15f, 150f), angle2);
                
                Vector2f point2 = MathUtils.getPointOnCircumference(proj_location, MathUtils.getRandomNumberInRange(4f, 35f), angle2);
                
                Global.getCombatEngine().addSmoothParticle(MathUtils.getRandomPointInCircle(point2, 4f),
                		offsetVel2,
                		MathUtils.getRandomNumberInRange(2f, 8f), //size
                		1.0f, //brightness
                		MathUtils.getRandomNumberInRange(0.5f, 0.6f), //duration
                		new Color(140,160,240,225));
            }
        	
        }
        
    	engine.spawnExplosion(proj_location, ship_velocity, new Color(100,100,240,155), 7f, 0.3f);
    }
}