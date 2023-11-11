package org.amazigh.kyeltziv.scripts;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class kyeltziv_TrocarOnFireEffect implements OnFireEffectPlugin {

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
    	
        engine.spawnProjectile(
        		projectile.getSource(),
        		projectile.getWeapon(),
        		"kyeltziv_trocar_latch",
        		projectile.getLocation(),
        		projectile.getFacing(),
        		weapon.getShip().getVelocity());
        
    	engine.removeEntity(projectile);
    }
}