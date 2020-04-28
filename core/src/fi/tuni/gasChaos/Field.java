package fi.tuni.gasChaos;

public class Field {

    private int amount = 0;
    private int growth = 0;
    private int fertilizerP = 0;
    private int fertilizerN = 0;
    private int reductionCounter = 3;
    private boolean owned;
    private boolean rented;
    final int MAX_GROWTH = 200; // with perfect growth this comes in 10 turns.
    final int REAPABLE_AMOUNT = 150;

    public Field(boolean owned, boolean rented) {
        this.owned = owned;
        this.rented = rented;
    }

    public void grow() {
        if (amount > 0) {
            if (fertilizerN >= 50 && fertilizerP >= 7) {
                // perfect growth N >= 50 and P >= 7
                growth = 20;
            } else if ((fertilizerN < 50 && fertilizerN >= 10)
                    && (fertilizerP < 7 && fertilizerP >= 3)) {
                // average growth N 10-49 and P 3-6
                growth = 15;
            } else {
                // poor growth N < 10 and P < 2
                growth = 10;
            }
            amount += growth;

            //noinspection ConstantConditions
            if (amount > MAX_GROWTH) {
                amount = MAX_GROWTH;
            }

            if (reductionCounter == 0) {
                fertilizerP -= 1;
                fertilizerN -= 3;
                reductionCounter = 4;

                if (fertilizerN < 0) {
                    fertilizerN = 0;
                }
                if (fertilizerP < 0) {
                    fertilizerP = 0;
                }
            }
            reductionCounter--;
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public int getFertilizerP() {
        return fertilizerP;
    }

    public void setFertilizerP(int fertilizerP) {
        this.fertilizerP = fertilizerP;
    }

    public int getFertilizerN() {
        return fertilizerN;
    }

    public void setFertilizerN(int fertilizerN) {
        this.fertilizerN = fertilizerN;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public int getReductionCounter() {
        return reductionCounter;
    }

    public void setReductionCounter(int reductionCounter) {
        this.reductionCounter = reductionCounter;
    }

    public int getMAX_GROWTH() {
        return MAX_GROWTH;
    }

    public Field(int amount, int growth, int fertilizerP, int fertilizerN, int reductionCounter, boolean owned, boolean rented) {
        this.amount = amount;
        this.growth = growth;
        this.fertilizerP = fertilizerP;
        this.fertilizerN = fertilizerN;
        this.reductionCounter = reductionCounter;
        this.owned = owned;
        this.rented = rented;
    }

    // Do not remove, crashes the game on loadGame()
    @SuppressWarnings("unused")
    public Field() {
    }
}
