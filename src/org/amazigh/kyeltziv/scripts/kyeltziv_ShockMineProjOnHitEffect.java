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

public class kyeltziv_ShockMineProjOnHitEffect implements OnHitEffectPlugin {

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

		if (target instanceof ShipAPI) {
			
			float dam = projectile.getDamageAmount();
			float emp = projectile.getEmpAmount();
			
			float arc_chance = ((ShipAPI)target).getFluxLevel();
			arc_chance *= 0.3f;
			arc_chance += 0.2f;
			
			if (arc_chance >= (float) Math.random()) {
				engine.spawnEmpArc(projectile.getSource(), projectile.getSpawnLocation(), target, target,
						DamageType.ENERGY,
						dam, // damage
						emp, // emp
						1000f, // max range
						"tachyon_lance_emp_impact",
						18f, // thickness
						new Color(25,100,155,100),
						new Color(255,255,255,100));
			}
		}
	}
}
