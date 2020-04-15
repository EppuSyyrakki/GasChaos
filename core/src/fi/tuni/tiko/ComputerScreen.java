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

public class ComputerScreen extends Location implements Screen {

    public ComputerScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        tiledMap = new TmxMapLoader().load("maps/Computer.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
        background = new Texture("ground/computerBackground.png");
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

    private void checkActionRectangles() {
        if (((Gdx.input.isKeyJustPressed(Input.Keys.BACK)) || getUIRec("RectangleExit"))
             && !userInterface.dialogFocus) {    // move back to home
            game.setHomeScreen();
        }

        if (getUIRec("RectangleUpgrades") && !userInterface.dialogFocus) {    // go to upgrades screen
            game.setUpgradeScreen();
        }

        if (getUIRec("RectangleBuySell") && !userInterface.dialogFocus) {    // go to buy/sell screen
            game.setBuySellScreen();
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

}
