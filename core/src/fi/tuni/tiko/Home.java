package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;


public class Home extends Building {
    Grandmother grandmother;

    public Home() {
        background = new Texture("homeBackground.png");
        grandmother = new Grandmother();
    }

    /**
     * Enable tutorial menu
     */
    public void actionCallGrandMother() {

    }

    /**
     * Add new Cow to cowsBought. moved to cowList when updateResources called in GameData.
     */
    public GameData actionBuyCow(GameData data) {
        ArrayList<Cow> tmpCowsBought = data.getCowsBought();

        if (data.getMoney() >= data.PRICE_OF_COW &&
                (tmpCowsBought.size() + data.getCowList().size()) < data.MAX_COWS) {
            data.setMoney(data.getMoney() - data.PRICE_OF_COW);
            tmpCowsBought.add(new Cow());
            data.setCowsBought(tmpCowsBought);
            data.setActionsDone(data.getActionsDone() + 1);
            // TODO cow bought, will arrive next turn UI message
        } else if (data.getMoney() < data.PRICE_OF_COW) {
            // TODO action blocked, not enough money to buy cow UI message
        } else if (data.getCowList().size() + tmpCowsBought.size() >= data.MAX_COWS) {
            // TODO action blocked, barn is full UI message
        }

        return data;
    }

    /**
     * Remove last Cow from cowList. Block if only 1 entry in list - must have at least 1 cow.
     * Increase data.money by half of data.PRICE_OF_COW
     */
    public GameData actionSellCow(GameData data) {
        ArrayList<Cow> tmpCowList = data.getCowList();

        if (tmpCowList.size() > 1) {
            tmpCowList.remove(tmpCowList.size() - 1);
            data.setMoney(data.PRICE_OF_COW / 2 + data.getMoney());
            data.setCowList(tmpCowList);
            data.setActionsDone(data.getActionsDone() + 1);
            // TODO cow sold UI message
        } else if (tmpCowList.size() == 1) {
            // TODO action blocked, can't sell last cow UI message
        }
        return data;
    }

    /**
     * Set data.feed to data.feed + 30. Reduce data.money by 30 * data.PRICE_OF_FEED. Block if not
     * enough money
     */
    public GameData actionBuy30Feed(GameData data) {
        int price = 30 * data.PRICE_OF_FEED;

        if (data.getFeed() + data.getFeedBought() < data.getFeedMax()) {
            if (data.getMoney() >= price) {
                data.setMoney(data.getMoney() - price);
                data.setFeedBought(data.getFeedBought() + 30);
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO 30 cow feed bought UI message
            } else {
                // TODO not enough money UI message
            }
        } else {
            // TODO feed storage will already be full next turn UI message
        }
        return data;
    }

    /**
     * Set data.feed to data.feedMax and reduce data.money by data.PRICE_OF_FEED *
     * (data.feedMax - data.feed). Block if not enough money.
     */
    public GameData actionBuyFullFeed(GameData data) {
        int amount = data.getFeedMax() - (data.getFeed() + data.getFeedBought());
        int price = data.PRICE_OF_FEED * amount;

        if (data.getFeed() + data.getFeedBought() < data.getFeedMax()) {
            if (data.getMoney() >= price) {
                data.setMoney(data.getMoney() - price);
                data.setFeedBought(data.getFeedBought() + amount);
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO amount of cow feed bought, storage will be full UI message
            } else {
                // TODO not enough money UI message
            }
        } else {
            // TODO feed storage will already be full next turn UI message
        }
        return data;
    }

    /**
     * Set data.solarPanelBasicBought to true. Block if data.solarPanelLevel > 0 or not enough
     * money.
     */
    public GameData actionSolarPanelBasic(GameData data) {
        if (data.getSolarPanelLevel() == 0
                && !data.isSolarPanelBasicBought() && !data.isSolarPanelAdvBought()) {
            if (data.getMoney() >= data.PRICE_OF_SOLAR) {
                data.setMoney(data.getMoney() - data.PRICE_OF_SOLAR);
                data.setSolarPanelBasicBought(true);
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO basic solar panel bought UI message
            } else {
                // TODO action blocked, not enough money UI message
            }
        } else {
            // TODO action blocked, already owned UI message
        }
        return data;
    }

    /**
     * Set data.solarPanelAdvBought to true. Block if data.solarPanelLevel = 2 or 0 or if already
     * bought or not enough money.
     */
    public GameData buySolarPanelAdvanced(GameData data) {
        if ( (data.getSolarPanelLevel() == 0 && data.isSolarPanelBasicBought())
                ||
                (data.getSolarPanelLevel() == 1 && !data.isSolarPanelAdvBought()) ) {

            if (data.getMoney() >= data.PRICE_OF_SOLAR) {
                data.setMoney(data.getMoney() - data.PRICE_OF_SOLAR);
                data.setSolarPanelAdvBought(true);
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO advanced solar panel bought UI message
            } else {
                // TODO action blocked, not enough money UI message
            }
        } else if (data.getSolarPanelLevel() == 0 && !data.isSolarPanelBasicBought()) {
            // TODO action blocked, must have basic panel first UI message
        } else if (data.getSolarPanelLevel() == 2
                || ( (data.getSolarPanelLevel() == 1 && data.isSolarPanelAdvBought())
                || (data.isSolarPanelBasicBought() && data.isSolarPanelAdvBought()))) {
            // TODO action blocked, already owned UI message
        }
        return data;
    }

    /**
     * Set data.gasCollectorAdvBought to true. Block if already owned, bought or not enough money.
     */
    public GameData buyGasCollectorAdvanced(GameData data) {
        if (data.getGasCollectorLevel() == 1 && !data.isGasCollectorAdvBought()) {
            if (data.getMoney() >= data.PRICE_OF_COLLECTOR) {
                data.setMoney(data.getMoney() - data.PRICE_OF_COLLECTOR);
                data.setGasCollectorAdvBought(true);
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO advanced gas collectors bought UI message
            } else {
                // TODO action blocked, not enough money UI message
            }
        } else if (data.getGasCollectorLevel() == 2 || data.isGasCollectorAdvBought()) {
            // TODO action blocked, already owned UI message
        }
        return data;
    }

    /**
     * Set data.milkingMachineAdvBought to true. Block if already owned, bought or not enough money.
     */
    public GameData buyMilkingMachineAdvanced(GameData data) {
        if (data.getMilkingMachineLevel() == 1 && !data.isMilkingMachineAdvBought()) {
            if (data.getMoney() >= data.PRICE_OF_MILKING) {
                data.setMoney(data.getMoney() - data.PRICE_OF_MILKING);
                data.setMilkingMachineAdvBought(true);
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO advanced milking machine bought UI message
            } else {
                // TODO action blocked, not enough money UI message
            }
        } else if (data.getMilkingMachineLevel() == 2 || data.isMilkingMachineAdvBought()) {
            // TODO action blocked, already owned UI message
        }
        return data;
    }

    /**
     * Set data.tractorAdvBought to true. Block if already owned, bought or not enough money.
     */
    public GameData buyTractorAdvanced(GameData data) {
        if (data.getTractorLevel() == 1 && !data.isTractorAdvBought()) {
            if (data.getMoney() >= data.PRICE_OF_TRACTOR) {
                data.setMoney(data.getMoney() - data.PRICE_OF_TRACTOR);
                data.setTractorAdvBought(true);
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO advanced tractor bought UI message
            } else {
                // TODO action blocked, not enough money UI message
            }
        } else if (data.getTractorLevel() == 2 || data.isTractorAdvBought()) {
            // TODO action blocked, already owned UI message
        }
        return data;
    }

    /**
     * Set data.tractorGasBought to true. Block if data.gasGeneratorLevel < 1 or
     * !data.gasGeneratorBought, or if data.tractorLevel < 2.
     */
    public GameData buyTractorGasEngine(GameData data) {
         if (data.getGasGeneratorLevel() == 1 || data.isGasGeneratorBought()) {

             if ((data.getTractorLevel() == 1 && data.isTractorAdvBought())
                     ||
                     (data.getTractorLevel() == 2 && !data.isTractorGasBought())) {

                 if (data.getMoney() >= data.PRICE_OF_TRACTOR) {
                     data.setMoney(data.getMoney() - data.PRICE_OF_TRACTOR);
                     data.setTractorGasBought(true);
                     data.setActionsDone(data.getActionsDone() + 1);
                     // TODO tractor gas engine bought UI message
                 } else {
                     // TODO action blocked, not enough money UI message
                 }
             } else if (data.getTractorLevel() == 3 || data.isTractorGasBought()) {
                 // TODO action blocked, tractor gas engine already owned
             } else if (data.getGasGeneratorLevel() == 0 || !data.isGasGeneratorBought()) {
                 // TODO action blocked, requires gas generator UI message
             }
         }
        return data;
    }

    /**
     * Set data.gasGeneratorLevel to 1. Block if already 1 or if data.gasGeneratorBought.
     */
    public GameData buyGasGenerator(GameData data) {
        if (data.getGasGeneratorLevel() == 0 && !data.isGasGeneratorBought()) {
            if (data.getMoney() >= data.PRICE_OF_SOLAR) {
                data.setMoney(data.getMoney() - data.PRICE_OF_SOLAR);
                data.setSolarPanelBasicBought(true);
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO basic solar panel bought UI message
            } else {
                // TODO action blocked, not enough money UI message
            }
        } else if (data.getGasGeneratorLevel() == 1 || data.isGasGeneratorBought()){
            // TODO action blocked, already owned UI message
        }
        return data;
    }

    /**
     * Set next 0 element to 1 in data.fields array. Block if all fields[] > 0
     */
    public GameData rentNewField(GameData data) {
        int[] tmpFields = data.getFields();
        int fieldsRented = 2;

        for (int i = 2; i < tmpFields.length; i++) {    // start at 2 because 0 and 1 are owned
            if (tmpFields[i] > 0) {
                fieldsRented++;
            }

            if (tmpFields[i] == 0) {
                tmpFields[i] = 1;
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO new field rented and ready to sow UI message
                break;
            }
        }

        if (fieldsRented >= data.MAX_FIELDS) {
            // TODO no fields available to rent UI message
        }
        data.setFields(tmpFields);
        return data;
    }

    /**
     * Set last index that is not 0 to 0 in data.fields array. Blocked if only indexes 0 and 1 != 0.
     */
    public GameData stopRentingField(GameData data) {
        int[] tmpFields = data.getFields();
        boolean fieldGivenUp = false;

        for (int i = tmpFields.length; i > 1; i--) {    // 0 and 1 are owned, so not touched here
            if (tmpFields[i] != 0) {
                tmpFields[i] = 0;
                fieldGivenUp = true;
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO last rented field given up UI message
                break;
            }
        }

        if (!fieldGivenUp) {
            // TODO action blocked, no fields rented
        }
        data.setFields(tmpFields);
        return data;
    }

    /**
     * Reduce data.manure by 50 if possible and increase data.manureSold by same amount
     */
    public GameData sellManure(GameData data) {
        if (data.getManure() >= 50) {
            data.setManure(data.getManure() - 50);
            data.setManureSold(data.getManureSold() + 50);
            data.setActionsDone(data.getActionsDone() + 1);
            // TODO manure sold UI message
        } else {
            // TODO not enough manure to sell UI message
        }
        return data;
    }
}
