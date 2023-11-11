package data.missions.kyeltziv_test;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

  @Override
	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "KNV", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "SIM", FleetGoal.ATTACK, true);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Kyeltziv Technocracy Simulated Fleet.");
		api.setFleetTagline(FleetSide.ENEMY, "Simulated Opposition Fleet.");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Simulator exercise to test Kyeltziv vessels against a hostile fleet.");
		
		// Set up the player's fleet.
		
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_ilkut_bomb", FleetMemberType.SHIP, "Test 0", true);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_narva_strike", FleetMemberType.SHIP, "Test 1", true);
		
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_khoronit_excursion", FleetMemberType.SHIP, "XOPOHNTb", true);
		
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_zlatko_p_sup", FleetMemberType.SHIP, "Pirate 1", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_perun_p_od", FleetMemberType.SHIP, "Pirate 2", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_ruslan_p_stk", FleetMemberType.SHIP, "Pirate 3", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_vassily_p_raider", FleetMemberType.SHIP, "Pirate 4", false);
		
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_podenka_sym_ass", FleetMemberType.SHIP, "Symmetrist 0", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_dazhbog_sym_bal", FleetMemberType.SHIP, "Symmetrist 1", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_peikko_sym_stk", FleetMemberType.SHIP, "Symmetrist 2", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_ruslan_sym_bomb", FleetMemberType.SHIP, "Symmetrist 3", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_ilya_sym_ass", FleetMemberType.SHIP, "Symmetrist 4", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_ilya_sym_bal", FleetMemberType.SHIP, "Symmetrist 5", false);
		
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_ilya_ex_sup", FleetMemberType.SHIP, "Export 1", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_otkryt_ex_stk2", FleetMemberType.SHIP, "Export 2", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_perun_ex_sup", FleetMemberType.SHIP, "Export 3", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_dazhbog_ex_sup2", FleetMemberType.SHIP, "Export 4", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_yinglong_ex_attack", FleetMemberType.SHIP, "Export 5", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_xinzang_ex_sup", FleetMemberType.SHIP, "Export 6", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_podenka_ex_od", FleetMemberType.SHIP, "Export 7", false);
		
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_kiilto_fs", FleetMemberType.SHIP, "Redacted 1", false);
		//api.addToFleet(FleetSide.PLAYER, "kyeltziv_paistaa_stk", FleetMemberType.SHIP, "Redacted 2", false);
		
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_pyralid_bomb", FleetMemberType.SHIP, "Test 2", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_strekoza_shk", FleetMemberType.SHIP, "Test 3", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_bystro_attack", FleetMemberType.SHIP, "Test 4", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_podenka_shk", FleetMemberType.SHIP, "Test 5", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_podenka_bal", FleetMemberType.SHIP, "Test 6", false);
		
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_katsoja_ass", FleetMemberType.SHIP, "Test 7", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_zlatko_ass", FleetMemberType.SHIP, "Test 8", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_dazhbog_bal", FleetMemberType.SHIP, "Test 9", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_dazhbog_shk", FleetMemberType.SHIP, "Test 10", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_xiezhi_bal", FleetMemberType.SHIP, "Test 11", false);
		
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_xinzang_bomb", FleetMemberType.SHIP, "Test 12", false);
		/*
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_xinzang_bomb", FleetMemberType.SHIP, "Spam Test 1", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_xinzang_bal", FleetMemberType.SHIP, "Spam Test 2", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_xinzang_bal", FleetMemberType.SHIP, "Spam Test 3", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_xinzang_sup", FleetMemberType.SHIP, "Spam Test 4", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_xinzang_sup", FleetMemberType.SHIP, "Spam Test 5", false);
		*/
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_yinglong_support", FleetMemberType.SHIP, "Test 13", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_perun_sup", FleetMemberType.SHIP, "Test 14", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_perun_bomb", FleetMemberType.SHIP, "Test 15", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_znatok_sup", FleetMemberType.SHIP, "Test 16", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_prochnyi_siege", FleetMemberType.SHIP, "Test 17", false);
		
		
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_peikko_stk", FleetMemberType.SHIP, "Test 18", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_ruslan_bomb", FleetMemberType.SHIP, "Test 19", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_ruslan_shk", FleetMemberType.SHIP, "Test 20", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_dobrynya_sup", FleetMemberType.SHIP, "Test 21", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_lycaenid_sup", FleetMemberType.SHIP, "Test 22", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_ilya_ass", FleetMemberType.SHIP, "Test 23", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_ilya_bal", FleetMemberType.SHIP, "Test 24", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_ilya_sup", FleetMemberType.SHIP, "Test 25", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_vassily_ass", FleetMemberType.SHIP, "Test 26", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_vassily_bal", FleetMemberType.SHIP, "Test 27", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_vassily_shk", FleetMemberType.SHIP, "Test 28", false);
		
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_paimen_cbt", FleetMemberType.SHIP, "Test 29", false);
		
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_phaeton_std", FleetMemberType.SHIP, "Logi 1", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_tarsus_std", FleetMemberType.SHIP, "Logi 2", false);
		api.addToFleet(FleetSide.PLAYER, "kyeltziv_valkyrie_std", FleetMemberType.SHIP, "Logi 3", false);
		
		
		
		// Set up the enemy fleet.
		
		api.addToFleet(FleetSide.ENEMY, "conquest_Standard", FleetMemberType.SHIP, "Verovering", true);
		
		api.addToFleet(FleetSide.ENEMY, "dominator_Support", FleetMemberType.SHIP, "Overheerser", false);
		api.addToFleet(FleetSide.ENEMY, "eagle_Assault", FleetMemberType.SHIP, "Adelaar", false);
		api.addToFleet(FleetSide.ENEMY, "eagle_Balanced", FleetMemberType.SHIP, "Arend", false);
		api.addToFleet(FleetSide.ENEMY, "heron_Attack", FleetMemberType.SHIP, "Reiger", false);
		api.addToFleet(FleetSide.ENEMY, "falcon_CS", FleetMemberType.SHIP, "Valk", false);
		api.addToFleet(FleetSide.ENEMY, "falcon_Attack", FleetMemberType.SHIP, "Valkerij", false);
		
		api.addToFleet(FleetSide.ENEMY, "medusa_Attack", FleetMemberType.SHIP, "Kwal", false);
		api.addToFleet(FleetSide.ENEMY, "enforcer_Assault", FleetMemberType.SHIP, "Handhaver", false);
		api.addToFleet(FleetSide.ENEMY, "enforcer_Elite", FleetMemberType.SHIP, "Leermeester", false);
		api.addToFleet(FleetSide.ENEMY, "condor_Attack", FleetMemberType.SHIP, "Slagzwaard", false);
		api.addToFleet(FleetSide.ENEMY, "condor_Attack", FleetMemberType.SHIP, "Vogel", false);
		api.addToFleet(FleetSide.ENEMY, "drover_Strike", FleetMemberType.SHIP, "Veedrijver", false);
		
		api.addToFleet(FleetSide.ENEMY, "wolf_hegemony_Assault", FleetMemberType.SHIP, "Haai", false);
		api.addToFleet(FleetSide.ENEMY, "wolf_Strike", FleetMemberType.SHIP, "Vreten", false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Standard", FleetMemberType.SHIP, "Sjerp", false);
		api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "Zweep", false);
		api.addToFleet(FleetSide.ENEMY, "shepherd_Frontier", FleetMemberType.SHIP, "Schaapherder", false);
		
		
		//api.defeatOnShipLoss("Test 1");
		
		// Set up the map.
		float width = 14000f;
		float height = 16000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		
	}

}
