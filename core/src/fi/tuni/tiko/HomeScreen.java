package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import java.util.ArrayList;

@SuppressWarnings("RedundantCast")
public class HomeScreen extends Location implements Screen {
    @SuppressWarnings({"CanBeFinal"})
    Player player;
    @SuppressWarnings("UnusedAssignment")
    boolean newTurn = false;
    final Sound roosterS = Gdx.audio.newSound(Gdx.files.internal("audio/rooster.mp3"));
    float roosterVolume = 0.1f;

    public HomeScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);
        background = new Texture("ground/homeForeground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        player = new Player();
        player.player(60f);
        player.setRX(4);
        player.setRY(4);
        player.setTargetX(player.getRX());
        player.setTargetY(player.getRY());
        newTurn = false;
        tiledMap = new TmxMapLoader().load("maps/Home.tmx");
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

        if (game.gameData.audio) {
            roosterVolume = 0.1f;
        } else {
            roosterVolume = 0f;
        }

        if (newTurn) {
            roosterS.play(roosterVolume);
            player.setRX(4);
            player.setRY(6);
            player.matchX(player.getRX());
            player.matchY(player.getRY());
            uiText = createMorningText();
            player.setInputActive(false);
            setActionInputActive(false);
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        resetInputProcessor();
                        newTurn = false;
                        remove();
                        setActionInputActive(true);
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }

        if (fadeIn) {
            fadeFromBlack();
            player.setFadeActive(false);
        } else {
            player.setFadeActive(true);
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

        if (!game.gameData.isHomeVisited() && !userInterface.dialogFocus) {
            homeTutorial();
        }

        userInterface.render(game.gameData);

        if (getRec("RectangleExit") || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {    // condition return to farm
            game.setFarmScreen();
            game.farmScreen.player.match();
        }

        if (getRec("RectanglePhone")) {    // condition call to grandmother
            game.setGrandmotherScreen();
        }

        if (getRec("RectanglePC")) {    // condition go to computer
            game.setComputerScreen();
        }

        if (getRec("RectangleBed") && !userInterface.dialogFocus) {  // condition end turn
            player.setInputActive(false);
            userInterface.dialogFocus = true;

            if (!game.gameData.isActionsAvailable()) {
                uiText = game.myBundle.get("askGoSleep");
            } else {
                uiText = game.myBundle.get("askGoSleepConfirm");
            }
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        game.setNewTurn();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        }
        newTurn = false;
    }

    private void homeTutorial() {
        userInterface.dialogFocus = true;
        uiText = game.myBundle.get("tutorialHome") + game.myBundle.get("tutorialHome2")+ "\n";
        player.setInputActive(false);
        setActionInputActive(false);
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    game.gameData.setHomeVisited(true);
                    userInterface.dialogFocus = false;
                    resetInputProcessor();
                    setActionInputActive(true);
                    remove();
                }
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
        player.dispose();
        tiledMap.dispose();
        userInterface.dispose();
        sunsetTexture.dispose();
        blackTexture.dispose();
        roosterS.dispose();
    }

    @SuppressWarnings("RedundantCast")
    public boolean getRec(String name) {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get(name));
        boolean action = playerAction(r);
        return player.getRectangle().overlaps(r) && action;
    }

    public void resetInputProcessor() {
        userInterface.dialogFocus = false;
        player.setInputActive(true);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    public void setNewTurn(boolean newTurn) {
        this.newTurn = newTurn;
    }

    private String createMorningText() {
        boolean fieldReapable = false;
        boolean fieldSowable = false;
        ArrayList<Field> tmpFields = game.gameData.getFields();
        String text = game.myBundle.get("newTurn") + "\n";

        for (Field field : tmpFields) {
            if (field.getAmount() == field.MAX_GROWTH) {
                fieldReapable = true;
            }
            if ((field.isRented() || field.isOwned()) && field.getAmount() == 0) {
                fieldSowable = true;
            }
        }

        if (game.gameData.getMoney() < 0) {
            text += "+ " + game.myBundle.get("outOfMoney") + "\n";
        }

        if (game.gameData.getGrainInBarn() <= 0) {
            text += "+ " + game.myBundle.get("cowsStarving") + "\n";
        }

        if (game.gameData.getManureInBarn() >= game.gameData.MANURE_DANGER) {
            text += "+ " + game.myBundle.get("overrunByShit") + "\n";
        }

        if (game.gameData.isFieldPenalty()) {
            text += "+ " + game.myBundle.get("overFertilize") + "\n";
        }

        if (game.gameData.getGardenGrowth() == 1) {
            text += "+ " + game.myBundle.get("needWeed") + "\n";
        } else if (game.gameData.getGardenAmount() == game.gameData.getGardenMax()) {
            text += "+ " + game.myBundle.get("gardenReady") + "\n";
        }

        if (fieldReapable) {
            text += "+ " + game.myBundle.get("fieldReadyToReap") + "\n";
        }

        if (fieldSowable) {
            text += "+ " + game.myBundle.get("fieldReadyToSow") + "\n";
        }
        return text;
    }
}
