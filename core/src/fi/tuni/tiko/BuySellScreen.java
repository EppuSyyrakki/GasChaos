package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        checkActionRectangles();
    }

    public void checkActionRectangles() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {    // move back to computer main
            game.setComputerScreen();
        }

        if (false) {    // buy cow
            uiText = game.myBundle.format("askBuyCow", game.gameData.PRICE_OF_COW);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionBuyCow();
                        remove();
                    } else {
                        userInterface.dialogFocus = false;
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (false) {    // buy feed
            final int amount = game.gameData.getCowList().get(0).getFeed() * 10;
            final int price = game.gameData.PRICE_OF_FEED * amount;
            uiText = game.myBundle.format("askBuyFeed", amount, price,
                    game.gameData.getFeed(),
                    game.gameData.getCowAmount() * game.gameData.getCowList().get(0).getFeed());
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionBuyFeed(price, amount);
                        remove();
                    } else {
                        userInterface.dialogFocus = false;
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (false) {    // sell manure
            uiText = game.myBundle.format("askSellManure",
                    game.gameData.MANURE_TO_SELL,
                    game.gameData.MONEY_FROM_MANURE * game.gameData.MANURE_TO_SELL);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSellManure();
                        remove();
                    } else {
                        userInterface.dialogFocus = false;
                        resetInputProcessor();
                        remove();
                    };
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
                        userInterface.dialogFocus = false;
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
                        userInterface.dialogFocus = false;
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
                        userInterface.dialogFocus = false;
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
        if (game.gameData.getFeed() + game.gameData.getFeedBought() < game.gameData.getFeedMax()) {
            if (game.gameData.getMoney() >= price) {
                game.gameData.setMoney(game.gameData.getMoney() - price);
                game.gameData.setFeedBought(game.gameData.getFeedBought() + amount);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                // 300 cow feed bought UI message
                uiText = game.myBundle.format("buyCowFeed", amount);
                Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                    protected void result(Object object) {
                        boolean result = (boolean) object;
                        if (result) {
                            userInterface.dialogFocus = false;
                            resetInputProcessor();
                            remove();
                        }
                    }
                };
                userInterface.createDialog(d, uiText, false);
            } else {
                // not enough money UI message
                uiText = game.myBundle.get("buyCowFeedNoMoney");
                Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                    protected void result(Object object) {
                        boolean result = (boolean) object;
                        if (result) {
                            userInterface.dialogFocus = false;
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
                        userInterface.dialogFocus = false;
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     * Reduce data.manure by MANURE_TO_SELL if possible and increase data.manureSold by same amount
     */
    public void actionSellManure() {
        if (game.gameData.getManure() >= game.gameData.MANURE_TO_SELL) {
            game.gameData.setManure(game.gameData.getManure() -
                    game.gameData.MANURE_TO_SELL);
            game.gameData.setManureSold(game.gameData.getManureSold() +
                    game.gameData.MANURE_TO_SELL);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // manure sold UI message
            uiText = game.myBundle.format("sellManureComplete", game.gameData.MANURE_TO_SELL);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        userInterface.dialogFocus = false;
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
                        userInterface.dialogFocus = false;
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
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

}
