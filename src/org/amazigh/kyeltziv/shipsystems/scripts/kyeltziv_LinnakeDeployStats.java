package org.amazigh.kyeltziv.shipsystems.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.loading.WeaponSlotAPI;

public class kyeltziv_LinnakeDeployStats extends BaseShipSystemScript {

	public String WING_NAME = "kyeltziv_linnake_base_base_wing";
	
	private boolean DEPLOYED = false;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		}
        CombatEngineAPI engine = Global.getCombatEngine();
        ShipVariantAPI variant = ship.getVariant();
        
        if (!DEPLOYED) {
        	DEPLOYED = true;
        	
            if (variant.getHullMods().contains("kyeltziv_pack_ass")) {
            	if (variant.getHullMods().contains("kyeltziv_loader_ass")) {
            		WING_NAME = "kyeltziv_linnake_assault_ass_wing";
        		} else if (variant.getHullMods().contains("kyeltziv_loader_siege")) {
        			WING_NAME = "kyeltziv_linnake_assault_siege_wing";
        		} else if (variant.getHullMods().contains("kyeltziv_loader_supp")) {
        			WING_NAME = "kyeltziv_linnake_assault_supp_wing";
        		} else {
        			WING_NAME = "kyeltziv_linnake_assault_base_wing";
        		}
    		} else if (variant.getHullMods().contains("kyeltziv_pack_siege")) {
    			if (variant.getHullMods().contains("kyeltziv_loader_ass")) {
            		WING_NAME = "kyeltziv_linnake_siege_ass_wing";
        		} else if (variant.getHullMods().contains("kyeltziv_loader_siege")) {
        			WING_NAME = "kyeltziv_linnake_siege_siege_wing";
        		} else if (variant.getHullMods().contains("kyeltziv_loader_supp")) {
        			WING_NAME = "kyeltziv_linnake_siege_supp_wing";
        		} else {
        			WING_NAME = "kyeltziv_linnake_siege_base_wing";
        		}
    		} else if (variant.getHullMods().contains("kyeltziv_pack_supp")) {
    			if (variant.getHullMods().contains("kyeltziv_loader_ass")) {
            		WING_NAME = "kyeltziv_linnake_supp_ass_wing";
        		} else if (variant.getHullMods().contains("kyeltziv_loader_siege")) {
        			WING_NAME = "kyeltziv_linnake_supp_siege_wing";
        		} else if (variant.getHullMods().contains("kyeltziv_loader_supp")) {
        			WING_NAME = "kyeltziv_linnake_supp_supp_wing";
        		} else {
        			WING_NAME = "kyeltziv_linnake_supp_base_wing";
        		}
    		} else {
    			if (variant.getHullMods().contains("kyeltziv_loader_ass")) {
            		WING_NAME = "kyeltziv_linnake_base_ass_wing";
        		} else if (variant.getHullMods().contains("kyeltziv_loader_siege")) {
        			WING_NAME = "kyeltziv_linnake_base_siege_wing";
        		} else if (variant.getHullMods().contains("kyeltziv_loader_supp")) {
        			WING_NAME = "kyeltziv_linnake_base_supp_wing";
        		}
    		}
    		
    		for (WeaponSlotAPI weapon : ship.getHullSpec().getAllWeaponSlotsCopy()) {
                if (weapon.isSystemSlot()) {
            		int owner = ship.getOwner();
            		
            		Vector2f posZero = weapon.computePosition(ship);
            		
            		CombatFleetManagerAPI FleetManager = engine.getFleetManager(owner);
            		FleetManager.setSuppressDeploymentMessages(true);
            		FleetMemberAPI platMember = Global.getFactory().createFleetMember(FleetMemberType.FIGHTER_WING, WING_NAME);
            		platMember.getRepairTracker().setCrashMothballed(false);
            		platMember.getRepairTracker().setMothballed(false);
            		platMember.getRepairTracker().setCR(1f);
            		platMember.setOwner(owner);
            		platMember.setAlly(ship.isAlly());
            		
            		ShipAPI platform = engine.getFleetManager(owner).spawnFleetMember(platMember, posZero, weapon.computeMidArcAngle(ship), 0f);
            		platform.setCRAtDeployment(0.7f);
            		platform.setCollisionClass(CollisionClass.FIGHTER);
            		
            		Vector2f platformVel = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(100f, 120f), weapon.computeMidArcAngle(ship) + MathUtils.getRandomNumberInRange(-5f, 5f));
            		platform.getVelocity().set(platformVel);
            		platform.setFacing(MathUtils.getRandomNumberInRange(0f, 360f));
            		FleetManager.setSuppressDeploymentMessages(false);
            		
            		engine.addSmoothParticle(posZero, //position
            				ship.getVelocity(), //velocity
                			75f, //size
                			0.8f, //brightness
                			0.05f, //duration
                			new Color(250,175,80,255));
            		
        			for (int i=0; i < 16; i++) {
                        Vector2f smokeVel = MathUtils.getRandomPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(4f, 60f));
        	            float randomSize2 = MathUtils.getRandomNumberInRange(20f, 25f);
        	            engine.addSmokeParticle(MathUtils.getRandomPointInCircle(posZero, 30f), smokeVel, randomSize2, 0.9f, MathUtils.getRandomNumberInRange(0.5f, 1.0f), new Color(110,110,100,180));
        	            
        	            for (int j=0; j < 2; j++) {
        	            	engine.addSmoothParticle(MathUtils.getRandomPointInCircle(posZero, 15f), //position
        	            			MathUtils.getRandomPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(40f, 120f)), //velocity
        	            			MathUtils.getRandomNumberInRange(4f, 9f), //size
        	            			MathUtils.getRandomNumberInRange(0.5f, 0.7f), //brightness
        	            			MathUtils.getRandomNumberInRange(0.4f, 0.6f), //duration
        	            			new Color(255,150,90,255));
        	            }
        			}
                }
    		}
        }
	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		DEPLOYED = false;
	}
}
