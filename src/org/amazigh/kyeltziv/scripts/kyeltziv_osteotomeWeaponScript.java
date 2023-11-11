package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.IntervalUtil;

public class kyeltziv_osteotomeWeaponScript implements EveryFrameWeaponEffectPlugin {

	private float steam = 0f;
	private IntervalUtil steamInterval = new IntervalUtil(0.05f, 0.05f);
	
	@Override
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
		
		// "steam/etc" "vent" visual effect
		
		if (weapon.getChargeLevel() > 0.96) {
			steam = 1.25f;
		}
		
		if (steam > 0f) {
			steamInterval.advance(amount);
			if (steamInterval.intervalElapsed()) {
				steam -= 0.05;
				
		        for (int i=0; i < 3; i++) {
					
					float angle1 = weapon.getCurrAngle() + MathUtils.getRandomNumberInRange(-4f, 4f);
		            Vector2f offsetVel1 = MathUtils.getPointOnCircumference(weapon.getShip().getVelocity(), MathUtils.getRandomNumberInRange(3f, 40f), angle1);
		            
		            Vector2f point1 = MathUtils.getPointOnCircumference(weapon.getFirePoint(0), MathUtils.getRandomNumberInRange(2f, 30f), angle1);
		            
		            engine.addNebulaSmokeParticle(MathUtils.getRandomPointInCircle(point1, 3f),
		            		offsetVel1,
		            		15f - (steam * 4f), //size
		            		MathUtils.getRandomNumberInRange(1.6f, 1.9f) - (steam * 0.32f), //end mult
		            		0.4f, //ramp fraction
		            		0.5f, //full bright fraction
		            		MathUtils.getRandomNumberInRange(0.7f, 1.0f), //duration
		            		new Color(115f/255f,
		            				100f/255f,
		            				140f/255f,
		            				(30f + (32f * steam))/255f));
		        	
		        }
			}
		}
		
	}
  }