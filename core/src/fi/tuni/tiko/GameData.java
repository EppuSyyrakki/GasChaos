package fi.tuni.tiko;

import java.util.ArrayList;

public class GameData {
    /**
     * Prices of resources for easy access & changing. Price per 1 unit.
     */
    private final int MONEY_FROM_MILK = 4;
    private final int MONEY_FROM_MANURE = 2;
    private final int MONEY_FROM_GRAIN = 3;
    private final int MONEY_FROM_METHANE = 2;
    private final int MONEY_FROM_GARDEN = 8;

    /**
     * Tracks amount of actions player has done and can take.
     */
    private int actionsDone = 0;
    final int MAX_ACTIONS = 3;

    /**
     * Resource amounts and limits
     */
    private int money = 2000;       // money available for purchases
    private int manure = 0;         // amount of manure in manure tank
    private int manureMax = 100;    // size of manure tank
    private int methane = 0;        // amount of methane in gas tank
    private int methaneMax = 100;   // size of methane tank
    private int debt = 10000;       // total amount of debt, reduced by debtPayment
    private int feed = 20;          // amount of feed for cows
    private int feedMax = 100;      // maximum amount of feed

    /**
     * Device levels. 0 = no device
     */
    private int solarPanelLevel = 0;
    private int gasCollectorLevel = 1;
    private int milkingMachineLevel = 1;
    private int tractorLevel = 1;
    private int gasGeneratorLevel = 0;

    /**
     * Expenditures per turn
     */
    private int debtPayment = -200; // debt and money reduced by this amount
    private int electricity = -20;  // affected by solarPanelLevel and gasGeneratorLevel
    private int petrol = -10;       // affected by tractorLevel
    private int fieldsRent;         // affected by number of elements over 0 in rentedFields array

    /**
     * Income per turn. Milk is sold every turn in updateResources.
     */
    private int grainSold;
    private int methaneSold;    // touched only if methane emptied this turn, must reset on turn change
    private int manureSold;     // touched only if manure sold this turn, must reset on turn change
    private int gardenSold;     // touched only if garden sold this turn, must reset on turn change

    /**
     * Keep states of fields in game. First 2 should not cost anything.
     * 0 = not rented
     * 1 = not sown
     * 2-20 = growing
     */
    private int[] fields = new int[6];

    /**
     * All cows in the barn.
     */
    private ArrayList<Cow> cowList;

    /**
     * Update variables. Use at end of turn. Resets xSold variables to 0 for next turn.
     */
    private void updateResources() {
        int grainSoldThisTurn = grainSold;
        int manureSoldThisTurn = manureSold;
        int gardenSoldThisTurn = gardenSold;
        int methaneSoldThisTurn = methaneSold;
        int milkSoldThisTurn = 0;

        for (Cow cow : cowList) {
            milkSoldThisTurn += cow.getMilk();
            cow.addMethane();
        }

        // TODO all calculations regarding money. income - expenditure

        grainSold = 0;
        manureSold = 0;
        gardenSold = 0;
        methaneSold = 0;
    }

    public int getActionsDone() {
        return actionsDone;
    }

    public void setActionsDone(int actionsDone) {
        this.actionsDone = actionsDone;
    }

    public int getManure() {
        return manure;
    }

    public void setManure(int manure) {
        this.manure = manure;
    }

    public int getManureMax() {
        return manureMax;
    }

    public void setManureMax(int manureMax) {
        this.manureMax = manureMax;
    }

    public int getMethane() {
        return methane;
    }

    public void setMethane(int methane) {
        this.methane = methane;
    }

    public int getMethaneMax() {
        return methaneMax;
    }

    public void setMethaneMax(int methaneMax) {
        this.methaneMax = methaneMax;
    }

    public int getFeed() {
        return feed;
    }

    public void setFeed(int feed) {
        this.feed = feed;
    }

    public int getFeedMax() {
        return feedMax;
    }

    public void setFeedMax(int feedMax) {
        this.feedMax = feedMax;
    }

    public int getSolarPanelLevel() {
        return solarPanelLevel;
    }

    public void setSolarPanelLevel(int solarPanelLevel) {
        this.solarPanelLevel = solarPanelLevel;
    }

    public int getGasCollectorLevel() {
        return gasCollectorLevel;
    }

    public void setGasCollectorLevel(int gasCollectorLevel) {
        this.gasCollectorLevel = gasCollectorLevel;
    }

    public int getMilkingMachineLevel() {
        return milkingMachineLevel;
    }

    public void setMilkingMachineLevel(int milkingMachineLevel) {
        this.milkingMachineLevel = milkingMachineLevel;
    }

    public int getTractorLevel() {
        return tractorLevel;
    }

    public void setTractorLevel(int tractorLevel) {
        this.tractorLevel = tractorLevel;
    }

    public int getGasGeneratorLevel() {
        return gasGeneratorLevel;
    }

    public void setGasGeneratorLevel(int gasGeneratorLevel) {
        this.gasGeneratorLevel = gasGeneratorLevel;
    }

    public int getGrainSold() {
        return grainSold;
    }

    public void setGrainSold(int grainSold) {
        this.grainSold = grainSold;
    }

    public int getMethaneSold() {
        return methaneSold;
    }

    public void setMethaneSold(int methaneSold) {
        this.methaneSold = methaneSold;
    }

    public int getManureSold() {
        return manureSold;
    }

    public void setManureSold(int manureSold) {
        this.manureSold = manureSold;
    }

    public int getGardenSold() {
        return gardenSold;
    }

    public void setGardenSold(int gardenSold) {
        this.gardenSold = gardenSold;
    }

    public int[] getFields() {
        return fields;
    }

    public void setFields(int[] fields) {
        this.fields = fields;
    }

    public ArrayList<Cow> getCowList() {
        return cowList;
    }

    public void setCowList(ArrayList<Cow> cowList) {
        this.cowList = cowList;
    }
}
