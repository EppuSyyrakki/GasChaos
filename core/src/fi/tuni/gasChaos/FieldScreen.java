package fi.tuni.gasChaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import java.util.ArrayList;

public class FieldScreen extends Location implements Screen {
    final Texture growth1;
    final Texture growth2;
    final Texture growth3;
    final Texture growth4;
    final Texture growth5;
    final Texture river1;
    final Texture river2;
    final Texture river3;
    final Texture cloud1;
    float cloudX1;
    float riverX1;
    float riverX2;
    float riverX3;
    final float cloudSpeed;
    final float riverSpeed;
    final Texture sunset;
    final Music river1S = Gdx.audio.newMusic(Gdx.files.internal("audio/river1.mp3"));
    final Music river2S = Gdx.audio.newMusic(Gdx.files.internal("audio/river2.mp3"));

    public FieldScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);
        sunset = new Texture("ground/fieldSunset.png");
        growth1 = new Texture("growth/growth1.png");
        growth2 = new Texture("growth/growth2.png");
        growth3 = new Texture("growth/growth3.png");
        growth4 = new Texture("growth/growth4.png");
        growth5 = new Texture("growth/growth5.png");
        river1 = new Texture("props/river1.png");
        river2 = new Texture("props/river2.png");
        river3 = new Texture("props/river3.png");
        cloud1 = new Texture("props/cloud1.png");
        riverX1 = 0f;
        riverX2 = -8f;
        riverX3 = 8f;
        cloudX1 = 1f;
        riverSpeed = 1f;
        cloudSpeed = 0.08f;
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        tiledMap = new TmxMapLoader().load("maps/Fields.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        if (game.gameData.audio) {
            river1S.setVolume(0.07f);
            river2S.setVolume(0.025f);
        } else {
            river1S.setVolume(0f);
            river2S.setVolume(0f);
        }

        if (fadeIn) {
            fadeFromBlack();
        }

        batch.begin();
        drawRiver();
        drawFields();
        sunsetSky(sunset);
        drawCloud();
        sunsetRender();
        black.draw(batch, blackness);
        batch.end();

        if (!game.gameData.isFieldVisited() && !userInterface.dialogFocus) {
            fieldTutorial();
        }

        userInterface.render(game.gameData);
        checkActionRectangles();
    }

    private void drawCloud() {
        cloudX1 = cloudX1 + cloudSpeed * Gdx.graphics.getDeltaTime();
        if (cloudX1 > 9.5f) {cloudX1 = -4.2f;}
        batch.draw(cloud1, cloudX1,13.9f, 37f / 9f, 16f / 9f);
    }

    /**
     * asks riverQuality the condition of the river.
     * returns a texture based on the response.
     */
    private Texture getRiver() {
        if (riverQuality() == 0) {
            return river1;
        } else if (riverQuality() == 1) {
            return river2;
        } else if (riverQuality() == 2) {
            return river3;
        } else {
            return river1;
        }
    }

    /**
     * if both numbers are under soft cap return 0
     * if both are over soft cap return 2
     * if one is over soft cap return 1
     */
    private int riverQuality() {
        int riverQuality;
        int p = game.gameData.highestFieldsP();
        int n = game.gameData.highestFieldsN();
        if (p < game.gameData.getMAX_P_PER_FIELD()
                && n < game.gameData.getMAX_N_PER_FIELD()) {
            riverQuality = 0;
        } else if (p >= game.gameData.getMAX_P_PER_FIELD()
                && n >= game.gameData.getMAX_N_PER_FIELD()) {
            riverQuality = 2;
        } else if (p >= game.gameData.getMAX_P_PER_FIELD()
                || n >= game.gameData.getMAX_N_PER_FIELD()) {
            riverQuality = 1;
        } else {
            riverQuality = 0;
        }

        return riverQuality;

    }

    private void checkActionRectangles() {
        final int fieldNumber = getTouchedFieldNumber();

        if ((Gdx.input.isKeyJustPressed(Input.Keys.BACK) || getUIRec("RectangleExit"))
                && !userInterface.dialogFocus) {
            game.setFarmScreen();
            game.farmScreen.player.match();
        } else if (fieldNumber > -1 && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            boolean[] actions = availableActions(fieldNumber);
            Field field = game.gameData.getFields().get(fieldNumber);
            uiText = getUIText(actions, field);

            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    int result = (int)object;
                    if (result == 0) {
                        resetInputProcessor();
                        remove();
                    } else if (result == 1){
                        actionFertilizeFieldN(fieldNumber);
                        remove();
                    } else if (result == 2) {
                        actionFertilizeFieldP(fieldNumber);
                        remove();
                    } else if (result == 3) {
                        actionRentNewField(fieldNumber);
                        remove();
                    } else if (result == 4) {
                        actionStopRentingField(fieldNumber);
                        remove();
                    } else if (result == 5) {
                        actionSowField(fieldNumber);
                        remove();
                    } else if (result == 6) {
                        actionReapField(fieldNumber);
                        remove();
                    }

                }
            };
            userInterface.createFieldDialog(d, uiText, actions);
        } else if (fieldNumber > -1 && !game.gameData.isActionsAvailable()
                && !userInterface.dialogFocus) {
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
     * Picks and formats correct UI text for given field.
     */
    private String getUIText(boolean[] actions, Field field) {
        String text = "";
        if (actions[4] && field.getAmount() < field.REAPABLE_AMOUNT && field.getAmount() > 0) {
            text = game.myBundle.format("fieldGrowingNotReady", field.getAmount());
        } else if (actions[4] && field.getAmount() >= field.REAPABLE_AMOUNT
                && field.getAmount() < field.MAX_GROWTH) {
            text = game.myBundle.format("fieldGrowingReady", field.getAmount());
        } else if (actions[4] && field.getAmount() >= field.MAX_GROWTH) {
            text = game.myBundle.format("fieldReady", field.getAmount());
        } else if (actions[4] && field.getAmount() == 0) {
            text = game.myBundle.get("fieldNotSown");
        } else if (!actions[4]) {
            text = game.myBundle.get("fieldNotYours");
        }

        if (actions[4]) {
            float reportedN = (float)field.getFertilizerN() * ((float)Math.random() * 0.3f + 0.9f);
            float reportedP = (float)field.getFertilizerP() * ((float)Math.random() * 0.3f + 0.9f);
            text = text + game.myBundle.format("fieldNP",
                    (int)reportedN, game.gameData.MAX_N_PER_FIELD,
                    (int)reportedP, game.gameData.MAX_P_PER_FIELD);
        }
        return text;
    }

    /**
     * River has 3 parts, each moves at river speed to the right. When river texture is at 10
     * (offscreen to the right) it moves to -8 (Mostly offscreen to the left)
     */
    private void drawRiver() {
        riverX1 = riverX1 + riverSpeed * Gdx.graphics.getDeltaTime();
        riverX2 = riverX2 + riverSpeed * Gdx.graphics.getDeltaTime();
        riverX3 = riverX3 + riverSpeed * Gdx.graphics.getDeltaTime();
        if (riverX1 > 9) {riverX1 = -9f;}
        if (riverX2 > 9) {riverX2 = -9f;}
        if (riverX3 > 9) {riverX3 = -9f;}
        batch.draw(getRiver(), riverX1,0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(getRiver(), riverX2,0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(getRiver(), riverX3,0, WORLD_WIDTH, WORLD_HEIGHT);
    }

    private void drawFields() {
        for (int i = 0; i < game.gameData.MAX_FIELDS; i++) {
            int growth = game.gameData.getFields().get(i).getAmount();
            int max = game.gameData.getFields().get(i).getMAX_GROWTH();

            if (i == 0 && growth > 0) {
                batch.draw(getFieldTexture(growth), 0.75f, 7.2f, 3.5f, 2.2f);
            } else if (i == 1 && growth > 0) {
                batch.draw(getFieldTexture(growth), 4.75f, 7.2f, 3.5f, 2.2f);
            } else if (i == 2 && growth > 0) {
                batch.draw(getFieldTexture(growth), 0.75f, 4.5f, 3.5f, 2.2f);
            } else if (i == 3 && growth > 0) {
                batch.draw(getFieldTexture(growth), 4.75f, 4.5f, 3.5f, 2.2f);
            } else if (i == 4 && growth > 0) {
                batch.draw(getFieldTexture(growth), 0.75f, 1.8f, 3.5f, 2.2f);
            } else if (i == 5 && growth > 0) {
                batch.draw(getFieldTexture(growth), 4.75f, 1.8f, 3.5f, 2.2f);
            }
        }
    }

    private Texture getFieldTexture(int growth) {
        if (growth > 10 && growth < 50) {
            return growth2;
        } else if (growth >= 51 && growth < 100) {
            return growth3;
        } else if (growth >= 100 && growth < 150) {
            return growth4;
        } else if (growth >= 150)  {
            return growth5;
        } else {
            return growth1;
        }
    }

    /**
     * Determine what actions are available for given field number.
     * [0] = rent
     * [1] = stop renting
     * [2] = sow
     * [3] = reap
     * [4] = fertilize (both N and P actions)
     */
    private boolean[] availableActions(int number) {
        boolean[] available = {false, false, false, false, false};
        ArrayList<Field> fields = game.gameData.getFields();
        if (number > -1) {
            Field field = fields.get(number);

            if (!field.isOwned() && !field.isRented()) {
                available[0] = true;
            }
            if (field.isRented() && field.getAmount() == 0) {
                available[1] = true;
            }
            if (field.getAmount() == 0 && (field.isOwned() || field.isRented())) {
                available[2] = true;
            }
            if (field.getAmount() >= field.REAPABLE_AMOUNT) {
                available[3] = true;
            }
            if (field.isOwned() || field.isRented()) {
                available[4] = true;
            }
        }
        return available;
    }

    /**
     * Increases amount to 1 in Fields[number]
     */
    public void actionSowField(int number) {
        ArrayList<Field> tmpFields = game.gameData.getFields();
        Field field = tmpFields.get(number);
        field.setAmount(1);
        tmpFields.set(number, field);
        game.gameData.setFields(tmpFields);
        game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
        // field sown UI message
        uiText = game.myBundle.get("sowFieldComplete");
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

    /**
     * Increases fertilizerN in Fields index number by half of maximum.
     */
    public void actionFertilizeFieldN(int number) {
        ArrayList<Field> tmpFields = game.gameData.getFields();
        Field field = tmpFields.get(number);
        int amount = game.gameData.N_FERTILIZE;

        if (game.gameData.getFertilizerN() >= amount) {
            field.setFertilizerN(field.getFertilizerN() + amount);
            tmpFields.set(number, field);
            game.gameData.setFields(tmpFields);
            game.gameData.setFertilizerN(game.gameData.getFertilizerN() - amount);
            float reportAmount = (float) field.getFertilizerN() * ((float) Math.random() * 0.2f + 0.9f);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // nitrogen added to field UI message
            uiText = game.myBundle.format("fertilizeFieldN", (int) reportAmount);
            Dialog d = new Dialog(game.myBundle.format("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        game.gameData.saveGame();
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getFertilizerN() < amount) {
            // not enough nitrogen fertilizer ui message
            uiText = game.myBundle.format("fertilizeFieldNNotEnough",
                    game.gameData.N_FERTILIZE);
            Dialog d = new Dialog(game.myBundle.format("postDialogTitle"), userInterface.skin) {
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
     * Increases fertilizerP in Fields index number by half of maximum.
     */
    public void actionFertilizeFieldP(int number) {
        ArrayList<Field> tmpFields = game.gameData.getFields();
        Field field = tmpFields.get(number);
        int amount = game.gameData.P_FERTILIZE;

        if (game.gameData.getFertilizerP() >= amount) {
            field.setFertilizerP(field.getFertilizerP() + amount);
            tmpFields.set(number, field);
            game.gameData.setFields(tmpFields);
            game.gameData.setFertilizerP(game.gameData.getFertilizerP() - amount);
            float reportAmount = (float) field.getFertilizerP() * ((float) Math.random() * 0.2f + 0.9f);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // phosphorous added to field UI message
            uiText = game.myBundle.format("fertilizeFieldP", (int) reportAmount);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        game.gameData.saveGame();
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getFertilizerP() < amount) {
            // not enough phosphorous fertilizer ui message
            uiText = game.myBundle.format("fertilizeFieldPNotEnough",
                    game.gameData.P_FERTILIZE);
            Dialog d = new Dialog(game.myBundle.format("postDialogTitle"), userInterface.skin) {
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
     * Reduce given field to 0 and increase data.grainSold
     */
    public void actionReapField(int number) {
        ArrayList<Field> tmpFields = game.gameData.getFields();
        Field field = tmpFields.get(number);
        float amount = (float)field.getAmount();

        if (game.gameData.getTractorLevel() == 2) {
            amount *= 1.25f;
        } else if (game.gameData.getTractorLevel() == 3) {
            amount *= 1.5f;
        }
        game.gameData.setGrain(game.gameData.getGrain() + (int)amount);
        field.setAmount(0);
        tmpFields.set(number, field);
        game.gameData.setFields(tmpFields);
        game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
        // grain reaped and stored UI message
        uiText = game.myBundle.format("reapFieldComplete", (int)amount);
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean)object;
                if (result) {
                    game.gameData.saveGame();
                    resetInputProcessor();
                    remove();
                }
            }
        };
        userInterface.createDialog(d, uiText, false);
    }

    /**
     * Change field.isRented at index number to true.
     */
    public void actionRentNewField(int number) {
        ArrayList<Field> tmpFields = game.gameData.getFields();
        Field field = tmpFields.get(number);
        field.setRented(true);
        tmpFields.set(number, field);
        game.gameData.setFields(tmpFields);
        game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
        // new field rented UI message
        uiText = game.myBundle.get("rentNewFieldComplete");
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean)object;
                if (result) {
                    game.gameData.saveGame();
                    resetInputProcessor();
                    remove();
                }
            }
        };
        userInterface.createDialog(d, uiText, false);
    }

    /**
     * Change field.isRented at index number to false.
     */
    public void actionStopRentingField(int number) {
        ArrayList<Field> tmpFields = game.gameData.getFields();
        Field field = tmpFields.get(number);
        field.setRented(false);
        tmpFields.set(number, field);
        game.gameData.setFields(tmpFields);
        game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
        // stopped renting field UI message
        uiText = game.myBundle.get("stopRentingFieldComplete");
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean)object;
                if (result) {
                    game.gameData.saveGame();
                    resetInputProcessor();
                    remove();
                }
            }
        };
        userInterface.createDialog(d, uiText, false);
    }

    private void fieldTutorial() {
        userInterface.dialogFocus = true;
        uiText = game.myBundle.get("tutorialField") + game.myBundle.get("tutorialField2") + "\n";
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    game.gameData.setFieldVisited(true);
                } else {
                    game.gameData.setAllVisited(true);
                }
                resetInputProcessor();
                remove();
            }
        };
        userInterface.showTutorial(d, uiText);
    }

    @Override
    public void show() {
        blackness = 1;
        fadeIn = true;
        river1S.setLooping(true);
        river2S.setLooping(true);
        river1S.play();
        river2S.play();
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
        river1S.stop();
        river2S.stop();
    }

    @Override
    public void dispose() {
        growth1.dispose();
        growth2.dispose();
        growth3.dispose();
        growth4.dispose();
        growth5.dispose();
        river1.dispose();
        river2.dispose();
        river3.dispose();
        tiledMap.dispose();
        sunsetTexture.dispose();
        blackTexture.dispose();
        userInterface.dispose();
        sunset.dispose();
        cloud1.dispose();
    }

    /**
     * Index number for the field player touched
     */
    public int getTouchedFieldNumber() {
        int fieldNumber = -1;

        if (getUIRec("RectangleField1") && !userInterface.dialogFocus) {
            fieldNumber = 0;
        } else if (getUIRec("RectangleField2") && !userInterface.dialogFocus) {
            fieldNumber = 1;
        } else if (getUIRec("RectangleField3") && !userInterface.dialogFocus) {
            fieldNumber = 2;
        } else if (getUIRec("RectangleField4") && !userInterface.dialogFocus) {
            fieldNumber = 3;
        } else if (getUIRec("RectangleField5") && !userInterface.dialogFocus) {
            fieldNumber = 4;
        } else if (getUIRec("RectangleField6") && !userInterface.dialogFocus) {
            fieldNumber = 5;
        }
        return fieldNumber;
    }

    private void resetInputProcessor() {
        userInterface.dialogFocus = false;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }
}
