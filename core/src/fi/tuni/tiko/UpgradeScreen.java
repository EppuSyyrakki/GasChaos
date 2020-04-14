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
        tiledMap = new TmxMapLoader().load("Upgrades.tmx");
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

        if (false) {    // buy solar panel basic
            uiText = game.myBundle.format("askBuySolarPanelBasic",
                    game.gameData.PRICE_OF_SOLAR);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSolarPanelBasic();
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

        if (false) {    // buy solar panel advanced
            uiText = game.myBundle.format("askBuySolarPanelAdvanced",
                    game.gameData.PRICE_OF_SOLAR);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionSolarPanelAdvanced();
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

        if (false) {     // buy advanced gas collector
            uiText = game.myBundle.format("askBuyGasCollectorAdvanced",
                    game.gameData.PRICE_OF_COLLECTOR);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionGasCollectorAdvanced();
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

        if (false) {    // buy advanced milking machine
            uiText = game.myBundle.format("askBuyMilkingMachineAdvanced",
                    game.gameData.PRICE_OF_MILKING);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionMilkingMachineAdvanced();
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

        if (false) {    // buy advanced tractor
            uiText = game.myBundle.format("askBuyTractorAdvanced",
                    game.gameData.PRICE_OF_TRACTOR);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionTractorAdvanced();
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

        if (false) {    // buy gas tractor
            uiText = game.myBundle.format("askBuyTractorGas",
                    game.gameData.PRICE_OF_TRACTOR);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionTractorGas();
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

        if (false) {    // buy gas generator
            uiText = game.myBundle.format("askBuyGasGenerator",
                    game.gameData.PRICE_OF_GENERATOR);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionGasGenerator();
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
                            userInterface.dialogFocus = false;
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
                            userInterface.dialogFocus = false;
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
                            userInterface.dialogFocus = false;
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
                            userInterface.dialogFocus = false;
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
                            userInterface.dialogFocus = false;
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
                            userInterface.dialogFocus = false;
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
                                userInterface.dialogFocus = false;
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
                            userInterface.dialogFocus = false;
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
                            userInterface.dialogFocus = false;
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