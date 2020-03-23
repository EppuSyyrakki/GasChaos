package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Cow {
    Texture texture;
    Sprite sprite;

    /**
     * Cow produces per turn.
     */
    private int manure = 9;
    private int milk = 25;
    private int methane = 300;

    /**
     * Cow eats per turn. If not eaten this turn (out of feed) no milk produced in getMilk().
     */
    private int feed = 10;

    private boolean eatenThisTurn = false;

    /**
     * Methane backpack storage.
     */
    private int methaneAmount = 0;
    private int methaneAmountMax = 900;

    /**
     * Cow eats feed. If no feed left, no milk produced in GameData update. Returns the amount of
     * feed minus what this cow ate.
     */
    public int eat(int totalFeed) {
        if (totalFeed > 0) {
            int newFeed = totalFeed - feed;
            eatenThisTurn = true;
            return newFeed;
        } else {
            return 0;
        }
    }

    public int poop() {

        return manure;
    }

    /**
     * Calculate how much milk player gets depending on milkingMachineLevel.
     */
    public int getMilk(int milkingMachineLevel) {
        if (milkingMachineLevel == 2) {
            float floatMilk =  (float)milk * 1.5f;
            int tmpMilk = (int)floatMilk;
            return tmpMilk;
        } else {
            return milk;
        }
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

    public void fart(int gasCollectorLevel) {
        int methaneToAdd = methane;

        if (gasCollectorLevel == 2) {
            float tmpMethane = (float)methane * 1.5f;
            methaneToAdd = (int)tmpMethane;
        }

        methaneAmount += methaneToAdd;

        if (methaneAmount > methaneAmountMax) {
            methaneAmount = methaneAmountMax;
        }
    }

    public boolean isEatenThisTurn() {
        return eatenThisTurn;
    }

    public void setEatenThisTurn(boolean eatenThisTurn) {
        this.eatenThisTurn = eatenThisTurn;
    }
}
