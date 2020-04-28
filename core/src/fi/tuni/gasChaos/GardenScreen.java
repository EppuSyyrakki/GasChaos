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

import static com.badlogic.gdx.math.MathUtils.random;

public class GardenScreen extends Location implements Screen {

    final Texture growthP1;
    final Texture growthP2;
    final Texture growthP3;
    final Texture growthT1;
    final Texture growthT2;
    final Texture growthT3;
    final Texture growthF1;
    final Texture growthF2;
    final Texture growthF3;
    String type;
    int fillerNoise;
    float birdVolume = 0.1f;
    float chickenVolume = 0.07f;

    public GardenScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);
        forest.setVolume(0.06f);
        noise.setVolume(0.09f);
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        growthP1 = new Texture("growth/potato1.png");
        growthP2 = new Texture("growth/potato2.png");
        growthP3 = new Texture("growth/potato3.png");
        growthT1 = new Texture("growth/tomato1.png");
        growthT2 = new Texture("growth/tomato2.png");
        growthT3 = new Texture("growth/tomato3.png");
        growthF1 = new Texture("growth/flower1.png");
        growthF2 = new Texture("growth/flower2.png");
        growthF3 = new Texture("growth/flower3.png");
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        resetInputProcessor();
        tiledMap = new TmxMapLoader().load("maps/Garden.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
        plantType();
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        if (game.gameData.audio) {
            forest.setVolume(0.06f);
            noise.setVolume(0.09f);
            birdVolume = 0.1f;
            chickenVolume = 0.07f;
        } else {
            forest.setVolume(0f);
            noise.setVolume(0f);
            birdVolume = 0f;
            chickenVolume = 0f;
        }

        if (fadeIn) {
            fadeFromBlack();
        }

        batch.begin();
        if (game.gameData.getGardenAmount() > 0) {
            batch.draw(growthRender(), 0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
        }
        sunsetRender();
        black.draw(batch, blackness);
        batch.end();

        if (!game.gameData.isGardenVisited() && !userInterface.dialogFocus) {
            gardenTutorial();
        }

        userInterface.render(game.gameData);
        checkActionRectangles();
    }

    public void plantType() {

        //Random number between 0 and 2.
        int n = random.nextInt(3);
        type = "potato";

        if (n == 0) {
            type = "potato";
        } else if (n == 1) {
            type = "tomato";
        } else if (n == 2) {
            type = "flower";
        }
    }

    public void checkActionRectangles() {
        if ((Gdx.input.isKeyJustPressed(Input.Keys.BACK) || getUIRec("RectangleExit"))
            && !userInterface.dialogFocus) {
            game.setFarmScreen();
            game.farmScreen.player.match();
        } else if (getUIRec("RectangleWeed") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            userInterface.dialogFocus = true;
            String gardenInfo = "";
            int weedsAmount = game.gameData.getWeedsAmount();

            if (weedsAmount == 0 || weedsAmount == 1) {
                gardenInfo = game.myBundle.get("noWeeds");
            } else if (weedsAmount == 2 || weedsAmount == 3) {
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
                        game.gameData.saveGame();
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
                        game.gameData.saveGame();
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
            plantType();
            game.gameData.setGardenAmount(1);
            game.gameData.setWeedsAmount(0);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // garden planted UI message
            uiText = game.myBundle.get("plantGardenComplete");
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
        } else {
            // garden is already growing UI message
            uiText = game.myBundle.get("plantGardenDoneAlready");
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
                        game.gameData.saveGame();
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
                        game.gameData.saveGame();
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    private void gardenTutorial() {
        userInterface.dialogFocus = true;
        uiText = game.myBundle.get("tutorialGarden") + "\n";
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    game.gameData.setGardenVisited(true);
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
        forest.setLooping(true);
        forest.play();
        noise.play();
        noise.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music noise) {
                int lastFillerNoise = fillerNoise;
                while (fillerNoise == lastFillerNoise) {
                    fillerNoise = random.nextInt(3);
                }
                if (fillerNoise == 0) {
                    bird1S.play(birdVolume);
                } else if (fillerNoise == 1) {
                    bird2S.play(birdVolume);
                } else if (fillerNoise == 2) {
                    chickenS.play(chickenVolume);
                }
                noise.play();
            }
        });
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
        noise.stop();
        forest.stop();

    }

    @Override
    public void dispose() {
        blackTexture.dispose();
        sunsetTexture.dispose();
        userInterface.dispose();
        tiledMap.dispose();
        growthT1.dispose();
        growthT2.dispose();
        growthT3.dispose();
        growthP1.dispose();
        growthP2.dispose();
        growthP3.dispose();
        growthF1.dispose();
        growthF2.dispose();
        growthF3.dispose();
    }

    private void resetInputProcessor() {
        userInterface.dialogFocus = false;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    public int growthProgress() {
        if (game.gameData.getGardenAmount() < 25) {
            return 0;
        } else if (game.gameData.getGardenAmount() < 50) {
            return 1;
        } else if (game.gameData.getGardenAmount() >= 50) {
            return 2;
        } else {
            return 0;
        }
    }

    public Texture growthRender() {
        int progress = growthProgress();
        if (progress == 0) {
            switch (type) {
                case "potato":
                    return growthP1;
                case "tomato":
                    return growthT1;
                case "flower":
                    return growthF1;
            }
        } else if (progress == 1) {
            switch (type) {
                case "potato":
                    return growthP2;
                case "tomato":
                    return growthT2;
                case "flower":
                    return growthF2;
            }
        } else if (progress == 2) {
            switch (type) {
                case "potato":
                    return growthP3;
                case "tomato":
                    return growthT3;
                case "flower":
                    return growthF3;
            }
        } else {
            return growthP1;
        }
        return null;
    }
}