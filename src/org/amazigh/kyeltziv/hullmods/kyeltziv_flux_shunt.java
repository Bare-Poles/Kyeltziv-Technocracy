package org.amazigh.kyeltziv.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_flux_shunt extends BaseHullMod {

	public static final float VENT_BOOST = 0.3f;

	private IntervalUtil interval1 = new IntervalUtil(0.1f,0.2f);
	private IntervalUtil interval2 = new IntervalUtil(0.1f,0.2f);
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

		stats.getVentRateMult().modifyMult(id, 1f + VENT_BOOST);
	}
		
	public void advanceInCombat(ShipAPI ship, float amount){
		if (!ship.isAlive()) return;
		
		CombatEngineAPI engine = Global.getCombatEngine();
		
		ShipSpecificData info = (ShipSpecificData) engine.getCustomData().get("PHASE_SHUNT_DATA_KEY" + ship.getId());
        if (info == null) {
            info = new ShipSpecificData();
        }
		
        // we do the vfx when venting as well as on system usage
        if(ship.getFluxTracker().isVenting()) {
        	info.VENT_TIME = ship.getFluxLevel();
        }
        
			// vfx / flux effects for the system
        if(info.WAVE_READY && ship.getSystem().isChargedown()) {

        	float softLevel = ship.getFluxLevel() - ship.getHardFluxLevel();
        	info.SOFT_MEM = softLevel;
        	if (info.WAVE_READY) {
        		info.VENT_TIME = softLevel + 0.2f;
        	}
        	
        	info.WAVE_READY = false;
			
			ship.getFluxTracker().decreaseFlux(softLevel * ship.getMaxFlux() * 0.4f);
				// remove soft flux
			ship.getFluxTracker().increaseFlux(softLevel * ship.getMaxFlux() * 0.04f, true);
				// increase hard flux
			
			float range = ship.getCollisionRadius() + 20f;
			
			 // vfx
			for (int i=0; i < 80; i++) {
				float angle = i * 4.5f;
				
	        	Vector2f sparkPoint = MathUtils.getPointOnCircumference(ship.getLocation(), MathUtils.getRandomNumberInRange(range * 0.45f, range), angle + MathUtils.getRandomNumberInRange(-2f, 2f));
	        	Vector2f sparkVel = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(10f, 40f), angle + MathUtils.getRandomNumberInRange(-4f, 4f));
	        	Global.getCombatEngine().addSmoothParticle(sparkPoint,
						sparkVel,
						MathUtils.getRandomNumberInRange(3f, 8f), //size
						0.5f, //brightness
						MathUtils.getRandomNumberInRange(0.45f, 0.6f) * (1f + softLevel), //duration
						new Color(130,70,155,115));
				
				Vector2f sparkPoint2 = MathUtils.getPointOnCircumference(ship.getLocation(), MathUtils.getRandomNumberInRange(range * 0.65f, range), angle + MathUtils.getRandomNumberInRange(-2f, 2f));
	        	Vector2f sparkVel2 = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(5f, 15f), angle + MathUtils.getRandomNumberInRange(-4f, 4f));
	        	Global.getCombatEngine().addSmoothParticle(sparkPoint2,
						sparkVel2,
						MathUtils.getRandomNumberInRange(3f, 6f), //size
						0.5f, //brightness
						MathUtils.getRandomNumberInRange(0.4f, 0.55f) * (1f + softLevel), //duration
						new Color(100,150,255,100));
	        }
			
			for (int j=0; j < 10; j++) {
				float angle = j * 36f;
				
				Vector2f nebPoint = MathUtils.getPointOnCircumference(ship.getLocation(), MathUtils.getRandomNumberInRange(range * 0.5f, range * 0.9f), angle + MathUtils.getRandomNumberInRange(-5f, 5f));
				Vector2f nebVel = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(5f, 20f), angle + MathUtils.getRandomNumberInRange(-8f, 8f));
				engine.addNebulaSmokeParticle(nebPoint,
						nebVel,
                		40f, //size
                		2.1f, //end mult
                		0.5f, //ramp fraction
                		0.4f, //full bright fraction
                		MathUtils.getRandomNumberInRange(0.5f, 0.7f) * (1f + softLevel), //duration
                		new Color(120,55,150,60));
				
			}
			
			 // so this should spawn emp arcs, but only if there is a non-trivial amount of soft flux to diss, count and brightness scaling with amount diss'd
			for (float k=0.1f; k < (softLevel * 5); k++) {
				
				float distanceRandom1 = MathUtils.getRandomNumberInRange(0.5f, 0.8f);
				float angleRandom1 = MathUtils.getRandomNumberInRange(0, 360);
				Vector2f arcPoint1 = MathUtils.getPointOnCircumference(ship.getLocation(), range * distanceRandom1, angleRandom1);
				
				float distanceRandom2 = distanceRandom1 * MathUtils.getRandomNumberInRange(0.95f, 1.1f);
				float angleRandom2 = angleRandom1 + MathUtils.getRandomNumberInRange(40, 80);
				Vector2f arcPoint2 = MathUtils.getPointOnCircumference(ship.getLocation(), range * distanceRandom2, angleRandom2);
				
				int alpha = (int) (softLevel * 25f);
				
				engine.spawnEmpArcVisual(arcPoint1, ship, arcPoint2, ship, 8f + (softLevel * 4f),
						new Color(155,70,130,alpha + 25),
						new Color(255,225,255,alpha + 30));
			}
		}
        
        if (info.VENT_TIME > 0f) {
        	interval1.advance(engine.getElapsedInLastFrame());
			interval2.advance(engine.getElapsedInLastFrame());
			if (interval1.intervalElapsed()) {
				info.VENT_TIME -= (0.06f - (ship.getHardFluxLevel() * 0.02f));
				float range = ship.getCollisionRadius() + 20f;
				float randAngle = MathUtils.getRandomNumberInRange(0f, 360f);
				
				for (int i=0; i < 12; i++) {
					float angle = randAngle + i * 30f;
					
		        	Vector2f sparkPoint = MathUtils.getPointOnCircumference(ship.getLocation(), MathUtils.getRandomNumberInRange(range * 0.45f, range), angle + MathUtils.getRandomNumberInRange(-5f, 5f));
		        	Vector2f sparkVel = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(10f, 40f), angle + MathUtils.getRandomNumberInRange(-10f, 10f));
		        	Global.getCombatEngine().addSmoothParticle(sparkPoint,
							sparkVel,
							MathUtils.getRandomNumberInRange(3f, 8f), //size
							0.5f, //brightness
							MathUtils.getRandomNumberInRange(0.45f, 0.6f) * (1f + info.SOFT_MEM), //duration
							new Color(130,70,155,115));
					
					Vector2f sparkPoint2 = MathUtils.getPointOnCircumference(ship.getLocation(), MathUtils.getRandomNumberInRange(range * 0.65f, range), angle + MathUtils.getRandomNumberInRange(-5f, 5f));
		        	Vector2f sparkVel2 = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(5f, 15f), angle + MathUtils.getRandomNumberInRange(-10f, 10f));
		        	Global.getCombatEngine().addSmoothParticle(sparkPoint2,
							sparkVel2,
							MathUtils.getRandomNumberInRange(3f, 6f), //size
							0.5f, //brightness
							MathUtils.getRandomNumberInRange(0.4f, 0.55f) * (1f + info.SOFT_MEM), //duration
							new Color(100,150,255,100));
		        }
				
				for (int j=0; j < 5; j++) {
					float angle = randAngle + j * 72f;
					
					Vector2f nebPoint = MathUtils.getPointOnCircumference(ship.getLocation(), MathUtils.getRandomNumberInRange(range * 0.5f, range * 0.9f), angle + MathUtils.getRandomNumberInRange(-15f, 15f));
					Vector2f nebVel = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(5f, 20f), angle + MathUtils.getRandomNumberInRange(-24f, 24f));
					engine.addNebulaSmokeParticle(nebPoint,
							nebVel,
	                		40f, //size
	                		2.1f, //end mult
	                		0.5f, //ramp fraction
	                		0.4f, //full bright fraction
	                		MathUtils.getRandomNumberInRange(0.5f, 0.7f) * (1f + info.SOFT_MEM), //duration
	                		new Color(120,55,150,60));
					
				}
				
				if (info.VENT_TIME > 0.1f) {
					float distanceRandom1 = MathUtils.getRandomNumberInRange(0.5f, 0.8f);
					float angleRandom1 = MathUtils.getRandomNumberInRange(0, 360);
					Vector2f arcPoint1 = MathUtils.getPointOnCircumference(ship.getLocation(), range * distanceRandom1, angleRandom1);
					
					float distanceRandom2 = distanceRandom1 * MathUtils.getRandomNumberInRange(0.95f, 1.1f);
					float angleRandom2 = angleRandom1 + MathUtils.getRandomNumberInRange(40, 80);
					Vector2f arcPoint2 = MathUtils.getPointOnCircumference(ship.getLocation(), range * distanceRandom2, angleRandom2);
					
					int alpha = (int) (info.SOFT_MEM * 25f);
					
					engine.spawnEmpArcVisual(arcPoint1, ship, arcPoint2, ship, 8f + (info.SOFT_MEM * 4f),
							new Color(155,70,130,alpha + 25),
							new Color(255,225,255,alpha + 30));
					
					Global.getSoundPlayer().playSound("tachyon_lance_emp_impact", 1.0f, 0.3f, ship.getLocation(), ship.getVelocity());
				}
				
			}
			
			if (interval2.intervalElapsed()) {
				info.VENT_TIME -= (0.06f - (ship.getHardFluxLevel() * 0.02f));
				float range = ship.getCollisionRadius() + 20f;
				float randAngle = MathUtils.getRandomNumberInRange(0f, 360f);
				
				for (int i=0; i < 12; i++) {
					float angle = randAngle + i * 30f;
					
		        	Vector2f sparkPoint = MathUtils.getPointOnCircumference(ship.getLocation(), MathUtils.getRandomNumberInRange(range * 0.45f, range), angle + MathUtils.getRandomNumberInRange(-5f, 5f));
		        	Vector2f sparkVel = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(10f, 40f), angle + MathUtils.getRandomNumberInRange(-10f, 10f));
		        	Global.getCombatEngine().addSmoothParticle(sparkPoint,
							sparkVel,
							MathUtils.getRandomNumberInRange(3f, 8f), //size
							0.5f, //brightness
							MathUtils.getRandomNumberInRange(0.45f, 0.6f) * (1f + info.SOFT_MEM), //duration
							new Color(130,70,155,115));
					
					Vector2f sparkPoint2 = MathUtils.getPointOnCircumference(ship.getLocation(), MathUtils.getRandomNumberInRange(range * 0.65f, range), angle + MathUtils.getRandomNumberInRange(-5f, 5f));
		        	Vector2f sparkVel2 = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(5f, 15f), angle + MathUtils.getRandomNumberInRange(-10f, 10f));
		        	Global.getCombatEngine().addSmoothParticle(sparkPoint2,
							sparkVel2,
							MathUtils.getRandomNumberInRange(3f, 6f), //size
							0.5f, //brightness
							MathUtils.getRandomNumberInRange(0.4f, 0.55f) * (1f + info.SOFT_MEM), //duration
							new Color(100,150,255,100));
		        }
				
				for (int j=0; j < 5; j++) {
					float angle = randAngle + j * 72f;
					
					Vector2f nebPoint = MathUtils.getPointOnCircumference(ship.getLocation(), MathUtils.getRandomNumberInRange(range * 0.5f, range * 0.9f), angle + MathUtils.getRandomNumberInRange(-15f, 15f));
					Vector2f nebVel = MathUtils.getPointOnCircumference(ship.getVelocity(), MathUtils.getRandomNumberInRange(5f, 20f), angle + MathUtils.getRandomNumberInRange(-24f, 24f));
					engine.addNebulaSmokeParticle(nebPoint,
							nebVel,
	                		40f, //size
	                		2.1f, //end mult
	                		0.5f, //ramp fraction
	                		0.4f, //full bright fraction
	                		MathUtils.getRandomNumberInRange(0.5f, 0.7f) * (1f + info.SOFT_MEM), //duration
	                		new Color(120,55,150,60));
					
				}
				
				if (info.VENT_TIME > 0.1f) {
					float distanceRandom1 = MathUtils.getRandomNumberInRange(0.5f, 0.8f);
					float angleRandom1 = MathUtils.getRandomNumberInRange(0, 360);
					Vector2f arcPoint1 = MathUtils.getPointOnCircumference(ship.getLocation(), range * distanceRandom1, angleRandom1);
					
					float distanceRandom2 = distanceRandom1 * MathUtils.getRandomNumberInRange(0.95f, 1.1f);
					float angleRandom2 = angleRandom1 + MathUtils.getRandomNumberInRange(40, 80);
					Vector2f arcPoint2 = MathUtils.getPointOnCircumference(ship.getLocation(), range * distanceRandom2, angleRandom2);
					
					int alpha = (int) (info.SOFT_MEM * 25f);
					
					engine.spawnEmpArcVisual(arcPoint1, ship, arcPoint2, ship, 8f + (info.SOFT_MEM * 4f),
							new Color(155,70,130,alpha + 25),
							new Color(255,225,255,alpha + 30));
					
					Global.getSoundPlayer().playSound("tachyon_lance_emp_impact", 1.0f, 0.3f, ship.getLocation(), ship.getVelocity());
				}
			}
        }
		
		if (!info.WAVE_READY && ship.getSystem().isCoolingDown()) {
			info.WAVE_READY = true;
		}
		
		engine.getCustomData().put("PHASE_SHUNT_DATA_KEY" + ship.getId(), info);
		
	}
	
    private class ShipSpecificData {
    	private boolean WAVE_READY = true;
    	private float VENT_TIME = 0f;
    	private float SOFT_MEM = 0f;
    }
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		return null;
	}
	
	@Override
	public boolean shouldAddDescriptionToTooltip(HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
		return false;
	}

	@Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		float opad = 10f;
		
		Color h = Misc.getHighlightColor();
		
		float VENT_DESC = (int) (VENT_BOOST * 100f);
		
		LabelAPI label = tooltip.addPara("The precisely tuned phase coil array of the Khoronit is linked directly to the vessels flux grid, and special systems allow it to bypass several traditional flux management issues by shunting excess flux directly into p-space.", opad);
		
		label = tooltip.addPara("The flux-phase shunt increases active flux vent rate by %s.", opad, h, "" + (int)VENT_DESC + "%");
		label.setHighlight("" + (int)VENT_DESC + "%");
		
		label = tooltip.addPara("When a Phase Disjunction is triggered, this flux-phase shunt engages and instantly dissipates %s of the vessels current soft flux, but generates hard flux proportional to %s of the dissipated flux.", opad, h, "40%", "10%");
		label.setHighlight("40%", "10%");
		label.setHighlightColors(h, h);
		
	}
	
	
    @Override
    public Color getBorderColor() {
        return new Color(197,207,169,200); //189,42,30,150
    }

    @Override
    public Color getNameColor() {
        return new Color(197,207,169,255);
    }
}
