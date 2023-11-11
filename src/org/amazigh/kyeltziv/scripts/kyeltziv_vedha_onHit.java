package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

public class kyeltziv_vedha_onHit implements OnHitEffectPlugin {
	
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
					  Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		
		Vector2f fxVel = new Vector2f();
		if (target != null) {
			fxVel.set(target.getVelocity());
		}
		
		float damageScalar = (1f + (projectile.getDamageAmount() / 80f)) / 2f;
		// cunning trick to scale the visual size of effects based on proj damage, so i can re-use the script across several weapons
		// half-way between the difference between the damage of the projectile and 80 is the multiplier
		// 160 damage (2x 80) = a 1.5x multiplier
		// 60 damage (0.75x 80) = a 0.875x multiplier
			// Should also scale up in size with any skill/etc buffs to damage, which is kinda cute
		
		// impact vfx, just because
		for (int i=0; i < (damageScalar * 36); i++) {
			
			float angle = (i * 9f) / damageScalar;
			Vector2f sparkRingLoc = MathUtils.getPointOnCircumference(point, damageScalar * MathUtils.getRandomNumberInRange(17f, 26f), angle);
			
			Global.getCombatEngine().addSmoothParticle(sparkRingLoc,
					MathUtils.getPointOnCircumference(fxVel, damageScalar * MathUtils.getRandomNumberInRange(3f, 21f), angle + MathUtils.getRandomNumberInRange(-4f, 4f)),
					MathUtils.getRandomNumberInRange(2f, 4f), //size
					0.8f, //brightness
					MathUtils.getRandomNumberInRange(0.45f, 0.6f), //duration
					new Color(175,255,175,240));
        }
		
		engine.addNebulaParticle(point,
				fxVel,
				damageScalar * 40f,
				MathUtils.getRandomNumberInRange(1.4f, 1.5f),
				0.4f,
				0.45f,
				0.6f,
				new Color(150,240,150,175));
		
		if (target instanceof ShipAPI) {
			for (int i=0; i < 2; i++) {
				// done like this so i can force it to bring them (close) to overload, and not just waste the entire soft flux spike if near max flux
				((ShipAPI) target).getFluxTracker().increaseFlux(projectile.getDamageAmount() * 0.25f, false);
				// Normal: (80 x 0.25 = 20) x 2 = 40
				// Bomber: (160 x 0.25 = 40) x 2 = 80
				// Drone: (60 x 0.25 = 15) x 2 = 30
			}
		}
	}
}
