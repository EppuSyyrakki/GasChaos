package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

public class GardenScreen extends Location implements Screen {

    public GardenScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        resetInputProcessor();
        tiledMap = new TmxMapLoader().load("maps/Garden.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
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

        batch.begin();
        sunsetRender();
        black.draw(batch, blackness);
        batch.end();

        userInterface.render(game.gameData);
        checkActionRectangles();
    }

    public void checkActionRectangles() {
        if ((Gdx.input.isKeyJustPressed(Input.Keys.BACK) || getUIRec("RectangleExit"))
            && !userInterface.dialogFocus) {
            game.setFarmScreen();
        } else if (getUIRec("RectangleWeed") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            String gardenInfo = "";
            int weedsAmount = game.gameData.getWeedsAmount();

            if (weedsAmount == 0) {
                gardenInfo = game.myBundle.get("noWeeds");
            } else if (weedsAmount > 0 && weedsAmount < 4) {
                gardenInfo = game.myBundle.get("someWeeds");
            } else if (weedsAmount >= 4) {
                gardenInfo = game.myBundle.get("manyWeeds");
            }
            uiText = game.myBundle.format("askWeedGarden", gardenInfo);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionWeedGarden();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleSell") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            float price = (float)game.gameData.getGardenAmount()
                    * (float)game.gameData.MONEY_FROM_GARDEN;
            price *= (float)Math.random() * 0.2f + 1f;
            uiText = game.myBundle.format("askSellGarden", price);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSellGarden();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectanglePlant") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.get("askPlantGarden");
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionPlantGarden();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if ((getUIRec("RectangleWeed") ||
                getUIRec("RectangleSell") ||
                getUIRec("RectanglePlant")) && !userInterface.dialogFocus
                && !game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.get("askGoSleep");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        game.setNewTurn();
                    }
                    resetInputProcessor();
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        }
    }

    /**
     * Increase data.gardenAmount from 0 to 1.
     */
    public void actionPlantGarden() {
        if (game.gameData.getGardenAmount() == 0) {
            game.gameData.setGardenAmount(1);
            game.gameData.setWeedsAmount(0);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // garden planted UI message
            uiText = game.myBundle.get("plantGardenComplete");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            // garden is already growing UI message
            uiText = game.myBundle.get("plantGardenDoneAlready");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
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
     * Reduce data.weedsAmount to 0 and increase data.gardenGrowth to 5.
     */
    public void actionWeedGarden() {
        if (game.gameData.getWeedsAmount() <= 0) {
            // garden is already weeded UI message
            uiText = game.myBundle.get("weedGardenNoWeed");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            game.gameData.setWeedsAmount(0);
            game.gameData.setGardenGrowth(5);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // garden has been weeded UI message
            uiText = game.myBundle.get("weedGardenComplete");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
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
     * Increase data.gardenSold by data.gardenAmount and reduce data. gardenAmount to 0.
     * Block if gardenAmount is not over 30 (garden is not ripe).
     */
    public void actionSellGarden() {
        if (game.gameData.getGardenAmount() > 30) {
            game.gameData.setGardenSold(game.gameData.getGardenAmount());
            game.gameData.setGardenAmount(0);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // garden produce sold UI message
            uiText = game.myBundle.get("sellGardenComplete");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            // garden produce not ripe UI message
            uiText = game.myBundle.get("sellGardenNotRipe");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
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

    private void resetInputProcessor() {
        userInterface.dialogFocus = false;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }
}