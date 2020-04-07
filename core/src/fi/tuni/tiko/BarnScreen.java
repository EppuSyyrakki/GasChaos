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

public class BarnScreen extends Location implements Screen {
    Player player;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    private final GasChaosMain game;
    Texture cow1 = new Texture("cow1.png");
    Texture cow2 = new Texture("cow2.png");
    Texture cow3 = new Texture("cow3.png");
    Texture cowBrown1 = new Texture("cowBrown1.png");
    Texture cowBrown2 = new Texture("cowBrown2.png");
    Texture cowBrown3 = new Texture("cowBrown3.png");
    Texture manure = new Texture("manure.png");
    Texture hay = new Texture("hay.png");
    Texture barnFence = new Texture("barnFence.png");
    Rectangle spawn;
    float cowSize = 150f;
    float[] manureX = {0.9f, 1.4f, 1f, 1.5f, 0.8f, 1.24f, 1.3f, 1f, 1.7f, 1.2f, 1.4f};
    float[] manureY = {8f, 7.1f, 6.3f, 5.7f, 5.1f, 4.7f, 3.9f, 3.2f, 2.9f, 2.2f, 1.9f};
    float[] hayX = {5.2f, 5.15f, 5.3f, 5.1f, 5.15f, 5.1f, 5.2f, 5.25f, 5.2f, 5.15f, 5f};
    float[] hayY = {8f, 7.1f, 6.1f, 5.4f, 4.5f, 4.1f, 3.4f, 2.5f, 1.9f, 1.2f, 0.9f};

    public BarnScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        background = new Texture("Barn.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.game = game;
        player = new Player();
        player.player(100f);
        player.setRX(7);
        player.setRY(10);
        player.setTargetX(player.getRX());
        player.setTargetY(player.getRY());
        tiledMap = new TmxMapLoader().load("Barn.tmx");
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
        if (!super.userInterface.dialogFocus) {
            player.checkCollisions(tiledMap);
            player.playerTouch(batch);
            player.playerMovement();
        }

        batch.begin();
        haySpawn(game.gameData.getFeedInBarn());
        player.draw(batch);
        cowSpawn(game.gameData.getCowAmount());
        manureSpawn(game.gameData.getManureInBarn());
        black.draw(batch, blackness);
        batch.end();

        userInterface.render(game.gameData);

        if (getRec("RectangleExit") || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {    // condition return to farm
            game.setFarmScreen();
        }

        if (getRec("ActionFeedCows")) {    // condition feed cows
            game.gameData = actionFeedCows(game.gameData);
        }

        if (getRec("ActionShovelManure")) {    // condition shovel manure
            game.gameData = actionShovelManure(game.gameData);
        }

        if (getRec("ActionCollectMethane")) {    // condition collect methane
            game.gameData = actionCollectMethane(game.gameData);
        }
    }

    /**
     * Reduce data.feed by amount needed to feed all cows for 2 days.  If not enough in data.feed,
     * decrease to 0 and increase by same amount. Block if data.feed = 0, and if cows have enough.
     */
    public GameData actionFeedCows(GameData data) {
        super.userInterface.dialogFocus = true;
        ArrayList<Cow> cowList = data.getCowList();
        Cow cow = cowList.get(0);
        int addAmount = cowList.size() * cow.getFeed() * 2; // enough food for 2 days

        if (data.getFeedInBarn() > 1.5 * addAmount) {
            uiText = game.myBundle.get("feedInBarnFull");
            super.userInterface.dialogLabel.setText(uiText);
            // TODO action blocked, cows have enough food UI message
        } else {
            if (data.getFeed() == 0) {
                uiText = game.myBundle.get("feedInBarnNoFeed");
                super.userInterface.dialogLabel.setText(uiText);
                // TODO action blocked, feed storage empty UI message
            } else if (data.getFeed() < addAmount) {
                uiText = game.myBundle.get("feedInBarnCompleteButEmpty");
                super.userInterface.dialogLabel.setText(uiText);
                data.setFeedInBarn(data.getFeed());
                data.setFeed(0);
                // TODO cows fed but feed storage empty UI message
                data.setActionsDone(data.getActionsDone() + 1);
            } else {
                uiText = game.myBundle.get("feedInBarnComplete");
                super.userInterface.dialogLabel.setText(uiText);
                data.setFeedInBarn(data.getFeedInBarn() + addAmount);
                data.setFeed(-addAmount);
                // TODO cows fed UI message
                data.setActionsDone(data.getActionsDone() + 1);
            }
        }
        return data;
    }

    /**
     * Reduce manureInBarn and increase data.manure by same amount if less than data.manureMax
     */
    public GameData actionShovelManure(GameData data) {
        super.userInterface.dialogFocus = true;

        if (data.getManureInBarn() == 0) {
            // action blocked, no manure in barn UI message
            uiText = game.myBundle.get("shovelManureNoManure");
            super.userInterface.dialogLabel.setText(uiText);
        } else if (data.getManureInBarn() < data.MANURE_SHOVELED) {
            // barn is clean UI message
            uiText = game.myBundle.get("shovelManureComplete");
            super.userInterface.dialogLabel.setText(uiText);
            data.setManure(data.getManure() + data.getManureInBarn());
            data.setManureInBarn(0);
            data.setActionsDone(data.getActionsDone() + 1);
        } else if (data.getManureInBarn() > data.MANURE_SHOVELED) {
            // barn cleaned but still a bit dirty UI message
            uiText = game.myBundle.get("shovelManurePartial");
            super.userInterface.dialogLabel.setText(uiText);
            data.setManure(data.getManure() + data.MANURE_SHOVELED);
            data.setManureInBarn(data.getManureInBarn() - data.MANURE_SHOVELED);
            data.setActionsDone(data.getActionsDone() + 1);
        }

        if (data.getManure() > data.getManureMax()) {   // check if manure within limit
            // manure pit full UI message
            uiText = game.myBundle.get("checkManureMax");
            super.userInterface.dialogLabel.setText(uiText);
            data.setManure(data.getManureMax());
        }
        return data;
    }

    /**
     * Reduce methaneAmount to 0 in each of data.cowList if possible and increase data.methane by
     * same amount.
     */
    public GameData actionCollectMethane(GameData data) {
        ArrayList<Cow> tmpCowList = data.getCowList();
        int methaneCollected = 0;

        for (int i = 0; i < tmpCowList.size(); i++) {
            Cow cow = tmpCowList.get(i);
            methaneCollected += cow.getMethaneAmount();
            cow.setMethaneAmount(0);
            tmpCowList.set(i, cow);
        }

        data.setCowList(tmpCowList);
        data.setMethane(data.getMethane() + methaneCollected);
        // TODO methane collector(s) empty UI message
        data.setActionsDone(data.getActionsDone() + 1);

        if (data.getMethane() >= data.getMethaneMax()) {
            // TODO methane tank dangerously full UI message
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

    public boolean getRec(String name) {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get(name));
        boolean action = playerAction(r);
        if (player.getRectangle().overlaps(r)  && action == true) {
            return true;
        } else {
            return false;
        }
    }

    public void cowRender(Texture cow, Rectangle spawn) {
        spawn.width = cow.getWidth()/cowSize;
        spawn.height = cow.getHeight()/cowSize;
        batch.draw(cow, spawn.x, spawn.y, spawn.width, spawn.height);

    }

    public void cowSpawn(int cowCount) {
        spawn = new Rectangle(0.0f, 0.0f, cowSize, cowSize);
        spawn.x = 2.8f;
        spawn.y = 8.1f;
        if (cowCount == 6) {
            cowRender(cow1, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 5) {
            cowRender(cow2, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 4) {
            cowRender(cow3, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 3) {
            cowRender(cowBrown1, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 2) {
            cowRender(cowBrown2, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 1) {
            cowRender(cowBrown3, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
    }

    private void manureSpawn(int manureInBarn) {
        int counter = manureInBarn / (game.gameData.getManureInBarnMax() / 10); // 0..10
        for (int i = 0; i < counter; i++) {
            batch.draw(manure, manureX[i], manureY[i], 0.8f, 0.4f);
        }
    }

    private void haySpawn(int feedInBarn) {

        int counter = feedInBarn / 54;

        if (counter == 0 && feedInBarn > 0) {   // ensure feed is visible even if it's very low
            counter = 1;
        }
        for (int i = 0; i < counter; i++) {
            batch.draw(hay, hayX[i], hayY[i] + 0.4f, 1f, 0.5f);
        }
    }
}


