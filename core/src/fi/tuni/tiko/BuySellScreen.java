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
        }

        if (getUIRec("RectangleBuyCow") && !userInterface.dialogFocus) {    // buy cow
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuyCow", game.gameData.PRICE_OF_COW);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionBuyCow();
                        remove();
                    } else {
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (getUIRec("RectangleBuyGrain") && !userInterface.dialogFocus) {    // buy feed
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
                        remove();
                    } else {
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (getUIRec("RectangleBuyN") && !userInterface.dialogFocus) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuyN");
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        // TODO implement actionBuyN();
                        remove();
                    } else {
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (getUIRec("RectangleBuyP") && !userInterface.dialogFocus) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuyP");
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        // TODO implement actionBuyP();
                        remove();
                    } else {
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (getUIRec("RectangleSellManure") && !userInterface.dialogFocus) {    // sell manure
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askSellManure",
                    game.gameData.MANURE_TO_SELL,
                    game.gameData.MONEY_FROM_MANURE * game.gameData.MANURE_TO_SELL);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSellManure();
                        remove();
                    } else {
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (getUIRec("RectangleSellGrain") && !userInterface.dialogFocus) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askSellGrain");
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        // TODO implement actionSellGrain();
                        remove();
                    } else {
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (getUIRec("RectangleSellGas") && !userInterface.dialogFocus) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askSellGas");
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        // TODO implement actionSellGas();
                        remove();
                    } else {
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (getUIRec("RectangleSellN") && !userInterface.dialogFocus) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askSellN");
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        // TODO implement actionSellN();
                        remove();
                    } else {
                        resetInputProcessor();
                        remove();
                    };
                }
            };
            userInterface.createDialog(d, uiText, true);
        }

        if (getUIRec("RectangleSellP") && !userInterface.dialogFocus) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askSellP");
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        // TODO implement actionSellP();
                        remove();
                    } else {
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
                // 300 cow feed bought UI message
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
                uiText = game.myBundle.get("buyCowFeedNoMoney");
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
