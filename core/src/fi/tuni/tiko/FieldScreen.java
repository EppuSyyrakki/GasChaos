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

import java.util.ArrayList;

public class FieldScreen extends Location implements Screen {
    Player player;
    private final GasChaosMain game;
    Texture growth1;
    Texture growth2;
    Texture growth3;
    Texture growth4;
    Texture river1;
    Texture river2;
    Texture river3;
    float riverX1;
    float riverX2;
    float riverX3;
    float riverSpeed;

    public FieldScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        growth1 = new Texture("growth1.png");
        growth2 = new Texture("growth2.png");
        growth3 = new Texture("growth3.png");
        growth4 = new Texture("growth4.png");
        river1 = new Texture("river1.png");
        river2 = new Texture("river1.png");
        river3 = new Texture("river1.png");
        riverX1 = 0f;
        riverX2 = -8f;
        riverX3 = 8f;
        riverSpeed = 1f;
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.game = game;
        userInterface = new UserInterface(game.myBundle);
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
            player.setInputActive(false);
        }

        if (!fadeIn) {
            player.setInputActive(true);
        }

        // River has 3 parts, each moves at river speed to the right.
        // When river texture is at 10 (offscreen to the right)
        // it moves to -8 (Mostly offscreen to the left)
        riverX1 = riverX1 + riverSpeed * Gdx.graphics.getDeltaTime();
        riverX2 = riverX2 + riverSpeed * Gdx.graphics.getDeltaTime();
        riverX3 = riverX3 + riverSpeed * Gdx.graphics.getDeltaTime();
        if (riverX1 > 10) {riverX1 = -8f;}
        if (riverX2 > 10) {riverX2 = -8f;}
        if (riverX3 > 10) {riverX3 = -8f;}

        batch.begin();
        batch.draw(river1, riverX1,0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(river2, riverX2,0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(river3, riverX3,0, WORLD_WIDTH, WORLD_HEIGHT);
        black.draw(batch, blackness);
        batch.end();

        userInterface.render(game.gameData);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {    // condition return to farm
            game.setFarmScreen();
        }

        if (getRec("RectangleFields")) {
            System.out.println(getFieldNumber());
            // boolean[] actions = availableActions(getFieldNumber());
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
    public boolean[] availableActions(int number) {
        boolean[] available = {false, false, false, false, false};
        ArrayList<Field> fields = game.gameData.getFields();

        Field field = fields.get(number);

        if (!field.isOwned() && !field.isRented()) {
            available[0] = true;
        }
        if (field.isRented() && field.getAmount() == 0) {
            available[1] = true;
        }
        if (field.getAmount() == 0) {
            available[2] = true;
        }
        if (field.getAmount() > 200) {
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
        float amount = (float)field.getAmount();
        if (data.getTractorLevel() == 2) {
            amount *= 1.25f;
        } else if (data.getTractorLevel() == 3)
        data.setGrainSold(data.getGrainSold() + (int)amount);
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

    @SuppressWarnings("RedundantCast")
    public boolean getRec(String name) {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get(name));
        boolean action = playerAction(r);
        return player.getRectangle().overlaps(r) && action;
    }

    /**
     * Index number for the field player touched
     */
    public int getFieldNumber() {
        int fieldNumber = -1;

        if (getRec("RectangleField1")) {
            fieldNumber = 0;
        } else if (getRec("RectangleField2")) {
            fieldNumber = 1;
        } else if (getRec("RectangleField3")) {
            fieldNumber = 2;
        } else if (getRec("RectangleField4")) {
            fieldNumber = 3;
        } else if (getRec("RectangleField5")) {
            fieldNumber = 4;
        } else if (getRec("RectangleField6")) {
            fieldNumber = 5;
        }
        return fieldNumber;
    }
}
