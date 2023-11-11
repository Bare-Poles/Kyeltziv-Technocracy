package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;

public class kyeltziv_fusionOnHitEffect implements OnHitEffectPlugin {
	
	private static final Color COLOR_P = new Color(255,105,75,255);
    private static final Color COLOR_X = new Color(255,155,125,125);
    
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		
		if (0.2f >= (float) Math.random()) {
			Vector2f tagVel = new Vector2f();
			if (target != null) {
				tagVel.set(target.getVelocity());
			}
			
			float damage = projectile.getDamageAmount() * 0.5f;
			float radius = damage * 0.4f;
			
			DamagingExplosionSpec blast = new DamagingExplosionSpec(0.15f,
					radius,
					radius * 0.66f,
					damage,
					damage/2f,
					CollisionClass.PROJECTILE_FF,
					CollisionClass.PROJECTILE_FIGHTER,
					4f,
					4f,
					0.8f,
					(int)radius,
					COLOR_P,
					COLOR_X);
			blast.setDamageType(DamageType.ENERGY);
			blast.setShowGraphic(true);
			blast.setUseDetailedExplosion(false);
			
			engine.spawnDamagingExplosion(blast,projectile.getSource(),point,false);
			
			Global.getSoundPlayer().playSound("explosion_flak", 1f, 0.5f, point, tagVel);
			
		}
		
		
	}
}