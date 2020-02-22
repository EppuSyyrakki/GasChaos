package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

public class Garden extends Building {
    private int weedsAmount = 0;    // bigger reduces gardenGrowth
    private int gardenGrowth = 5;
    private int gardenAmount = 0;   // increased by gardenGrowth every turn in less than gardenMax;
    private int gardenMax = 50;

    public Garden() {
        background = new Texture("gardenBackground.png");
    }

    /**
     * Reduce this.weeds to 0.
     */
    public GameData actionWeedGarden(GameData data) {
        if (weedsAmount <= 0) {
            // TODO garden is already weeded UI message
        } else {
            weedsAmount = 0;
            // TODO garden has been weeded UI message
            data.setActionsDone(data.getActionsDone() + 1);
        }
        return data;
    }

    /**
     * Increase data.gardenSold by gardenAmount and reduce gardenAmount to 0. Block if gardenAmount
     * is not over 30 (garden is not ripe).
     */
    public GameData actionSellGarden(GameData data) {
        if (gardenAmount > 30) {
            data.setGardenSold(gardenAmount);
            gardenAmount = 0;
            // TODO garden produce sold UI message
            data.setActionsDone(data.getActionsDone() + 1);
        } else {
            // TODO garden produce not ripe UI message
        }
        return data;
    }

    /**
     * Decrease gardenGrowth by weedsAmount, increase gardenAmount by gardenGrowth and increase
     * weedsAmount by 1. Minimum gardenGrowth = 1.
     */
    public void update() {
        gardenGrowth -= weedsAmount;

        if (gardenGrowth < 1) {
            gardenGrowth = 1;
        }

        gardenAmount += gardenGrowth;
        weedsAmount++;

        if (gardenAmount > 50) {
            gardenAmount = 50;
        }

        if (weedsAmount > 5) {
            weedsAmount = 5;
        }
    }
}
