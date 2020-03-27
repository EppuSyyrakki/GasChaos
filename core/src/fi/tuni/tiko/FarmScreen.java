package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private ShapeRenderer shapeRenderer;

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
        shapeRenderer = new ShapeRenderer();


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
        homeRec();
        barnRec();
        fieldsRec();
        gasTankRec();

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

    public void homeRec() {


        MapLayer collisionObjectLayer = (MapLayer)tiledMap.getLayers().get("RectangleHome");

        // all of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // add to array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, WORLD_SCALE);

            if (player.getRectangle().overlaps(rectangle)) {
                game.setScreen(new HomeScreen(batch, camera, game, this));
            }
        }
    }

    public void barnRec() {


        MapLayer collisionObjectLayer = (MapLayer)tiledMap.getLayers().get("RectangleBarn");

        // all of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // add to array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, WORLD_SCALE);

            if (player.getRectangle().overlaps(rectangle)) {
                System.out.println("Barn");
            }
        }
    }

    public void gasTankRec() {


        MapLayer collisionObjectLayer = (MapLayer)tiledMap.getLayers().get("RectangleGasTank");

        // all of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // add to array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, WORLD_SCALE);

            if (player.getRectangle().overlaps(rectangle)) {
                System.out.println("Gas, gas gas I'm gonna step on the gas");
            }
        }
    }

    public void fieldsRec() {


        MapLayer collisionObjectLayer = (MapLayer)tiledMap.getLayers().get("RectangleField");

        // all of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // add to array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, WORLD_SCALE);

            if (player.getRectangle().overlaps(rectangle)) {
                System.out.println("It's almost harvesting season");
            }
        }
    }

    private Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangleScale = new Rectangle();
        rectangleScale.x      = r.x * scale;
        rectangleScale.y      = r.y * scale;
        rectangleScale.width  = r.width * scale;
        rectangleScale.height = r.height * scale;
        return rectangleScale;
    }

}



