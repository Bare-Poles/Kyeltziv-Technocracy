package org.amazigh.kyeltziv.scripts.world.systems;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.PlanetConditionGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;
import com.fs.starfarer.api.util.Misc;

public class kyeltziv_koltsevayaGen {
  
    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Koltsevaya");
        system.getLocation().set(-15500, 16500);
        system.setBackgroundTextureFilename("graphics/backgrounds/background2.jpg");
        
     // Star
        PlanetAPI koltsevaya_star = system.initStar(
                "kyeltziv_koltsevaya",
                "star_orange",
                805f,
                701f);
        
        koltsevaya_star.setName("Koltsevaya");
        
        system.setLightColor(new Color(230, 200, 156));
        
        
     // Inner uninhabited planet
        PlanetAPI koltsevaya_1 = system.addPlanet("kyeltziv_puotila",
        		koltsevaya_star,
                "Puotila",
                "lava",
                300f, //starting angle
                108f, //size
                3601f, // orbit radius
                311f); // orbit time
        koltsevaya_1.setCustomDescriptionId("kyeltziv_planet_puotila");
        PlanetConditionGenerator.generateConditionsForPlanet(koltsevaya_1, StarAge.AVERAGE);
        
        
        
     // Mining Planet
        PlanetAPI koltsevaya_2 = system.addPlanet("kyeltziv_yasenevo",
        		koltsevaya_star,
                "Yasenevo",
                "rocky_metallic",
                200f, //starting angle
                149f, //size
                5732f, // orbit radius
                391f); // orbit time
        koltsevaya_2.setCustomDescriptionId("kyeltziv_planet_yasenevo");  
        
        MarketAPI koltsevaya_2_market = kyeltziv_AddMarketplace.addMarketplace("kyeltziv",
                koltsevaya_2,
                null,
                "Yasenevo",
                5,
                new ArrayList<String>(
                    Arrays.asList(
                        Conditions.POPULATION_5,
                        Conditions.LOW_GRAVITY,
                        Conditions.COLD,
                        Conditions.THIN_ATMOSPHERE,
                        Conditions.ORE_RICH,
                        Conditions.RARE_ORE_ABUNDANT
                    )
                ),
                new ArrayList<String>(
                    Arrays.asList(
                        Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                    )
                ),
                new ArrayList<String>(
                    Arrays.asList(
                        Industries.POPULATION,
                        Industries.MEGAPORT,
                        Industries.MINING,
                        //Industries.REFINING,
                        //Industries.ORBITALWORKS,
                        //"commerce",
                        "kyeltziv_starfortress", // Industries.STARFORTRESS_MID,
                        Industries.HEAVYBATTERIES,
                        Industries.MILITARYBASE
                    )
                ),
                true,
                false);
        
        koltsevaya_2_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.GAMMA_CORE);
        koltsevaya_2_market.getIndustry(Industries.MINING).setAICoreId(Commodities.GAMMA_CORE);
        koltsevaya_2_market.getIndustry("kyeltziv_starfortress").setAICoreId(Commodities.GAMMA_CORE);
        koltsevaya_2_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.GAMMA_CORE);
        koltsevaya_2_market.getIndustry(Industries.MILITARYBASE).setAICoreId(Commodities.GAMMA_CORE);

        koltsevaya_2_market.addIndustry(Industries.ORBITALWORKS,new ArrayList<String>(Arrays.asList(Items.CORRUPTED_NANOFORGE)));
        koltsevaya_2_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.BETA_CORE);
        
        
        
     // Gas Giant
        PlanetAPI koltsevaya_3 = system.addPlanet("kyeltziv_kuzminki",
        		koltsevaya_star,
                "Kuzminki",
                "gas_giant",
                100f, //starting angle
                333f, //size
                6546f, // orbit radius
                442f); // orbit time
        koltsevaya_3.setCustomDescriptionId("kyeltziv_planet_kuzminki");  
        
        MarketAPI koltsevaya_3_market = kyeltziv_AddMarketplace.addMarketplace("kyeltziv",
                koltsevaya_3,
                null,
                "Kuzminki",
                4,
                new ArrayList<String>(
                    Arrays.asList(
                        Conditions.POPULATION_4,
                        Conditions.HIGH_GRAVITY,
                        Conditions.DENSE_ATMOSPHERE,
                        Conditions.VOLATILES_ABUNDANT
                    )
                ),
                new ArrayList<String>(
                    Arrays.asList(
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                    )
                ),
                new ArrayList<String>(
                    Arrays.asList(
                        Industries.POPULATION,
                        Industries.MEGAPORT,
                        Industries.MINING,
                        Industries.FUELPROD,
                        // "commerce",
                        "kyeltziv_battlestation", //Industries.BATTLESTATION_MID,
                        Industries.GROUNDDEFENSES
                    )
                ),
                true,
                false);
        
        koltsevaya_3_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.GAMMA_CORE);
        koltsevaya_3_market.getIndustry(Industries.MINING).setAICoreId(Commodities.GAMMA_CORE);
        koltsevaya_3_market.getIndustry(Industries.FUELPROD).setAICoreId(Commodities.BETA_CORE);
        koltsevaya_3_market.getIndustry("kyeltziv_battlestation").setAICoreId(Commodities.GAMMA_CORE);
        koltsevaya_3_market.getIndustry(Industries.GROUNDDEFENSES).setAICoreId(Commodities.GAMMA_CORE);
        
        
     // Capital
        PlanetAPI koltsevaya_4 = system.addPlanet("kyeltziv_kurskaya",
        		koltsevaya_star,
                "Kurskaya",
                "terran-eccentric",
                10f, //starting angle
                191f, //size
                8394f, // orbit radius
                497f); // orbit time
        koltsevaya_4.setCustomDescriptionId("kyeltziv_planet_kurskaya");  
        
        if (Global.getSettings().getModManager().isModEnabled("IndEvo")) {
        	MarketAPI koltsevaya_4_market = kyeltziv_AddMarketplace.addMarketplace("kyeltziv",
                    koltsevaya_4,
                    null,
                    "Kurskaya",
                    6,
                    new ArrayList<String>(
                        Arrays.asList(
                            Conditions.POPULATION_6,
                            Conditions.HABITABLE,
                            "IndEvo_mineFieldCondition",
                            Conditions.FARMLAND_ADEQUATE,
                            Conditions.ORGANICS_TRACE
                        )
                    ),
                    new ArrayList<String>(
                        Arrays.asList(
                            Submarkets.GENERIC_MILITARY,
                            Submarkets.SUBMARKET_OPEN,
                            Submarkets.SUBMARKET_STORAGE,
                            Submarkets.SUBMARKET_BLACK
                        )
                    ),
                    new ArrayList<String>(
                        Arrays.asList(
                            Industries.POPULATION,
                            Industries.MEGAPORT,
                            Industries.FARMING,
                            Industries.MINING,
                            "commerce",
                            "kyeltziv_starfortress", // Industries.STARFORTRESS_MID,
                            Industries.HEAVYBATTERIES,
                            Industries.HIGHCOMMAND
                        )
                    ),
                    true,
                    false);
        	
        	koltsevaya_4_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry(Industries.FARMING).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry(Industries.MINING).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry("kyeltziv_starfortress").setAICoreId(Commodities.BETA_CORE);
            koltsevaya_4_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.BETA_CORE);
        } else {
        	MarketAPI koltsevaya_4_market = kyeltziv_AddMarketplace.addMarketplace("kyeltziv",
                    koltsevaya_4,
                    null,
                    "Kurskaya",
                    6,
                    new ArrayList<String>(
                        Arrays.asList(
                            Conditions.POPULATION_6,
                            Conditions.HABITABLE,
                            Conditions.STEALTH_MINEFIELDS,
                            Conditions.FARMLAND_ADEQUATE,
                            Conditions.ORGANICS_TRACE
                        )
                    ),
                    new ArrayList<String>(
                        Arrays.asList(
                            Submarkets.GENERIC_MILITARY,
                            Submarkets.SUBMARKET_OPEN,
                            Submarkets.SUBMARKET_STORAGE,
                            Submarkets.SUBMARKET_BLACK
                        )
                    ),
                    new ArrayList<String>(
                        Arrays.asList(
                            Industries.POPULATION,
                            Industries.MEGAPORT,
                            Industries.FARMING,
                            Industries.MINING,
                            "commerce",
                            "kyeltziv_starfortress", // Industries.STARFORTRESS_MID,
                            Industries.HEAVYBATTERIES,
                            Industries.HIGHCOMMAND
                        )
                    ),
                    true,
                    false);
        	
        	koltsevaya_4_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry(Industries.FARMING).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry(Industries.MINING).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry("kyeltziv_starfortress").setAICoreId(Commodities.BETA_CORE);
            koltsevaya_4_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.GAMMA_CORE);
            koltsevaya_4_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.BETA_CORE);
        }
        
     // Light industry station
        SectorEntityToken koltsevaya_station = system.addCustomEntity("kyeltziv_solntsevo", "Solntsevo Station", "station_midline1", "kyeltziv");
        koltsevaya_station.setCircularOrbitPointingDown(koltsevaya_star, 210, 10016f ,564f);
        koltsevaya_station.setCustomDescriptionId("kyeltziv_station_solntsevo");
        
		MarketAPI koltsevaya_station_market = kyeltziv_AddMarketplace.addMarketplace("kyeltziv",
				koltsevaya_station, null,
                "Solntsevo Station",
                4,
                new ArrayList<String>(
                        Arrays.asList(
                                Conditions.POPULATION_4,
                                Conditions.NO_ATMOSPHERE,
                                Conditions.OUTPOST
                        )
                ),
                new ArrayList<String>(
                        Arrays.asList(
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE,
                                Submarkets.SUBMARKET_BLACK
                        )
                ),
                new ArrayList<String>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.SPACEPORT,
                                Industries.LIGHTINDUSTRY,
                                Industries.REFINING,
                                Industries.WAYSTATION,
                                Industries.PATROLHQ,
                                "kyeltziv_orbitalstation" // Industries.ORBITALSTATION_MID
                        )
                ),
                true,
                false);
        
		koltsevaya_station_market.getIndustry(Industries.LIGHTINDUSTRY).setAICoreId(Commodities.GAMMA_CORE);
		koltsevaya_station_market.getIndustry(Industries.REFINING).setAICoreId(Commodities.GAMMA_CORE);
		koltsevaya_station_market.getIndustry("kyeltziv_orbitalstation").setAICoreId(Commodities.GAMMA_CORE);
		

		
		        
	 // Outer uninhabited planets
		PlanetAPI koltsevaya_5 = system.addPlanet("kyeltziv_rastila",
				koltsevaya_star,
				"Rastila",
				"irradiated",
				250f, //starting angle
				123f, //size
				12129f, // orbit radius
				625f); // orbit time
		koltsevaya_5.setCustomDescriptionId("kyeltziv_planet_rastila");
        PlanetConditionGenerator.generateConditionsForPlanet(koltsevaya_5, StarAge.AVERAGE);
		        
		PlanetAPI koltsevaya_6 = system.addPlanet("kyeltziv_kontula",
				koltsevaya_star,
				"Kontula",
				"frozen2",
				70f, //starting angle
				189f, //size
				14526f, // orbit radius
				649f); // orbit time
		koltsevaya_6.setCustomDescriptionId("kyeltziv_planet_kontula");
        PlanetConditionGenerator.generateConditionsForPlanet(koltsevaya_6, StarAge.AVERAGE);
        
	 // Relay/Buoy/Array 
        SectorEntityToken koltsevaya_relay = system.addCustomEntity("kyeltziv_koltsevaya_relay", // unique id
                "Koltsevaya Relay", // name - if null, defaultName from custom_entities.json will be used
                "comm_relay", // type of object, defined in custom_entities.json
                "kyeltziv"); // faction
        koltsevaya_relay.setCircularOrbitPointingDown( koltsevaya_star, 31f, 7240, 370);
        
        SectorEntityToken koltsevaya_buoy = system.addCustomEntity("kyeltziv_koltsevaya_buoy", // unique id
                "Koltsevaya Buoy", // name - if null, defaultName from custom_entities.json will be used
                "nav_buoy", // type of object, defined in custom_entities.json
                "kyeltziv"); // faction
        koltsevaya_buoy.setCircularOrbitPointingDown( koltsevaya_star, 151f, 7290, 370);
        
        SectorEntityToken koltsevaya_array = system.addCustomEntity("kyeltziv_koltsevaya_relay", // unique id
                "Koltsevaya Array", // name - if null, defaultName from custom_entities.json will be used
                "sensor_array", // type of object, defined in custom_entities.json
                "kyeltziv"); // faction
        koltsevaya_array.setCircularOrbitPointingDown( koltsevaya_star, 271f, 7240, 370);
        

     // Inner system jump point
        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("kyeltziv_koltsevaya_inner_jump", "Koltsevaya Inner Jump Point");
        jumpPoint1.setCircularOrbit(koltsevaya_star, 134, 3461, 213);
        system.addEntity(jumpPoint1);
	        
        
     // Asteroid field around Rastila
        SectorEntityToken koltsevaya_astfield_1 = system.addTerrain(Terrain.ASTEROID_FIELD,
				new AsteroidFieldParams(
					630f, // min radius
					780f, // max radius
					21, // min asteroid count
					27, // max asteroid count
					4f, // min asteroid radius 
					15f, // max asteroid radius
					null)); // null for default name
        koltsevaya_astfield_1.setCircularOrbit(koltsevaya_5, 0, 1, 96);
        
        
     // thick ring intersecting Solntsevo Station
        system.addRingBand(koltsevaya_star, "misc", "rings_dust0", 		256f, 0, Color.white, 200f, 9846, 491f);
        system.addRingBand(koltsevaya_star, "misc", "rings_dust0", 		256f, 1, Color.white, 200f, 10016, 511f);
        system.addRingBand(koltsevaya_star, "misc", "rings_dust0", 		256f, 0, Color.white, 220f, 10186, 531f);  
        system.addAsteroidBelt(koltsevaya_star, 73, 9956, 165, 479, 539, Terrain.ASTEROID_BELT, null);
        system.addAsteroidBelt(koltsevaya_star, 101, 10116, 165, 483, 543, Terrain.ASTEROID_BELT, null);
        
     // asteroids around Yasenevo
		SectorEntityToken koltsevaya_astfield_yas1 = system.addTerrain(Terrain.ASTEROID_FIELD,
				new AsteroidFieldParams(
						390f, // min radius
						520f, // max radius
						11, // min asteroid count
						15, // max asteroid count
						6f, // min asteroid radius 
						19f, // max asteroid radius
						null)); // null for default name
		koltsevaya_astfield_yas1.setCircularOrbit(koltsevaya_star, 190f, 5730f, 391f);
		 
		SectorEntityToken koltsevaya_astfield_yas2 = system.addTerrain(Terrain.ASTEROID_FIELD,
				new AsteroidFieldParams(
						510f, // min radius
						590f, // max radius
						17, // min asteroid count
						21, // max asteroid count
						5f, // min asteroid radius 
						17f, // max asteroid radius
						null)); // null for default name
		koltsevaya_astfield_yas2.setCircularOrbit(koltsevaya_star, 200f, 5741f, 391f);
		  
		SectorEntityToken koltsevaya_astfield_yas3 = system.addTerrain(Terrain.ASTEROID_FIELD,
				new AsteroidFieldParams(
						390f, // min radius
						520f, // max radius
						11, // min asteroid count
						15, // max asteroid count
						5f, // min asteroid radius 
						19f, // max asteroid radius
						null)); // null for default name
		koltsevaya_astfield_yas3.setCircularOrbit(koltsevaya_star, 210f, 5730f, 391f);
		  
		
     // Gas Giant Ring
		 system.addRingBand(koltsevaya_3, "misc", "rings_special0", 256f, 0, Color.white, 300f, 660, 243f, Terrain.RING, null);
		
		
     // generates hyperspace destinations for in-system jump points
		system.autogenerateHyperspaceJumpPoints(true, true);

     // Finally cleans up hyperspace
		cleanup(system);
	}

    //Shorthand function for cleaning up hyperspace
    private void cleanup(StarSystemAPI system){
    	HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;
        float radius = system.getMaxRadiusInHyperspace();
        
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius * 0.5f, 0f, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0f, 360f, 0.25f);
    }
}
