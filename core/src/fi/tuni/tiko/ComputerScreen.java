package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class ComputerScreen extends Location implements Screen {
    private final GasChaosMain game;

    public ComputerScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        background = new Texture("computerBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.game = game;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (fadeIn) {
            fadeFromBlack();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setHomeScreen();
        }

        batch.begin();
        batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        black.draw(batch, blackness);
        batch.end();

        userInterface.render(game.gameData);
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
    public GameData action30Feed(GameData data) {
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
    public GameData actionFullFeed(GameData data) {
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
    public GameData actionSolarPanelAdvanced(GameData data) {
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
    public GameData actionGasCollectorAdvanced(GameData data) {
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
    public GameData actionMilkingMachineAdvanced(GameData data) {
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
    public GameData actionTractorAdvanced(GameData data) {
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
    public GameData actionTractorGasEngine(GameData data) {
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
    public GameData actionGasGenerator(GameData data) {
        if (data.getGasGeneratorLevel() == 0 && !data.isGasGeneratorBought()) {
            if (data.getMoney() >= data.PRICE_OF_GENERATOR) {
                data.setMoney(data.getMoney() - data.PRICE_OF_GENERATOR);
                data.setGasGeneratorBought(true);
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
     * Reduce data.manure by 50 if possible and increase data.manureSold by same amount
     */
    public GameData actionSellManure(GameData data) {
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

    @Override
    public void show() {
        blackness = 1;
        fadeIn = true;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
    }

}
