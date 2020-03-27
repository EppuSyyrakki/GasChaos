package fi.tuni.tiko;

public class Field {
    /**
     * Keep states of field.
     * 0 = not sown
     * 1-20 = sown & growing
     * 21-29 = ripe & growing
     * 30 ripe & not growing
     * fieldGrowth is the rate of growth for fields. 1 = normal, 2 = fertilized
     * fieldFertilizer is a turn counter from 5 to 0. When above 0, fieldGrowth is 2, when down to
     * 0, fieldGrowth is 1.
     */
    private int amount = 0;
    private int growth = 0;
    private int fertilizerP = 0;
    private int fertilizerN = 0;
    private boolean owned;
    private boolean rented;

    public Field(boolean owned, boolean rented) {
        this.owned = owned;
        this.rented = rented;
    }

    public void grow() {

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
