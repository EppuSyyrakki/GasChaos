package fi.tuni.tiko;

import java.util.ArrayList;

public class GameData {
    /**
     * Prices of resources and upgrades. Money gained is per 1 unit.
     */
    final int MONEY_FROM_MILK = 4;
    final int MONEY_FROM_MANURE = 2;
    final int MONEY_FROM_GRAIN = 3;
    final int MONEY_FROM_METHANE = 2;
    final int MONEY_FROM_GARDEN = 8;
    final int PRICE_OF_COW = 800;
    final int MAX_COWS = 8;
    final int PRICE_OF_FEED = 2;
    final int PRICE_OF_SOLAR = 1000;
    final int PRICE_OF_COLLECTOR = 1000;
    final int PRICE_OF_MILKING = 1000;
    final int PRICE_OF_TRACTOR = 1000;
    final int PRICE_OF_GENERATOR = 1000;

    /**
     * Tracks game progression.
     */
    private int currentTurn = 1;
    private int actionsDone = 0;
    final int MAX_ACTIONS = 3;

    /**
     * Resource amounts and limits
     */
    private int money = 2000;       // money available for purchases
    private int manure = 0;         // amount of manure in manure pit
    private int manureMax = 100;    // size of manure pit
    private int methane = 0;        // amount of methane in gas tank
    private int methaneMax = 100;   // size of methane tank
    private int debt = 10000;       // total amount of debt, reduced by debtPayment
    private int feed = 20;          // amount of feed for cows
    private int feedMax = 100;      // maximum amount of feed
    final int MAX_FIELDS = 6;       // maximum number of fields

    /**
     * Device levels. 0 = no device, Used in updateResource calculations and to draw correct
     * graphics.
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
    private int fieldsRent = 0;     // affected by number of elements over 0 in rentedFields array

    /**
     * Income per turn. Touched only if sold this turn. Milk is sold every turn in updateResources.
     */
    private int grainSold;
    private int methaneSold;
    private int manureSold;
    private int gardenSold;

    /**
     * Keep states of fields in game. First 2 should not cost anything.
     * 0 = not rented
     * 1 = not sown
     * 2-20 = sown & growing
     * 21-29 = ripe & growing
     * 30 ripe & not growing
     * fieldGrowth is the rate of growth for fields. 1 = normal, 2 = fertilized
     * fieldFertilizer is a turn counter from 5 to 0. When above 0, fieldGrowth is 2, when down to
     * 0, fieldGrowth is 1.
     */
    private int[] fields = new int[MAX_FIELDS];
    private int[] fieldGrowth = new int[MAX_FIELDS];
    private int[] fieldFertilizer = new int[MAX_FIELDS];

    /**
     * All cows in the barn.
     */
    private ArrayList<Cow> cowList;

    /**
     * Things bought this turn that will come to farm on next turn. Touched in buying actions.
     */
    private ArrayList<Cow> cowsBought;
    private int feedBought;
    private int[] fieldsRented = new int[MAX_FIELDS - 2];   // -2 since first 2 are owned, no rent
    private boolean solarPanelBasicBought;
    private boolean solarPanelAdvBought;
    private boolean gasCollectorAdvBought;
    private boolean milkingMachineAdvBought;
    private boolean tractorAdvBought;
    private boolean tractorGasBought;
    private boolean gasGeneratorBought;

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
            milkSoldThisTurn += cow.getMilk() * milkingMachineLevel;
            cow.addMethane(gasCollectorLevel);
        }

        cowList.addAll(cowsBought);
        cowsBought.clear();

        // TODO all calculations regarding money, depending on device levels
        // TODO raise device levels and feed and reset bought variables

        grainSold = 0;
        manureSold = 0;
        gardenSold = 0;
        methaneSold = 0;
    }

    public GameData() {
        for (int i = 0; i < fields.length; i++) {
            fieldGrowth[i] = 1;
            fieldFertilizer[i] = 0;
        }
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

    public int[] getFieldGrowth() {
        return fieldGrowth;
    }

    public void setFieldGrowth(int[] fieldGrowth) {
        this.fieldGrowth = fieldGrowth;
    }

    public int[] getFieldFertilizer() {
        return fieldFertilizer;
    }

    public void setFieldFertilizer(int[] fieldFertilizer) {
        this.fieldFertilizer = fieldFertilizer;
    }

    public ArrayList<Cow> getCowList() {
        return cowList;
    }

    public void setCowList(ArrayList<Cow> cowList) {
        this.cowList = cowList;
    }

    public ArrayList<Cow> getCowsBought() {
        return cowsBought;
    }

    public void setCowsBought(ArrayList<Cow> cowsBought) {
        this.cowsBought = cowsBought;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getFeedBought() {
        return feedBought;
    }

    public void setFeedBought(int feedBought) {
        this.feedBought = feedBought;
    }

    public int[] getFieldsRented() {
        return fieldsRented;
    }

    public void setFieldsRented(int[] fieldsRented) {
        this.fieldsRented = fieldsRented;
    }

    public boolean isSolarPanelBasicBought() {
        return solarPanelBasicBought;
    }

    public void setSolarPanelBasicBought(boolean solarPanelBasicBought) {
        this.solarPanelBasicBought = solarPanelBasicBought;
    }

    public boolean isSolarPanelAdvBought() {
        return solarPanelAdvBought;
    }

    public void setSolarPanelAdvBought(boolean solarPanelAdvBought) {
        this.solarPanelAdvBought = solarPanelAdvBought;
    }

    public boolean isGasCollectorAdvBought() {
        return gasCollectorAdvBought;
    }

    public void setGasCollectorAdvBought(boolean gasCollectorAdvBought) {
        this.gasCollectorAdvBought = gasCollectorAdvBought;
    }

    public boolean isMilkingMachineAdvBought() {
        return milkingMachineAdvBought;
    }

    public void setMilkingMachineAdvBought(boolean milkingMachineAdvBought) {
        this.milkingMachineAdvBought = milkingMachineAdvBought;
    }

    public boolean isTractorAdvBought() {
        return tractorAdvBought;
    }

    public void setTractorAdvBought(boolean tractorAdvBought) {
        tractorAdvBought = tractorAdvBought;
    }

    public boolean isTractorGasBought() {
        return tractorGasBought;
    }

    public void setTractorGasBought(boolean tractorGasBought) {
        tractorGasBought = tractorGasBought;
    }

    public boolean isGasGeneratorBought() {
        return gasGeneratorBought;
    }

    public void setGasGeneratorBought(boolean gasGeneratorBought) {
        this.gasGeneratorBought = gasGeneratorBought;
    }
}
