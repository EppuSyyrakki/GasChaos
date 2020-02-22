package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Cow {
    Texture texture;
    Sprite sprite;

    /**
     * Cow produces per turn.
     */
    private int manure = 3;
    private int milk = 3;
    private int methane = 6;

    /**
     * Cow eats per turn.
     */
    private int feed = 6;

    /**
     * Methane backpack storage.
     */
    private int methaneAmount = 0;
    private int methaneAmountMax = 18;

    public int getManure() {
        return manure;
    }

    public int getMilk() {
        return milk;
    }

    public int getFeed() {
        return feed;
    }

    public int getMethaneAmount() {
        return methaneAmount;
    }

    public void setMethaneAmount(int methaneAmount) {
        this.methaneAmount = methaneAmount;
    }

    public int getMethaneAmountMax() {
        return methaneAmountMax;
    }

    public void setMethaneAmountMax(int methaneAmountMax) {
        this.methaneAmountMax = methaneAmountMax;
    }

    public void addMethane(int gasCollectorLevel) {
        methaneAmount += methane * gasCollectorLevel;
        methaneAmountMax *= gasCollectorLevel;

        if (methaneAmount > methaneAmountMax) {
            methaneAmount = methaneAmountMax;
        }
    }
}
