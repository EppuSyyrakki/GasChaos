package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class BarnScreen extends Location implements Screen {
    final float WORLD_WIDTH = 9f;
    final float WORLD_HEIGHT = 16f;
    Player player;

    public BarnScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game, Screen parent) {
        background = new Texture("barnBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.parent = parent;
        this.game = game;
        player = new Player();
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (fadeIn) {
            fadeFromBlack();
        }

        batch.begin();
        batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        black.draw(batch, blackness);
        batch.end();

        if (false) {    // condition return to farm
            game.setScreen(parent);
        }

        if (false) {    // condition feed cows
            game.gameData = actionFeedCows(game.gameData);
        }

        if (false) {    // condition shovel manure
            game.gameData = actionShovelManure(game.gameData);
        }

        if (false) {    // condition collect methane
            game.gameData = actionCollectMethane(game.gameData);
        }
    }

    /**
     * Reduce data.feed by amount needed to feed all cows for 2 days.  If not enough in data.feed,
     * decrease to 0 and increase by same amount. Block if data.feed = 0, and if cows have enough.
     */
    public GameData actionFeedCows(GameData data) {
        ArrayList<Cow> cowList = data.getCowList();
        Cow cow = cowList.get(0);
        int addAmount = cowList.size() * cow.getFeed() * 2; // enough food for 2 days

        if (data.getFeedInBarn() > 1.5 * addAmount) {
            // TODO action blocked, cows have enough food UI message
        } else {
            if (data.getFeed() == 0) {
                // TODO action blocked, feed storage empty UI message
            } else if (data.getFeed() < addAmount) {
                data.setFeedInBarn(data.getFeed());
                data.setFeed(0);
                // TODO feed storage empty UI message
                data.setActionsDone(data.getActionsDone() + 1);
            } else {
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
        if (data.getManureInBarn() == 0) {
            // TODO action blocked, no manure in barn UI message
        } else if (data.getManureInBarn() < data.MANURE_SHOVELED) {
            data.setManure(data.getManure() + data.getManureInBarn());
            data.setManureInBarn(0);
            // TODO barn is clean UI message
            data.setActionsDone(data.getActionsDone() + 1);
        } else if (data.getManureInBarn() > data.MANURE_SHOVELED) {
            data.setManure(data.getManure() + data.MANURE_SHOVELED);
            data.setManureInBarn(data.getManureInBarn() - data.MANURE_SHOVELED);
            // TODO barn cleaned but still a bit dirty UI message
            data.setActionsDone(data.getActionsDone() + 1);
        }

        if (data.getManure() > data.getManureMax()) {   // check if manure within limit
            data.setManure(data.getManureMax());
            // TODO manure pit full UI message
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
}
