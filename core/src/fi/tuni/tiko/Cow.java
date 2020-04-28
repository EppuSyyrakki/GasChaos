package fi.tuni.tiko;

@SuppressWarnings("FieldCanBeLocal")
public class Cow {

    /**
     * Cow produces per turn.
     */
    private final int manure = 15;
    private final int milk = 25;
    private final int methane = 300;

    /**
     * Cow eats per turn. If not eaten this turn (out of feed) no milk produced in getMilk().
     */
    private final int feed = 30;

    private boolean eatenLastTurn = true;

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
        if (totalFeed > feed) {
            int newFeed = totalFeed - feed;
            eatenLastTurn = true;
            return newFeed;
        } else {
            eatenLastTurn = false;
            return totalFeed;
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
            return (int)floatMilk;
        } else {
            return milk;
        }
    }

    public int getFeed() {
        return feed;
    }

    public int getManure() {
        return manure;
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

    public boolean isEatenLastTurn() {
        return eatenLastTurn;
    }

    public void setEatenLastTurn(boolean eatenLastTurn) {
        this.eatenLastTurn = eatenLastTurn;
    }

    public int getMethane() {
        return methane;
    }

    // Do not remove, crashes the game on loadGame()
    public Cow() {
    }
}
