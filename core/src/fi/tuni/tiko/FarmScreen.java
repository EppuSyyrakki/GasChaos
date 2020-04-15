package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class FarmScreen extends Location implements Screen {
    @SuppressWarnings("CanBeFinal")
    Player player;
    Texture backgroundSolar;
    Texture backgroundSolar2;
    Texture sun;
    float sunX;
    float sunY;

    public FarmScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);

        // Check what level of solar panels and set foreground accordingly.
        backgroundSolar = new Texture("ground/farmForegroundSolar.png");
        backgroundSolar2 = new Texture("ground/farmForegroundSolar2.png");
        background = new Texture("ground/farmForeground.png");
        sun = new Texture("props/sun.png");

        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        tiledMap = new TmxMapLoader().load("maps/Farm.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        player = new Player();
        player.player(150f);
        player.setRX(4.5f);
        player.setRY(6f);
        player.matchX(player.getRX());
        player.matchY(player.getRY());
        game.gameData.saveGame();
        game.gameData.loadGame();
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

        //Player movement
        if (!userInterface.dialogFocus) {
            player.playerTouch(batch);
            player.playerMovement(tiledMap);
        }

        if (game.gameData.getActionsDone() == 0) {
            sunX = 0.7f;
            sunY = 13.5f;
        } else if (game.gameData.getActionsDone() == 1) {
            sunX = 3f;
            sunY = 13.8f;
        } else if (game.gameData.getActionsDone() == 2) {
            sunX = 5.5f;
            sunY = 14.1f;
        } else if (game.gameData.getActionsDone() == 3) {
            sunX = 7.7f;
            sunY = 13.1f;
        }

        batch.begin();
        player.draw(batch);
        batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        // Check what level of solar panels and set foreground accordingly.
        if (game.gameData.getSolarPanelLevel() == 1) {
            batch.draw(backgroundSolar, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        } else if (game.gameData.getSolarPanelLevel() == 2) {
            batch.draw(backgroundSolar2, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        }
        batch.draw(sun, sunX,sunY, 1, 1);
        black.draw(batch, blackness);
        batch.end();

        userInterface.render(game.gameData);

        checkActionRectangles();
    }

    public void checkActionRectangles() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setMenuScreen();
        } else if (getRec("RectangleHome")) {    // condition enter home
            game.setHomeScreen();
        } else if (getRec("RectangleBarn")) {    // condition enter barn
            game.setBarnScreen();
        } else if (getRec("RectangleGarden")) {    // condition enter garden
            game.setGardenScreen();
        } else if (getRec("RectangleField")) {    // condition enter fields
            game.setFieldScreen();
        } else if (getRec("RectangleGasTank")) {    // condition enter gasTank
            game.setGasTankScreen();
        }
    }

    @Override
    public void show() {
        blackness = 1;
        fadeIn = true;
    }

    @Override
    public void hide() {

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
    public void dispose() {
        background.dispose();
    }

    @SuppressWarnings("RedundantCast")
    public boolean getRec(String name) {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get(name));
        boolean action = playerAction(r);
        return player.getRectangle().overlaps(r) && action;
    }
}



