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

public class UpgradeScreen extends Location implements Screen {
    private final GasChaosMain game;

    public UpgradeScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.game = game;
        userInterface = new UserInterface(game.myBundle);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        tiledMap = new TmxMapLoader().load("maps/Upgrades.tmx");
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || getUIRec("RectangleExit")) {
            game.setHomeScreen();
        }

        batch.begin();
        black.draw(batch, blackness);
        batch.end();

        userInterface.render(game.gameData);
        checkActionRectangles();
    }

    public void checkActionRectangles() {
        if ((Gdx.input.isKeyJustPressed(Input.Keys.BACK) || getUIRec("RectangleExit"))
                && !userInterface.dialogFocus) {
            game.setComputerScreen();
        } else if (getUIRec("RectangleSolarBasic") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {    // buy solar panel basic
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuySolarPanelBasic",
                    game.gameData.PRICE_OF_SOLAR);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSolarPanelBasic();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleSolarAdvanced") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {    // buy solar panel advanced
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuySolarPanelAdvanced",
                    game.gameData.PRICE_OF_SOLAR);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSolarPanelAdvanced();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleGasCollector") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {     // buy advanced gas collector
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuyGasCollectorAdvanced",
                    game.gameData.PRICE_OF_COLLECTOR);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionGasCollectorAdvanced();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleMilkingMachine") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {    // buy advanced milking machine
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuyMilkingMachineAdvanced",
                    game.gameData.PRICE_OF_MILKING);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionMilkingMachineAdvanced();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleTractorAdvanced") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuyTractorAdvanced",
                    game.gameData.PRICE_OF_TRACTOR);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionTractorAdvanced();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleTractorGas") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuyTractorGas",
                    game.gameData.PRICE_OF_TRACTOR);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionTractorGas();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getUIRec("RectangleGasGenerator") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {    // buy gas generator
            userInterface.dialogFocus = true;
            uiText = game.myBundle.format("askBuyGasGenerator",
                    game.gameData.PRICE_OF_GENERATOR);
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionGasGenerator();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (!game.gameData.isActionsAvailable() && (
                getUIRec("RectangleSolarBasic") ||
                getUIRec("RectangleSolarAdvanced") ||
                getUIRec("RectangleGasGenerator") ||
                getUIRec("RectangleTractorGas") ||
                getUIRec("RectangleTractorAdvanced") ||
                getUIRec("RectangleMilkingMachine") ||
                getUIRec("RectangleGasCollector"))) {
            uiText = game.myBundle.get("noActions");
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
     * Set data.solarPanelBasicBought to true. Block if data.solarPanelLevel > 0 or not enough
     * money.
     */
    public void actionSolarPanelBasic() {
        if (game.gameData.getSolarPanelLevel() == 0
                && !game.gameData.isSolarPanelBasicBought() &&
                !game.gameData.isSolarPanelAdvBought()) {
            if (game.gameData.getMoney() >= game.gameData.PRICE_OF_SOLAR) {
                game.gameData.setMoney(game.gameData.getMoney() - game.gameData.PRICE_OF_SOLAR);
                game.gameData.setSolarPanelBasicBought(true);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                // basic solar panel bought UI message
                uiText = game.myBundle.get("buySolarPanelBasicComplete");
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
                // action blocked, not enough money UI message
                uiText = game.myBundle.get("buySolarPanelBasicNoMoney");
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
            // action blocked, already owned UI message
            uiText = game.myBundle.get("buySolarPanelBasicIsOwned");
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
     * Set data.solarPanelAdvBought to true. Block if data.solarPanelLevel = 2 or 0 or if already
     * bought or not enough money.
     */
    public void actionSolarPanelAdvanced() {
        if ( (game.gameData.getSolarPanelLevel() == 0 && game.gameData.isSolarPanelBasicBought())
                ||
                (game.gameData.getSolarPanelLevel() == 1 &&
                        !game.gameData.isSolarPanelAdvBought()) ) {

            if (game.gameData.getMoney() >= game.gameData.PRICE_OF_SOLAR) {
                game.gameData.setMoney(game.gameData.getMoney() - game.gameData.PRICE_OF_SOLAR);
                game.gameData.setSolarPanelAdvBought(true);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                // advanced solar panel bought UI message
                uiText = game.myBundle.get("buySolarPanelAdvancedComplete");
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
                // action blocked, not enough money UI message
                uiText = game.myBundle.get("buySolarPanelAdvancedNoMoney");
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
        } else if (game.gameData.getSolarPanelLevel() == 0 &&
                !game.gameData.isSolarPanelBasicBought()) {
            // action blocked, must have basic panel first UI message
            uiText = game.myBundle.get("buySolarPanelAdvancedNoBasic");
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
        } else if (game.gameData.getSolarPanelLevel() == 2
                || ( (game.gameData.getSolarPanelLevel() == 1 &&
                game.gameData.isSolarPanelAdvBought())
                || (game.gameData.isSolarPanelBasicBought() &&
                game.gameData.isSolarPanelAdvBought()))) {
            // action blocked, already owned UI message
            uiText = game.myBundle.get("buySolarPanelAdvancedIsOwned");
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
     * Set data.gasCollectorAdvBought to true. Block if already owned, bought or not enough money.
     */
    public void actionGasCollectorAdvanced() {
        if (game.gameData.getGasCollectorLevel() == 1 && !game.gameData.isGasCollectorAdvBought()) {
            if (game.gameData.getMoney() >= game.gameData.PRICE_OF_COLLECTOR) {
                game.gameData.setMoney(game.gameData.getMoney() - game.gameData.PRICE_OF_COLLECTOR);
                game.gameData.setGasCollectorAdvBought(true);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                // advanced gas collectors bought UI message
                uiText = game.myBundle.get("buyAdvancedGasCollectorsComplete");
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
                // action blocked, not enough money UI message
                uiText = game.myBundle.get("buyAdvancedGasCollectorsNoMoney");
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
        } else if (game.gameData.getGasCollectorLevel() == 2 ||
                game.gameData.isGasCollectorAdvBought()) {
            // action blocked, already owned UI message
            uiText = game.myBundle.get("buyAdvancedGasCollectorsIsOwned");
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
     * Set data.milkingMachineAdvBought to true. Block if already owned, bought or not enough money.
     */
    public void actionMilkingMachineAdvanced() {
        if (game.gameData.getMilkingMachineLevel() == 1 &&
                !game.gameData.isMilkingMachineAdvBought()) {
            if (game.gameData.getMoney() >= game.gameData.PRICE_OF_MILKING) {
                game.gameData.setMoney(game.gameData.getMoney() - game.gameData.PRICE_OF_MILKING);
                game.gameData.setMilkingMachineAdvBought(true);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                // advanced milking machine bought UI message
                uiText = game.myBundle.get("buyAdvancedMilkingMachineComplete");
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
                // action blocked, not enough money UI message
                uiText = game.myBundle.get("buyAdvancedMilkingMachineNoMoney");
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
        } else if (game.gameData.getMilkingMachineLevel() == 2 ||
                game.gameData.isMilkingMachineAdvBought()) {
            // action blocked, already owned UI message
            uiText = game.myBundle.get("buyAdvancedMilkingMachineIsOwned");
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
     * Set data.tractorAdvBought to true. Block if already owned, bought or not enough money.
     */
    public void actionTractorAdvanced() {
        if (game.gameData.getTractorLevel() == 1 && !game.gameData.isTractorAdvBought()) {
            if (game.gameData.getMoney() >= game.gameData.PRICE_OF_TRACTOR) {
                game.gameData.setMoney(game.gameData.getMoney() - game.gameData.PRICE_OF_TRACTOR);
                game.gameData.setTractorAdvBought(true);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                // advanced tractor bought UI message
                uiText = game.myBundle.get("buyAdvancedTractorComplete");
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
                // action blocked, not enough money UI message
                uiText = game.myBundle.get("buyAdvancedTractorNoMoney");
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
        } else if (game.gameData.getTractorLevel() == 2 || game.gameData.isTractorAdvBought()) {
            // action blocked, already owned UI message
            uiText = game.myBundle.get("buyAdvancedTractorIsOwned");
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
     * Set data.tractorGasBought to true. Block if data.gasGeneratorLevel < 1 or
     * !data.gasGeneratorBought, or if data.tractorLevel < 2.
     */
    public void actionTractorGas() {
        if (game.gameData.getGasGeneratorLevel() == 1 || game.gameData.isGasGeneratorBought()) {

            if ((game.gameData.getTractorLevel() == 1 && game.gameData.isTractorAdvBought())
                    ||
                    (game.gameData.getTractorLevel() == 2 && !game.gameData.isTractorGasBought())) {

                if (game.gameData.getMoney() >= game.gameData.PRICE_OF_TRACTOR) {
                    game.gameData.setMoney(
                            game.gameData.getMoney() - game.gameData.PRICE_OF_TRACTOR);
                    game.gameData.setTractorGasBought(true);
                    game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                    // tractor gas engine bought UI message
                    uiText = game.myBundle.get("buyTractorGasComplete");
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
                    // action blocked, not enough money UI message
                    uiText = game.myBundle.get("buyTractorGasNoMoney");
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
            } else if (game.gameData.getTractorLevel() == 3 ||
                    game.gameData.isTractorGasBought()) {
                // action blocked, tractor gas engine already owned
                uiText = game.myBundle.get("buyTractorGasIsOwned");
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
            } else if (game.gameData.getGasGeneratorLevel() == 0 ||
                    !game.gameData.isGasGeneratorBought()) {
                // action blocked, requires gas generator UI message
                uiText = game.myBundle.get("buyTractorGasNoGenerator");
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
    }

    /**
     * Set data.gasGeneratorLevel to 1. Block if already 1 or if data.gasGeneratorBought.
     */
    public void actionGasGenerator() {
        if (game.gameData.getGasGeneratorLevel() == 0 && !game.gameData.isGasGeneratorBought()) {
            if (game.gameData.getMoney() >= game.gameData.PRICE_OF_GENERATOR) {
                game.gameData.setMoney(game.gameData.getMoney() - game.gameData.PRICE_OF_GENERATOR);
                game.gameData.setGasGeneratorBought(true);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                // gas generator bought UI message
                uiText = game.myBundle.get("buyGasGeneratorComplete");
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
                // action blocked, not enough money UI message
                uiText = game.myBundle.get("buyGasGeneratorNoMoney");
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
        } else if (game.gameData.getGasGeneratorLevel() == 1 ||
                game.gameData.isGasGeneratorBought()){
            // action blocked, already owned UI message
            uiText = game.myBundle.get("buyGasGeneratorIsOwned");
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

    public void newTurn() {
        game.gameData.sleep();
        game.homeScreen.setNewTurn(true);
        game.setHomeScreen();
        game.farmScreen.player.setRX(2);
        game.farmScreen.player.setRY(5);
        game.farmScreen.player.matchX(2);
        game.farmScreen.player.matchY(5);
    }
}
