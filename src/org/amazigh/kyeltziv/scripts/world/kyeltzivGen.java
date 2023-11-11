package org.amazigh.kyeltziv.scripts.world;

import org.amazigh.kyeltziv.scripts.world.systems.kyeltziv_koltsevayaGen;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;

public class kyeltzivGen implements SectorGeneratorPlugin {
   
    @Override
    public void generate(SectorAPI sector) {
        
        new kyeltziv_koltsevayaGen().generate(sector);

        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("kyeltziv");

        FactionAPI kyeltziv = sector.getFaction("kyeltziv");
        FactionAPI player = sector.getFaction(Factions.PLAYER);
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
        FactionAPI diktat = sector.getFaction(Factions.DIKTAT);
        FactionAPI kol = sector.getFaction(Factions.KOL);
        FactionAPI persean = sector.getFaction(Factions.PERSEAN);
        FactionAPI guard = sector.getFaction(Factions.LIONS_GUARD);
        FactionAPI remnant = sector.getFaction(Factions.REMNANTS);
        FactionAPI derelict = sector.getFaction(Factions.DERELICT);

        //vanilla factions
        kyeltziv.setRelationship(hegemony.getId(), -0.25f);
        kyeltziv.setRelationship(player.getId(), 0);
        kyeltziv.setRelationship(pirates.getId(), -0.5f);
        
        kyeltziv.setRelationship(independent.getId(), 0.2f);
        
        kyeltziv.setRelationship(tritachyon.getId(), -0.5f);
        
        kyeltziv.setRelationship(kol.getId(), -0.5f);
        kyeltziv.setRelationship(path.getId(), -0.75f);
        kyeltziv.setRelationship(church.getId(), -0.5f);
        
        kyeltziv.setRelationship(persean.getId(), 0.5f);
        kyeltziv.setRelationship(guard.getId(), 0.25f);
        kyeltziv.setRelationship(diktat.getId(), 0.25f);
        
        
        //environment
        kyeltziv.setRelationship(remnant.getId(), RepLevel.HOSTILE);
        kyeltziv.setRelationship(derelict.getId(), RepLevel.HOSTILE);
        
        // mod factions
        kyeltziv.setRelationship("vic", -0.25f);
        kyeltziv.setRelationship("ironsentinel", -0.25f);
        kyeltziv.setRelationship("ironshell", -0.25f);
        kyeltziv.setRelationship("domhist", -0.25f);
        
        kyeltziv.setRelationship("sylphon", -0.4f);
        
        kyeltziv.setRelationship("xhanempire", -0.2f);
        kyeltziv.setRelationship("unitedpamed", 0.4f);
        
        kyeltziv.setRelationship("Coalition", -0.3f);
        kyeltziv.setRelationship("tiandong", 0f);
        kyeltziv.setRelationship("kadur_remnant", 0.25f);
        kyeltziv.setRelationship("blackrock_driveyards", 0.25f);
		kyeltziv.setRelationship("interstellarimperium", 0.25f);
		kyeltziv.setRelationship("HMI", 0.25f);
		kyeltziv.setRelationship("al_ars", -0.25f);
        kyeltziv.setRelationship("mayorate", -0.25f);
        kyeltziv.setRelationship("SCY", 0.25f);
        kyeltziv.setRelationship("blade_breakers", -0.5f);
        kyeltziv.setRelationship("dassault_mikoyan", 0f);
        kyeltziv.setRelationship("diableavionics", -0.5f);
        kyeltziv.setRelationship("ORA", 0.25f);
        kyeltziv.setRelationship("gmda", -0.25f);
        kyeltziv.setRelationship("gmda_patrol", -0.25f);

        kyeltziv.setRelationship("tahlan_legioinfernalis", -0.75f);
        kyeltziv.setRelationship("yrxp", 0f);
        kyeltziv.setRelationship("uaf", -0.25f);
        
        kyeltziv.setRelationship("cabal", -0.5f);
        
        // the below are sorta based on pamed, but might be fine?
        kyeltziv.setRelationship("shadow_industry", -0.6f);
        kyeltziv.setRelationship("roider", 0.25f);
        kyeltziv.setRelationship("exipirated", 0.25f);
        kyeltziv.setRelationship("draco", -0.6f);
        kyeltziv.setRelationship("fang", -0.6f);
        kyeltziv.setRelationship("junk_pirates", -0.4f);
        kyeltziv.setRelationship("junk_pirates_hounds", -0.4f);
        kyeltziv.setRelationship("junk_pirates_junkboys", -0.4f);
        kyeltziv.setRelationship("junk_pirates_technicians", -0.4f);
        kyeltziv.setRelationship("the_cartel", 0.25f);
        kyeltziv.setRelationship("nullorder", 0.25f);
        kyeltziv.setRelationship("templars", -0.6f);
        kyeltziv.setRelationship("crystanite_pir", -0.6f);
        kyeltziv.setRelationship("infected", -0.6f);
        kyeltziv.setRelationship("new_galactic_order", -0.4f);
        kyeltziv.setRelationship("TF7070_D3C4", -0.6f);
        kyeltziv.setRelationship("minor_pirate_1", -0.5f);
        kyeltziv.setRelationship("minor_pirate_2", -0.5f);
        kyeltziv.setRelationship("minor_pirate_3", -0.5f);
        kyeltziv.setRelationship("minor_pirate_4", -0.5f);
        kyeltziv.setRelationship("minor_pirate_5", -0.5f);
        kyeltziv.setRelationship("minor_pirate_6", -0.5f);
        
    }
}