package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;

public class kyeltziv_CompactShockDriverOnHitEffect implements OnHitEffectPlugin {

    private static final Color COLOR_P = new Color(255,205,205,255);
    private static final Color COLOR_X = new Color(25,100,155,255);
    
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		
		Vector2f tagVel = new Vector2f();
		if (target != null) {
			tagVel.set(target.getVelocity());
		}
		
		if (shieldHit && target instanceof ShipAPI) {
			float blastDam = projectile.getEmpAmount() * 0.8f;
	        DamagingExplosionSpec blast = new DamagingExplosionSpec(0.24f,
	                30f,
	                20f,
	                blastDam,
	                blastDam * 0.8f,
	                CollisionClass.PROJECTILE_FF,
	                CollisionClass.PROJECTILE_FIGHTER,
	                2.5f,
	                4.5f,
	                0.8f,
	                72,
	                COLOR_P,
	                COLOR_X);
	        blast.setDamageType(DamageType.ENERGY);
	        blast.setShowGraphic(true);
	        blast.setUseDetailedExplosion(false);
            engine.spawnDamagingExplosion(blast,projectile.getSource(),point,false);
            Global.getSoundPlayer().playSound("kyeltziv_shock_burst", 1.0f, 0.85f, point, tagVel);
    	}

    	if (!shieldHit && target instanceof ShipAPI) {

            Global.getCombatEngine().addSmoothParticle(point,
            		tagVel,
                    25f, //size
                    1.0f, //brightness
                    0.3f, //duration
                    COLOR_X);
            
            for (int i=0; i < 36; i++) {
                Vector2f randomVel = MathUtils.getRandomPointOnCircumference(tagVel, MathUtils.getRandomNumberInRange(2.5f, 21f));
                
                Vector2f randomPoint = MathUtils.getRandomPointInCircle(point, 12f);
                
                float randomSize = MathUtils.getRandomNumberInRange(2f, 6f);
                Global.getCombatEngine().addSmoothParticle(randomPoint,
                    randomVel,
                    randomSize, //size
                    0.6f, //brightness
                    0.6f, //duration
                    COLOR_P);
            }
    		
    		float dam = projectile.getEmpAmount() * 0.5f;
    		float emp = projectile.getEmpAmount() * 1.5f;
    		float rng = 0.25f;
    		float arc_num = ((ShipAPI)target).getFluxLevel();
    		arc_num *= 2.9f;
    	    arc_num += 0.1f;
    		
    		if (rng >= (float) Math.random()) {
    			arc_num += 1.0f;
    		}
    		
    		for (int i = 0; i < arc_num; i++) {
    			engine.spawnEmpArc(projectile.getSource(), point, target, target,
    					DamageType.ENERGY, 
    					dam, // damage
    					emp, // emp 
    					100000f, // max range 
    					"tachyon_lance_emp_impact",
    					20f, // thickness
    					new Color(25,100,155,255),
    					new Color(255,255,255,255));
	  		}
    	}
	}
}
