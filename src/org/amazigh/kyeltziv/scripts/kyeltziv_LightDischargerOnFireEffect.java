package org.amazigh.kyeltziv.scripts;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class kyeltziv_LightDischargerOnFireEffect implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
    
	public static final int BARREL_COUNT = 3;
    private int shotCounter = 0;
    
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {

        	shotCounter++;
        	if (shotCounter >= BARREL_COUNT) {
        		shotCounter = 0;

            	Vector2f vel = weapon.getShip().getVelocity();
                float angle = projectile.getFacing();
        		engine.spawnProjectile(weapon.getShip(), weapon, "kyeltziv_light_discharger_jet_1", projectile.getLocation(), angle, vel);
            	engine.spawnProjectile(weapon.getShip(), weapon, "kyeltziv_light_discharger_jet_3", projectile.getLocation(), angle, vel);
            	
        	} else {
            	engine.removeEntity(projectile);
        	}
    		
    }
	
	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
	}
  }