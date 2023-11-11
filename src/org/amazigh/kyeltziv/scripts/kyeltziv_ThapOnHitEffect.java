package org.amazigh.kyeltziv.scripts;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

import org.lazywizard.lazylib.MathUtils;

public class kyeltziv_ThapOnHitEffect implements OnHitEffectPlugin {

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

    	for (int i = 0; i < 3; i++) {
    		float fullRandom = MathUtils.getRandomNumberInRange(120, 240);
            Vector2f randomVel = MathUtils.getRandomPointOnCircumference(null, MathUtils.getRandomNumberInRange(5f, 50f));
            
            engine.spawnProjectile(projectile.getSource(),
                    projectile.getWeapon(),
                    "kyeltziv_chaff_single",
                    point,
                    projectile.getFacing() + fullRandom,
                    randomVel);
    	}
	}
}
