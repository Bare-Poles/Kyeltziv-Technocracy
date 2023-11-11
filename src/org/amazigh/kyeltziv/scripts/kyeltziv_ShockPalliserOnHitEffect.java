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

public class kyeltziv_ShockPalliserOnHitEffect implements OnHitEffectPlugin {

    private static final Color COLOR_P = new Color(255,205,205,255);
    private static final Color COLOR_X = new Color(25,100,155,255);

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		
		Vector2f tagVel = new Vector2f();
		if (target != null) {
			tagVel.set(target.getVelocity());
		}
		
		if (shieldHit && target instanceof ShipAPI) {
			float blastDam = projectile.getEmpAmount() * 1.25f;
	        DamagingExplosionSpec blast = new DamagingExplosionSpec(0.24f,
	                25f,
	                15f,
	                blastDam,
	                blastDam * 0.8f,
	                CollisionClass.PROJECTILE_FF,
	                CollisionClass.PROJECTILE_FIGHTER,
	                2.0f,
	                4.0f,
	                0.8f,
	                64,
	                COLOR_P,
	                COLOR_X);
	        blast.setDamageType(DamageType.ENERGY);
	        blast.setShowGraphic(true);
	        blast.setUseDetailedExplosion(false);
            engine.spawnDamagingExplosion(blast,projectile.getSource(),point,false);
            Global.getSoundPlayer().playSound("kyeltziv_shock_burst", 1.0f, 0.80f, point, tagVel);
    	}

    if (!shieldHit && target instanceof ShipAPI) {
    	
    	Global.getCombatEngine().addSmoothParticle(point,
    			tagVel,
                20f, //size
                1.0f, //brightness
                0.3f, //duration
                COLOR_X);
        
        for (int i=0; i < 32; i++) {
            Vector2f randomVel = MathUtils.getRandomPointOnCircumference(tagVel, MathUtils.getRandomNumberInRange(2f, 17f));

            Vector2f randomPoint = MathUtils.getRandomPointInCircle(point, 10f);
            
            float randomSize = MathUtils.getRandomNumberInRange(1.5f, 5f);
            Global.getCombatEngine().addSmoothParticle(randomPoint,
                randomVel,
                randomSize, //size
                0.6f, //brightness
                0.6f, //duration
                COLOR_P);
        }

      float dam = projectile.getEmpAmount();
      float emp = projectile.getEmpAmount() * 1.5f;
      float rng = 0.2f;
      float arc_num = ((ShipAPI)target).getFluxLevel();
      arc_num *= 2.9f;
      
      if (rng >= (float) Math.random()) {
        arc_num += 1.1f;
      }

      for (int i = 1; i < arc_num; i++) {
        engine.spawnEmpArc(projectile.getSource(), point, target, target,
          DamageType.ENERGY, 
          dam, // damage
          emp, // emp 
          250f, // max range 
          "tachyon_lance_emp_impact",
          18f, // thickness
          new Color(25,100,155,155),
          new Color(255,255,255,155));
	  	}
	  	
	  }
	}
}
