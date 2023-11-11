package org.amazigh.kyeltziv.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.econ.impl.OrbitalStation;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;

public class kyeltziv_stationChecker extends OrbitalStation {
    
    @Override
    public boolean isAvailableToBuild() {
        SectorAPI sector = Global.getSector();
        
        FactionAPI player = sector.getFaction(Factions.PLAYER);
        FactionAPI kyeltziv = sector.getFaction("kyeltziv");
        //while it may not be strictly necessary we want to be sure that the market exists
        boolean canBuild = market != null &&
                (player.getRelationshipLevel(kyeltziv).isAtWorst(RepLevel.WELCOMING) ||
                    Global.getSector().getPlayerFaction().knowsIndustry(getId()));
        return canBuild;
    }

    @Override
    public String getUnavailableReason() {
        return "Station type unavailable.";
    }

    @Override
    public boolean showWhenUnavailable() {
        return false;
    }
}
