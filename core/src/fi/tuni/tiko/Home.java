package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;


public class Home extends Building {
    Grandmother grandmother;

    public Home() {
        background = new Texture("homeBackground.png");
        grandmother = new Grandmother();
    }

    /**
     * Enable tutorial menu
     */
    public void actionCallGrandMother() {

    }

    /**
     * Add new Cow to cowList.
     */
    public GameData actionBuyCow(GameData data) {

        return data;
    }

    /**
     * Remove last Cow from cowList. Block if only 1 entry in list - must have at least 1 cow
     */
    public GameData actionSellCow(GameData data) {

        return data;
    }

    /**
     * Set data.feed to feedMax. Cost depends on data.feedMax - data.feed
     */
    public GameData actionBuyFeed(GameData data) {

        return data;
    }

    /**
     * Increase data.solarPanelLevel
     */
    public GameData actionSolarPanelBasic(GameData data) {

        return data;
    }

    /**
     * Increase data.solarPanelLevel
     */
    public GameData buySolarPanelAdvanced(GameData data) {

        return data;
    }

    /**
     * Increase data.gasCollectorLevel
     */
    public GameData buyGasCollectorAdvanced(GameData data) {

        return data;
    }

    /**
     * Increase data.milkingMachineLevel
     */
    public GameData buyMilkingMachineAdvanced(GameData data) {

        return data;
    }

    /**
     * Increase data.tractorLevel
     */
    public GameData buyTractorAdvanced(GameData data) {

        return data;
    }

    /**
     * Set next 0 element to 1 in data.fields array
     */
    public GameData rentNewField(GameData data) {

        return data;
    }

    /**
     * Set last available 1 to 0 in data.fields array. Blocked if only indexes 0 and 1 != 0.
     */
    public GameData stopRentingField(GameData data) {

        return data;
    }

    /**
     * Reduce data.manure by 50 if possible and increase data.manureSold by same amount
     */
    public GameData sellManure(GameData data) {

        return data;
    }
}
