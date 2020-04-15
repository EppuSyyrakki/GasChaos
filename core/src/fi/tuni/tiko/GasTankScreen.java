package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GasTankScreen extends Location implements Screen {
    private final GasChaosMain game;

    public GasTankScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.game = game;
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
        black.draw(batch, blackness);
        batch.end();

        userInterface.render(game.gameData);

        if ((Gdx.input.isKeyJustPressed(Input.Keys.BACK) || getUIRec("RectangleExit"))
                && !userInterface.dialogFocus) {
            game.setFarmScreen();
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
