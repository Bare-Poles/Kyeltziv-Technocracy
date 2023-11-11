package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.util.IntervalUtil;

public class kyeltziv_espBeamEffect implements BeamEffectPlugin {

	private IntervalUtil fxInterval = new IntervalUtil(0.16f, 0.16f); // consistent timing here, for reliable "muzzle" fx (3 procs of the muzzle, with the last being a bit before the end of the pulse, so visuals don't last beyond the pulse *too* much)
	private IntervalUtil procInterval = new IntervalUtil(0.16f, 0.24f); // this tick rate allows 2-3 ticks per "pulse", just a little bit of randomness to spice things up!
	private boolean wasZero = true;
	private float weightMult = 0.99f;
	
	public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
		CombatEntityAPI target = beam.getDamageTarget();

		float Dur = beam.getDamage().getDpsDuration();
		fxInterval.advance(Dur);
		
		if (fxInterval.intervalElapsed()) {
			
			Vector2f sparkVel = beam.getWeapon().getShip().getVelocity();
			
			for (int i=0; i < 2; i++) {
                  Vector2f randomVelA = MathUtils.getRandomPointOnCircumference(sparkVel, MathUtils.getRandomNumberInRange(30f, 70f));
                  
                  float randomSize = MathUtils.getRandomNumberInRange(6f, 24f);
                  Global.getCombatEngine().addSmoothParticle(beam.getFrom(),
                      randomVelA,
                      randomSize, //size
                      1.0f, //brightness
                      0.3f, //duration
                      new Color(130,50,180,65));
              }
			Vector2f randomVelB = MathUtils.getRandomPointOnCircumference(sparkVel, MathUtils.getRandomNumberInRange(30f, 70f));
			
			float randomSize = MathUtils.getRandomNumberInRange(5f, 15f);
			Global.getCombatEngine().addSmoothParticle(beam.getFrom(),
					randomVelB,
					randomSize, //size
					1.0f, //brightness
					0.25f, //duration
					new Color(110,255,250,70));
		}
		
		if (target instanceof ShipAPI) {
			if (!wasZero) Dur = 0;
			wasZero = beam.getDamage().getDpsDuration() <= 0;
			procInterval.advance(Dur);
			
			if (procInterval.intervalElapsed()) {
				Vector2f point = beam.getRayEndPrevFrame();
				
				Vector2f tagVel = target.getVelocity();
				
                Global.getCombatEngine().addSmoothParticle(point,
                		tagVel,
                    45f, //size
                    1.0f, //brightness
                    0.3f, //duration
                    new Color(90,50,180,100));

  				for (int i=0; i < 2; i++) {
	                  Vector2f randomVel1 = MathUtils.getRandomPointOnCircumference(tagVel, MathUtils.getRandomNumberInRange(30f, 70f));
	                  Vector2f point1 = MathUtils.getRandomPointInCircle(point, 4f);
	                  
	                  float randomSize1 = MathUtils.getRandomNumberInRange(5f, 15f);
	                  Global.getCombatEngine().addSmoothParticle(point1,
	                      randomVel1,
	                      randomSize1, //size
	                      1.0f, //brightness
	                      0.25f, //duration
	                      new Color(125,255,255,60));
	  				for (int j=0; j < 2; j++) {
		                  Vector2f randomVel2 = MathUtils.getRandomPointOnCircumference(tagVel, MathUtils.getRandomNumberInRange(30f, 70f));
		                  Vector2f point2 = MathUtils.getRandomPointInCircle(point, 5f);
		                  
		                  float randomSize2 = MathUtils.getRandomNumberInRange(5f, 20f);
		                  Global.getCombatEngine().addSmoothParticle(point2,
		                      randomVel2,
		                      randomSize2, //size
		                      1.0f, //brightness
		                      0.3f, //duration
		                      new Color(100,50,155,55));
		              }
	              }
	            
				weightMult = 0.89f;
				weightMult += (0.1f * Math.min(1f, (target.getMass() / 3500f) ) );
						// scaling from a 1% slow, to an "11%" slow, but it's practically impossible to hit the "true" maximum slow, as that'd require a ship with Zero mass
				 // the power scales with target mass, so faster+lighter targets are slowed more, but slower/heavier targets are slowed less
				 // the "peak" at which this stops scaling at is: 3500 mass (onslaught)
				 // the idea here is:
					// Heavier (Larger) ships tend to have proportionally worse acceleration than smaller ones, so they speed losses are more consequential (also it's harder to slow a heavier ship)
					// this means that even stacking a LOT of ESP Beams, you will have a lot of trouble "freezing" a capital ship, but they prove more effective at slowing down lighter ships
				
	    		tagVel.x = tagVel.x * weightMult;
	    		tagVel.y = tagVel.y * weightMult;
	    		target.getVelocity().set(tagVel);
	    		
	    		float spinMult = 0.92f;
				spinMult += (0.06f * Math.min(1f, (target.getMass() / 3500f) ) );
					// same scaling idea as with speed, but goes from 2% to "8%"
	    		
	    		float angVel = target.getAngularVelocity();
	            angVel *= spinMult;
	    		target.setAngularVelocity(angVel);
	    		
	    		// so these can really bully stuff when in large numbers (like 10+ or something stupid), but if you have this many beams, then you are going to be dealing quite a decent chunk of damage, so the slow won't matter all that much at that point.
	    		// I think this effect is fine, and more importantly, Funny.
			}
		}
	}
}