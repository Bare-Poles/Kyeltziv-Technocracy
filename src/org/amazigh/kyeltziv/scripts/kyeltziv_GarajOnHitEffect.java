package org.amazigh.kyeltziv.scripts;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;

public class kyeltziv_GarajOnHitEffect implements OnHitEffectPlugin {
	
    private static final Color COLOR_P = new Color(105,155,125,255);
    private static final Color COLOR_X = new Color(60,75,50,155);
    private static final int BLAST_COUNT = 7;

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		
		Vector2f tagVel = new Vector2f();
		//if (target != null) {
		//	tagVel.set(target.getVelocity());
		//}
		
        float blastDamage = projectile.getDamageAmount()*0.25f;
        DamagingExplosionSpec blast = new DamagingExplosionSpec(0.2f,
                90f,
                50f,
                blastDamage,
                blastDamage/2f,
                CollisionClass.PROJECTILE_FF,
                CollisionClass.PROJECTILE_FIGHTER,
                2f,
                6f,
                0.8f,
                64,
                COLOR_P,
                COLOR_X);
        blast.setDamageType(DamageType.FRAGMENTATION);
        blast.setShowGraphic(false);

        for (int i = 0; i < BLAST_COUNT; i++) {
        	
            Vector2f blastPoint = MathUtils.getPointOnCircumference(point, MathUtils.getRandomNumberInRange(18f, 180f), projectile.getFacing() + MathUtils.getRandomNumberInRange(-42, 42));
            
            engine.spawnDamagingExplosion(blast,projectile.getSource(),blastPoint,false);
            
            float timerRandom = MathUtils.getRandomNumberInRange(0.5f, 0.7f);
            engine.spawnExplosion(blastPoint, tagVel, COLOR_X, 90f, timerRandom);
            
            engine.addSmoothParticle(
            		blastPoint,
					tagVel,
					120f, //size
					1.0f, //brightness
					0.05f, //duration
					COLOR_P);
            
        	engine.addSmoothParticle(
            		blastPoint,
					tagVel,
					40f, //size
					1.0f, //brightness
					0.15f, //duration
					new Color(155,140,105,255));
        	
            for (int j = 0; j < 4; j++) {
            	engine.addNebulaSmokeParticle(MathUtils.getRandomPointInCircle(blastPoint, 45f),
                		tagVel,
                		MathUtils.getRandomNumberInRange(28f, 32f), //size
                		2.3f, //end mult
                		0.65f, //ramp fraction
                		0.7f, //full bright fraction
                		timerRandom + MathUtils.getRandomNumberInRange(0.35f, 0.55f), //duration
                		new Color(105,95,90,70));
            	
                for (int k = 0; k < 4; k++) {
                	engine.addSmoothParticle(
                			MathUtils.getRandomPointInCircle(blastPoint, 11f),
                			MathUtils.getRandomPointInCircle(tagVel, 140f),
                			MathUtils.getRandomNumberInRange(4f, 9f), //size
        					0.9f, //brightness
        					timerRandom - 0.15f, //duration
        					new Color(165,135,100,255));
                }
            }
        }
	}
}

