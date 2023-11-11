// Based on the MagicMissileAI script By Tartiflette.
// this script is a "ballistic DEM" script
package org.amazigh.kyeltziv.scripts.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.GuidedMissileAI;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.util.IntervalUtil;

import org.magiclib.util.MagicTargeting;

import java.awt.Color;

import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class kyeltziv_AvtomatDrumfireMissileAI implements MissileAIPlugin, GuidedMissileAI {
    
	//////////////////////
	//     SETTINGS     //
	//////////////////////
	
	//Angle with the target beyond which the missile turn around without accelerating. Avoid endless circling.
	//  Set to a negative value to disable
	private final float OVERSHOT_ANGLE=60;
	
	//Time to complete a wave in seconds.
	private final float WAVE_TIME=2;
	
	//Max angle of the waving in degree (divided by 3 with ECCM). Set to a negative value to avoid all waving.
	private final float WAVE_AMPLITUDE=10;
	
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
	private final int fighters=0;
	private final int frigates=1;
	private final int destroyers=2;
	private final int cruisers=3;
	private final int capitals=4;

	//Arc to look for targets into
	//set to 360 or more to ignore
	private final int SEARCH_CONE=360;

	//range in which the missile seek a target in game units.
	private final int MAX_SEARCH_RANGE = 1500;

	//should the missile fall back to the closest enemy when no target is found within the search parameters
	//only used with limited search cones
	private final boolean FAILSAFE = false;

	//range under which the missile start to get progressively more precise in game units.
	private float PRECISION_RANGE=800;

	//Is the missile lead the target or tailchase it?
	private final boolean LEADING=true;

	//Leading loss without ECCM hullmod. The higher, the less accurate the leading calculation will be.
	//   1: perfect leading with and without ECCM
	//   2: half precision without ECCM
	//   3: a third as precise without ECCM. Default
	//   4, 5, 6 etc : 1/4th, 1/5th, 1/6th etc precision.
	private float ECCM=3;   //A VALUE BELOW 1 WILL PREVENT THE MISSILE FROM EVER HITTING ITS TARGET!
	
	
	
	// how many shots to fire
	private int AMMO = 30;
	
	// checks for whether the missile is:  engine use allowed / within range / firing / which side to strafe to / in final attack
	private boolean ENGINE = true;
	private boolean RANGE = false;
	private boolean FIRING = false;
	private boolean SIDE = false;
	private boolean DOWN = false;
	
	// square of range to switch to "attack mode"
	private float DETECT_RANGE=562500; //750^2
	
	// square of range to target at which to stop shooting and resume boosting
	private float FAIL_RANGE=640000; //800^2
	
	// velocity of the shots fired, for target leading purposes
	private float SHOT_VEL=700;
	
	// how fast for the missile to accelerate when strafing
	private float STRAFE_ACCEL = 40f;
	
	// timers for:  "startup" / shooting / "wind-down" / flameout timer
	private IntervalUtil startInterval = new IntervalUtil(0.3f, 0.3f);
	private IntervalUtil shotInterval = new IntervalUtil(0.2f, 0.2f);
	private IntervalUtil downInterval = new IntervalUtil(0.5f, 0.5f);
	private IntervalUtil flameInterval = new IntervalUtil(8f, 8f);
	
	
	
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
	private float timer=0, check=0f, START_SPEED=300f;
	
	//////////////////////
	//  DATA COLLECTING //
	//////////////////////
	
    public kyeltziv_AvtomatDrumfireMissileAI(MissileAPI missile, ShipAPI launchingShip) {
        this.MISSILE = missile;
        MAX_SPEED = missile.getMaxSpeed();
        if (missile.getSource().getVariant().getHullMods().contains("eccm")){
            ECCM=1;
        }
        float initSpeed = MISSILE.getSource().getMutableStats().getMissileMaxSpeedBonus().computeEffective(missile.getMaxSpeed() * 0.6f);
        START_SPEED =  initSpeed * initSpeed;
        // compute start stage speed based on ship stat bonuses
    	
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
            	
            	// believed speed is set to 300, so the missile will lead approprately for the forcibly damped maximum velocity. 
            	float leadVel = 300f * ECCM; //if eccm is intalled the point is accurate, otherwise it's placed closer to the target (almost tailchasing)
            	
            	//Vector2f tagVel = target.getVelocity();
            	
            	float tagX = target.getVelocity().x;
            	float tagY = target.getVelocity().y;
            	
            	if (FIRING) {
            		leadVel = SHOT_VEL; // have the missile lead with the velocity of the fired projectile, so it should be decently accurate
            		
            		//tagVel.x = (tagVel.x - MISSILE.getVelocity().x); // doing it this way **somehow** made the missile psychic, and caused the target to get launched?
            		//tagVel.y = (tagVel.y - MISSILE.getVelocity().y);
            		tagX -= MISSILE.getVelocity().x; //this is done so we can mess with the believed target velocity to compensate for strafing when firing.
            		tagY -= MISSILE.getVelocity().y;
            		
            	} else if (DOWN) {
            		leadVel =  MAX_SPEED*ECCM;
            	}
            	
            	Vector2f tagVel = new Vector2f(tagX, tagY);
            	
            	lead = AIUtils.getBestInterceptPoint(
                        MISSILE.getLocation(),
                        leadVel,
                        target.getLocation(),
                        tagVel
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
        		flameInterval.advance(amount);
            	
            	if (flameInterval.intervalElapsed()) {
            		MISSILE.flameOut(); // manual handling of the flameout timer, as i only want it to tick up when not firing.
            	}
        		
            	
        		if (!DOWN && MISSILE.getVelocity().lengthSquared() > START_SPEED) {
        			
        			// clamping the "loaded" missile speed here, so it's faster after firing its ammo!
        			
        			float velMult = START_SPEED / MISSILE.getVelocity().lengthSquared();
        			
        			Vector2f dampedVel = MISSILE.getVelocity();
                    dampedVel.x = dampedVel.x * velMult;
                    dampedVel.y = dampedVel.y * velMult;
                    
                    MISSILE.getVelocity().set(dampedVel);
        		}
        		
                if (!DOWN && MathUtils.getDistanceSquared(MISSILE.getLocation(), target.getLocation()) <= DETECT_RANGE) {
                	RANGE = true;
                }        		
        	}
        }
        
        if (aimAngle < 0) {
            MISSILE.giveCommand(ShipCommand.TURN_RIGHT);
        } else {
            MISSILE.giveCommand(ShipCommand.TURN_LEFT);
        }  
        
        
        if (RANGE) {
        	ENGINE = false;
        	
            startInterval.advance(amount);
            
            Vector2f dampedVel = MISSILE.getVelocity();
            dampedVel.x = dampedVel.x * 0.9f; // was 0.92, but we lowered init time (0.5 > 0.3) so had to be made stronger to ~compensate
            dampedVel.y = dampedVel.y * 0.9f;
            
            MISSILE.getVelocity().set(dampedVel);
            
        	if (startInterval.intervalElapsed()) {
        		RANGE = false;
        		FIRING = true;
        		
        		if (Math.random() > 0.5) {
        			SIDE = true; // randomly pick whether we strafe to the left or right.
        		} else {
        			SIDE = false;
        		}
        		
        	}
        }
        
        if (FIRING) {
        	shotInterval.advance(amount);
        	
        	float strafeDir = MISSILE.getFacing() + 80f; // we strafe *slightly* forwards, to give minor aid with keeping target in range.
        	
        	if (SIDE) {
        		strafeDir += 200f;
        	}
        	Vector2f strafeVel = MathUtils.getPointOnCircumference(MISSILE.getVelocity(), STRAFE_ACCEL*amount, strafeDir);
            MISSILE.getVelocity().set(strafeVel); // strafe to the side, because it looks kinda cool, and for (minor) PD avoidance.
        	
            
        	if (shotInterval.intervalElapsed()) {
        		
        		AMMO --;
        		
        		Vector2f vel = MISSILE.getVelocity();
            	Vector2f loc = MISSILE.getLocation();
            	
				engine.spawnProjectile(MISSILE.getSource(), MISSILE.getWeapon(), "kyeltziv_shock_avtomat_drum", loc, MISSILE.getFacing() + MathUtils.getRandomNumberInRange(-5f, 5f), vel);
				Global.getSoundPlayer().playSound("light_machinegun_fire", 0.9f, 1.2f, loc, vel);
				
            	for (int i=0; i < 8; i++) {
            		Vector2f muzzleLoc = MathUtils.getPointOnCircumference(loc, MathUtils.getRandomNumberInRange(9f, 19f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(-6f, 6f));
            		Vector2f muzzleRandomVel = MathUtils.getPointOnCircumference(vel, MathUtils.getRandomNumberInRange(15f, 30f), MISSILE.getFacing() + MathUtils.getRandomNumberInRange(-6f, 6f));
            		
            		engine.addSmoothParticle(muzzleLoc, muzzleRandomVel, MathUtils.getRandomNumberInRange(6f, 14f), 1f, MathUtils.getRandomNumberInRange(0.12f, 0.18f),  new Color(165,185,225,155));
            	}
            	
            	if (MathUtils.getDistanceSquared(MISSILE.getLocation(), target.getLocation()) > FAIL_RANGE) {
            		ENGINE = true;
        			FIRING = false;
        			// this is an "emergency engine reset" if the target gets out of range
            	}
            	
        		if (AMMO <= 0) {
        			FIRING = false;
        			DOWN = true;
        		}
        	}
        }
        
        if (DOWN) {
        	
        	downInterval.advance(amount);
        	
        	if (downInterval.intervalElapsed()) {
        		ENGINE = true;
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