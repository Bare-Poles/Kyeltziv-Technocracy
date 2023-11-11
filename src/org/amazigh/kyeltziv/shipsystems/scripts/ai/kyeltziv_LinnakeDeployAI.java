package org.amazigh.kyeltziv.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class kyeltziv_LinnakeDeployAI implements ShipSystemAIScript {

    private ShipAPI ship;
    private CombatEngineAPI engine;
    
    private IntervalUtil timer = new IntervalUtil(0.5f, 0.5f);
    
    private float RANGE = 2140f; // what should be the value for platform (primary) weapon range -200.
    
    @Override
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.engine = engine;
    }
    
    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        
    	// don't check if paused / can't use the system
    	if (engine.isPaused() || !AIUtils.canUseSystemThisFrame(ship)) {
            return;
        }
    	
        // don't check if timer not up
        timer.advance(amount);
        if (!timer.intervalElapsed()) {
            return;
        }
        
        // configure what range to check for based on packages
        ShipVariantAPI variant = ship.getVariant();
        
        if (variant.getHullMods().contains("kyeltziv_loader_siege")) {
			if (variant.getHullMods().contains("kyeltziv_pack_siege")) {
        		RANGE = 2700f;
    		} else {
        		RANGE = 2500f;
    		}
		} else if (variant.getHullMods().contains("kyeltziv_pack_siege")) {
			RANGE = 2320f;
		}
        
        if (variant.getHullMods().contains("kyeltziv_loader_supp")) {
			RANGE = 2300f;
		} 
        
        // setup variables
        Vector2f targetLocation = null;
        
        // assign our target location to whatever ship we are attacking
        if (target != null && target.getOwner() != ship.getOwner()) {
            targetLocation = target.getLocation();
        }
        
        // if we have a target that is within range then deploy a platform.
        if (targetLocation == null) {
        	return;
        } else if (MathUtils.isWithinRange(ship, target, RANGE)) {
        	ship.useSystem();
        }
    }
}