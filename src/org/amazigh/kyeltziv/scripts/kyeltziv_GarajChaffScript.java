package org.amazigh.kyeltziv.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import org.jetbrains.annotations.NotNull;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.List;

public class kyeltziv_GarajChaffScript extends BaseEveryFrameCombatPlugin {

	private static final float TIMER = 2.0f; // the time the missile starts dropping chaff.
	private static final float DELAY = 0.8f;
	
	private MissileAPI missile;
	private float fxCounter;
	private float chaffCounter;
	
	public kyeltziv_GarajChaffScript(@NotNull MissileAPI missile) {
		this.missile = missile;
	}

	//Main advance method
	@Override
	public void advance(float amount, List<InputEventAPI> events) {
		//Sanity checks
		if (Global.getCombatEngine() == null) {
			return;
		}
		if (Global.getCombatEngine().isPaused()) {
			amount = 0f;
		}
		  CombatEngineAPI engine = Global.getCombatEngine();

		//Checks if our script should be removed from the combat engine
		if (missile == null || missile.didDamage() || missile.isFading() || !Global.getCombatEngine().isEntityInPlay(missile)) {
			engine.removePlugin(this);
			return;
		}
		
		// spawn a particle trail, because it looks kinda cool :)
		fxCounter += amount;
		if (fxCounter > 0.05f) {
			for (int i=0; i < 5; i++) {
				
				float fxVariance = (Math.min(chaffCounter, 1.6f) / 1.6f);
				Vector2f fxVel = MathUtils.getRandomPointOnCircumference(null, (MathUtils.getRandomNumberInRange(13f, 34f) * fxVariance));
				
				float alphaVariance = 60 + (61f * fxVariance);
				int fxAlpha = (int)alphaVariance;
				
				Global.getCombatEngine().addSmoothParticle(
						missile.getLocation(),
						fxVel,
						MathUtils.getRandomNumberInRange(4f, 12f), //size
						fxVariance, //brightness
						fxVariance * 1.8f, //duration
		                new Color(135,165,105,fxAlpha));
			}
			fxCounter -= 0.05f;
		}
		
		//Ticks up our timer, and if over the trigger time, start vomiting flares
		chaffCounter += amount;
		if (chaffCounter > TIMER) {
			
			for (int i=0; i < 2; i++) {
				
				Vector2f vel = (Vector2f) (missile.getVelocity());
				float chaffFacing = missile.getFacing() + 90f + (i * 180f);
				
				engine.spawnProjectile(missile.getWeapon().getShip(), missile.getWeapon(), "kyeltziv_chaff_garaj", missile.getLocation(), chaffFacing, vel);
                
				Vector2f smokeVel = new Vector2f(vel.x * 0.5f, vel.y * 0.5f);
				
                Vector2f puffRandomVel = MathUtils.getRandomPointOnCircumference(smokeVel, MathUtils.getRandomNumberInRange(4f, 12f));
            	engine.addSmokeParticle(missile.getLocation(), puffRandomVel, MathUtils.getRandomNumberInRange(10f, 20f), 0.8f, 0.8f, new Color(100,100,100,150));
            }
			chaffCounter -= DELAY;
            Global.getSoundPlayer().playSound("launch_flare_1", 1f, 1f, missile.getLocation(), missile.getVelocity());
		}
	}
}