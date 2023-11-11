package org.amazigh.kyeltziv.scripts;

import com.fs.starfarer.api.combat.*;
import org.lazywizard.lazylib.combat.CombatUtils;

import java.util.ArrayList;
import java.util.List;

public class kyeltziv_GarajWeaponScript implements EveryFrameWeaponEffectPlugin {

    private List<MissileAPI> alreadyRegisteredProjectiles = new ArrayList<MissileAPI>();
    
    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

        for (MissileAPI proj : CombatUtils.getMissilesWithinRange(weapon.getLocation(), 200f)) {
            if (proj.getWeapon() == weapon && !alreadyRegisteredProjectiles.contains(proj) && engine.isEntityInPlay(proj) && !proj.didDamage() && !proj.isDecoyFlare()) {
            	engine.addPlugin(new kyeltziv_GarajChaffScript(proj));
            	alreadyRegisteredProjectiles.add(proj);
            }
        }

        //And clean up our registered projectile list
        List<MissileAPI> cloneList = new ArrayList<>(alreadyRegisteredProjectiles);
        for (MissileAPI proj : cloneList) {
            if (!engine.isEntityInPlay(proj) || proj.didDamage()) {
                alreadyRegisteredProjectiles.remove(proj);
            }
        }

    }
}
