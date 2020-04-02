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

import java.util.ArrayList;

public class FieldScreen extends Location implements Screen {
    Player player;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    private final GasChaosMain game;

    public FieldScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        background = new Texture("fieldBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.game = game;
        player = new Player();
        player.player(150f);
        player.setRX(5);
        player.setRY(5);
        player.setTargetX(player.getRX());
        player.setTargetY(player.getRY());
        tiledMap = new TmxMapLoader().load("Fields.tmx");
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
        //batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        black.draw(batch, blackness);
        player.draw(batch);
        batch.end();

        if (exitRec() || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {    // condition return to farm
            game.setFarmScreen();
        }

        if (fieldsRec()) {
            boolean[] actions = availableActions(game.gameData, getFieldNumber());
            // TODO UI available actions to this field
        }

    }

    /**
     * Determine what actions are available for given field number.
     * [0] = rent
     * [1] = stop renting
     * [2] = sow
     * [3] = reap
     * [4] = fertilize (both N and P actions)
     */
    public boolean[] availableActions(GameData data, int number) {
        boolean[] available = {false, false, false, false, false};
        ArrayList<Field> tmpFields = data.getFields();
        Field field = tmpFields.get(number);

        if (!field.isOwned() && !field.isRented()) {
            available[0] = true;
        }
        if (field.isRented() && field.getAmount() == 0) {
            available[1] = true;
        }
        if (field.getAmount() == 0) {
            available[2] = true;
        }
        if (field.getAmount() > 20) {
            available[3] = true;
        }
        if (field.isOwned() || field.isRented()) {
            available[4] = true;
        }
        return available;
    }

    /**
     * Increases amount to 1 in Fields[number]
     */
    public GameData actionSowField(GameData data, int number) {
        ArrayList<Field> tmpFields = data.getFields();
        Field field = tmpFields.get(number);
        field.setAmount(1);
        tmpFields.set(number, field);
        data.setFields(tmpFields);

        // TODO field sown UI message

        return data;
    }

    /**
     * Increases fertilizerN in Fields index number.
     */
    public GameData actionFertilizeFieldN(GameData data, int number, int amount) {
        ArrayList<Field> tmpFields = data.getFields();
        Field field = tmpFields.get(number);
        field.setFertilizerN(field.getFertilizerN() + amount);
        tmpFields.set(number, field);
        data.setFields(tmpFields);

        // TODO nitrogen added to field UI message

        return data;
    }

    /**
     * Increases fertilizerP in Fields index number.
     */
    public GameData actionFertilizeFieldP(GameData data, int number, int amount) {
        ArrayList<Field> tmpFields = data.getFields();
        Field field = tmpFields.get(number);
        field.setFertilizerP(field.getFertilizerP() + amount);
        tmpFields.set(number, field);
        data.setFields(tmpFields);

        // TODO phosphorous added to field UI message

        return data;
    }

    /**
     * Reduce given field to 0 and increase data.grainSold
     */
    public GameData actionReapField(GameData data, int number) {
        ArrayList<Field> tmpFields = data.getFields();
        Field field = tmpFields.get(number);
        data.setGrainSold(data.getGrainSold() + field.getAmount());
        field.setAmount(0);

        // TODO grain reaped and sold UI message

        data.setActionsDone(data.getActionsDone() + 1);
        tmpFields.set(number, field);
        data.setFields(tmpFields);
        return data;
    }

    /**
     * Change field.isRented at index number to true.
     */
    public GameData actionRentNewField(GameData data, int number) {
        ArrayList<Field> tmpFields = data.getFields();
        Field field = tmpFields.get(number);
        field.setRented(true);
        data.setActionsDone(data.getActionsDone() + 1);

        // TODO new field rented UI message

        tmpFields.set(number, field);
        data.setFields(tmpFields);
        return data;
    }

    /**
     * Change field.isRented at index number to false.
     */
    public GameData actionStopRentingField(GameData data, int number) {
        ArrayList<Field> tmpFields = data.getFields();
        Field field = tmpFields.get(number);
        field.setRented(false);
        data.setActionsDone(data.getActionsDone() + 1);

        // TODO stopped renting field UI message

        tmpFields.set(number, field);
        data.setFields(tmpFields);
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

    public boolean exitRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleExit"));
        boolean action = playerAction(r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean fieldsRec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleFields"));
        boolean action = playerAction(r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Index number for the field player touched
     */
    public int getFieldNumber() {
        int fieldNumber = -1;

        if (field1Rec()) {
            fieldNumber = 0;
        } else if (field2Rec()) {
            fieldNumber = 1;
        } else if (field3Rec()) {
            fieldNumber = 2;
        } else if (field4Rec()) {
            fieldNumber = 3;
        } else if (field5Rec()) {
            fieldNumber = 4;
        } else if (field6Rec()) {
            fieldNumber = 5;
        }
        return fieldNumber;
    }

    public boolean field1Rec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleField1"));
        boolean action = playerAction(r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean field2Rec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleField2"));
        boolean action = playerAction(r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean field3Rec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleField3"));
        boolean action = playerAction(r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean field4Rec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleField4"));
        boolean action = playerAction(r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean field5Rec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleField5"));
        boolean action = playerAction(r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean field6Rec() {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get("RectangleField6"));
        boolean action = playerAction(r);
        if (player.getRectangle().overlaps(r) && action == true) {
            return true;
        } else {
            return false;
        }
    }
}
