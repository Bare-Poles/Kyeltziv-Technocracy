package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class kyeltziv_ShrapDriverOnFire implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {

	public static final int BARREL_COUNT = 18;
    private int shotCounter = 0;
	
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
		
        Vector2f ship_velocity = weapon.getShip().getVelocity();
        Vector2f proj_location = projectile.getLocation();
        float angle = projectile.getFacing();
        
        // random projectile velocity thing (scales velocity from -10% to +10%)
		float velScale = projectile.getProjectileSpec().getMoveSpeed(weapon.getShip().getMutableStats(), weapon);
		Vector2f newVel = MathUtils.getPointOnCircumference(projectile.getVelocity(), MathUtils.getRandomNumberInRange(velScale * -0.1f, velScale * 0.1f) , angle);
		projectile.getVelocity().x = newVel.x;
		projectile.getVelocity().y = newVel.y;
        
		shotCounter++;
    	if (shotCounter >= BARREL_COUNT) {
    		shotCounter = 0;
    		
        	for (int ex=0; ex < 3; ex++) {
                float angleEx = angle + (-80 + (ex * 40));
                Vector2f offsetVelEx = MathUtils.getPointOnCircumference(ship_velocity, MathUtils.getRandomNumberInRange(3f, 7.5f), angleEx);
                
                Vector2f pointEx = MathUtils.getPointOnCircumference(proj_location, MathUtils.getRandomNumberInRange(3f, 9f), angleEx);
            	
            	engine.addNebulaSmokeParticle(MathUtils.getRandomPointInCircle(pointEx, 10f),
            		offsetVelEx,
            		MathUtils.getRandomNumberInRange(12f, 16f), //size
            		MathUtils.getRandomNumberInRange(1.5f, 1.8f), //end mult
            		0.4f, //ramp fraction
            		0.5f, //full bright fraction
            		MathUtils.getRandomNumberInRange(1.2f, 1.4f), //duration
            		new Color(125,100,90,75));
            }
        	
            for (int j=0; j < 10; j++) {
            	float angle1 = angle + MathUtils.getRandomNumberInRange(-10f, 10f);
                Vector2f offsetVel1 = MathUtils.getPointOnCircumference(ship_velocity, MathUtils.getRandomNumberInRange(3f, 40f), angle1);
                
                Vector2f point1 = MathUtils.getPointOnCircumference(proj_location, MathUtils.getRandomNumberInRange(2f, 15f), angle1);
                
                engine.addNebulaSmokeParticle(MathUtils.getRandomPointInCircle(point1, 3f),
                		offsetVel1,
                		MathUtils.getRandomNumberInRange(9f, 12f), //size
                		MathUtils.getRandomNumberInRange(1.4f, 1.7f), //end mult
                		0.4f, //ramp fraction
                		0.5f, //full bright fraction
                		MathUtils.getRandomNumberInRange(1.0f, 1.2f), //duration
                		new Color(135,105,90,75));
                
                for (int k=0; k < 3; k++) {
                    float angle2 = angle + MathUtils.getRandomNumberInRange(-15f, 15f);
                    Vector2f offsetVel2 = MathUtils.getPointOnCircumference(ship_velocity, MathUtils.getRandomNumberInRange(18f, 180f), angle2);
                    
                    Vector2f point2 = MathUtils.getPointOnCircumference(proj_location, MathUtils.getRandomNumberInRange(4f, 40f), angle2);
                    
                    Global.getCombatEngine().addSmoothParticle(MathUtils.getRandomPointInCircle(point2, 4f),
                    		offsetVel2,
                    		MathUtils.getRandomNumberInRange(2f, 9f), //size
                    		1.0f, //brightness
                    		MathUtils.getRandomNumberInRange(0.85f, 0.95f), //duration
                    		new Color(240,160,140,225));
                }
            	
            }
    		
        	engine.spawnExplosion(proj_location, ship_velocity, new Color(240,120,80,155), 10f, 0.5f);
    		Global.getSoundPlayer().playSound("flak_fire", 0.75f, 1.6f, proj_location, ship_velocity);
    	}
    	
    }

	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
	}

  }