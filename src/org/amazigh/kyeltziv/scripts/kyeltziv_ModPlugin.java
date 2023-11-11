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
}