package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class GasChaosDebugger {
    public void debugger (GameData gameData) {
        Gdx.app.log("Other", "y = " + gameData.getActionsDone());

        ArrayList<Field> fields = gameData.getFields();

        String fieldLog = "";
        String fieldGrowthLog = "";
        String fieldFertilizerLog = "";
        String fieldRentLog = "";


        // Upgrades
        Gdx.app.log("Upgrade", "Solar level = " + gameData.getSolarPanelLevel());
        Gdx.app.log("Upgrade", "Gas Collector level = " + gameData.getGasCollectorLevel());
        Gdx.app.log("Upgrade", "Milking machine level = " + gameData.getMilkingMachineLevel());
        Gdx.app.log("Upgrade", "Tractor level = " + gameData.getTractorLevel());
        Gdx.app.log("Upgrade", "Gas generator level = " + gameData.getGasGeneratorLevel());

        // Manure and Methane
        Gdx.app.log("Resource", "Manure = " + gameData.getManure());
        Gdx.app.log("Resource", "Manure max = " + gameData.getManureMax());
        Gdx.app.log("Resource", "Methane = " + gameData.getMethane());
        Gdx.app.log("Resource", "Methane max = " + gameData.getMethaneMax());

        // Fields
        Gdx.app.log("Resource array", "Fields = " + fieldLog);
        Gdx.app.log("Resource array", "Field growth = " + fieldGrowthLog);
        Gdx.app.log("Resource array", "Field fertilizer = " + fieldFertilizerLog);
        Gdx.app.log("Resource array", "Fields rented = " + fieldRentLog);

        // Cows
        Gdx.app.log("Resource", "Cow list = " + gameData.getCowList());
        Gdx.app.log("Resource", "Cows bought = " + gameData.getCowsBought());
        Gdx.app.log("Resource", "feed = " + gameData.getFeed());
        Gdx.app.log("Resource", "Feed max = " + gameData.getFeedMax());

        // Economy
        Gdx.app.log("Economy", "Grain sold = " + gameData.getGrainSold());
        Gdx.app.log("Economy", "Methane sold = " + gameData.getMethaneSold());
        Gdx.app.log("Economy", "Manure sold = " + gameData.getManureSold());
        Gdx.app.log("Economy", "Garden sold = " + gameData.getGardenSold());
        Gdx.app.log("Economy", "Feed bought = " + gameData.getFeedBought());
        Gdx.app.log("Economy", "Current turn = " + gameData.getCurrentTurn());
        Gdx.app.log("Economy", "Money = " + gameData.getMoney());

        // Bought check
        Gdx.app.log("Is bought", "Is basic solar bought = " + gameData.isSolarPanelBasicBought());
        Gdx.app.log("Is bought", "Is adv solar bought = " + gameData.isSolarPanelAdvBought());
        Gdx.app.log("Is bought", "Is adv gas collector bought = " + gameData.isGasCollectorAdvBought());
        Gdx.app.log("Is bought", "Is adv milking machine bought = " + gameData.isMilkingMachineAdvBought());
        Gdx.app.log("Is bought", "Is adv tractor bought = " + gameData.isTractorAdvBought());
        Gdx.app.log("Is bought", "Is tractor gas bought = " + gameData.isTractorGasBought());
        Gdx.app.log("Is bought", "Is gas generator bought = " + gameData.isGasGeneratorBought());

    }
}
