package fi.tuni.tiko;

import java.util.ArrayList;

public class GameData {

    int debugCounter = 0;

    /**
     * Prices of resources and upgrades. Money gained is per 1 unit.
     */
    final int MONEY_FROM_MILK = 4;
    final int MONEY_FROM_MANURE = 2;
    final int MONEY_FROM_GRAIN = 3;
    final int MONEY_FROM_METHANE = 2;
    final int MONEY_FROM_GARDEN = 8;
    final int PRICE_OF_COW = 800;
    final int PRICE_OF_FEED = 2;
    final int PRICE_OF_SOLAR = 1000;
    final int PRICE_OF_COLLECTOR = 1000;
    final int PRICE_OF_MILKING = 1000;
    final int PRICE_OF_TRACTOR = 1000;
    final int PRICE_OF_GENERATOR = 1000;
    final int PRICE_OF_FIELD = 20;

    /**
     * Tracks game progression.
     */
    private int currentTurn = 1;
    private int actionsDone = 0;
    final int MAX_ACTIONS = 3;  // actions per turn

    /**
     * Resource amounts and limits
     */
    private int money = 2000;       // money available for purchases
    private int manure = 0;         // amount of manure in manure pit
    private int manureInBarn = 0;   // amount of manure produced on previous turn
    private int manureMax = 2500;   // size of manure pit
    private int methane = 0;        // amount of methane in gas tank
    private int methaneMax = 15000; // size of methane tank
    private int debt = 10000;       // total amount of debt, reduced by debtPayment
    private int feed = 0;           // total amount of feed
    private int feedInBarn = 30;    // amount of feed for cows in barn
    private int feedMax = 9000;     // maximum amount of feed
    private float interest = 1.03f; // 5% interest rate to calculate debt payments
    final int MAX_FIELDS = 6;       // maximum number of fields
    final int MAX_COWS = 6;         // maximum number of cows
    final int MANURE_SHOVELED = 45; // how much manure removed from barn in single remove action
    final int MAX_P_PER_FIELD = 8;  // max phosphorous per field before penalty
    final int MAX_N_NER_FIELD = 80; // max nitrogen per field before penalty

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
    private int debtPayment = 200;  // debt and money reduced by this amount (plus interest rate)
    private int electricity = 100;  // affected by solarPanelLevel and gasGeneratorLevel
    private int petrol = 20;        // affected by tractorLevel
    // also rent of fields is an expense. Calculated by number of fields != 0 in updateResources.

    /**
     * Income per turn. Touched only if sold this turn.
     */
    private int grainSold;
    private int methaneSold;
    private int manureSold;
    private int gardenSold;
    // also milk is sold every turn in updateResources

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
    private int[] fieldFertilizerP = new int[MAX_FIELDS];
    private int[] fieldFertilizerN = new int[MAX_FIELDS];

    /**
     * Keep state of garden.
     */
    private int weedsAmount = 0;    // bigger reduces gardenGrowth
    private int gardenGrowth = 5;
    private int gardenAmount = 0;   // increased by gardenGrowth every turn in less than gardenMax;
    private int gardenMax = 50;

    /**
     * All cows in the barn.
     */
    private ArrayList<Cow> cowList;

    /**
     * Things bought this turn that will come to farm on next turn. Touched in buying actions. Will
     * reset to default in updateResources at end of turn.
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
     * Update variables. Use at end of turn.
     */
    public void updateResources() {
        updateMoney();  // calculate new money based on device levels and sold resources, cows eat
        updateThings(); // update device levels, bought resources and cows
        updateGarden(); // update garden vegetable growth
        updateFields(); // update fields growth
        resetVariables();  // reset xSold to 0 and xBought to false, also reset eatenThisTurn in cows.
    }

    private void updateMoney() {
        int milkSold = 0;
        int moneyThisTurn = 0;
        float floatPayment = debt * interest + debtPayment;
        int debtPaymentThisTurn = (int)floatPayment;
        int fieldsRentThisTurn = 0;
        int petrolThisTurn = petrol;
        int electricityThisTurn = electricity;                      // default total 100

        if (solarPanelLevel == 1) {
            electricityThisTurn = electricity - (electricity / 3);  // 67
        } else if (solarPanelLevel == 2) {
            electricityThisTurn = electricity / 3;                  // 33
        }
        if (gasGeneratorLevel == 1) {
            electricityThisTurn = electricity - (electricity / 3);  // 0
        }

        if (tractorLevel == 2) {
            petrolThisTurn = petrol / 2;
        } else if (tractorLevel == 3) {
            petrolThisTurn = 0;
        }

        for (Cow cow : cowList) {
            feed = cow.eat(feed);

            if (cow.isEatenThisTurn()) {  // if cow not eaten, no milk, manure and methane produced
                milkSold += cow.getMilk(milkingMachineLevel);
                cow.fart(gasCollectorLevel);
                manureInBarn += cow.poop();
            }
        }

        for (int i = 2; i < MAX_FIELDS; i++) {  // start at 2 because 0 and 1 are owned, not rented
            if (fields[i] != 0) {
                fieldsRentThisTurn += PRICE_OF_FIELD;
            }
        }

        moneyThisTurn += grainSold * MONEY_FROM_GRAIN;
        moneyThisTurn += manureSold * MONEY_FROM_MANURE;
        moneyThisTurn += gardenSold * MONEY_FROM_GARDEN;
        moneyThisTurn += methaneSold * MONEY_FROM_METHANE;
        moneyThisTurn += milkSold * MONEY_FROM_MILK;
        moneyThisTurn -= debtPaymentThisTurn;
        moneyThisTurn -= electricityThisTurn;
        moneyThisTurn -= petrolThisTurn;
        moneyThisTurn -= fieldsRentThisTurn;
        money += moneyThisTurn;
    }

    private void updateThings() {
        cowList.addAll(cowsBought);

        if (solarPanelBasicBought) {
            solarPanelLevel = 1;
            solarPanelBasicBought = false;
        }
        if (solarPanelAdvBought) {
            solarPanelLevel = 2;
            solarPanelAdvBought = false;
        }
        if (gasCollectorAdvBought) {
            gasCollectorLevel = 2;
            gasCollectorAdvBought = false;
        }
        if (milkingMachineAdvBought) {
            milkingMachineLevel = 2;
            milkingMachineAdvBought = false;
        }
        if (tractorAdvBought) {
            tractorLevel = 2;
            tractorAdvBought = false;
        }
        if (tractorGasBought) {
            tractorLevel = 3;
            tractorGasBought = false;
        }
        if (gasGeneratorBought) {
            gasGeneratorLevel = 1;
            gasGeneratorBought = false;
        }

        for (int i = 0; i < fieldsRented.length; i++) {
            if (fieldsRented[i] == 1) {         // new field rented
                fields[i + 2] = 1;              // + 2 because first 2 fields are owned, not rented
            } else if (fieldsRented[i] == -1) { // stopped renting field
                fields[i + 2] = 0;
            }
        }
    }

    /**
     * Decrease gardenGrowth by weedsAmount, increase gardenAmount by gardenGrowth and increase
     * weedsAmount by 1. Minimum gardenGrowth = 1. Maximum weeds amount = 5. No growth if garden
     * is 0 (not planted).
     */
    public void updateGarden() {

        if (gardenAmount > 0) {
            gardenAmount += gardenGrowth;
            gardenGrowth -= weedsAmount;

            if (gardenGrowth < 1) {
                gardenGrowth = 1;
            }

            if (gardenAmount > gardenMax) {
                gardenAmount = gardenMax;
            }
        }
        weedsAmount++;

        if (weedsAmount > 5) {
            weedsAmount = 5;
        }
    }

    private void updateFields() {
        // TODO growth through fertilization
    }

    private void resetVariables() {
        grainSold = 0;
        manureSold = 0;
        gardenSold = 0;
        methaneSold = 0;
        cowsBought.clear();

        for (int i = 0; i < fieldsRented.length; i++) {
            fieldsRented[i] = 0;
        }

        for (Cow cow : cowList) {
            cow.setEatenThisTurn(false);
        }
    }

    public GameData() {
        for (int i = 0; i < fields.length; i++) {   // create field arrays
            fieldGrowth[i] = 1;
            fieldFertilizerP[i] = 0;
            fieldFertilizerN[i] = 0;
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

    public int getGasCollectorLevel() {
        return gasCollectorLevel;
    }

    public int getMilkingMachineLevel() {
        return milkingMachineLevel;
    }

    public int getTractorLevel() {
        return tractorLevel;
    }

    public int getGasGeneratorLevel() {
        return gasGeneratorLevel;
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

    public int[] getFieldFertilizerN() {
        return fieldFertilizerN;
    }

    public void setFieldFertilizerN(int[] fieldFertilizerN) {
        this.fieldFertilizerN = fieldFertilizerN;
    }

    public int[] getFieldFertilizerP() {
        return fieldFertilizerP;
    }

    public void setFieldFertilizerP(int[] fieldFertilizerP) {
        this.fieldFertilizerP = fieldFertilizerP;
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
        this.tractorAdvBought = tractorAdvBought;
    }

    public boolean isTractorGasBought() {
        return tractorGasBought;
    }

    public void setTractorGasBought(boolean tractorGasBought) {
        this.tractorGasBought = tractorGasBought;
    }

    public boolean isGasGeneratorBought() {
        return gasGeneratorBought;
    }

    public void setGasGeneratorBought(boolean gasGeneratorBought) {
        this.gasGeneratorBought = gasGeneratorBought;
    }

    public int getManureInBarn() {
        return manureInBarn;
    }

    public void setManureInBarn(int manureInBarn) {
        this.manureInBarn = manureInBarn;
    }

    public int getFeedInBarn() {
        return feedInBarn;
    }

    public void setFeedInBarn(int feedInBarn) {
        this.feedInBarn = feedInBarn;
    }

    public int getWeedsAmount() {
        return weedsAmount;
    }

    public void setWeedsAmount(int weedsAmount) {
        this.weedsAmount = weedsAmount;
    }

    public void setGardenGrowth(int gardenGrowth) {
        this.gardenGrowth = gardenGrowth;
    }

    public int getGardenAmount() {
        return gardenAmount;
    }

    public void setGardenAmount(int gardenAmount) {
        this.gardenAmount = gardenAmount;
    }
    }
