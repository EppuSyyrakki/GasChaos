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

        userInterface.render(game.gameData);

        if (!game.gameData.isGasTankVisited() && !userInterface.dialogFocus) {
            gasTankTutorial();
        }

        if ((Gdx.input.isKeyJustPressed(Input.Keys.BACK) || getUIRec("RectangleExit"))
                && !userInterface.dialogFocus) {
            game.setFarmScreen();
            game.farmScreen.player.match();
        }

        if (false) {    // condition open emergency valve
            game.gameData = actionOpenEmergencyValve(game.gameData);
        }
    }

    /**
     * Decrease data.methane by half.
     */
    public GameData actionOpenEmergencyValve(GameData data) {
        data.setMethane(data.getMethane() / 2);
        // TODO methane tank depressurized UI message
        data.setActionsDone(data.getActionsDone() + 1);
        return data;
    }

    /**
     * Increase data.methaneSold by data.methane. data.methane reduction handled in GameData update.
     */
    public GameData actionSellGas(GameData data) {
        if (data.getMethane() > 0) {
            data.setMethaneSold(data.getMethane());
            data.setActionsDone(data.getActionsDone() + 1);
            // TODO methane sold UI message
        } else {
            // TODO no methane to sell UI message
        }

        return data;
    }

    private void gasTankTutorial() {
        userInterface.dialogFocus = true;
        // uiText = game.myBundle.get("tutorialGasTank"); TODO add text to myBundle
        uiText = "gasTank tutorial";
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    game.gameData.setGasTankVisited(true);
                    userInterface.dialogFocus = false;
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
                    bird1S.play(0.1f);
                } else if (fillerNoise == 1) {
                    bird2S.play(0.1f);
                } else if (fillerNoise == 2) {
                    chickenS.play(0.07f);
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
