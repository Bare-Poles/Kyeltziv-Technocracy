package org.amazigh.kyeltziv.scripts;

import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;

public class kyeltziv_ShockMineOnHitEffect implements ProximityExplosionEffect {
	
	public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
		
		CombatEngineAPI engine = Global.getCombatEngine();
		Vector2f point = explosion.getLocation();
        
		Color color = new Color(25,100,155,255);
		
        for (int i = 0; i < 3; i++) {
            float distanceRandom1 = MathUtils.getRandomNumberInRange(42f, 56f);
			float angleRandom1 = MathUtils.getRandomNumberInRange(0, 360);
            Vector2f arcPoint1 = MathUtils.getPointOnCircumference(point, distanceRandom1, angleRandom1);

            float distanceRandom2 = distanceRandom1 * MathUtils.getRandomNumberInRange(0.9f, 1.1f);
            float angleRandom2 = angleRandom1 + MathUtils.getRandomNumberInRange(40, 80);
            Vector2f arcPoint2 = MathUtils.getPointOnCircumference(point, distanceRandom2, angleRandom2);
            
            engine.spawnEmpArcVisual(arcPoint1, explosion, arcPoint2, explosion, 18f,
            		color,
                    new Color(255,255,255,155));
        }
        
        for (int i = 0; i< 4; i++) {
        	float distanceRandom1 = MathUtils.getRandomNumberInRange(75f, 105f);
			float angleRandom1 = MathUtils.getRandomNumberInRange(0, 360);
            Vector2f arcPoint1 = MathUtils.getPointOnCircumference(point, distanceRandom1, angleRandom1);
        	
            float distanceRandom2 = distanceRandom1 * MathUtils.getRandomNumberInRange(0.9f, 1.1f);
            float angleRandom2 = angleRandom1 + MathUtils.getRandomNumberInRange(40, 80);
            Vector2f arcPoint2 = MathUtils.getPointOnCircumference(point, distanceRandom2, angleRandom2);
            
            engine.spawnEmpArcVisual(arcPoint1, explosion, arcPoint2, explosion, 12f,
            		color,
                    new Color(255,255,255,155));
        }
        
        for (int i = 0; i < 30; i++) {
        	engine.spawnProjectile(
        			explosion.getSource(),
        			explosion.getWeapon(),
            		"kyeltziv_mine_shock_proj",
            		point,
            		i * 12f,
            		explosion.getVelocity()
            		);
        }
	}
}