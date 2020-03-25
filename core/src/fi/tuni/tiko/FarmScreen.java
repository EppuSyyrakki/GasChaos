package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class FarmScreen extends Location implements Screen {
    Player player;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    public FarmScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game, Screen parent) {
        background = new Texture("farmBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        tiledMap = new TmxMapLoader().load("Farm.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
        this.batch = batch;
        this.camera = camera;
        this.parent = parent;
        this.game = game;
        player = new Player();
        player.player();

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        checkCollisions();
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        if (fadeIn) {
            fadeFromBlack();
        }

        //Player movement

        player.playerTouch(batch);
        player.playerMovement();

        batch.begin();
        //batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        black.draw(batch, blackness);
        player.draw(batch);
        batch.end();



        if (false) {    // condition return to menu
            game.setScreen(parent);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {    // condition enter home
            game.setScreen(new HomeScreen(batch, camera, game, this));
        }

        if (false) {    // condition enter barn
            game.setScreen(new BarnScreen(batch, camera, game, this));
        }

        if (false) {    // condition enter garden
            game.setScreen(new GardenScreen(batch, camera, game, this));
        }

        if (false) {    // condition enter fields
            game.setScreen(new FieldScreen(batch, camera, game, this));
        }

        if (false) {    // condition enter gasTank
            game.setScreen(new GasTankScreen(batch, camera, game, this));
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

    public void checkCollisions() {

        MapLayer collisionObjectLayer = (MapLayer)tiledMap.getLayers().get("RectangleCollision");

        // all of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // add to array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, WORLD_SCALE);

            if (player.getBoundingRectangle().overlaps(rectangle)) {
                System.out.println("placeholder");
            }
        }
    }

    private Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangle = new Rectangle();
        rectangle.x      = r.x * scale;
        rectangle.y      = r.y * scale;
        rectangle.width  = r.width * scale;
        rectangle.height = r.height * scale;
        return rectangle;
    }


}



