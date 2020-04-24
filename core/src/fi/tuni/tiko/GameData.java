package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Arrays;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

@SuppressWarnings({"DanglingJavadoc", "unchecked"})
public class GameData {

    /**
     * Prices of resources and upgrades. Money gained is per 1 unit.
     */
    final int MONEY_FROM_MILK = 4;
    final int MONEY_FROM_MANURE = 1;
    final int MONEY_FROM_GRAIN = 2;
    final int MONEY_FROM_METHANE = 2;
    final int MONEY_FROM_GARDEN = 8;
    final int MONEY_FROM_N = 2;
    final int MONEY_FROM_P = 6;
    final int PRICE_OF_COW = 800;
    final int PRICE_OF_GRAIN = 2;
    final int PRICE_OF_SOLAR = 1000;
    final int PRICE_OF_COLLECTOR = 1000;
    final int PRICE_OF_MILKING = 1000;
    final int PRICE_OF_TRACTOR = 1000;
    final int PRICE_OF_GENERATOR = 1000;
    final int PRICE_OF_FIELD = 20;
    final int PRICE_OF_N = 3;
    final int PRICE_OF_P = 8;

    /**
     * Tracks game progression.
     */
    private int currentTurn = 1;
    private int actionsDone = 0;
    final int MAX_ACTIONS = 3;  // actions per turn
    private boolean actionsAvailable;
    private boolean fieldPenalty = false;

    /**
     * Resource amounts and limits
     */
    private int money = 2000;       // money available for purchases
    private int manure = 0;         // amount of manure in manure pit
    private int manureInBarn = 0;   // amount of manure produced on previous turn
    private int manureInBarnMax = 300;  // maximum amount of manure on barn floor
    private int manureMax = 2500;   // size of manure pit
    private int methane = 0;        // amount of methane in gas tank
    private int methaneMax = 15000; // size of methane tank
    private int debt = 10000;       // total amount of debt, reduced by debtPayment
    private int grain = 100;        // total amount of grain on farm
    private int grainInBarn = 30;   // amount of feed (grain) for cows in barn
    private int grainMax = 9000;    // maximum amount of grain
    private int fertilizerN = 35;   // Nitrogen fertilizer storage
    private int fertilizerNMax = 500;
    private int fertilizerP = 6;    // Phosphorous fertilizer storage
    private int fertilizerPMax = 100;

    private float interest = 0.03f; // 3% interest rate to calculate debt payments
    final int MAX_FIELDS = 6;       // maximum number of fields
    final int OWNED_FIELDS = 2;     // owned fields at start (no rent)
    final int MAX_COWS = 6;         // maximum number of cows
    final int MANURE_SHOVELED = 100;// how much manure removed from barn in single remove action
    final int MAX_P_PER_FIELD = 13; // max phosphorous per field before penalty
    final int MAX_N_PER_FIELD = 80; // max nitrogen per field before penalty
    final int N_FERTILIZE = 35;     // how much fertilizers go into field when fertilizing
    final int P_FERTILIZE = 6;
    final int MANURE_DANGER = 200;  // when amount of manure will affect milk production.

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
    private int NSold;
    private int PSold;
    // also milk is sold every turn in updateResources

    /**
     * Array to represent manure slowly composting into fertilizerN and fertilizerP. Change is done
     * in updateResources. Full change takes 3+1 turns.
     */
    private int[] compost = new int[3];

    /**
     * All fields in FieldScreen. All fields exist at start of game.
     */
    private ArrayList<Field> fields;

    /**
     * Keep state of garden.
     */
    private int weedsAmount = 0;    // bigger reduces gardenGrowth
    private int gardenGrowth = 5;
    private int gardenAmount = 0;   // increased by gardenGrowth every turn in less than gardenMax;
    private int gardenMax = 50;

    /**
     * All cows in the barn and chickens in the coop.
     */
    private ArrayList<Cow> cowList;

    /**
     * Things bought this turn that will come to farm on next turn. Touched in buying actions. Will
     * reset to default in updateResources at end of turn.
     */
    private ArrayList<Cow> cowsBought;
    private int grainBought;
    private int nBought;
    private int pBought;
    private int[] fieldsRented = new int[MAX_FIELDS - OWNED_FIELDS];   // -2 since first 2 owned, no rent
    private boolean solarPanelBasicBought;
    private boolean solarPanelAdvBought;
    private boolean gasCollectorAdvBought;
    private boolean milkingMachineAdvBought;
    private boolean tractorAdvBought;
    private boolean tractorGasBought;
    private boolean gasGeneratorBought;

    /**
     * Tutorial dialog triggers
     */
    private boolean barnVisited;
    private boolean buySellVisited;
    private boolean computerVisited;
    private boolean farmVisited;
    private boolean fieldVisited;
    private boolean gardenVisited;
    private boolean gasTankVisited;
    private boolean homeVisited;
    private boolean upgradeVisited;

    /**
     * save game file My Preferences.xml.
     */
    Preferences prefs = Gdx.app.getPreferences("GasPreferences");
    /**
     * Update variables. Use at end of turn.
     */
    public void updateResources() {
        updateMoney();  // calculate new money based on device levels and sold resources, cows eat
        updateThings(); // update device levels, bought resources and cows
        updateGarden(); // update garden vegetable growth
        updateFields(); // update fields growth
        updateCompost();    // slowly turn manure into fertilizers
        resetVariables();   // xSold to 0 and xBought to false, also reset eatenThisTurn in cows.
    }


    private void updateMoney() {
        int milkSold = 0;
        int moneyThisTurn = 0;
        int fieldsRentThisTurn = 0;
        int debtPaymentThisTurn = 0;
        int petrolThisTurn = petrol;
        int electricityThisTurn = electricity;                      // default total 100

        if (solarPanelLevel == 1) {
            electricityThisTurn = electricity - (electricity / 3);  // 67
        } else if (solarPanelLevel == 2) {
            electricityThisTurn = electricity - (electricity / 3) - (electricity / 3);  // 33
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
            grainInBarn = cow.eat(grainInBarn);

            if (cow.isEatenThisTurn()) {  // if cow not eaten, no milk, manure and methane produced
                int milkFromCow = cow.getMilk(milkingMachineLevel);
                cow.fart(gasCollectorLevel);
                manureInBarn += cow.poop();
                if (manureInBarn > MANURE_DANGER) {   // if barn is filthy, 33% less milk
                    milkFromCow -= (milkFromCow / 3);
                }
                milkSold += milkFromCow;
            }
        }

        if (manureInBarn > manureInBarnMax) {
            manureInBarn = manureInBarnMax;
        }

        for (Field field : fields) {
            if (field.isRented()) {
                fieldsRentThisTurn += PRICE_OF_FIELD;
            }
        }

        if (currentTurn % 2 == 0) { // debt is paid on every other turn.
            float floatPayment = (float)debt * interest + debtPayment;
            debtPaymentThisTurn = (int)floatPayment;
        }

        moneyThisTurn += grainSold * MONEY_FROM_GRAIN;
        moneyThisTurn += manureSold * MONEY_FROM_MANURE;
        moneyThisTurn += gardenSold * MONEY_FROM_GARDEN;
        moneyThisTurn += methaneSold * MONEY_FROM_METHANE;
        moneyThisTurn += milkSold * MONEY_FROM_MILK;
        moneyThisTurn += NSold * MONEY_FROM_N;
        moneyThisTurn += PSold * MONEY_FROM_P;
        moneyThisTurn -= debtPaymentThisTurn;
        moneyThisTurn -= electricityThisTurn;
        moneyThisTurn -= petrolThisTurn;
        moneyThisTurn -= fieldsRentThisTurn;
        money += moneyThisTurn;
    }

    private void updateThings() {
        cowList.addAll(cowsBought);
        fertilizerN += nBought;
        fertilizerP += pBought;
        grain += grainBought;

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
        for (Field field : fields) {
            if (field.isOwned() || field.isRented()) {
                field.grow();
            }
        }
    }

    public int highestFieldsP() {
        int highestP = 0;
        for (Field field : fields) {
            if (field.isOwned() || field.isRented()) {
                if (field.getFertilizerP() > highestP) {
                    highestP = field.getFertilizerP();
                }
            }
        }
        return highestP;
    }

    public int highestFieldsN() {
        int highestN = 0;
        for (Field field : fields) {
            if (field.isOwned() || field.isRented()) {
                if (field.getFertilizerN() > highestN) {
                    highestN = field.getFertilizerN();
                }
            }
        }
        return highestN;
    }

    private void updateCompost() {
        int toCompost = (cowList.get(0).getManure() * cowList.size()) / 10;  // 1..9
        fertilizerN += compost[2] * 5;
        fertilizerP += compost[2];
        compost[2] = compost[1];
        compost[1] = compost[2];
        compost[0] = toCompost;
        manure -= toCompost;
    }

    private void resetVariables() {
        grainSold = 0;
        manureSold = 0;
        gardenSold = 0;
        methaneSold = 0;
        cowsBought.clear();
        nBought = 0;
        pBought = 0;
        grainBought = 0;
        actionsDone = 0;
        currentTurn++;
        actionsAvailable = true;

        // Old code in case the new optimized one doesn't work
        //for (int i = 0; i < fieldsRented.length; i++) {fieldsRented[i] = 0;}
        Arrays.fill(fieldsRented, 0);

        for (Cow cow : cowList) {
            cow.setEatenThisTurn(false);
        }
        saveGame();
    }

    public GameData() {
        barnVisited = false;
        buySellVisited = false;
        computerVisited = false;
        farmVisited = false;
        fieldVisited = false;
        gardenVisited = false;
        gasTankVisited = false;
        homeVisited = false;
        upgradeVisited = false;
        fields = new ArrayList<>();
        for (int i = 0; i < MAX_FIELDS; i++) {
            if (i < OWNED_FIELDS) {
                fields.add(new Field(true, false));
            } else {
                fields.add(new Field(false, false));
            }
        }
        cowList = new ArrayList<>();
        cowList.add(new Cow());
        cowsBought = new ArrayList<>();

        //noinspection ConstantConditions
        actionsAvailable = actionsDone < MAX_ACTIONS;
        //loadGame();
    }

    public int getActionsDone() {
        return actionsDone;
    }

    public void setActionsDone(int actionsDone) {
        this.actionsDone = actionsDone;
        if (getActionsDone() >= MAX_ACTIONS) {
            setActionsAvailable(false);
        }
    }

    public int getManure() {
        return manure;
    }

    public void setManure(int manure) {
        this.manure = manure;
    }

    public int getManureInBarnMax() {
        return manureInBarnMax;
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

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    public int getDebtPayment() {
        return debtPayment;
    }

    public void setDebtPayment(int debtPayment) {
        this.debtPayment = debtPayment;
    }

    public int getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

    public int getPetrol() {
        return petrol;
    }

    public void setPetrol(int petrol) {
        this.petrol = petrol;
    }

    public int getGardenGrowth() {
        return gardenGrowth;
    }

    public int getGardenMax() {
        return gardenMax;
    }

    public void setGardenMax(int gardenMax) {
        this.gardenMax = gardenMax;
    }

    public void setManureInBarnMax(int manureInBarnMax) {
        this.manureInBarnMax = manureInBarnMax;
    }

    public int getGrain() {
        return grain;
    }

    public void setGrain(int grain) {
        this.grain = grain;
    }

    public int getGrainMax() {
        return grainMax;
    }

    public void setGrainMax(int grainMax) {
        this.grainMax = grainMax;
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

    public void setSolarPanelLevel(int solarPanelLevel) {
        this.solarPanelLevel = solarPanelLevel;
    }

    public void setGasCollectorLevel(int gasCollectorLevel) {
        this.gasCollectorLevel = gasCollectorLevel;
    }

    public void setMilkingMachineLevel(int milkingMachineLevel) {
        this.milkingMachineLevel = milkingMachineLevel;
    }

    public void setTractorLevel(int tractorLevel) {
        this.tractorLevel = tractorLevel;
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

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
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

    public int getGrainBought() {
        return grainBought;
    }

    public void setGrainBought(int grainBought) {
        this.grainBought = grainBought;
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

    public int getGrainInBarn() {
        return grainInBarn;
    }

    public void setGrainInBarn(int grainInBarn) {
        this.grainInBarn = grainInBarn;
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

    public int getCowAmount() {
        return cowList.size();
    }

    public int getTotalMethaneInCows() {
        int totalMethane = 0;
        for (Cow cow : cowList) {
            totalMethane += cow.getMethaneAmount();
        }
        return totalMethane;
    }

    public int getTotalMaxMethaneInCows() {
        int totalMaxMethane = 0;
        for (Cow cow : cowList) {
            totalMaxMethane += cow.getMethaneAmountMax();
        }
        return totalMaxMethane;
    }

    public int getMAX_ACTIONS() {
        return MAX_ACTIONS;
    }

    public int getMAX_FIELDS() {
        return MAX_FIELDS;
    }

    public int getOWNED_FIELDS() {
        return OWNED_FIELDS;
    }

    public int getMAX_COWS() {
        return MAX_COWS;
    }

    public int getMANURE_SHOVELED() {
        return MANURE_SHOVELED;
    }

    public int getMAX_P_PER_FIELD() {
        return MAX_P_PER_FIELD;
    }

    public int getMAX_N_PER_FIELD() {
        return MAX_N_PER_FIELD;
    }

    public int getMANURE_DANGER() {
        return MANURE_DANGER;
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }

    public int getMONEY_FROM_GRAIN() {
        return MONEY_FROM_GRAIN;
    }

    public int getPRICE_OF_FIELD() {
        return PRICE_OF_FIELD;
    }

    public int getFertilizerN() {
        return fertilizerN;
    }

    public void setFertilizerN(int fertilizerN) {
        this.fertilizerN = fertilizerN;
    }

    public int getFertilizerNMax() {
        return fertilizerNMax;
    }

    public int getFertilizerP() {
        return fertilizerP;
    }

    public void setFertilizerP(int fertilizerP) {
        this.fertilizerP = fertilizerP;
    }

    public int getFertilizerPMax() {
        return fertilizerPMax;
    }

    public void setFertilizerNMax(int fertilizerNMax) {
        this.fertilizerNMax = fertilizerNMax;
    }

    public void setFertilizerPMax(int fertilizerPMax) {
        this.fertilizerPMax = fertilizerPMax;
    }

    public int getNSold() {
        return NSold;
    }

    public void setNSold(int NSold) {
        this.NSold = NSold;
    }

    public int getPSold() {
        return PSold;
    }

    public void setPSold(int PSold) {
        this.PSold = PSold;
    }

    public boolean isActionsAvailable() {
        return actionsAvailable;
    }

    public void setActionsAvailable(boolean actionsAvailable) {
        this.actionsAvailable = actionsAvailable;
    }

    public int getnBought() {
        return nBought;
    }

    public void setnBought(int nBought) {
        this.nBought = nBought;
    }

    public int getpBought() {
        return pBought;
    }

    public void setpBought(int pBought) {
        this.pBought = pBought;
    }

    public boolean isBarnVisited() {
        return barnVisited;
    }

    public void setBarnVisited(boolean barnVisited) {
        this.barnVisited = barnVisited;
    }

    public boolean isBuySellVisited() {
        return buySellVisited;
    }

    public void setBuySellVisited(boolean buySellVisited) {
        this.buySellVisited = buySellVisited;
    }

    public boolean isComputerVisited() {
        return computerVisited;
    }

    public void setComputerVisited(boolean computerVisited) {
        this.computerVisited = computerVisited;
    }

    public boolean isFarmVisited() {
        return farmVisited;
    }

    public void setFarmVisited(boolean farmVisited) {
        this.farmVisited = farmVisited;
    }

    public boolean isFieldVisited() {
        return fieldVisited;
    }

    public void setFieldVisited(boolean fieldVisited) {
        this.fieldVisited = fieldVisited;
    }

    public boolean isGardenVisited() {
        return gardenVisited;
    }

    public void setGardenVisited(boolean gardenVisited) {
        this.gardenVisited = gardenVisited;
    }

    public boolean isGasTankVisited() {
        return gasTankVisited;
    }

    public void setGasTankVisited(boolean gasTankVisited) {
        this.gasTankVisited = gasTankVisited;
    }

    public boolean isHomeVisited() {
        return homeVisited;
    }

    public void setHomeVisited(boolean homeVisited) {
        this.homeVisited = homeVisited;
    }

    public boolean isUpgradeVisited() {
        return upgradeVisited;
    }

    public void setUpgradeVisited(boolean upgradeVisited) {
        this.upgradeVisited = upgradeVisited;
    }

    public boolean isFieldPenalty() {
        return fieldPenalty;
    }

    public void setFieldPenalty(boolean fieldPenalty) {
        this.fieldPenalty = fieldPenalty;
    }

    public GameData(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public void saveGame() {

        if (prefs == null) {
            prefs = Gdx.app.getPreferences("GasPreferences");
        }

        /**
         * Tracks game progression.
         */
        prefs.putInteger("currentTurn", getCurrentTurn());
        prefs.putInteger("actionsDone", getActionsDone());
        prefs.putBoolean("actionsAvailable", isActionsAvailable());
        prefs.putBoolean("fieldPenalty", isFieldPenalty());

        /**
         * Resource amounts.
         */
        prefs.putInteger("money", getMoney());
        prefs.putInteger("manure", getManure());
        prefs.putInteger("manureInBarn", getManureInBarn());
        prefs.putInteger("manureInBarnMax", getManureInBarnMax());
        prefs.putInteger("manureMax", getManureMax());
        prefs.putInteger("methane", getMethane());
        prefs.putInteger("methaneMax", getMethaneMax());
        prefs.putInteger("debt", getDebt());
        prefs.putInteger("grain", getGrain());
        prefs.putInteger("grainInBarn", getGrainInBarn());
        prefs.putInteger("grainMax", getGrainMax());
        prefs.putInteger("fertilizerN", getFertilizerN());
        prefs.putInteger("fertilizerNMax", getFertilizerNMax());
        prefs.putInteger("fertilizerP", getFertilizerP());
        prefs.putInteger("fertilizerPMax", getFertilizerPMax());
        prefs.putFloat("interest", getInterest());

        /**
         * Device levels. 0 = no device, Used in updateResource calculations and to draw correct
         * graphics.
         */
        prefs.putInteger("solarPanelLevel", getSolarPanelLevel());
        prefs.putInteger("gasCollectorLevel", getGasCollectorLevel());
        prefs.putInteger("milkingMachineLevel", getMilkingMachineLevel());
        prefs.putInteger("tractorLevel", getTractorLevel());
        prefs.putInteger("gasGeneratorLevel", getGasGeneratorLevel());

        /**
         * Expenditures per turn
         */
        prefs.putInteger("debtPayment", getDebtPayment());
        prefs.putInteger("electricity", getElectricity());
        prefs.putInteger("petrol", getPetrol());

        /**
         * Keep state of garden.
         */
        prefs.putInteger("weedsAmount", getWeedsAmount());
        prefs.putInteger("gardenGrowth", getGardenGrowth());
        prefs.putInteger("gardenAmount", getGardenAmount());
        prefs.putInteger("gardenMax", getGardenMax());

        /**
         * Things bought this turn that will come to farm on next turn. Touched in buying actions. Will
         * reset to default in updateResources at end of turn.
         */

        prefs.putInteger("grainBought", getGrainBought());
        prefs.putInteger("nBought", getnBought());
        prefs.putInteger("pBought", getpBought());
        prefs.putBoolean("solarPanelBasicBought", isSolarPanelBasicBought());
        prefs.putBoolean("solarPanelAdvBought", isSolarPanelAdvBought());
        prefs.putBoolean("gasCollectorAdvBought", isGasCollectorAdvBought());
        prefs.putBoolean("milkingMachineAdvBought", isMilkingMachineAdvBought());
        prefs.putBoolean("tractorAdvBought", isTractorAdvBought());
        prefs.putBoolean("tractorGasBought", isTractorGasBought());
        prefs.putBoolean("gasGeneratorBought", isGasGeneratorBought());

        /**
         * Income per turn. Touched only if sold this turn.
         */
        prefs.putInteger("grainSold", getGrainSold());
        prefs.putInteger("methaneSold", getMethaneSold());
        prefs.putInteger("manureSold", getManureSold());
        prefs.putInteger("gardenSold", getGardenSold());
        prefs.putInteger("NSold", getNSold());
        prefs.putInteger("PSold", getPSold());


        /**
         * Array and arrayList accessories.
         */

        String jsonField = json.toJson(fields);
        prefs.putString("fields", jsonField);
        //System.out.println("json: " + jsonField);
        String jsonCowList = json.toJson(cowList);
        prefs.putString("cowList", jsonCowList);
        //System.out.println("json: " + jsonCowList);

        prefs.putInteger("fieldRent0", fieldsRented[0]);
        prefs.putInteger("fieldRent1", fieldsRented[1]);
        prefs.putInteger("fieldRent2", fieldsRented[2]);
        prefs.putInteger("fieldRent3", fieldsRented[3]);
        //String jsonFieldsRented = json.toJson(fieldsRented);
        //prefs.putString("fieldsRented", jsonFieldsRented);
        //System.out.println("json: " + jsonFieldsRented);

        /**
         * Array to represent manure slowly composting into fertilizerN and fertilizerP. Change is done
         * in updateResources. Full change takes 3+1 turns.
         */
        prefs.putInteger("compost0", compost[0]);
        prefs.putInteger("compost1", compost[1]);
        prefs.putInteger("compost2", compost[2]);

        /**
         * Tutorial dialog triggers
         */
        prefs.putBoolean("barnVisited", barnVisited);
        prefs.putBoolean("buySellVisited", buySellVisited);
        prefs.putBoolean("computerVisited", computerVisited);
        prefs.putBoolean("farmVisited", farmVisited);
        prefs.putBoolean("fieldVisited", fieldVisited);
        prefs.putBoolean("gardenVisited", gardenVisited);
        prefs.putBoolean("gasTankVisited", gasTankVisited);
        prefs.putBoolean("homeVisited", homeVisited);
        prefs.putBoolean("upgradeVisited", upgradeVisited);

        prefs.flush();
        //int i = prefs.getInteger("currentTurn");
        //System.out.println(i);

        // bulk update your preferences

    }

    public void loadGame() {

        if (prefs == null) {
            prefs = Gdx.app.getPreferences("GasPreferences");
        }

        /**
         * Tracks game progression.
         */
        setCurrentTurn(prefs.getInteger("currentTurn", getCurrentTurn()));
        setActionsDone(prefs.getInteger("actionsDone", getActionsDone()));
        setActionsAvailable(prefs.getBoolean("actionsAvailable"));
        setFieldPenalty(prefs.getBoolean("fieldPenalty"));

        /**
         * Resource amounts.
         */
        setMoney(prefs.getInteger("money", getMoney()));
        setManure(prefs.getInteger("manure", getManure()));
        setManureInBarn(prefs.getInteger("manureInBarn", getManureInBarn()));
        setManureInBarnMax(prefs.getInteger("manureInBarnMax", getManureInBarnMax()));
        setManureMax(prefs.getInteger("manureMax", getManureMax()));
        setMethane(prefs.getInteger("methane", getMethane()));
        setMethaneMax(prefs.getInteger("methaneMax", getMethaneMax()));
        setDebt(prefs.getInteger("debt", getDebt()));
        setGrain(prefs.getInteger("grain", getGrain()));
        setGrainInBarn(prefs.getInteger("grainInBarn", getGrainInBarn()));
        setGrainMax(prefs.getInteger("grainMax", getGrainMax()));
        setFertilizerP(prefs.getInteger("fertilizerP", getFertilizerP()));
        setFertilizerPMax(prefs.getInteger("fertilizerPMax", getFertilizerPMax()));
        setFertilizerN(prefs.getInteger("fertilizerN", getFertilizerN()));
        setFertilizerPMax(prefs.getInteger("fertilizerNMax", getFertilizerNMax()));
        setInterest(prefs.getFloat("interest", getInterest()));

        /**
         * Device levels. 0 = no device, Used in updateResource calculations and to draw correct
         * graphics.
         */
        setSolarPanelLevel(prefs.getInteger("solarPanelLevel", getSolarPanelLevel()));
        setGasCollectorLevel(prefs.getInteger("gasCollectorLevel", getGasCollectorLevel()));
        setMilkingMachineLevel(prefs.getInteger("milkingMachineLevel", getMilkingMachineLevel()));
        setTractorLevel(prefs.getInteger("tractorLevel", getTractorLevel()));
        setGasGeneratorLevel(prefs.getInteger("gasGeneratorLevel", getGasGeneratorLevel()));

        /**
         * Expenditures per turn
         */
        setDebtPayment(prefs.getInteger("debtPayment", getDebtPayment()));
        setElectricity(prefs.getInteger("electricity", getElectricity()));
        setPetrol(prefs.getInteger("petrol", getPetrol()));

        /**
         * Keep state of garden.
         */
        setWeedsAmount(prefs.getInteger("weedsAmount", getWeedsAmount()));
        setGardenGrowth(prefs.getInteger("gardenGrowth", getGardenGrowth()));
        setGardenAmount(prefs.getInteger("gardenAmount", getGardenAmount()));
        setGardenMax(prefs.getInteger("gardenMax", getGardenMax()));

        /**
         * Things bought this turn that will come to farm on next turn. Touched in buying actions. Will
         * reset to default in updateResources at end of turn.
         */

        setGrainBought(prefs.getInteger("grainBought", getGrainBought()));
        setnBought(prefs.getInteger("nBought", getnBought()));
        setpBought(prefs.getInteger("pBought", getpBought()));
        setSolarPanelBasicBought(prefs.getBoolean("solarPanelBasicBought", isSolarPanelBasicBought()));
        setSolarPanelAdvBought(prefs.getBoolean("solarPanelAdvBought", isSolarPanelAdvBought()));
        setGasCollectorAdvBought(prefs.getBoolean("gasCollectorAdvBought", isGasCollectorAdvBought()));
        setMilkingMachineAdvBought(prefs.getBoolean("milkingMachineAdvBought", isMilkingMachineAdvBought()));
        setTractorAdvBought(prefs.getBoolean("tractorAdvBought", isTractorAdvBought()));
        setTractorGasBought(prefs.getBoolean("tractorGasBought", isTractorGasBought()));
        setGasGeneratorBought(prefs.getBoolean("gasGeneratorBought", isGasGeneratorBought()));

        /**
         * Income per turn. Touched only if sold this turn.
         */
        setGrainSold(prefs.getInteger("grainSold", getGrainSold()));
        setMethaneSold(prefs.getInteger("methaneSold", getMethaneSold()));
        setManureSold(prefs.getInteger("manureSold", getManureSold()));
        setGardenSold(prefs.getInteger("gardenSold", getGardenSold()));
        setNSold(prefs.getInteger("NSold", getNSold()));
        setPSold(prefs.getInteger("PSold", getPSold()));

        /**
         * Array and arrayList accessories.
         * Field ArrayList load
         */
        String fieldString = prefs.getString("fields");
        //System.out.println("fieldString: " + fieldString);
        fields = json.fromJson(ArrayList.class, fieldString);

        //int i = prefs.getInteger("currentTurn");
        //System.out.println(i);

        /**
         * Cow ArrayList load
         */

        String cowString = prefs.getString("cowList");
        //System.out.println("cowString: " + cowString);
        cowList = json.fromJson(ArrayList.class, cowString);

        /**
         * int array fieldsRented.
         */

        fieldsRented[0] = prefs.getInteger("fieldRent0", fieldsRented[0]);
        fieldsRented[1] = prefs.getInteger("fieldRent1", fieldsRented[1]);
        fieldsRented[2] = prefs.getInteger("fieldRent2", fieldsRented[2]);
        fieldsRented[3] = prefs.getInteger("fieldRent3", fieldsRented[3]);

        //String fieldsRentedString = prefs.getString("fieldsRented");
        //System.out.println("fieldsRentedString: " + fieldsRentedString);

        /**
         * Array to represent manure slowly composting into fertilizerN and fertilizerP. Change is done
         * in updateResources. Full change takes 3+1 turns.*/

        compost[0] = prefs.getInteger("compost0", compost[0]);
        compost[1] = prefs.getInteger("compost1", compost[1]);
        compost[2] = prefs.getInteger("compost2", compost[2]);

        /**
         * Tutorial dialog triggers
         */
        setBarnVisited(prefs.getBoolean("barnVisited", isBarnVisited()));
        setBuySellVisited(prefs.getBoolean("buySellVisited", isBuySellVisited()));
        setComputerVisited(prefs.getBoolean("computerVisited", isComputerVisited()));
        setFarmVisited(prefs.getBoolean("farmVisited", isFarmVisited()));
        setFieldVisited(prefs.getBoolean("fieldVisited", isFieldVisited()));
        setGardenVisited(prefs.getBoolean("gardenVisited", isGardenVisited()));
        setGasTankVisited(prefs.getBoolean("gasTankVisited", isGasTankVisited()));
        setHomeVisited(prefs.getBoolean("homeVisited", isHomeVisited()));
        setUpgradeVisited(prefs.getBoolean("upgradeVisited", isUpgradeVisited()));
    }

    public void resetSave() {

        prefs.remove("currentTurn");
        prefs.remove("actionsDone");
        prefs.remove("actionsAvailable");
        prefs.remove("fieldPenalty");

        /**
         * Resource amounts.
         */
        prefs.remove("money");
        prefs.remove("manure");
        prefs.remove("manureInBarn");
        prefs.remove("manureInBarnMax");
        prefs.remove("manureMax");
        prefs.remove("methane");
        prefs.remove("methaneMax");
        prefs.remove("debt");
        prefs.remove("grain");
        prefs.remove("grainInBarn");
        prefs.remove("grainMax");
        prefs.remove("fertilizerN");
        prefs.remove("fertilizerNMax");
        prefs.remove("fertilizerP");
        prefs.remove("fertilizerPMax");
        prefs.remove("interest");

        /**
         * Device levels. 0 = no device, Used in updateResource calculations and to draw correct
         * graphics.
         */
        prefs.remove("solarPanelLevel");
        prefs.remove("gasCollectorLevel");
        prefs.remove("milkingMachineLevel");
        prefs.remove("tractorLevel");
        prefs.remove("gasGeneratorLevel");

        /**
         * Expenditures per turn
         */
        prefs.remove("debtPayment");
        prefs.remove("electricity");
        prefs.remove("petrol");

        /**
         * Keep state of garden.
         */
        prefs.remove("weedsAmount");
        prefs.remove("gardenGrowth");
        prefs.remove("gardenAmount");
        prefs.remove("gardenMax");

        /**
         * Things bought this turn that will come to farm on next turn. Touched in buying actions. Will
         * reset to default in updateResources at end of turn.
         */

        prefs.remove("grainBought");
        prefs.remove("nBought");
        prefs.remove("pBought");
        prefs.remove("solarPanelBasicBought");
        prefs.remove("solarPanelAdvBought");
        prefs.remove("gasCollectorAdvBought");
        prefs.remove("milkingMachineAdvBought");
        prefs.remove("tractorAdvBought");
        prefs.remove("tractorGasBought");
        prefs.remove("gasGeneratorBought");

        /**
         * Income per turn. Touched only if sold this turn.
         */
        prefs.remove("grainSold");
        prefs.remove("methaneSold");
        prefs.remove("manureSold");
        prefs.remove("gardenSold");
        prefs.remove("NSold");
        prefs.remove("PSold");


        /**
         * Array and arrayList accessories.
         */

        String jsonField = json.toJson(fields);
        prefs.remove("fields");
        prefs.remove("cowList");

        prefs.remove("fieldRent0");
        prefs.remove("fieldRent1");
        prefs.remove("fieldRent2");
        prefs.remove("fieldRent3");
        //String jsonFieldsRented = json.toJson(fieldsRented);
        //prefs.putString("fieldsRented", jsonFieldsRented);
        //System.out.println("json: " + jsonFieldsRented);

        /**
         * Array to represent manure slowly composting into fertilizerN and fertilizerP. Change is done
         * in updateResources. Full change takes 3+1 turns.
         */
        prefs.remove("compost0");
        prefs.remove("compost1");
        prefs.remove("compost2");

        /**
         * Tutorial dialog triggers
         */
        prefs.remove("barnVisited");
        prefs.remove("buySellVisited");
        prefs.remove("computerVisited");
        prefs.remove("farmVisited");
        prefs.remove("fieldVisited");
        prefs.remove("gardenVisited");
        prefs.remove("gasTankVisited");
        prefs.remove("homeVisited");
        prefs.remove("upgradeVisited");

        prefs.flush();
    }
}
