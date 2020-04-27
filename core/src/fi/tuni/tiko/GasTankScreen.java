package fi.tuni.tiko;

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

public class GasTankScreen extends Location implements Screen {
    final Texture sunset;
    final Texture generatorBackground;
    int fillerNoise;
    float birdVolume = 0.1f;
    float chickenVolume = 0.07f;

    public GasTankScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);
        forest.setVolume(0.06f);
        noise.setVolume(0.09f);
        sunset = new Texture("ground/gasSunset.png");
        generatorBackground = new Texture("ground/gasGenBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        tiledMap = new TmxMapLoader().load("maps/GasTank.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
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
        if (game.gameData.getGasGeneratorLevel() == 1) {
            batch.draw(generatorBackground, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        }
        sunsetSky(sunset);
        sunsetRender();
        black.draw(batch, blackness);
        batch.end();

        if (!game.gameData.isGasTankVisited() && !userInterface.dialogFocus) {
            gasTankTutorial();
        }

        userInterface.render(game.gameData);
        checkActionRectangles();
    }

    private void checkActionRectangles() {
        if ((Gdx.input.isKeyJustPressed(Input.Keys.BACK) || getUIRec("RectangleExit"))
                && !userInterface.dialogFocus) {
            game.setFarmScreen();
            game.farmScreen.player.match();
        }

        if (getUIRec("RectangleResources") && !userInterface.dialogFocus) {
            userInterface.dialogFocus = true;
            uiText = getResourceDialogText();
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    game.gameData.saveGame();
                    resetInputProcessor();
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    private String getResourceDialogText() {
        String text = game.myBundle.get("resourceView") + "\n\n";
        text += game.myBundle.format("resourceGrain",
                game.gameData.getGrain(),
                game.gameData.getGrainMax()) + "\n";
        text += game.myBundle.format("resourceCows",
                game.gameData.getCowAmount(),
                game.gameData.MAX_COWS) + "\n";
        text += game.myBundle.format("resourceGrainInBarn",
                game.gameData.getGrainInBarn()) + "\n";
        text += game.myBundle.format("resourceCowsEat",
                game.gameData.getCowConsumption()) + "\n";
        text += game.myBundle.format("resourceGas",
                game.gameData.getMethane(),
                game.gameData.getMethaneMax()) + "\n";
        text += game.myBundle.format("resourceGasProduction",
                game.gameData.getCowMethaneProduction()) + "\n";
        text += game.myBundle.format("resourceManure",
                game.gameData.getManure(),
                game.gameData.getManureMax()) + "\n";
        text += game.myBundle.format("resourceManureProduction",
                game.gameData.getCowManureProduction()) + "\n";
        text += game.myBundle.format("resourceN",
                game.gameData.getFertilizerN(),
                game.gameData.getFertilizerNMax()) + "\n";
        text += game.myBundle.format("resourceP",
                game.gameData.getFertilizerP(),
                game.gameData.getFertilizerPMax()) + "\n";
        return text;
    }

    private void gasTankTutorial() {
        userInterface.dialogFocus = true;
        uiText = game.myBundle.get("tutorialGasTank") + "\n";
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    game.gameData.setGasTankVisited(true);
                    resetInputProcessor();
                    remove();
                }
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
        userInterface.dispose();
        tiledMap.dispose();
        sunset.dispose();
        generatorBackground.dispose();
        sunsetTexture.dispose();
        blackTexture.dispose();
    }

    private void resetInputProcessor() {
        userInterface.dialogFocus = false;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }
}
