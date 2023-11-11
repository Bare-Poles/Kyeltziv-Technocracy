package org.amazigh.kyeltziv.scripts;

import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.impl.combat.DisintegratorEffect;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_osteotomeOnHitEffect implements OnHitEffectPlugin {

	public static float DAMAGE = 100;

	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
					  Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		
		if (target instanceof ShipAPI) {
			if (shieldHit) {
				
				for (int i=0; i < 5; i++) {
					// done like this so i can force it to bring them (close) to overload, and not just waste the entire soft flux spike if near max flux
					((ShipAPI) target).getFluxTracker().increaseFlux(projectile.getDamageAmount() * 0.25f, false);
					// 480 x 0.25 = 120) x 5 = 600
				}
				
				// impact vfx
				float shieldOrient = VectorUtils.getAngle(target.getLocation(), point);
				
				for (int j=0; j < 18; j++) {
					
					float angle = j * 10f;
					Vector2f sparkRingLoc = MathUtils.getPointOnCircumference(point, MathUtils.getRandomNumberInRange(4f, 24f), angle);
					
					Global.getCombatEngine().addSmoothParticle(sparkRingLoc,
							MathUtils.getPointOnCircumference(target.getVelocity(), MathUtils.getRandomNumberInRange(2f, 18f), shieldOrient - 92.5f + angle + MathUtils.getRandomNumberInRange(-4f, 4f)),
							MathUtils.getRandomNumberInRange(3f, 6f), //size
							1.0f, //brightness
							MathUtils.getRandomNumberInRange(0.5f, 0.7f), //duration
							new Color(125,175,255,240));
		        }
				for (int k=0; k < 2; k++) {
					engine.addNebulaParticle(MathUtils.getRandomPointInCircle(point, 5f),
							target.getVelocity(),
							50f,
							MathUtils.getRandomNumberInRange(1.3f, 1.6f),
							0.5f,
							0.4f,
							MathUtils.getRandomNumberInRange(0.95f, 1.05f),
							new Color(150,125,240,160));
					
				}
				
				for (int l=0; l < 16; l++) {
					
					Vector2f sparkVel1 = MathUtils.getPointOnCircumference(target.getVelocity(), MathUtils.getRandomNumberInRange(30f, 150f), shieldOrient + MathUtils.getRandomNumberInRange(81f, 89f));
					Vector2f sparkVel2 = MathUtils.getPointOnCircumference(target.getVelocity(), MathUtils.getRandomNumberInRange(30f, 150f), shieldOrient + MathUtils.getRandomNumberInRange(-81f, -89f));
					
					Global.getCombatEngine().addSmoothParticle(point,
							sparkVel1,
							MathUtils.getRandomNumberInRange(2f, 9f), //size
							0.9f, //brightness
							MathUtils.getRandomNumberInRange(0.6f, 0.95f), //duration
							new Color(180,120,255,255));
					Global.getCombatEngine().addSmoothParticle(point,
							sparkVel2,
							MathUtils.getRandomNumberInRange(2f, 9f), //size
							0.9f, //brightness
							MathUtils.getRandomNumberInRange(0.6f, 0.95f), //duration
							new Color(180,120,255,255));
					
					for (int m=0; m < 3; m++) {
						Vector2f sparkVel3 = MathUtils.getPointOnCircumference(target.getVelocity(), MathUtils.getRandomNumberInRange(12f, 70f), shieldOrient + MathUtils.getRandomNumberInRange(-80f, 80f));
						
						Global.getCombatEngine().addSmoothParticle(MathUtils.getRandomPointInCircle(point, 5f),
								sparkVel3,
								MathUtils.getRandomNumberInRange(2f, 6f), //size
								0.8f, //brightness
								MathUtils.getRandomNumberInRange(0.6f, 0.9f), //duration
								new Color(140,115,255,255));
						
					}
					
				}
				
				
			} else {
				DAMAGE = projectile.getDamageAmount() / 4.8f;
				dealArmorDamage(projectile, (ShipAPI) target, point);

				engine.addNebulaParticle(point,
						target.getVelocity(),
						45f,
						1.5f,
						0.45f,
						0.5f,
						MathUtils.getRandomNumberInRange(0.75f, 0.85f),
						new Color(175,125,210,160));
				
				// spawn a "jet" to the rear of impact angle, for the *cool factor*
				for (int i=0; i < 20; i++) {
					
					for (int j=0; j < 4; j++) {
						Vector2f sparkVel1 = MathUtils.getPointOnCircumference(target.getVelocity(), MathUtils.getRandomNumberInRange(10f, 120f), projectile.getFacing() + MathUtils.getRandomNumberInRange(120f, 240f));
						
						Global.getCombatEngine().addSmoothParticle(point,
								sparkVel1,
								MathUtils.getRandomNumberInRange(3f, 8f), //size
								0.8f, //brightness
								MathUtils.getRandomNumberInRange(0.55f, 0.7f), //duration
								new Color(210,140,100,255));
					}
					
					Vector2f sparkVel2 = MathUtils.getPointOnCircumference(target.getVelocity(), MathUtils.getRandomNumberInRange(80f, 200f), projectile.getFacing() + MathUtils.getRandomNumberInRange(176f, 184f));
					
					Global.getCombatEngine().addSmoothParticle(point,
							sparkVel2,
							MathUtils.getRandomNumberInRange(2f, 9f), //size
							0.9f, //brightness
							MathUtils.getRandomNumberInRange(0.6f, 0.8f), //duration
							new Color(180,120,255,255));
		        }
				
				for (int k=0; k < 12; k++) {
		        	float angle1 = projectile.getFacing() + MathUtils.getRandomNumberInRange(166f, 184f);
		            Vector2f offsetVel1 = MathUtils.getPointOnCircumference(target.getVelocity(), MathUtils.getRandomNumberInRange(3f, 40f), angle1);
		            
		            Vector2f point1 = MathUtils.getPointOnCircumference(point, MathUtils.getRandomNumberInRange(2f, 23f), angle1);
		            
		            engine.addNebulaSmokeParticle(MathUtils.getRandomPointInCircle(point1, 3f),
		            		offsetVel1,
		            		MathUtils.getRandomNumberInRange(9f, 12f), //size
		            		MathUtils.getRandomNumberInRange(1.4f, 1.7f), //end mult
		            		0.4f, //ramp fraction
		            		0.5f, //full bright fraction
		            		MathUtils.getRandomNumberInRange(0.7f, 0.9f), //duration
		            		new Color(120,100,105,75));
				}
				
				
				Global.getCombatEngine().spawnExplosion(point, target.getVelocity(), new Color(170,120,255,255), 11f, 0.7f);
			}
		}
		
		
	}
	
	public static void dealArmorDamage(DamagingProjectileAPI projectile, ShipAPI target, Vector2f point) {
		CombatEngineAPI engine = Global.getCombatEngine();
		
		ArmorGridAPI grid = target.getArmorGrid();
		int[] cell = grid.getCellAtLocation(point);
		if (cell == null) return;
		
		int gridWidth = grid.getGrid().length;
		int gridHeight = grid.getGrid()[0].length;
		
		float damageTypeMult = DisintegratorEffect.getDamageTypeMult(projectile.getSource(), target);
		
		float damageDealt = 0f;
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				if ((i == 2 || i == -2) && (j == 2 || j == -2)) continue; // skip corners
				
				int cx = cell[0] + i;
				int cy = cell[1] + j;
				
				if (cx < 0 || cx >= gridWidth || cy < 0 || cy >= gridHeight) continue;
				
				float damMult = 1/30f;
				if (i == 0 && j == 0) {
					damMult = 1/15f;
				} else if (i <= 1 && i >= -1 && j <= 1 && j >= -1) { // S hits
					damMult = 1/15f;
				} else { // T hits
					damMult = 1/30f;
				}
				
				float armorInCell = grid.getArmorValue(cx, cy);
				float damage = DAMAGE * damMult * damageTypeMult;
				damage = Math.min(damage, armorInCell);
				if (damage <= 0) continue;
				
				target.getArmorGrid().setArmorValue(cx, cy, Math.max(0, armorInCell - damage));
				damageDealt += damage;
			}
		}
		
		if (damageDealt > 0) {
			if (Misc.shouldShowDamageFloaty(projectile.getSource(), target)) {
				engine.addFloatingDamageText(point, damageDealt, Misc.FLOATY_ARMOR_DAMAGE_COLOR, target, projectile.getSource());
			}
			target.syncWithArmorGridState();
		}
	}
}
