package org.amazigh.kyeltziv.shipsystems.scripts.ai;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.util.IntervalUtil;

public class kyeltziv_microburst_AI implements ShipSystemAIScript {

    private ShipAPI ship;
    private CombatEngineAPI engine;

    // check about two times a second, a balance of optimization and responsiveness
    private IntervalUtil timer = new IntervalUtil(0.4f, 0.6f);
        
    @Override
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.engine = engine;
    }
    
    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        if (engine.isPaused()) {
            return;
        }

        timer.advance(amount);

        if (!timer.intervalElapsed()) {
            return;
        }
        if (!AIUtils.canUseSystemThisFrame(ship)) {
            return;
        }

        boolean useMe = false;

        // if we have a hostile target, set our target loc to it
        Vector2f targetLocation = null;
        if (target != null && target.getOwner() != ship.getOwner()) {
            targetLocation = target.getLocation();
        }
        
        // if our hostile target is within "combat range", activate
        if (targetLocation == null) {
            return;
        } else if (MathUtils.isWithinRange(ship, target, 750f)) {
            useMe = true;
        }

        if (useMe) {
            ship.useSystem();
        }

    }
}
