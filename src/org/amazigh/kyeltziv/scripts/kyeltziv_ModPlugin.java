package org.amazigh.kyeltziv.scripts;

import org.amazigh.kyeltziv.scripts.ai.kyeltziv_AvtomatDrumfireMissileAI;
import org.amazigh.kyeltziv.scripts.ai.kyeltziv_BellisMagicMissileAI;
import org.amazigh.kyeltziv.scripts.ai.kyeltziv_TrocarMissileAI;
import org.amazigh.kyeltziv.scripts.ai.kyeltziv_VedhaMagicMissileAI;
import org.amazigh.kyeltziv.scripts.world.kyeltzivGen;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.CampaignPlugin;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.codex.CodexDataV2;

import org.dark.shaders.util.ShaderLib;
import org.dark.shaders.util.TextureData;
import org.dark.shaders.light.LightData;

public class kyeltziv_ModPlugin extends BaseModPlugin {

	public static final String BELLIS_CANISTER_MISSILE_ID = "kyeltziv_bellis_canister";
	public static final String AVTOMAT_DRUMFIRE_MISSILE_ID = "kyeltziv_drumfire_avtomat";
	public static final String VEDHA_MISSILE_ID = "kyeltziv_vedha_rocket";
	public static final String VEDIC_MISSILE_ID = "kyeltziv_vedha_rocket_b";
	public static final String TROCAR_MISSILE_ID = "kyeltziv_trocar_limpet_latch";
	
	public boolean HAS_GRAPHICSLIB = false;
	
    //New game stuff
    @Override
    public void onNewGame() {
        new kyeltzivGen().generate(Global.getSector());
    }
    
    public void onApplicationLoad() {
        boolean hasGraphicsLib = Global.getSettings().getModManager().isModEnabled("shaderLib");
        if (hasGraphicsLib) {
            HAS_GRAPHICSLIB = true;
            ShaderLib.init();
            TextureData.readTextureDataCSV((String)"data/config/kyeltziv_texture_data.csv");
            LightData.readLightDataCSV((String)"data/config/kyeltziv_lights_data.csv");
        }
    }
    
    @Override
    public PluginPick<MissileAIPlugin> pickMissileAI(MissileAPI missile, ShipAPI launchingShip) {
        switch (missile.getProjectileSpecId()) {
            case BELLIS_CANISTER_MISSILE_ID:
                return new PluginPick<MissileAIPlugin>(new kyeltziv_BellisMagicMissileAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC);
            case AVTOMAT_DRUMFIRE_MISSILE_ID:
                return new PluginPick<MissileAIPlugin>(new kyeltziv_AvtomatDrumfireMissileAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC);
            case VEDHA_MISSILE_ID:
                return new PluginPick<MissileAIPlugin>(new kyeltziv_VedhaMagicMissileAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC);
            case VEDIC_MISSILE_ID:
                return new PluginPick<MissileAIPlugin>(new kyeltziv_VedhaMagicMissileAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC);
            case TROCAR_MISSILE_ID:
                return new PluginPick<MissileAIPlugin>(new kyeltziv_TrocarMissileAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC);
            default:
                return null;
        }
    }
    
	@Override
	public void onCodexDataGenerated() {
		
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_pack_ass"), CodexDataV2.getHullmodEntryId("kyeltziv_pack_slot"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_pack_siege"), CodexDataV2.getHullmodEntryId("kyeltziv_pack_slot"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_pack_supp"), CodexDataV2.getHullmodEntryId("kyeltziv_pack_slot"));
		
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_pack_ass"), CodexDataV2.getHullmodEntryId("kyeltziv_pack_siege"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_pack_ass"), CodexDataV2.getHullmodEntryId("kyeltziv_pack_supp"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_pack_supp"), CodexDataV2.getHullmodEntryId("kyeltziv_pack_siege"));
		
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_ass"), CodexDataV2.getHullmodEntryId("kyeltziv_pack_slot"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_siege"), CodexDataV2.getHullmodEntryId("kyeltziv_pack_slot"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_supp"), CodexDataV2.getHullmodEntryId("kyeltziv_pack_slot"));
		
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_ass"), CodexDataV2.getHullmodEntryId("kyeltziv_loader_siege"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_ass"), CodexDataV2.getHullmodEntryId("kyeltziv_loader_supp"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_supp"), CodexDataV2.getHullmodEntryId("kyeltziv_loader_siege"));
		
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_ass"), CodexDataV2.getHullmodEntryId("kyeltziv_variable_booster"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_ass"), CodexDataV2.getShipSystemEntryId("kyeltziv_VariableBooster"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_siege"), CodexDataV2.getHullmodEntryId("kyeltziv_variable_booster"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_siege"), CodexDataV2.getShipSystemEntryId("kyeltziv_VariableBooster"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_supp"), CodexDataV2.getHullmodEntryId("kyeltziv_variable_booster"));
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_loader_supp"), CodexDataV2.getShipSystemEntryId("kyeltziv_VariableBooster"));
		// linked the packages to each other!
		
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_vassily"), CodexDataV2.getShipEntryId("kyeltziv_vassily_p"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_ilya"), CodexDataV2.getShipEntryId("kyeltziv_ilya_ex"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_ilya"), CodexDataV2.getShipEntryId("kyeltziv_ilya_sym"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_ruslan"), CodexDataV2.getShipEntryId("kyeltziv_ruslan_p"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_ruslan"), CodexDataV2.getShipEntryId("kyeltziv_ruslan_sym"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_peikko"), CodexDataV2.getShipEntryId("kyeltziv_peikko_sym"));
		
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_valkyrie"), CodexDataV2.getShipEntryId("valkyrie"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_tarsus"), CodexDataV2.getShipEntryId("tarsus"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_phaeton"), CodexDataV2.getShipEntryId("phaeton"));
		
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_perun"), CodexDataV2.getShipEntryId("kyeltziv_perun_ex"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_perun"), CodexDataV2.getShipEntryId("kyeltziv_perun_p"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_dazhbog"), CodexDataV2.getShipEntryId("kyeltziv_dazhbog_ex"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_dazhbog"), CodexDataV2.getShipEntryId("kyeltziv_dazhbog_sym"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_yinglong"), CodexDataV2.getShipEntryId("kyeltziv_znatok"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_yinglong"), CodexDataV2.getShipEntryId("kyeltziv_yinglong_ex"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_xinzang"), CodexDataV2.getShipEntryId("kyeltziv_xinzang_ex"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_zlatko"), CodexDataV2.getShipEntryId("kyeltziv_zlatko_p"));
		
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_podenka"), CodexDataV2.getShipEntryId("kyeltziv_podenka_sym"));
		CodexDataV2.makeRelated(CodexDataV2.getShipEntryId("kyeltziv_podenka"), CodexDataV2.getShipEntryId("kyeltziv_podenka_ex"));
		// linked export/pirate/sym variants to each other!
		
		CodexDataV2.makeRelated(CodexDataV2.getHullmodEntryId("kyeltziv_GunPlatform_info"), CodexDataV2.getShipSystemEntryId("kyeltziv_linnake_deploy"));
		
//		CodexDataV2.makeRelated(CodexDataV2.getShipSystemEntryId("kyeltziv_linnake_deploy"), CodexDataV2.getFighterEntryId("kyeltziv_linnake_assault_ass_wing"));
//		CodexDataV2.makeRelated(CodexDataV2.getShipSystemEntryId("kyeltziv_linnake_deploy"), CodexDataV2.getFighterEntryId("kyeltziv_linnake_assault_base_wing"));
//		CodexDataV2.makeRelated(CodexDataV2.getShipSystemEntryId("kyeltziv_linnake_deploy"), CodexDataV2.getFighterEntryId("kyeltziv_linnake_assault_siege_wing"));
//		CodexDataV2.makeRelated(CodexDataV2.getShipSystemEntryId("kyeltziv_linnake_deploy"), CodexDataV2.getFighterEntryId("kyeltziv_linnake_assault_supp_wing"));
		
//		CodexDataV2.makeRelated(CodexDataV2.getShipSystemEntryId("kyeltziv_drone_strike"), CodexDataV2.getFighterEntryId("kyeltziv_kohta_wing"));
		
		/*
kyeltziv_linnake_base_ass_wing
kyeltziv_linnake_base_base_wing
kyeltziv_linnake_base_siege_wing
kyeltziv_linnake_base_supp_wing
kyeltziv_linnake_siege_ass_wing
kyeltziv_linnake_siege_base_wing
kyeltziv_linnake_siege_siege_wing
kyeltziv_linnake_siege_supp_wing
kyeltziv_linnake_supp_ass_wing
kyeltziv_linnake_supp_base_wing
kyeltziv_linnake_supp_siege_wing
kyeltziv_linnake_supp_supp_wing
		 */
		
		// linked the platforms! (only not because it wasn't working??? at least linked the hullmod to the system!)
	}
}
