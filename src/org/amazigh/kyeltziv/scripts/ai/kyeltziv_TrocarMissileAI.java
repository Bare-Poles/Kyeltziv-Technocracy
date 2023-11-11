// Based on the MagicMissileAI script By Tartiflette.
// this script is a custom ""arc shooting DEM"" script
package org.amazigh.kyeltziv.scripts.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.GuidedMissileAI;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.util.IntervalUtil;

import org.magiclib.util.MagicTargeting;

import java.awt.Color;

import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class kyeltziv_TrocarMissileAI implements MissileAIPlugin, GuidedMissileAI {
    
	//////////////////////
	//     SETTINGS     //
	//////////////////////
	
	//Angle with the target beyond which the missile turn around without accelerating. Avoid endless circling.
	//  Set to a negative value to disable
	private final float OVERSHOT_ANGLE=60;
	
	//Time to complete a wave in seconds.
	private final float WAVE_TIME=2;
	
	//Max angle of the waving in degree (divided by 3 with ECCM). Set to a negative value to avoid all waving.
	private final float WAVE_AMPLITUDE=15;
	
	//Damping of the turn speed when closing on the desired aim. The smaller the snappier.
	private final float DAMPING=0.1f;
	
	//Does the missile try to correct it's velocity vector as fast as possible or just point to the desired direction and drift a bit?
	//  Can create strange results with large waving
	//  Require a projectile with a decent turn rate and around twice that in turn acceleration
	//  Usefull for slow torpedoes with low forward acceleration, or ultra precise anti-fighter missiles.     
	private final boolean OVERSTEER=false;  //REQUIRE NO OVERSHOOT ANGLE!
	
	//Does the missile switch its target if it has been destroyed?
	private final boolean TARGET_SWITCH=true;

	//Target class priorities
	//set to 0 to ignore that class
	private final int fighters=1;
	private final int frigates=2;
	private final int destroyers=3;
	private final int cruisers=4;
	private final int capitals=5;

	//Arc to look for targets into
	//set to 360 or more to ignore
	private final int SEARCH_CONE=360;

	//range in which the missile seek a target in game units.
	private final int MAX_SEARCH_RANGE = 1500;

	//should the missile fall back to the closest enemy when no target is found within the search parameters
	//only used with limited search cones
	private final boolean FAILSAFE = false;

	//range under which the missile start to get progressively more precise in game units.
	private float PRECISION_RANGE=400;

	//Is the missile lead the target or tailchase it?
	private final boolean LEADING=true;

	//Leading loss without ECCM hullmod. The higher, the less accurate the leading calculation will be.
	//   1: perfect leading with and without ECCM
	//   2: half precision without ECCM
	//   3: a third as precise without ECCM. Default
	//   4, 5, 6 etc : 1/4th, 1/5th, 1/6th etc precision.
	private float ECCM=3f;   //A VALUE BELOW 1 WILL PREVENT THE MISSILE FROM EVER HITTING ITS TARGET!
	
	// how many arcs to fire
	private int AMMO = 10;
	
	// checks for whether the missile is:  within range / firing
	private boolean RANGE = false;
	private boolean ENGINE = true;
	private boolean FIRING = false;
	
	// square of range to switch to "attack mode"
	private float DETECT_RANGE=250000; //500^2
	
	// timers for:  startup / shooting / vfx / damping
	private IntervalUtil startInterval = new IntervalUtil(0.5f, 0.5f);
	private IntervalUtil shotInterval = new IntervalUtil(0.06f, 0.12f);
	private IntervalUtil vfxInterval = new IntervalUtil(0.05f, 0.06f);
	private IntervalUtil dampInterval = new IntervalUtil(0.05f, 0.05f);
	
	//////////////////////
	//    VARIABLES     //
	//////////////////////
	
	//max speed of the missile after modifiers.
	private final float MAX_SPEED;
	//Random starting offset for the waving.
	private final float OFFSET;
	private CombatEngineAPI engine;
	private final MissileAPI MISSILE;
	private CombatEntityAPI target;
	private Vector2f lead = new Vector2f();
	private boolean launch=true;
	private float timer=0, check=0f;
	
	//////////////////////
	//  DATA COLLECTING //
	//////////////////////
	
    public kyeltziv_TrocarMissileAI(MissileAPI missile, ShipAPI launchingShip) {
        this.MISSILE = missile;
        MAX_SPEED = missile.getMaxSpeed();
        if (missile.getSource().getVariant().getHullMods().contains("eccm")){
            ECCM=1;
        }
        
        //calculate the precision range factor
        PRECISION_RANGE=(float)Math.pow((2*PRECISION_RANGE),2);
        OFFSET=(float)(Math.random()*MathUtils.FPI*2);
    }
    
    //////////////////////
    //   MAIN AI LOOP   //
    //////////////////////
    
    @Override
    public void advance(float amount) {
        
        if (engine != Global.getCombatEngine()) {
            this.engine = Global.getCombatEngine();
        }
        
        //skip the AI if the game is paused, the missile is engineless or fading
        if (Global.getCombatEngine().isPaused() || MISSILE.isFading() || MISSILE.isFizzling()) {return;}
        
        //assigning a target if there is none or it got destroyed
        if (target == null
                || (TARGET_SWITCH 
                        && ((target instanceof ShipAPI && !((ShipAPI) target).isAlive())
                                  || !engine.isEntityInPlay(target))
                   )
                ){
            setTarget(
                    MagicTargeting.pickTarget(
                        MISSILE,
                        MagicTargeting.targetSeeking.NO_RANDOM,
                        MAX_SEARCH_RANGE,
                        SEARCH_CONE,
                        fighters,
                        frigates, 
                        destroyers,
                        cruisers,
                        capitals, 
                        FAILSAFE
                )
            );
            //forced acceleration by default
            MISSILE.giveCommand(ShipCommand.ACCELERATE);
            
        	vfxInterval.advance(amount);
        	if (vfxInterval.intervalElapsed()) {
        		 // spawn some particle jets, just for fun
        		for (int i=0; i < 3; i++) {
    				
    				Vector2f sparkVel1 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(24f, 144f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(155f, 165f));
    				Vector2f sparkVel2 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(24f, 144f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(195f, 205f));
    				
    				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
    						sparkVel1,
    						MathUtils.getRandomNumberInRange(2f, 6f), //size
    						0.8f, //brightness
    						MathUtils.getRandomNumberInRange(0.3f, 0.45f), //duration
    						new Color(175,205,255,200));
    				
    				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
    						sparkVel2,
    						MathUtils.getRandomNumberInRange(2f, 6f), //size
    						0.8f, //brightness
    						MathUtils.getRandomNumberInRange(0.3f, 0.45f), //duration
    						new Color(175,205,255,200));
    			}
        	}
            
            return;
        }
        
        timer+=amount;
        //finding lead point to aim to        
        if(launch || timer>=check){
            launch=false;
            timer -=check;
            //set the next check time
            check = Math.min(
                    0.25f,
                    Math.max(
                            0.05f,
                            MathUtils.getDistanceSquared(MISSILE.getLocation(), target.getLocation())/PRECISION_RANGE)
            );
            if(LEADING){
                //best intercepting point
            	float leadVel =  MAX_SPEED*ECCM;
            	
            	lead = AIUtils.getBestInterceptPoint(
                        MISSILE.getLocation(),
                        leadVel,
                        target.getLocation(),
                        target.getVelocity()
                );
                //null pointer protection
                if (lead == null) {
                    lead = target.getLocation(); 
                }
            } else {
                lead = target.getLocation();
            }
        }
        
        //best velocity vector angle for interception
        float correctAngle = VectorUtils.getAngle(
                        MISSILE.getLocation(),
                        lead
                );
        
        if (OVERSTEER){
            //velocity angle correction
            float offCourseAngle = MathUtils.getShortestRotation(
                    VectorUtils.getFacing(MISSILE.getVelocity()),
                    correctAngle
                    );

            float correction = MathUtils.getShortestRotation(                
                    correctAngle,
                    VectorUtils.getFacing(MISSILE.getVelocity())+180
                    ) 
                    * 0.5f * //oversteer
                    (float)((FastTrig.sin(MathUtils.FPI/90*(Math.min(Math.abs(offCourseAngle),45))))); //damping when the correction isn't important

            //modified optimal facing to correct the velocity vector angle as soon as possible
            correctAngle = correctAngle+correction;
        }
        
        if(WAVE_AMPLITUDE>0){            
            //waving
            float multiplier=1;
            if(ECCM<=1){
                multiplier=0.3f;
            }
            correctAngle+=multiplier*WAVE_AMPLITUDE*check*Math.cos(OFFSET+MISSILE.getElapsed()*(2*MathUtils.FPI/WAVE_TIME));
        }
        
        //target angle for interception        
        float aimAngle = MathUtils.getShortestRotation( MISSILE.getFacing(), correctAngle);
        
        if(OVERSHOT_ANGLE<=0 || Math.abs(aimAngle)<OVERSHOT_ANGLE){
        	if (ENGINE) {
        		MISSILE.giveCommand(ShipCommand.ACCELERATE);
        	}
        	
        	vfxInterval.advance(amount);
        	if (vfxInterval.intervalElapsed()) {
        		 // spawn some particle jets, just for fun
        		for (int i=0; i < 3; i++) {
    				
    				Vector2f sparkVel1 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(24f, 144f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(155f, 165f));
    				Vector2f sparkVel2 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(24f, 144f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(195f, 205f));
    				
    				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
    						sparkVel1,
    						MathUtils.getRandomNumberInRange(2f, 6f), //size
    						0.8f, //brightness
    						MathUtils.getRandomNumberInRange(0.3f, 0.45f), //duration
    						new Color(175,205,255,200));
    				
    				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
    						sparkVel2,
    						MathUtils.getRandomNumberInRange(2f, 6f), //size
    						0.8f, //brightness
    						MathUtils.getRandomNumberInRange(0.3f, 0.45f), //duration
    						new Color(175,205,255,200));
    			}
        	}
        	
            if (MathUtils.getDistanceSquared(MISSILE.getLocation(), target.getLocation()) <= (DETECT_RANGE + target.getCollisionRadius())) {
            	RANGE = true;
            }
        }
        
        if (aimAngle < 0) {
            MISSILE.giveCommand(ShipCommand.TURN_RIGHT);
        } else {
            MISSILE.giveCommand(ShipCommand.TURN_LEFT);
        }  
        
        
        if (RANGE) {
        	
        	if (MISSILE.getVelocity().lengthSquared() > 14400) {
    			// if speed is over 120, then turn engines off.
    			ENGINE = false;
    		} else {
    			ENGINE = true;
    		}
        	
        	dampInterval.advance(amount);
        	if (dampInterval.intervalElapsed()) {
        		Vector2f dampVel = MISSILE.getVelocity();
        		dampVel.x = dampVel.x * 0.85f;
        		dampVel.y = dampVel.y * 0.85f;
        		MISSILE.getVelocity().set(dampVel);
    	    			// 15% damping while "armed"
        	}
        	
        	if (!FIRING) {
        		startInterval.advance(amount);
            	
            	if (startInterval.intervalElapsed()) {
            		FIRING = true;
            	}
            	
        	} else {
        		
            	shotInterval.advance(amount);
            	
            	 // Failure state for if the target gets too far away
            	if (MathUtils.getDistanceSquared(MISSILE.getLocation(), target.getLocation()) > (DETECT_RANGE + target.getCollisionRadius()) * 2.5) {
            		
            		for (int i=0; i < 60; i++) {
        				
        				float angle = (i * 6f);
        				Vector2f sparkRingLoc = MathUtils.getPointOnCircumference(MISSILE.getLocation(), MathUtils.getRandomNumberInRange(4f, 32f), angle);

        				Global.getCombatEngine().addSmoothParticle(sparkRingLoc,
        						MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(2f, 24f), angle + MathUtils.getRandomNumberInRange(-4f, 4f)),
        						MathUtils.getRandomNumberInRange(2f, 4f), //size
        						0.8f, //brightness
        						MathUtils.getRandomNumberInRange(0.5f, 0.7f), //duration
        						new Color(175,205,255,255));
        	        }
        			
        			engine.addNebulaParticle(MISSILE.getLocation(),
        					MISSILE.getVelocity(),
        					60f,
        					MathUtils.getRandomNumberInRange(1.4f, 1.5f),
        					0.4f,
        					0.45f,
        					0.8f,
        					new Color(25,100,155,200));
        			
        			 // spawn some particle jets, just for fun
        			for (int i=0; i < 18; i++) {
        				
        				Vector2f sparkVel1 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(48f, 220f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(155f, 165f));
        				Vector2f sparkVel2 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(48f, 220f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(195f, 205f));
        				
        				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
        						sparkVel1,
        						MathUtils.getRandomNumberInRange(2f, 8f), //size
        						0.8f, //brightness
        						MathUtils.getRandomNumberInRange(0.6f, 0.8f), //duration
        						new Color(175,205,255,255));
        				
        				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
        						sparkVel2,
        						MathUtils.getRandomNumberInRange(2f, 8f), //size
        						0.8f, //brightness
        						MathUtils.getRandomNumberInRange(0.6f, 0.8f), //duration
        						new Color(175,205,255,255));
        			}
        			
        			engine.addNebulaSmokeParticle(MISSILE.getLocation(),
        					MISSILE.getVelocity(),
							16f, //size
							1.7f, //end mult
							0.5f, //ramp fraction
							0.65f, //full bright fraction
							0.9f, //duration
							new Color(175,205,255,120));
        			
        			// smoke thing
        			for (int i=0; i < 2; i++) {
        	            
                        Vector2f puffRandomVel = MathUtils.getRandomPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(4f, 20f));
                    	engine.addSmokeParticle(MISSILE.getLocation(),
                    			puffRandomVel,
                    			MathUtils.getRandomNumberInRange(20f, 40f),
                    			0.8f,
                    			0.8f,
                    			new Color(80,100,120,100));
                    	
                        for (int j=0; j < 2; j++) {

                            float randomSize = MathUtils.getRandomNumberInRange(20f, 40f);
                            
                        	Vector2f smokeRandomVel = MathUtils.getRandomPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(4f, 20f));
                        	
                        	engine.addNebulaSmokeParticle(MISSILE.getLocation(),
                            		smokeRandomVel,
                            		randomSize, //size
                            		2.2f, //end mult
                            		0.5f, //ramp fraction
                            		0.75f, //full bright fraction
                            		0.9f, //duration
                            		new Color(50,100,150,100));
                            
                            for (int k=0; k < 2; k++) {
                            	
                            	Vector2f randomVel = MathUtils.getRandomPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(15f, 61f));
        		                
                            	engine.addSmoothParticle(MISSILE.getLocation(),
                            			randomVel,
                            			MathUtils.getRandomNumberInRange(3f, 11f), //size
                            			1.0f, //brightness
                            			0.65f, //duration
                            			new Color(80,120,255,255));
                            	
                            	engine.addSmoothParticle(MathUtils.getRandomPointInCircle(MISSILE.getLocation(), 14f),
                            			MISSILE.getVelocity(),
                            			MathUtils.getRandomNumberInRange(4f, 7f), //size
                            			1.0f, //brightness
                            			MathUtils.getRandomNumberInRange(0.7f, 0.9f), //duration
                            			new Color(175,205,255,255));
                            }
                        }
        			}
        			
        			float blastDam = MISSILE.getDamageAmount() / 10f * AMMO;
        	        DamagingExplosionSpec blast = new DamagingExplosionSpec(0.3f,
        	                50f + (AMMO * 10f),
        	                25f + (AMMO * 5f),
        	                blastDam,
        	                blastDam * 0.75f,
        	                CollisionClass.PROJECTILE_FF,
        	                CollisionClass.PROJECTILE_FIGHTER,
        	                2f,
        	                2f,
        	                0.8f,
        	                50 + (AMMO * 10),
        	                new Color(175,205,255,255),
        	                new Color(25,100,155,200));
        	        blast.setDamageType(DamageType.ENERGY);
        	        blast.setShowGraphic(true);
        	        blast.setUseDetailedExplosion(false);
                    engine.spawnDamagingExplosion(blast,MISSILE.getSource(),MISSILE.getLocation(),false);
                    Global.getSoundPlayer().playSound("kyeltziv_shock_burst", 1.0f, 1.25f, MISSILE.getLocation(), MISSILE.getVelocity());
        			
        			engine.removeEntity(MISSILE);
        			
            	}
            	
            	if (shotInterval.intervalElapsed()) {
            		AMMO --;
            		
            		for (int i=0; i < 2; i++) {
            			
                		boolean piercedShield = false;
                		
                		if (target instanceof ShipAPI) {
                    		float pierceChance = Math.max(0.05f, ((ShipAPI)target).getHardFluxLevel() - 0.05f);
                    		pierceChance *= ((ShipAPI)target).getMutableStats().getDynamic().getValue(Stats.SHIELD_PIERCED_MULT);
                    		piercedShield = (float) Math.random() < pierceChance;
                			
                		}
                		
                		if (piercedShield) {
                			engine.spawnEmpArcPierceShields(MISSILE.getSource(), MathUtils.getPointOnCircumference(MISSILE.getLocation(), 6f, -77f + (i * 50f)), target, target,
                					DamageType.ENERGY, 
        	        				MISSILE.getDamageAmount() / 6f, // damage
        	 		        		MISSILE.getEmpAmount() / 6f, // emp 
        	 		        		100000f, // max range 
        	 		        		"tachyon_lance_emp_impact",
        	 		        		13f, // thickness
        	 		        		new Color(25,100,155,255),
        	 		        		new Color(255,255,255,255));
        	        		// EMP arc
        	 				
        	        		((ShipAPI) target).getFluxTracker().increaseFlux(MISSILE.getDamageAmount() * 0.25f, true);
        	 					// 60 Hard flux
        	        		
        	        		Vector2f tagVel = target.getVelocity();
        	        		tagVel.x = tagVel.x * 0.95f;
        	 	    		tagVel.y = tagVel.y * 0.95f;
        	 	    		target.getVelocity().set(tagVel);
        	 	    			// 5% slow
        	 	    		
        	 	    		float angVel = target.getAngularVelocity();
        	 	            angVel *= 0.95;
        	 	            target.setAngularVelocity(angVel);
        	 	    			// 5% reduction of turn rate
        				} else {
        	        		engine.spawnEmpArc(MISSILE.getSource(), MathUtils.getPointOnCircumference(MISSILE.getLocation(), 6f, -77f + (i * 50f)), target, target,
        	        				DamageType.ENERGY, 
        	        				MISSILE.getDamageAmount() / 6f, // damage
        	 		        		MISSILE.getEmpAmount() / 6f, // emp 
        	 		        		100000f, // max range 
        	 		        		"tachyon_lance_emp_impact",
        	 		        		13f, // thickness
        	 		        		new Color(25,100,155,255),
        	 		        		new Color(255,255,255,255));
        	        		// EMP arc
        	        		
        	        		if (target instanceof ShipAPI) {
            	        		((ShipAPI) target).getFluxTracker().increaseFlux(MISSILE.getDamageAmount() / 6f, false);
            	 					// 40 Soft flux	
        	        		}
        				}
            		}
            		
            		 // spawn some particle jets, just for fun
            		for (int i=0; i < 7; i++) {
        				
        				Vector2f sparkVel1 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(50f, 210f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(155f, 165f));
        				Vector2f sparkVel2 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(50f, 210f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(195f, 205f));
        				
        				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
        						sparkVel1,
        						MathUtils.getRandomNumberInRange(2f, 7f), //size
        						0.8f, //brightness
        						MathUtils.getRandomNumberInRange(0.45f, 0.6f), //duration
        						new Color(175,205,255,255));
        				
        				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
        						sparkVel2,
        						MathUtils.getRandomNumberInRange(2f, 7f), //size
        						0.8f, //brightness
        						MathUtils.getRandomNumberInRange(0.45f, 0.6f), //duration
        						new Color(175,205,255,255));
        			}
            		// vfx here
            		
            		if (AMMO <= 0) {
            			 //nebula vfx
            			engine.addNebulaParticle(MISSILE.getLocation(),
            					MISSILE.getVelocity(),
            					60f,
            					MathUtils.getRandomNumberInRange(1.4f, 1.5f),
            					0.4f,
            					0.45f,
            					0.7f,
            					new Color(25,100,155,200));
            			
            			 // spawn some particle jets, just for fun
            			for (int i=0; i < 20; i++) {
            				
            				Vector2f sparkVel1 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(32f, 240f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(155f, 165f));
            				Vector2f sparkVel2 = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(32f, 240f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(195f, 205f));
            				
            				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
            						sparkVel1,
            						MathUtils.getRandomNumberInRange(2f, 8f), //size
            						0.8f, //brightness
            						MathUtils.getRandomNumberInRange(0.6f, 0.8f), //duration
            						new Color(175,205,255,255));
            				
            				Global.getCombatEngine().addSmoothParticle(MISSILE.getLocation(),
            						sparkVel2,
            						MathUtils.getRandomNumberInRange(2f, 8f), //size
            						0.8f, //brightness
            						MathUtils.getRandomNumberInRange(0.6f, 0.8f), //duration
            						new Color(175,205,255,255));
            			}
            			
            			engine.addNebulaSmokeParticle(MISSILE.getLocation(),
            					MISSILE.getVelocity(),
    							16f, //size
    							1.7f, //end mult
    							0.5f, //ramp fraction
    							0.65f, //full bright fraction
    							0.9f, //duration
    							new Color(175,205,255,120));
            			
            			 // smoke thing
            			for (int i=0; i < 2; i++) {
            				
            				Vector2f puffRandomVel = MathUtils.getRandomPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(4f, 20f));
            				engine.addSmokeParticle(MISSILE.getLocation(),
            						puffRandomVel,
            						MathUtils.getRandomNumberInRange(20f, 40f),
            						0.8f,
            						0.8f,
            						new Color(80,100,120,100));
                            
                        	engine.addSmoothParticle(MathUtils.getRandomPointInCircle(MISSILE.getLocation(), 14f),
                        			MISSILE.getVelocity(),
                        			MathUtils.getRandomNumberInRange(4f, 6f), //size
                        			1.0f, //brightness
                        			MathUtils.getRandomNumberInRange(0.7f, 0.9f), //duration
                        			new Color(120,180,255,255));
            				
            				for (int j=0; j < 2; j++) {
            					
            					float randomSize = MathUtils.getRandomNumberInRange(20f, 40f);
            					
            					Vector2f smokeRandomVel = MathUtils.getRandomPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(4f, 20f));
            					
            					engine.addNebulaSmokeParticle(MISSILE.getLocation(),
            							smokeRandomVel,
            							randomSize, //size
            							2.2f, //end mult
            							0.5f, //ramp fraction
            							0.75f, //full bright fraction
            							0.9f, //duration
            							new Color(50,100,150,100));
            					
            					for (int k=0; k < 2; k++) {
            						
            						Vector2f randomVel = MathUtils.getRandomPointOnCircumference(MISSILE.getVelocity(), MathUtils.getRandomNumberInRange(15f, 61f));
            						
            						engine.addSmoothParticle(MISSILE.getLocation(),
            								randomVel,
            								MathUtils.getRandomNumberInRange(3f, 11f), //size
            								1.0f, //brightness
            								0.65f, //duration
            								new Color(80,120,255,255));
                                	
                                	engine.addSmoothParticle(MathUtils.getRandomPointInCircle(MISSILE.getLocation(), 14f),
                                			MISSILE.getVelocity(),
                                			MathUtils.getRandomNumberInRange(4f, 7f), //size
                                			1.0f, //brightness
                                			MathUtils.getRandomNumberInRange(0.7f, 0.9f), //duration
                                			new Color(175,205,255,255));
            					}
            				}
            			}
            			
            			engine.removeEntity(MISSILE);
            		}
            	}
        	}
        }
        
        
		
        // Damp angular velocity if the missile aim is getting close to the targeted angle
        if (Math.abs(aimAngle) < Math.abs(MISSILE.getAngularVelocity()) * DAMPING) {
            MISSILE.setAngularVelocity(aimAngle / DAMPING);
        }
    }
    
    //////////////////////
    //    TARGETING     //
    //////////////////////
    
    @Override
    public CombatEntityAPI getTarget() {
        return target;
    }

    @Override
    public void setTarget(CombatEntityAPI target) {
        this.target = target;
    }
    
    public void init(CombatEngineAPI engine) {}
}