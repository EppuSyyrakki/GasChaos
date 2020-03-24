package fi.tuni.tiko;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GardenScreen extends Location implements Screen {

    public GardenScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game, Screen parent) {
        background = new Texture("barnBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.parent = parent;
        this.game = game;
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

        if (false) {    // condition weed garden
            game.gameData = actionWeedGarden(game.gameData);
        }

        if (false) {    // condition sell garden
            game.gameData = actionSellGarden(game.gameData);
        }

        if (false) {    // condition plant garden
            game.gameData = actionPlantGarden(game.gameData);
        }
    }

    /**
     * Increase data.gardenAmount from 0 to 1.
     */
    public GameData actionPlantGarden(GameData data) {
        if (data.getGardenAmount() == 0) {
            data.setGardenAmount(1);
            data.setWeedsAmount(0);
            // TODO garden planted and weeded UI message
            data.setActionsDone(data.getActionsDone() + 1);
        } else {
            // TODO garden is already growing UI message
        }
        return data;
    }

    /**
     * Reduce data.weedsAmount to 0 and increase data.gardenGrowth to 5.
     */
    public GameData actionWeedGarden(GameData data) {
        if (data.getWeedsAmount() <= 0) {
            // TODO garden is already weeded UI message
        } else {
            data.setWeedsAmount(0);
            data.setGardenGrowth(5);
            // TODO garden has been weeded UI message
            data.setActionsDone(data.getActionsDone() + 1);
        }
        return data;
    }

    /**
     * Increase data.gardenSold by data.gardenAmount and reduce data. gardenAmount to 0.
     * Block if gardenAmount is not over 30 (garden is not ripe).
     */
    public GameData actionSellGarden(GameData data) {
        if (data.getGardenAmount() > 30) {
            data.setGardenSold(data.getGardenAmount());
            data.setGardenAmount(0);
            // TODO garden produce sold UI message
            data.setActionsDone(data.getActionsDone() + 1);
        } else {
            // TODO garden produce not ripe UI message
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