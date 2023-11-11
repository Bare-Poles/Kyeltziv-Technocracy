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

public class kyeltziv_ShockMissileOnHitEffect implements OnHitEffectPlugin {

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

		if (!shieldHit && target instanceof ShipAPI) {

			float dam = projectile.getEmpAmount();
			float emp = projectile.getEmpAmount() * 1.5f;
			float arc_chance = ((ShipAPI)target).getFluxLevel();
			arc_chance *= 0.4f;
			arc_chance += 0.1f;

			for (int i = 1; i < 2; i++) {
				if (arc_chance >= (float) Math.random()) {
					engine.spawnEmpArc(projectile.getSource(), point, target, target,
							DamageType.ENERGY, 
							dam, // damage
							emp, // emp 
							200f, // max range 
							"tachyon_lance_emp_impact",
							16f, // thickness
							new Color(25,100,155,120),
							new Color(255,255,255,120));
				}
			}
		}
	}
}
