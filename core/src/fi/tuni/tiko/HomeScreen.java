package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class HomeScreen extends Location implements Screen {
    Player player;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    private final GasChaosMain game;

    public HomeScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super();
        background = new Texture("homeBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.game = game;
        this.batch = batch;
        this.camera = camera;

        player = new Player();
        player.player(100f);
        player.setRX(4);
        player.setRY(4);
        player.setTargetX(player.getRX());
        player.setTargetY(player.getRY());
        tiledMap = new TmxMapLoader().load("Home.tmx");
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

        if (fadeIn) {
            fadeFromBlack();
        }

        // Player movement
        player.checkCollisions(tiledMap);
        player.playerTouch(batch);
        player.playerMovement();

        batch.begin();
        black.draw(batch, blackness);
        player.draw(batch);
        batch.end();

        if (exitRec() || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {    // condition return to farm
            game.setFarmScreen();
        }

        if (phoneRec()) {    // condition call to grandmother
            game.setGrandmotherScreen();
        }

        if (computerRec()) {    // condition go to computer
            game.setComputerScreen();
        }

        if (bedRec()) {  // condition end turn
            System.out.println("i hve the sleepy"); // end turn
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

    public boolean exitRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleExit"));
        boolean action = player.playerAction(batch, r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean bedRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleBed"));
        boolean action = player.playerAction(batch, r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean phoneRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectanglePhone"));
        boolean action = player.playerAction(batch, r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean computerRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectanglePC"));
        boolean action = player.playerAction(batch, r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

}
