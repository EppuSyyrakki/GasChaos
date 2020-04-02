package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.math.Rectangle.tmp;

public class FarmScreen extends Location implements Screen {
    Player player;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    private final GasChaosMain game;

    // private ShapeRenderer shapeRenderer;

    public FarmScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        background = new Texture("farmBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        tiledMap = new TmxMapLoader().load("Farm.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
        this.batch = batch;
        this.camera = camera;

        this.game = game;
        player = new Player();
        player.player(150f);

        // shapeRenderer = new ShapeRenderer();


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
        player.checkCollisions(tiledMap);
        player.playerTouch(batch);
        player.playerMovement();

        batch.begin();
        //batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        black.draw(batch, blackness);
        player.draw(batch);
        batch.end();

        if (false) {    // condition return to menu
            game.setMenuScreen();
        }

        if (homeRec()) {    // condition enter home
            game.setHomeScreen();
            // game.setScreen(new HomeScreen(batch, camera, game, this));
        }

        if (barnRec()) {    // condition enter barn
            game.setBarnScreen();
        }

        if (false) {    // condition enter garden
            game.setGardenScreen();
        }

        if (fieldsRec()) {    // condition enter fields
            game.setFieldScreen();
        }

        if (gasTankRec()) {    // condition enter gasTank
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

    public boolean homeRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleHome"));
        if (player.getRectangle().overlaps(r)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean barnRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleBarn"));
        if (player.getRectangle().overlaps(r)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean gasTankRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleGasTank"));
        if (player.getRectangle().overlaps(r)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean fieldsRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleField"));
        if (player.getRectangle().overlaps(r)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean gardenRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleGarden"));
        if (player.getRectangle().overlaps(r)) {
            return true;
        } else {
            return false;
        }
    }



}



