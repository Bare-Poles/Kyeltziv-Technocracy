package org.amazigh.kyeltziv.scripts;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class kyeltziv_KillCloudOnFire implements OnFireEffectPlugin {

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
    	
        float angle = projectile.getFacing();
    	// random projectile velocity thing (scales velocity from -20% to +20%)
        float velScale = projectile.getProjectileSpec().getMoveSpeed(weapon.getShip().getMutableStats(), weapon);
        Vector2f newVel = MathUtils.getPointOnCircumference(projectile.getVelocity(), MathUtils.getRandomNumberInRange(velScale * -0.2f, velScale * 0.2f) , angle);
        projectile.getVelocity().x = newVel.x;
        projectile.getVelocity().y = newVel.y;
    	
    }
}