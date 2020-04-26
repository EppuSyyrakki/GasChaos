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

import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class BarnScreen extends Location implements Screen {
    Player player;

    final Texture cow1 = new Texture("cows/cow1.png");
    final Texture cow2 = new Texture("cows/cow2.png");
    final Texture cow3 = new Texture("cows/cow3.png");
    final Texture cowBrown1 = new Texture("cows/cowBrown1.png");
    final Texture cowBrown2 = new Texture("cows/cowBrown2.png");
    final Texture cowBrown3 = new Texture("cows/cowBrown3.png");
    final Texture hay1 = new Texture("props/hay1.png");
    final Texture hay2 = new Texture("props/hay2.png");
    final Texture manure = new Texture("props/manure.png");
    Rectangle spawn;
    final float cowSize = 150f;
    float[] manureX = new float[11];
    float[] manureY = new float[11];
    float[] hayX = new float[11];
    float[] hayY = new float[11];

    public BarnScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);
        background = new Texture("ground/barnForeground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        player = new Player();
        player.player(100f);
        player.setRX(5);
        player.setRY(10);
        player.setTargetX(player.getRX());
        player.setTargetY(player.getRY());
        tiledMap = new TmxMapLoader().load("maps/Barn.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
        resetInputProcessor();
        for (int i = 0; i < manureX.length; i++) {
            manureX[i] = 0.4f + (float)Math.random();
            manureY[i] = 7f - ((float)i * 0.8f) + (float)Math.random();
        }
        for (int i = 0; i < hayX.length; i++) {
            hayX[i] = (float)Math.random() * 0.5f + 3.8f;
            hayY[i] = 6.4f - ((float)i * 0.8f) + (float)Math.random();
        }
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

        // Player movement
        if (!userInterface.dialogFocus) {
            player.playerTouch(batch);
            player.playerMovement(tiledMap);
        }

        batch.begin();
        haySpawn(game.gameData.getGrainInBarn());
        player.draw(batch);
        batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        manureSpawn(game.gameData.getManureInBarn());
        cowSpawn(game.gameData.getCowAmount());
        black.draw(batch, blackness);
        batch.end();

        if (!game.gameData.isBarnVisited() && !userInterface.dialogFocus) {
            barnTutorial();
        }

        userInterface.render(game.gameData);
        checkActionRectangles();
    }

    public void checkActionRectangles() {
        if ((getRec("RectangleExit") || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
                && !userInterface.dialogFocus) {
            game.setFarmScreen();
            game.farmScreen.player.match();
        } else if (getRec("ActionFeedCows") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            player.setInputActive(false);
            int maxFeed = game.gameData.getCowList().size() *
                    game.gameData.getCowList().get(0).getFeed() * 3;
            uiText = game.myBundle.format("askFeedCows",
                    game.gameData.getGrainInBarn(),
                    maxFeed);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionFeedCows();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getRec("ActionShovelManure") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            player.setInputActive(false);
            uiText = game.myBundle.format("askShovelManure",
                    game.gameData.getManureInBarn(),
                    game.gameData.MANURE_DANGER);
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionShovelManure();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (getRec("ActionCollectMethane") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {    // condition collect methane
            player.setInputActive(false);
            uiText = game.myBundle.format("askCollectMethane",
                    game.gameData.getTotalMethaneInCows(),
                    game.gameData.getTotalMaxMethaneInCows());
            userInterface.dialogFocus = true;
            Dialog d = new Dialog(game.myBundle.get("preDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        actionCollectMethane();
                    } else {
                        resetInputProcessor();
                    }
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        } else if (!game.gameData.isActionsAvailable() && (
                getRec("ActionCollectMethane") ||
                getRec("ActionShovelManure") ||
                getRec("ActionFeedCows"))) {
            player.setInputActive(false);
            userInterface.dialogFocus = true;
            uiText = game.myBundle.get("askGoSleep");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        game.setNewTurn();
                        player.setRX(6);
                        player.setRY(10);
                        player.match();
                    }
                    resetInputProcessor();
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, true);
        }
    }

    /**
     * Reduce data.feed by amount needed to feed all cows for 2 days.  If not enough in data.feed,
     * decrease to 0 and increase by same amount. Block if data.feed = 0, and if cows have enough.
     */
    public void actionFeedCows() {
        ArrayList<Cow> cowList = game.gameData.getCowList();
        int addAmount = cowList.size() * cowList.get(0).getFeed() * 2;  // enough food for 2 days
        int maxAmount = cowList.size() * cowList.get(0).getFeed() * 3;  // max amount of food

        if (game.gameData.getGrainInBarn() > maxAmount) {
            // Action blocked, enough food already UI message
            uiText = game.myBundle.get("feedCowsFull");
            Dialog d = new Dialog(myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            if (game.gameData.getGrain() == 0) {
                // action blocked, feed storage empty UI message
                uiText = game.myBundle.get("feedCowsNoFeed");
                Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                    protected void result(Object object) {
                        boolean result = (boolean)object;
                        if (result) {
                            resetInputProcessor();
                            remove();
                        }
                    }
                };
                userInterface.createDialog(d, uiText, false);
            } else if (game.gameData.getGrain() <= addAmount) {
                // cows fed but feed storage empty
                addAmount = game.gameData.getGrain();
                game.gameData.setGrainInBarn(game.gameData.getGrainInBarn() + addAmount);
                game.gameData.setGrain(0);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                uiText = game.myBundle.get("feedCowsCompleteButEmpty");
                Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                    protected void result(Object object) {
                        boolean result = (boolean)object;
                        if (result) {
                            resetInputProcessor();
                            remove();
                        }
                    }
                };
                userInterface.createDialog(d, uiText, false);
            } else if (game.gameData.getGrain() >= addAmount){
                // cows fed UI message
                game.gameData.setGrainInBarn(game.gameData.getGrainInBarn() + addAmount);
                game.gameData.setGrain(game.gameData.getGrain()-addAmount);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                uiText = game.myBundle.format("feedCowsComplete", game.gameData.getGrain());
                Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                    protected void result(Object object) {
                        boolean result = (boolean)object;
                        if (result) {
                            resetInputProcessor();
                            remove();
                        }
                    }
                };
                userInterface.createDialog(d, uiText, false);
            }
        }
    }

    /**
     * Reduce manureInBarn and increase data.manure by same amount if less than data.manureMax
     */
    public void actionShovelManure() {
        if (game.gameData.getManureInBarn() == 0) {
            // action blocked, no manure in barn
            uiText = game.myBundle.get("shovelManureNoManure");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getManureInBarn() <= game.gameData.MANURE_SHOVELED) {
            // barn is clean after shoveling
            game.gameData.setManure(game.gameData.getManure() + game.gameData.getManureInBarn());
            game.gameData.setManureInBarn(0);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            uiText = game.myBundle.get("shovelManureComplete");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getManureInBarn() > game.gameData.MANURE_SHOVELED) {
            // barn cleaned but still a bit dirty
            game.gameData.setManure(game.gameData.getManure() + game.gameData.MANURE_SHOVELED);
            game.gameData.setManureInBarn(game.gameData.getManureInBarn()
                    - game.gameData.MANURE_SHOVELED);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            uiText = game.myBundle.get("shovelManurePartial");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }

        if (game.gameData.getManure() > game.gameData.getManureMax()) {
            // manure pit full
            game.gameData.setManure(game.gameData.getManureMax());
            uiText = game.myBundle.get("checkManureMax");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean)object;
                    if (result) {
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    /**
     * Reduce methaneAmount to 0 in each of data.cowList if possible and increase data.methane by
     * same amount.
     */
    public void actionCollectMethane() {
        ArrayList<Cow> tmpCowList = game.gameData.getCowList();
        int methaneCollected = 0;

        for (int i = 0; i < tmpCowList.size(); i++) {
            Cow cow = tmpCowList.get(i);
            methaneCollected += cow.getMethaneAmount();
            cow.setMethaneAmount(0);
            tmpCowList.set(i, cow);
        }

        if (methaneCollected > 0) {
            game.gameData.setCowList(tmpCowList);
            game.gameData.setMethane(game.gameData.getMethane() + methaneCollected);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            // methane collector(s) empty UI message
            uiText = game.myBundle.get("getMethaneEmpty");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        userInterface.dialogFocus = false;
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            // no methane to collect UI message
            uiText = game.myBundle.get("noMethaneToCollect");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        userInterface.dialogFocus = false;
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }

        if (game.gameData.getMethane() >= game.gameData.getMethaneMax()) {
            // methane tank dangerously full UI message
            uiText = game.myBundle.get("getMethaneMax");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        userInterface.dialogFocus = false;
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    private void barnTutorial() {
        userInterface.dialogFocus = true;
        // uiText = game.myBundle.get("tutorialBarn"); TODO add text to myBundle
        uiText = "barn tutorial";
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    game.gameData.setBarnVisited(true);
                    userInterface.dialogFocus = false;
                    resetInputProcessor();
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
        cow1.dispose();
        cow2.dispose();
        cow3.dispose();
        cowBrown1.dispose();
        cowBrown2.dispose();
        cowBrown3.dispose();
        hay1.dispose();
        hay2.dispose();
        manure.dispose();
        tiledMap.dispose();
        batch.dispose();
        player.dispose();
    }

    @SuppressWarnings("RedundantCast")
    public boolean getRec(String name) {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get(name));
        boolean action = playerAction(r);
        return player.getRectangle().overlaps(r) && action;
    }

    public void cowRender(Texture cow, Rectangle spawn) {
        spawn.width = cow.getWidth()/cowSize;
        spawn.height = cow.getHeight()/cowSize;
        batch.draw(cow, spawn.x, spawn.y, spawn.width, spawn.height);

    }

    public void cowSpawn(int cowCount) {
        spawn = new Rectangle(0.0f, 0.0f, cowSize, cowSize);
        spawn.x = 2.2f;
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
            //noinspection UnusedAssignment
            cowCount--;
        }
    }

    private void manureSpawn(int manureInBarn) {
        int counter = manureInBarn / (game.gameData.getManureInBarnMax() / 10); // 0..10
        for (int i = 0; i < counter; i++) {
            batch.draw(manure, manureX[i], manureY[i], 2.0f, 1.0f);
        }
    }

    private void haySpawn(int feedInBarn) {
        int counter = feedInBarn / 54;

        if (counter == 0 && feedInBarn > 0) {   // ensure feed is visible even if it's very low
            counter = 1;
        }
        for (int i = 0; i < counter; i++) {
            if (i % 2 == 0) {
                batch.draw(hay1, hayX[i], hayY[i] + 0.4f, 3f, 1.5f);
            } else {
                batch.draw(hay2, hayX[i], hayY[i] + 0.4f, 3f, 1.5f);
            }
        }
    }

    private void resetInputProcessor() {
        userInterface.dialogFocus = false;
        player.setInputActive(true);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }
}


