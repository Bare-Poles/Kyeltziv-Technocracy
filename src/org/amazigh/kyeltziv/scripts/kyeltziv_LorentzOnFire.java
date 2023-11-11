package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;

public class kyeltziv_LorentzOnFire implements OnFireEffectPlugin {

    // Explosion muzzle-flash configuration
    private static final Color FLASH_COLOR = new Color(225,185,95,155); // Explosion color
    
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {

        Vector2f vel = weapon.getShip().getVelocity();
        
        for (int i=0; i < 4; i++) {
        	Vector2f lrtzRandomVel =  MathUtils.getRandomPointOnCircumference(vel, MathUtils.getRandomNumberInRange(10f, 100f));
        	
        	engine.spawnProjectile(
        			projectile.getSource(),
        			projectile.getWeapon(),
        			"kyeltziv_lorentz_fragment",
        			projectile.getLocation(),
        			projectile.getFacing(),
        			lrtzRandomVel);
        }
        
        ShipAPI ship = weapon.getShip();
        engine.spawnExplosion(projectile.getLocation(), ship.getVelocity(), FLASH_COLOR, 2.5f, 0.25f);
    }
}