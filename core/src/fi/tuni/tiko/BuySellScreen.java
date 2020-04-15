package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import java.util.ArrayList;

public class BuySellScreen extends Location implements Screen {
    private final GasChaosMain game;

    public BuySellScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        background = new Texture("computerBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.game = game;
        userInterface = new UserInterface(game.myBundle);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        tiledMap = new TmxMapLoader().load("BuySell.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
    }

    @Override
    public void show() {
        blackness = 1;
        fadeIn = true;
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        if (fadeIn) {
            fadeFromBlack();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setHomeScreen();
        }

        batch.begin();
        black.draw(batch, blackness);
        batch.end();

        userInterface.render(game.gameData);
        checkActionRectangles();
    }

    public void checkActionRectangles() {
        if ((Gdx.input.isKeyJustPressed(Input.Keys.BACK ) || getUIRec("RectangleExit"))
                && !userInterface.dialogFocus) {
            game.setComputerScreen();
        } else if (getUIRec("RectangleBuyCow") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuyCow", game.gameData.PRICE_OF_COW);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionBuyCow();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleBuyGrain") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            final int amount = game.gameData.getCowList().get(0).getFeed() * 10;
            final int price = game.gameData.PRICE_OF_FEED * amount;
            uiText = game.myBundle.format("askBuyGrain", amount, price,
                    game.gameData.getGrain(),
                    game.gameData.getCowAmount() * game.gameData.getCowList().get(0).getFeed());
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionBuyFeed(price, amount);
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleBuyN") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            final int amount = game.gameData.getFertilizerNMax() / 5;
            final int price = amount * game.gameData.PRICE_OF_N;
            uiText = game.myBundle.format("askBuyN", amount, price,
                    game.gameData.getFertilizerN(),
                    game.gameData.getFertilizerNMax());
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionBuyN(amount, price);
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleBuyP") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            final int amount = game.gameData.getFertilizerPMax() / 5;
            final int price = amount * game.gameData.PRICE_OF_P;
            uiText = game.myBundle.format("askBuyP", amount, price,
                    game.gameData.getFertilizerP(),
                    game.gameData.getFertilizerPMax());
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionBuyP(amount, price);
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleSellManure") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {    // sell manure
            userInterface.dialogFocus = true;
            final int amount = game.gameData.getManure() / 2;
            final int price = amount * game.gameData.MONEY_FROM_MANURE;
            uiText = game.myBundle.format("askSellManure", amount, price);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSellManure(amount, price);
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleSellGrain") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            final int amount = game.gameData.getGrain() / 2;
            final int price = amount * game.gameData.MONEY_FROM_GRAIN;
            uiText = game.myBundle.format("askSellGrain", amount, price);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSellGrain(amount, price);
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleSellGas") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            final int amount = game.gameData.getMethane() / 2;
            final int price = amount * game.gameData.MONEY_FROM_METHANE;
            uiText = game.myBundle.format("askSellGas", amount, price);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSellGas(amount, price);
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleSellN") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            final int amount = game.gameData.getFertilizerN() / 2;
            final int price = amount * game.gameData.MONEY_FROM_N;
            uiText = game.myBundle.format("askSellN");
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSellN(amount, price);
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleSellP") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            final int amount = game.gameData.getFertilizerP() / 2;
            final int price = amount * game.gameData.MONEY_FROM_P;
            uiText = game.myBundle.format("askSellP");
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSellP(amount, price);
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (!game.gameData.isActionsAvailable() && (
                getUIRec("RectangleBuyCow") ||
                getUIRec("RectangleBuyGrain") ||
                getUIRec("RectangleBuyN") ||
                getUIRec("RectangleBuyP") ||
                getUIRec("RectangleSellManure") ||
                getUIRec("RectangleSellGrain") ||
                getUIRec("RectangleSellGas") ||
                getUIRec("RectangleSellN") ||
                getUIRec("RectangleSellP"))) {
            uiText = game.myBundle.get("noActions");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        // TODO sleep
                        resetInputProcessor();
                        remove();
                    } else {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, true);
        }
    }

    /**
     * Add new Cow to cowsBought. moved to cowList when updateResources called in GameData.
     */
    public void actionBuyCow() {
        ArrayList<Cow> tmpCowsBought = game.gameData.getCowsBought();

        if (game.gameData.getMoney() >= game.gameData.PRICE_OF_COW &&
                (tmpCowsBought.size() +
                        game.gameData.getCowList().size()) < game.gameData.MAX_COWS) {
            game.gameData.setMoney(game.gameData.getMoney() - game.gameData.PRICE_OF_COW);
            tmpCowsBought.add(new Cow());
            game.gameData.setCowsBought(tmpCowsBought);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // cow bought, will arrive next turn UI message
            uiText = game.myBundle.get("buyCowComplete");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getMoney() < game.gameData.PRICE_OF_COW) {
            // action blocked, not enough money to buy cow UI message
            uiText = game.myBundle.get("buyCowNoMoney");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getCowList().size() +
                tmpCowsBought.size() >= game.gameData.MAX_COWS) {
            // action blocked, barn is full UI message
            uiText = game.myBundle.get("buyCowNoSpace");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     * UNUSED
     */
    public GameData actionSellCow(GameData data) {
        ArrayList<Cow> tmpCowList = data.getCowList();

        if (tmpCowList.size() > 1) {
            tmpCowList.remove(tmpCowList.size() - 1);
            data.setMoney(data.PRICE_OF_COW / 2 + data.getMoney());
            data.setCowList(tmpCowList);
            data.setActionsDone(data.getActionsDone() + 1);
            // cow sold UI message
        } else if (tmpCowList.size() == 1) {
            // action blocked, can't sell last cow UI message
        }
        return data;
    }

    /**
     * Set data.feed to data.feed + 30. Reduce data.money by 30 * data.PRICE_OF_FEED. Block if not
     * enough money
     */
    public void actionBuyFeed(int price, int amount) {
        if (game.gameData.getGrain() + game.gameData.getFeedBought() < game.gameData.getGrainMax()) {
            if (game.gameData.getMoney() >= price) {
                game.gameData.setMoney(game.gameData.getMoney() - price);
                game.gameData.setFeedBought(game.gameData.getFeedBought() + amount);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                // cow feed bought UI message
                uiText = game.myBundle.format("buyCowFeed", amount);
                Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                    protected void result(Object object) {
                        boolean result = (boolean) object;
                        if (result) {
                            resetInputProcessor();
                            remove();
                        }
                    }
                };
                userInterface.createDialog(d, uiText, false);
            } else {
                // not enough money UI message
                uiText = game.myBundle.format("buyCowFeedNoMoney", price, amount);
                Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                    protected void result(Object object) {
                        boolean result = (boolean) object;
                        if (result) {
                            resetInputProcessor();
                            remove();
                        }
                    }
                };
                userInterface.createDialog(d, uiText, false);
            }
        } else {
            // feed storage will already be full next turn UI message
            uiText = game.myBundle.get("buyCowFeedFull");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     * Increase gameData.FertilizerN by a fifth of the maximum amount. Blocked if not enough money
     * or if storage would go over the limit after addition.
     */
    public void actionBuyN(int amount, int price) {
        if (game.gameData.getMoney() < price) {
            // not enough money UI message
            uiText = game.myBundle.format("buyNNoMoney", price);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getFertilizerN() + amount >= game.gameData.getFertilizerNMax()){
            // N storage would be full UI message
            uiText = game.myBundle.format("buyNFull", price);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            game.gameData.setFertilizerN(game.gameData.getFertilizerN() + amount);
            game.gameData.setMoney(game.gameData.getMoney() - price);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // N bought UI message
            uiText = game.myBundle.format("buyNComplete", amount);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     * Increase gameData.FertilizerP by a fifth of the maximum amount. Blocked if not enough money
     * or if storage would go over the limit after addition.
     */
    public void actionBuyP(int amount, int price) {
        if (game.gameData.getMoney() < price) {
            // not enough money UI message
            uiText = game.myBundle.format("buyPNoMoney", price);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getFertilizerP() + amount >= game.gameData.getFertilizerPMax()){
            // N storage would be full UI message
            uiText = game.myBundle.format("buyPFull", price);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            game.gameData.setFertilizerP(game.gameData.getFertilizerP() + amount);
            game.gameData.setMoney(game.gameData.getMoney() - price);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // N bought UI message
            uiText = game.myBundle.format("buyPComplete", amount);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     * Decrease data.grain by half and increase grainSold by same amount. Blocked if amount = 0.
     */
    public void actionSellGrain(int amount, int price) {
        if (amount > 0) {
            game.gameData.setGrain(game.gameData.getGrain() - amount);
            game.gameData.setGrainSold(game.gameData.getGrainSold() + amount);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // Grain sold UI message
            uiText = game.myBundle.format("sellGrainComplete", amount, price);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            // Not enough grain to sell UI message
            uiText = game.myBundle.get("sellGrainNotEnough");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     * Decrease data.methane by half and increase methaneSold by same amount. Blocked if amount = 0.
     */
    public void actionSellGas(int amount, int price) {
        if (amount > 0) {
            game.gameData.setMethane(game.gameData.getMethane() - amount);
            game.gameData.setMethaneSold(game.gameData.getMethaneSold() + amount);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // Methane sold UI message
            uiText = game.myBundle.format("sellGasComplete", amount, price);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            // Not enough methane to sell UI message
            uiText = game.myBundle.get("sellGasNotEnough");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     * Decrease data.fertilizerN by half and increase NSold by same amount. Blocked if amount = 0.
     */
    public void actionSellN(int amount, int price) {
        if (amount > 0) {
            game.gameData.setFertilizerN(game.gameData.getFertilizerN() - amount);
            game.gameData.setNSold(game.gameData.getNSold() + amount);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // Nitrogen fertilizer sold UI message
            uiText = game.myBundle.format("sellNComplete", amount, price);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            // Not enough Nitrogen fertilizer to sell UI message
            uiText = game.myBundle.get("sellNNotEnough");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     *
     */
    public void actionSellP(int amount, int price) {
        if (amount > 0) {
            game.gameData.setFertilizerP(game.gameData.getFertilizerP() - amount);
            game.gameData.setPSold(game.gameData.getPSold() + amount);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // Phosphorous fertilizer sold UI message
            uiText = game.myBundle.format("sellPComplete", amount, price);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            // Not enough Phosphorous fertilizer to sell UI message
            uiText = game.myBundle.get("sellNNotEnough");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     * Reduce data.manure by half, increase money by manure / 2 * MONEY_FROM_MANURE. Blocked if
     * amount = 0.
     */
    public void actionSellManure(int amount, int price) {
        if (amount > 0) {
            game.gameData.setManure(game.gameData.getManure() - amount);
            game.gameData.setManureSold(game.gameData.getManureSold() + amount);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // manure sold UI message
            uiText = game.myBundle.format("sellManureComplete", amount, price);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            // not enough manure to sell UI message
            uiText = game.myBundle.get("sellManureNotEnough");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
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

    private void resetInputProcessor() {
        userInterface.dialogFocus = false;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

}
