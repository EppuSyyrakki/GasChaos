package fi.tuni.tiko;

public class Field {

    private int amount = 0;
    private int growth = 0;
    private int fertilizerP = 0;
    private int fertilizerN = 0;
    private int reductionCounter = 3;
    private boolean owned;
    private boolean rented;
    final int MAX_GROWTH = 300; // with perfect growth this comes in 10 turns.

    public Field(boolean owned, boolean rented) {
        this.owned = owned;
        this.rented = rented;
    }

    public void grow() {
        if (amount == 1) {
            if (fertilizerN >= 50 && fertilizerP >= 7) {
                // perfect growth N >= 50 and P >= 7
                growth = 30;
            } else if ((fertilizerN < 50 && fertilizerN >= 10)
                    && (fertilizerP < 7 && fertilizerP >= 3)) {
                // average growth N 10-49 and P 3-6
                growth = 20;
            } else {
                // poor growth N < 10 and P < 2
                growth = 10;
            }
            amount += growth;

            if (amount > MAX_GROWTH) {
                amount = MAX_GROWTH;
            }

            if (reductionCounter == 0) {
                fertilizerP -= 1;
                fertilizerN -= 3;
                reductionCounter = 3;

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
}
