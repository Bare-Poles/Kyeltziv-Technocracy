package org.amazigh.kyeltziv.shipsystems.scripts;

import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.impl.combat.MineStrikeStatsAIInfoProvider;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;

public class kyeltziv_DroneStrikeStats extends BaseShipSystemScript implements MineStrikeStatsAIInfoProvider {

	// literally the vanilla mine strike script, but with commented unused lines stripped to save filesize
		// and i tweaked it and added some fuckery to make it spawn a drone platform instead of a mine
	protected static float MINE_RANGE = 1000f;
	public static final float MIN_SPAWN_DIST = 70f;
	public static final float MIN_SPAWN_DIST_FRIGATE = 100f;
	public static final Color JITTER_COLOR = new Color(165,100,255,75);
	public static final Color JITTER_UNDER_COLOR = new Color(165,100,255,155);
	public static float getRange(ShipAPI ship) {
		if (ship == null) return MINE_RANGE;
		return ship.getMutableStats().getSystemRangeBonus().computeEffective(MINE_RANGE);
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}
		float jitterLevel = effectLevel;
		if (state == State.OUT) {
			jitterLevel *= jitterLevel;
		}
		float maxRangeBonus = 25f;
		float jitterRangeBonus = jitterLevel * maxRangeBonus;
		if (state == State.OUT) {
		}
		ship.setJitterUnder(this, JITTER_UNDER_COLOR, jitterLevel, 11, 0f, 3f + jitterRangeBonus);
		ship.setJitter(this, JITTER_COLOR, jitterLevel, 4, 0f, 0 + jitterRangeBonus);
		if (state == State.IN) {
		} else if (effectLevel >= 1) {
			Vector2f target = ship.getMouseTarget();
			if (ship.getShipAI() != null && ship.getAIFlags().hasFlag(AIFlags.SYSTEM_TARGET_COORDS)){
				target = (Vector2f) ship.getAIFlags().getCustom(AIFlags.SYSTEM_TARGET_COORDS);
			}
			if (target != null) {
				float dist = Misc.getDistance(ship.getLocation(), target);
				float max = getMaxRange(ship) + ship.getCollisionRadius();
				if (dist > max) {
					float dir = Misc.getAngleInDegrees(ship.getLocation(), target);
					target = Misc.getUnitVectorAtDegreeAngle(dir);
					target.scale(max);
					Vector2f.add(target, ship.getLocation(), target);
				}
				target = findClearLocation(ship, target);
				if (target != null) {
					spawnMine(ship, target);
				}
			}
		}
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
	}
	
	public void spawnMine(ShipAPI source, Vector2f mineLoc) {
		CombatEngineAPI engine = Global.getCombatEngine();
		Vector2f currLoc = Misc.getPointAtRadius(mineLoc, 30f + (float) Math.random() * 30f);
		float start = (float) Math.random() * 360f;
		for (float angle = start; angle < start + 390; angle += 30f) {
			if (angle != start) {
				Vector2f loc = Misc.getUnitVectorAtDegreeAngle(angle);
				loc.scale(50f + (float) Math.random() * 30f);
				currLoc = Vector2f.add(mineLoc, loc, new Vector2f());
			}
			for (MissileAPI other : Global.getCombatEngine().getMissiles()) {
				if (!other.isMine()) continue;
				
				float dist = Misc.getDistance(currLoc, other.getLocation());
				if (dist < other.getCollisionRadius() + 40f) {
					currLoc = null;
					break;
				}
			}
			if (currLoc != null) {
				break;
			}
		}
		if (currLoc == null) {
			currLoc = Misc.getPointAtRadius(mineLoc, 30f + (float) Math.random() * 30f);
		}
		
		
		
		// here is where the platform is spawned, prolly kinda fucky tbh.
		
		int owner = source.getOwner();
		
		CombatFleetManagerAPI FleetManager = engine.getFleetManager(source.getOwner());
		FleetManager.setSuppressDeploymentMessages(true);
		FleetMemberAPI missileMember = Global.getFactory().createFleetMember(FleetMemberType.FIGHTER_WING, "kyeltziv_kohta_wing");
		missileMember.getRepairTracker().setCrashMothballed(false);
		missileMember.getRepairTracker().setMothballed(false);
		missileMember.getRepairTracker().setCR(1f);
		missileMember.setOwner(owner);
		missileMember.setAlly(source.isAlly());
		
		ShipAPI platform = engine.getFleetManager(owner).spawnFleetMember(missileMember, currLoc, (float) (Math.random() * 360f), 0f);
		platform.setCRAtDeployment(0.7f);
		platform.setCollisionClass(CollisionClass.FIGHTER);
		
		Vector2f platformVel = MathUtils.getRandomPointInCircle(null, 40f);
		platform.getVelocity().set(platformVel);
		platform.setFacing(MathUtils.getRandomNumberInRange(0f, 360f));
		FleetManager.setSuppressDeploymentMessages(false);
		
		
		engine.addSmoothParticle(currLoc, //position
    			platformVel, //velocity
    			85f, //size
    			0.8f, //brightness
    			0.05f, //duration
    			new Color(200,95,255,255));
		
        for (int i=0; i < 2; i++) {
        	engine.addSmoothParticle(currLoc,
        			MathUtils.getRandomPointInCircle(platformVel, 6f), //velocity
        			MathUtils.getRandomNumberInRange(50f, 60f), //size
        			0.4f, //brightness
        			MathUtils.getRandomNumberInRange(0.65f, 0.75f), //duration
        			new Color(160,80,255,200));
        	
    		engine.addSmoothParticle(currLoc,
        			MathUtils.getRandomPointInCircle(platformVel, 6f), //velocity
        			MathUtils.getRandomNumberInRange(50f, 60f), //size
        			1f, //brightness
        			MathUtils.getRandomNumberInRange(0.2f, 0.25f), //duration
        			new Color(150,60,255,255));
    		
            for (int j=0; j < 15; j++) {
            	engine.addSmoothParticle(MathUtils.getRandomPointInCircle(currLoc, 10f), //position
            			MathUtils.getRandomPointOnCircumference(platformVel, MathUtils.getRandomNumberInRange(40f, 120f)), //velocity
            			MathUtils.getRandomNumberInRange(2f, 7f), //size
            			MathUtils.getRandomNumberInRange(0.5f, 0.7f), //brightness
            			MathUtils.getRandomNumberInRange(0.5f, 0.65f), //duration
            			new Color(145,90,255,255));
            	
            	engine.addSmoothParticle(MathUtils.getRandomPointInCircle(currLoc, 15f), //position
            			MathUtils.getRandomPointOnCircumference(platformVel, MathUtils.getRandomNumberInRange(20f, 60f)), //velocity
            			MathUtils.getRandomNumberInRange(2f, 7f), //size
            			MathUtils.getRandomNumberInRange(0.5f, 0.7f), //brightness
            			MathUtils.getRandomNumberInRange(0.45f, 0.6f), //duration
            			new Color(130,70,250,255));
            }
        }
		
		
		
		Global.getSoundPlayer().playSound("mine_teleport", 1f, 1f, platform.getLocation(), platform.getVelocity());
	}

	
	protected float getMaxRange(ShipAPI ship) {
		return getMineRange(ship);
	}

	@Override
	public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
		if (system.isOutOfAmmo()) return null;
		if (system.getState() != SystemState.IDLE) return null;
		Vector2f target = ship.getMouseTarget();
		if (target != null) {
			float dist = Misc.getDistance(ship.getLocation(), target);
			float max = getMaxRange(ship) + ship.getCollisionRadius();
			if (dist > max) {
				return "OUT OF RANGE";
			} else {
				return "READY";
			}
		}
		return null;
	}

	@Override
	public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
		return ship.getMouseTarget() != null;
	}
	
	private Vector2f findClearLocation(ShipAPI ship, Vector2f dest) {
		if (isLocationClear(dest)) return dest;
		float incr = 50f;
		WeightedRandomPicker<Vector2f> tested = new WeightedRandomPicker<Vector2f>();
		for (float distIndex = 1; distIndex <= 32f; distIndex *= 2f) {
			float start = (float) Math.random() * 360f;
			for (float angle = start; angle < start + 360; angle += 60f) {
				Vector2f loc = Misc.getUnitVectorAtDegreeAngle(angle);
				loc.scale(incr * distIndex);
				Vector2f.add(dest, loc, loc);
				tested.add(loc);
				if (isLocationClear(loc)) {
					return loc;
				}
			}
		}
		if (tested.isEmpty()) return dest; // shouldn't happen
		return tested.pick();
	}
	
	private boolean isLocationClear(Vector2f loc) {
		for (ShipAPI other : Global.getCombatEngine().getShips()) {
			if (other.isShuttlePod()) continue;
			if (other.isFighter()) continue;
			Vector2f otherLoc = other.getShieldCenterEvenIfNoShield();
			float otherR = other.getShieldRadiusEvenIfNoShield();
			if (other.isPiece()) {
				otherLoc = other.getLocation();
				otherR = other.getCollisionRadius();
			}
			float dist = Misc.getDistance(loc, otherLoc);
			float r = otherR;
			float checkDist = MIN_SPAWN_DIST;
			if (other.isFrigate()) checkDist = MIN_SPAWN_DIST_FRIGATE;
			if (dist < r + checkDist) {
				return false;
			}
		}
		for (CombatEntityAPI other : Global.getCombatEngine().getAsteroids()) {
			float dist = Misc.getDistance(loc, other.getLocation());
			if (dist < other.getCollisionRadius() + MIN_SPAWN_DIST) {
				return false;
			}
		}		
		return true;
	}

	public float getFuseTime() {
		return 2f;
	}

	public float getMineRange(ShipAPI ship) {
		return getRange(ship);
	}	
}