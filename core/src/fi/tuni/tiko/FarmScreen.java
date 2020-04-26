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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

public class FarmScreen extends Location implements Screen {
    @SuppressWarnings("CanBeFinal")
    Player player;
    final Texture backgroundSolar;
    final Texture backgroundSolar2;
    final Texture sunset;
    final Texture tractor1;
    final Texture tractor2;
    final Texture tractor3;
    final Texture sun;
    final Texture cloud1;
    float cloudX1;
    final float cloudSpeed;
    float sunX;
    float sunY;
    final float tractorX;
    final float tractorY;

    public FarmScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);

        // Check what level of solar panels and set foreground accordingly.
        backgroundSolar = new Texture("ground/farmForegroundSolar.png");
        backgroundSolar2 = new Texture("ground/farmForegroundSolar2.png");
        background = new Texture("ground/farmForeground.png");
        sunset = new Texture("ground/farmSunset.png");
        tractor1 = new Texture("props/tractor1.png");
        tractor2 = new Texture("props/tractor1.png");
        tractor3 = new Texture("props/tractor1.png");
        sun = new Texture("props/sun.png");
        cloud1 = new Texture("props/cloud1.png");
        tractorX = 0.75f;
        tractorY = 8.6f;
        cloudX1 = 1f;
        cloudSpeed = 0.08f;

        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        tiledMap = new TmxMapLoader().load("maps/Farm.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        player = new Player();
        player.player(150f);
        player.setRX(4.5f);
        player.setRY(6f);
        player.matchX(player.getRX());
        player.matchY(player.getRY());
        game.gameData.saveGame();
        game.gameData.loadGame();
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
            player.setFadeActive(false);
        } else {
            player.setFadeActive(true);
        }

        //Player movement
        if (!userInterface.dialogFocus) {
            player.playerTouch(batch);
            player.playerMovement(tiledMap);
        }

        sun();

        batch.begin();
        batch.draw(tractorTex(), tractorX,tractorY, 2f, 1.5f);
        player.draw(batch);
        batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        // Check what level of solar panels and set foreground accordingly.
        if (game.gameData.getSolarPanelLevel() == 1) {
            batch.draw(backgroundSolar, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        } else if (game.gameData.getSolarPanelLevel() == 2) {
            batch.draw(backgroundSolar2, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        }
        sunsetSky(sunset);
        batch.draw(sun, sunX,sunY, 0.65f, 0.65f);
        drawCloud();
        sunsetRender();
        black.draw(batch, blackness);
        batch.end();

        if (!game.gameData.isFarmVisited() && !userInterface.dialogFocus) {
            farmTutorial1();
        }

        userInterface.render(game.gameData);
        checkActionRectangles();
    }

    private void drawCloud() {
        cloudX1 = cloudX1 + cloudSpeed * Gdx.graphics.getDeltaTime();
        if (cloudX1 > 9.5f) {cloudX1 = -4.2f;}
        batch.draw(cloud1, cloudX1,13.83f, 37f / 9f, 16f / 9f);
    }

    public void checkActionRectangles() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setMenuScreen();
        } else if (getRec("RectangleHome")) {    // condition enter home
            game.setHomeScreen();
            game.homeScreen.player.match();
        } else if (getRec("RectangleBarn")) {    // condition enter barn
            game.setBarnScreen();
            game.barnScreen.player.match();
        } else if (getRec("RectangleGarden")) {    // condition enter garden
            game.setGardenScreen();
        } else if (getRec("RectangleField")) {    // condition enter fields
            game.setFieldScreen();
        } else if (getRec("RectangleGasTank")) {    // condition enter gasTank
            game.setGasTankScreen();
        }
    }

    private void farmTutorial1() {
        userInterface.dialogFocus = true;
        uiText = game.myBundle.get("G1");
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    userInterface.dialogFocus = false;
                    resetInputProcessor();
                    remove();
                    farmTutorial2();
                } else {
                    game.gameData.setAllVisited(true);
                    userInterface.dialogFocus = false;
                    resetInputProcessor();
                    remove();
                }

            }
        };
        userInterface.showTutorial(d, uiText);
    }

    private void farmTutorial2() {
        userInterface.dialogFocus = true;
        uiText = game.myBundle.get("G2");
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    userInterface.dialogFocus = false;
                    resetInputProcessor();
                    remove();
                    farmTutorial3();
                } else {
                    game.gameData.setAllVisited(true);
                    userInterface.dialogFocus = false;
                    resetInputProcessor();
                    remove();
                }

            }
        };
        userInterface.showTutorial(d, uiText);
    }

    private void farmTutorial3() {
        userInterface.dialogFocus = true;
        uiText = game.myBundle.get("G3");
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    game.gameData.setFarmVisited(true);
                } else {
                    game.gameData.setAllVisited(true);
                }
                userInterface.dialogFocus = false;
                resetInputProcessor();
                remove();
            }
        };
        userInterface.showTutorial(d, uiText);
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
        backgroundSolar.dispose();
        backgroundSolar2.dispose();
        blackTexture.dispose();
        sunsetTexture.dispose();
        sunset.dispose();
        userInterface.dispose();
        tiledMap.dispose();
        tractor1.dispose();
        tractor2.dispose();
        tractor3.dispose();
        sun.dispose();
        cloud1.dispose();
        player.dispose();
    }

    @SuppressWarnings("RedundantCast")
    public boolean getRec(String name) {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get(name));
        boolean action = playerAction(r);
        return player.getRectangle().overlaps(r) && action;
    }

    public void sun() {
        if (game.gameData.getActionsDone() == 0) {
            sunX = 0.7f;
            sunY = 13.9f;
        } else if (game.gameData.getActionsDone() == 1) {
            sunX = 3f;
            sunY = 14.1f;
        } else if (game.gameData.getActionsDone() == 2) {
            sunX = 5.5f;
            sunY = 14.2f;
        } else if (game.gameData.getActionsDone() == 3) {
            sunX = 7.7f;
            sunY = 13.1f;
        }
    }

    public Texture tractorTex() {
        if (game.gameData.getTractorLevel() == 1) {
            return tractor1;
        } else if (game.gameData.getTractorLevel() == 2) {
            return tractor2;
        } else if (game.gameData.getTractorLevel() == 3) {
            return tractor3;
        } else {
            return tractor1;
        }
    }

    private void resetInputProcessor() {
        userInterface.dialogFocus = false;
        player.setInputActive(true);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }
}



