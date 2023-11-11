package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

public class kyeltziv_IonRepeaterOnHitEffect implements OnHitEffectPlugin {

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

    if (!shieldHit && target instanceof ShipAPI) {

    	float emp = projectile.getEmpAmount() / 2f;
		float dam = projectile.getDamageAmount() * 0.666666f;

      if (0.3f >= (float) Math.random()) {
        engine.spawnEmpArc(projectile.getSource(), point, target, target,
          DamageType.ENERGY,
          dam, // damage
          emp, // emp 
          250f, // max range 
          "kyeltziv_shock_burst", // tachyon_lance_emp_impact
          15f, // thickness
		   new Color(20,100,155,255),
		   new Color(220,255,255,255));
	  	}
	  }
	}
}
