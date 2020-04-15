package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

@SuppressWarnings("RedundantCast")
public class HomeScreen extends Location implements Screen {
    @SuppressWarnings("CanBeFinal")
    Player player;
    private final GasChaosMain game;

    public HomeScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        background = new Texture("homeForeground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.game = game;
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        player = new Player();
        player.player(60f);
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
            player.setInputActive(false);
        }

        if (!fadeIn) {
            player.setInputActive(true);
        }

        // Player movement
        //player.checkCollisions(tiledMap);
        player.playerTouch(batch);
        player.playerMovement(tiledMap);

        batch.begin();
        player.draw(batch);
        batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        black.draw(batch, blackness);
        batch.end();

        userInterface.render(game.gameData);

        if (getRec("RectangleExit") || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {    // condition return to farm
            game.setFarmScreen();
        }

        if (getRec("RectanglePhone")) {    // condition call to grandmother
            game.setGrandmotherScreen();
        }

        if (getRec("RectanglePC")) {    // condition go to computer
            game.setComputerScreen();
        }

        if (getRec("RectangleBed")) {  // condition end turn
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

    @SuppressWarnings("RedundantCast")
    public boolean getRec(String name) {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get(name));
        boolean action = playerAction(r);
        return player.getRectangle().overlaps(r) && action;
    }
}
