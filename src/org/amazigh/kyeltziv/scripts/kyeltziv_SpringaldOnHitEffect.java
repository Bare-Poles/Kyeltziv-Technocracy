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
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class kyeltziv_SpringaldOnHitEffect implements OnHitEffectPlugin {

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

		if (target instanceof ShipAPI) {
			
	    	ShipAPI ship = (ShipAPI) target;
			float dam = projectile.getEmpAmount() * 0.25f;
			float emp = projectile.getEmpAmount() * 0.5f;
			
			float rng = 0.3f;
			float arc_num = ship.getFluxLevel();
			
			 // this block asks: did i hit shield? if yes then roll a chance to just fail instantly, scaling with target hardflux level and SHIELD_PIERCED_MULT
			 // if you pass the check then arc damage is reduced, AND flux dependent arc count is scaled based on target hardflux level
			if (shieldHit) {
				boolean piercedShield = false;
				float pierceChance = ((ShipAPI)target).getHardFluxLevel() - 0.1f;
				pierceChance *= ship.getMutableStats().getDynamic().getValue(Stats.SHIELD_PIERCED_MULT);
				
				piercedShield = (float) Math.random() < pierceChance;
				if (!piercedShield) {
					return;
				}
				
				dam *= 0.5f;
				emp *= 0.5f;
				arc_num *= ship.getHardFluxLevel();
		    }
			
			arc_num *= 2.9f;
			
			if (rng >= (float) Math.random()) {
				arc_num += 1.1f;
			}
			
			
			for (int i = 1; i < arc_num; i++) {
				
				
				engine.spawnEmpArcPierceShields(projectile.getSource(), point, target, target,
						DamageType.ENERGY,
						dam, // damage
						emp, // emp
						250f, // max range
						"tachyon_lance_emp_impact",
						18f, // thickness
						new Color(25,100,155,155),
						new Color(255,255,255,155));
			}
		}
	}
}
