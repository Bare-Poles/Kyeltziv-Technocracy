package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class kyeltziv_FougasseOnFire implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
	
	public static final int BARREL_COUNT = 6;
	private int shotCounter = 0;
	private static final Color FLASH_COLOR = new Color(165,185,225,155);
	
	public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
		
		Vector2f ship_velocity = weapon.getShip().getVelocity();
		Vector2f proj_location = projectile.getLocation();
		float angle = projectile.getFacing();
		
		// random projectile velocity thing (scales velocity from -15% to +10%)
		float velScale = projectile.getProjectileSpec().getMoveSpeed(weapon.getShip().getMutableStats(), weapon);
		Vector2f newVel = MathUtils.getPointOnCircumference(projectile.getVelocity(), MathUtils.getRandomNumberInRange(velScale * -0.15f, velScale * 0.1f) , angle);
		projectile.getVelocity().x = newVel.x;
		projectile.getVelocity().y = newVel.y;
		
		for (int i=0; i < 6; i++) {
			
			float angle1 = angle + MathUtils.getRandomNumberInRange(-30f, 30f);
			Vector2f sparkVel = MathUtils.getPointOnCircumference(ship_velocity, MathUtils.getRandomNumberInRange(10f, 100f), angle1);
			
			Global.getCombatEngine().addSmoothParticle(MathUtils.getRandomPointInCircle(proj_location, 3f),
					sparkVel,
					MathUtils.getRandomNumberInRange(3f, 6f), //size
					1f, //brightness
					MathUtils.getRandomNumberInRange(0.4f, 0.6f), //duration
					FLASH_COLOR);
		}
			
		shotCounter++;
		if (shotCounter >= BARREL_COUNT) {
			shotCounter = 0;
			engine.spawnExplosion(proj_location, ship_velocity, FLASH_COLOR, 5f, 0.2f);
			
		}
		
	}
	
	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
	}
	
}