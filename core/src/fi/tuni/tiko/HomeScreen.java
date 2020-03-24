package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HomeScreen extends Location implements Screen {
    Player player;

    public HomeScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game, Screen parent) {
        background = new Texture("homeBackground.png");
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

        if (false) {    // condition call to grandmother
            game.setScreen(new GrandmotherScreen(batch, camera, game, this));
        }

        if (false) {    // condition go to computer
            game.setScreen(new ComputerScreen(batch, camera, game, this));
        }

        if (game.gameData.getActionsDone() >= game.gameData.MAX_ACTIONS) {  // condition end turn
            // end turn
        }
    }

    /**
     * Enable tutorial menu
     */
    public void actionCallGrandMother() {

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
}
