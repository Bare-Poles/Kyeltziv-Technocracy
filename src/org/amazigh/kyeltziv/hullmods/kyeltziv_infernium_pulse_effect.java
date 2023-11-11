package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.util.IntervalUtil;

public class kyeltziv_infernium_pulse_effect extends BaseHullMod {

	private IntervalUtil interval1 = new IntervalUtil(0.05f,0.15f); // spark/smoke #1
	private IntervalUtil interval2 = new IntervalUtil(0.05f,0.15f); // spark/smoke #2
		// done in two with time variance so that there is reduced "clumping" of particles
	
	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		
		CombatEngineAPI engine = Global.getCombatEngine();
		
		ShipSpecificData info = (ShipSpecificData) engine.getCustomData().get("KYELTZIV_INFERNIUM_PULSE_DATA_KEY" + ship.getId());
        if (info == null) {
            info = new ShipSpecificData();
        }
		
		if (info.PULSE_READY && ship.getSystem().isStateActive() ) {
			info.PULSE_READY = false;
			
			CombatUtils.applyForce(ship, ship.getFacing() + MathUtils.getRandomNumberInRange(-18f, 18f), 1111f);
			// so we apply a (slightly inaccurate) burst of force to the ship, because it's funny and makes it go *nyoom*
			// this part is done in a hullmod, so that i can keep using the vanilla system AI.
			
			Global.getSoundPlayer().playSound("explosion_fleet_member", 1f, 1.0f, ship.getLocation(), ship.getVelocity());
		}
		
		// doing the effects in the hullmod as well, because vanilla system AI is stinky.
		if (ship.getSystem().isActive()) {
			float effectLevel = ship.getSystem().getEffectLevel();
			interval1.advance(engine.getElapsedInLastFrame());
			interval2.advance(engine.getElapsedInLastFrame());
	        if (interval1.intervalElapsed()) {
	        	Vector2f smokePos1 = MathUtils.getPointOnCircumference(ship.getLocation(), 50f, ship.getFacing() + 180f);
	        	
	        	Vector2f smokePos2 = MathUtils.getPointOnCircumference(ship.getLocation(), 36.5f, ship.getFacing() + 152f);
	        	Vector2f smokePos3 = MathUtils.getPointOnCircumference(ship.getLocation(), 36.5f, ship.getFacing() + 208f);
	        	// manually creating offset locations for the engines, so i can use vanilla system AI (vanilla AI is picky about a lot of things)
	        	
	        	Vector2f shipVel = new Vector2f();
	        	shipVel.x += ship.getVelocity().x;
	        	shipVel.y += ship.getVelocity().y;
	        	
	        	Vector2f smokeVel1 = (Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.4f, 0.5f));
	        	Vector2f smokeVel2 = (Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.4f, 0.5f));
	        	Vector2f smokeVel3 = (Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.4f, 0.5f));
	        	
	        	float effectMult = Math.max(0.3f, effectLevel);
	        	
	        	engine.addNebulaParticle(MathUtils.getRandomPointInCircle(smokePos1, 8f),
	        			smokeVel1,
	        			MathUtils.getRandomNumberInRange(26f, 36f), //size
	        			2.2f, //end mult
	        			0.5f, //ramp fraction
	        			0.75f * effectMult, //full bright fraction
	        			0.4f, //1.1 //duration
	        			new Color(115,105,100,80), //105 alpha
	        			true);
	        	engine.addNebulaParticle(MathUtils.getRandomPointInCircle(smokePos2, 5f),
	        			smokeVel2,
	        			MathUtils.getRandomNumberInRange(15f, 24f), //size
	        			2.1f, //end mult
	        			0.5f, //ramp fraction
	        			0.75f * effectMult, //full bright fraction
	        			0.4f, //1.1 //duration
	        			new Color(115,105,100,75),
	        			true);
	        	engine.addNebulaParticle(MathUtils.getRandomPointInCircle(smokePos3, 5f),
	        			smokeVel3,
	        			MathUtils.getRandomNumberInRange(15f, 24f), //size
	        			2.1f, //end mult
	        			0.5f, //ramp fraction
	        			0.75f * effectMult, //full bright fraction
	        			0.4f, //1.1 //duration
	        			new Color(115,105,100,75),
	        			true);
	        	
	        	for (int i=0; i < 3; i++) {
	        		
	        		Vector2f sparkVel1 = MathUtils.getRandomPointOnCircumference((Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.3f, 0.4f)), MathUtils.getRandomNumberInRange(24f, 80f));
	        		Vector2f sparkVel2 = MathUtils.getRandomPointOnCircumference((Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.3f, 0.4f)), MathUtils.getRandomNumberInRange(24f, 80f));
	        		Vector2f sparkVel3 = MathUtils.getRandomPointOnCircumference((Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.3f, 0.4f)), MathUtils.getRandomNumberInRange(24f, 80f));
	        		
	        		engine.addSmoothParticle(smokePos1,
	        				sparkVel1,
	        				MathUtils.getRandomNumberInRange(4f, 10f), //size
	        				1.0f * effectMult, //brightness
	        				0.45f, //duration
	        				new Color(255,110,80,255));
	        		engine.addSmoothParticle(smokePos2,
	        				sparkVel2,
	        				MathUtils.getRandomNumberInRange(4f, 10f), //size
	        				1.0f * effectMult, //brightness
	        				0.45f, //duration
	        				new Color(255,110,80,255));
	        		engine.addSmoothParticle(smokePos3,
	        				sparkVel3,
	        				MathUtils.getRandomNumberInRange(4f, 10f), //size
	        				1.0f * effectMult, //brightness
	        				0.45f, //duration
	        				new Color(255,110,80,255));
	        	}
	        }
	        if (interval2.intervalElapsed()) {
	        	Vector2f smokePos1 = MathUtils.getPointOnCircumference(ship.getLocation(), 50f, ship.getFacing() + 180f);
	        	
	        	Vector2f smokePos2 = MathUtils.getPointOnCircumference(ship.getLocation(), 36.5f, ship.getFacing() + 152f);
	        	Vector2f smokePos3 = MathUtils.getPointOnCircumference(ship.getLocation(), 36.5f, ship.getFacing() + 208f);
	        	// manually creating offset locations for the engines, so i can use vanilla system AI (vanilla AI is picky about a lot of things)
	        	
	        	Vector2f shipVel = new Vector2f();
	        	shipVel.x += ship.getVelocity().x;
	        	shipVel.y += ship.getVelocity().y;
	        	
	        	Vector2f smokeVel1 = (Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.4f, 0.5f));
	        	Vector2f smokeVel2 = (Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.4f, 0.5f));
	        	Vector2f smokeVel3 = (Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.4f, 0.5f));
	        	
	        	float effectMult = Math.max(0.3f, effectLevel);
	        	
	        	engine.addNebulaParticle(MathUtils.getRandomPointInCircle(smokePos1, 8f),
	        			smokeVel1,
	        			MathUtils.getRandomNumberInRange(26f, 36f), //size
	        			2.2f, //end mult
	        			0.5f, //ramp fraction
	        			0.75f * effectMult, //full bright fraction
	        			0.4f, //1.1 //duration
	        			new Color(115,105,100,80), //105 alpha
	        			true);
	        	engine.addNebulaParticle(MathUtils.getRandomPointInCircle(smokePos2, 5f),
	        			smokeVel2,
	        			MathUtils.getRandomNumberInRange(15f, 24f), //size
	        			2.1f, //end mult
	        			0.5f, //ramp fraction
	        			0.75f * effectMult, //full bright fraction
	        			0.4f, //1.1 //duration
	        			new Color(115,105,100,75),
	        			true);
	        	engine.addNebulaParticle(MathUtils.getRandomPointInCircle(smokePos3, 5f),
	        			smokeVel3,
	        			MathUtils.getRandomNumberInRange(15f, 24f), //size
	        			2.1f, //end mult
	        			0.5f, //ramp fraction
	        			0.75f * effectMult, //full bright fraction
	        			0.4f, //1.1 //duration
	        			new Color(115,105,100,75),
	        			true);
	        	
	        	for (int i=0; i < 3; i++) {
	        		
	        		Vector2f sparkVel1 = MathUtils.getRandomPointOnCircumference((Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.3f, 0.4f)), MathUtils.getRandomNumberInRange(24f, 80f));
	        		Vector2f sparkVel2 = MathUtils.getRandomPointOnCircumference((Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.3f, 0.4f)), MathUtils.getRandomNumberInRange(24f, 80f));
	        		Vector2f sparkVel3 = MathUtils.getRandomPointOnCircumference((Vector2f) shipVel.scale(MathUtils.getRandomNumberInRange(0.3f, 0.4f)), MathUtils.getRandomNumberInRange(24f, 80f));
	        		
	        		engine.addSmoothParticle(smokePos1,
	        				sparkVel1,
	        				MathUtils.getRandomNumberInRange(4f, 10f), //size
	        				1.0f * effectMult, //brightness
	        				0.45f, //duration
	        				new Color(255,110,80,255));
	        		engine.addSmoothParticle(smokePos2,
	        				sparkVel2,
	        				MathUtils.getRandomNumberInRange(4f, 10f), //size
	        				1.0f * effectMult, //brightness
	        				0.45f, //duration
	        				new Color(255,110,80,255));
	        		engine.addSmoothParticle(smokePos3,
	        				sparkVel3,
	        				MathUtils.getRandomNumberInRange(4f, 10f), //size
	        				1.0f * effectMult, //brightness
	        				0.45f, //duration
	        				new Color(255,110,80,255));
	        	}
	        }
		}
		
		if (!info.PULSE_READY && !ship.getSystem().isStateActive()) {
			info.PULSE_READY = true;
		}
		engine.getCustomData().put("KYELTZIV_INFERNIUM_PULSE_DATA_KEY" + ship.getId(), info);
	}
	
    private class ShipSpecificData {
    	private boolean PULSE_READY = true;
    }
}
