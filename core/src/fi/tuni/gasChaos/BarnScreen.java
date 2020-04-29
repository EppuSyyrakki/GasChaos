package fi.tuni.gasChaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

@SuppressWarnings({"CanBeFinal", "unchecked", "rawtypes", "RedundantCast"})
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
    final Sound cow1S = Gdx.audio.newSound(Gdx.files.internal("audio/cow1.mp3"));
    final Sound cow2S = Gdx.audio.newSound(Gdx.files.internal("audio/cow2.mp3"));
    final Sound cow3S = Gdx.audio.newSound(Gdx.files.internal("audio/cow3.mp3"));
    final Texture milkingMachine;
    Rectangle spawn;
    final float cowSize = 150f;
    float[] manureX = new float[11];
    float[] manureY = new float[11];
    float[] hayX = new float[11];
    float[] hayY = new float[11];
    float timer = 0;
    int timerReset = 10;
    int cowSound = random.nextInt(3);
    float cowVolume = 0.1f;
    int cowRows = 1;
    int cowCols = 2;
    float stateTime = 1.0f;
    Animation<TextureRegion> C1Animation;
    Animation<TextureRegion> C2Animation;
    Animation<TextureRegion> C3Animation;
    Animation<TextureRegion> CB1Animation;
    Animation<TextureRegion> CB2Animation;
    Animation<TextureRegion> CB3Animation;
    TextureRegion C1CurrentFrame;
    TextureRegion C2CurrentFrame;
    TextureRegion C3CurrentFrame;
    TextureRegion CB1CurrentFrame;
    TextureRegion CB2CurrentFrame;
    TextureRegion CB3CurrentFrame;

    public BarnScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);
        background = new Texture("ground/barnForeground.png");
        milkingMachine = new Texture("ground/milkingMachine.png");
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

        // Create texture regions for cows.
        TextureRegion[][] tmpC1 = TextureRegion.split(
                cow1,
                cow1.getWidth() / cowCols,
                cow1.getHeight() / cowRows);
        TextureRegion[][] tmpC2 = TextureRegion.split(
                cow2,
                cow2.getWidth() / cowCols,
                cow2.getHeight() / cowRows);
        TextureRegion[][] tmpC3 = TextureRegion.split(
                cow3,
                cow3.getWidth() / cowCols,
                cow3.getHeight() / cowRows);
        TextureRegion[][] tmpCB1 = TextureRegion.split(
                cowBrown1,
                cowBrown1.getWidth() / cowCols,
                cowBrown1.getHeight() / cowRows);
        TextureRegion[][] tmpCB2 = TextureRegion.split(
                cowBrown2,
                cowBrown2.getWidth() / cowCols,
                cowBrown2.getHeight() / cowRows);
        TextureRegion[][] tmpCB3 = TextureRegion.split(
                cowBrown3,
                cowBrown3.getWidth() / cowCols,
                cowBrown3.getHeight() / cowRows);

        TextureRegion[] C1 = transformTo1D(tmpC1, cowRows, cowCols);
        TextureRegion[] C2 = transformTo1D(tmpC2, cowRows, cowCols);
        TextureRegion[] C3 = transformTo1D(tmpC3, cowRows, cowCols);
        TextureRegion[] CB1 = transformTo1D(tmpCB1, cowRows, cowCols);
        TextureRegion[] CB2 = transformTo1D(tmpCB2, cowRows, cowCols);
        TextureRegion[] CB3 = transformTo1D(tmpCB3, cowRows, cowCols);

        C1Animation = new Animation(16 / 60f, (Object[]) C1);
        C2Animation = new Animation(25 / 60f, (Object[]) C2);
        C3Animation = new Animation(20 / 60f, (Object[]) C3);
        CB1Animation = new Animation(22 / 60f, (Object[]) CB1);
        CB2Animation = new Animation(31 / 60f, (Object[]) CB2);
        CB3Animation = new Animation(18 / 60f, (Object[]) CB3);
    }

    @Override
    public void render(float delta) {

        stateTime += Gdx.graphics.getDeltaTime();
        C1CurrentFrame = C1Animation.getKeyFrame(stateTime, true);
        C2CurrentFrame = C2Animation.getKeyFrame(stateTime, true);
        C3CurrentFrame = C3Animation.getKeyFrame(stateTime, true);
        CB1CurrentFrame = CB1Animation.getKeyFrame(stateTime, true);
        CB2CurrentFrame = CB2Animation.getKeyFrame(stateTime, true);
        CB3CurrentFrame = CB3Animation.getKeyFrame(stateTime, true);

        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        if (game.gameData.audio) {
            cowVolume = 0.1f;
        } else {
            cowVolume = 0f;
        }


        timer = timer + Gdx.graphics.getDeltaTime();
        if (timer > timerReset) {
            timer = 0;
            if (cowSound == 0) {
                cow1S.play(cowVolume);
            } else if (cowSound == 1) {
                cow2S.play(cowVolume);
            } else if (cowSound == 2) {
                cow3S.play(cowVolume);
            }
            timerReset = random.nextInt(7) + 10;
            int lastCowSound = cowSound;
            while (lastCowSound == cowSound) {
                cowSound = random.nextInt(3);
            }
        }

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
        if (game.gameData.getMilkingMachineLevel() == 2) {
            batch.draw(milkingMachine, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        }
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

    @SuppressWarnings("SameParameterValue")
    private TextureRegion[] transformTo1D(TextureRegion[][] tmp, int rows, int cols) {
        TextureRegion [] walkFrames
                = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        return walkFrames;
    }

    public void checkActionRectangles() {
        if ((getRec("RectangleExit") || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
                && !userInterface.dialogFocus) {
            game.setFarmScreen();
            game.farmScreen.player.match();
        } else if (getRec("ActionFeedCows") && !userInterface.dialogFocus
                && game.gameData.isActionsAvailable()) {
            player.setInputActive(false);
            int maxFeed = game.gameData.getCowList().get(0).getFeed() * 3 * game.gameData.MAX_COWS;
            uiText = game.myBundle.format("askFeedCows",
                    game.gameData.getGrainInBarn(),
                    maxFeed, game.gameData.getCowConsumption());
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
                    game.gameData.MANURE_DANGER * game.gameData.getCowList().size());
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
        int maxAmount = cowList.size() * cowList.get(0).getFeed() * 3
                * game.gameData.MAX_COWS;  // max amount of food

        if (game.gameData.getGrainInBarn() >= maxAmount) {
            // Action blocked, enough food already UI message
            uiText = game.myBundle.get("feedCowsFull");
            Dialog d = new Dialog(myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    resetInputProcessor();
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else {
            if (game.gameData.getGrain() == 0) {
                // action blocked, feed storage empty UI message
                uiText = game.myBundle.get("feedCowsNoFeed");
                Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                    protected void result(Object object) {
                        resetInputProcessor();
                        remove();
                    }
                };
                userInterface.createDialog(d, uiText, false);
            } else if (game.gameData.getGrain() <= addAmount) {
                // cows partially fed but feed storage empty
                addAmount = game.gameData.getGrain();
                game.gameData.setGrainInBarn(game.gameData.getGrainInBarn() + addAmount);
                game.gameData.setGrain(0);
                game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
                uiText = game.myBundle.get("feedCowsCompleteButEmpty");
                Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                    protected void result(Object object) {
                        resetInputProcessor();
                        remove();
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
                        resetInputProcessor();
                        remove();
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
        int removeAmount = game.gameData.MANURE_SHOVELED * game.gameData.getCowList().size();

        if (game.gameData.getManureInBarn() == 0) {
            // action blocked, no manure in barn
            uiText = game.myBundle.get("shovelManureNoManure");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    resetInputProcessor();
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getManureInBarn() <= removeAmount) {
            // barn is clean after shoveling
            game.gameData.setManure(game.gameData.getManure() + game.gameData.getManureInBarn());
            game.gameData.setManureInBarn(0);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            uiText = game.myBundle.get("shovelManureComplete");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    resetInputProcessor();
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, false);
        } else if (game.gameData.getManureInBarn() > removeAmount) {
            // barn cleaned but still a bit dirty
            game.gameData.setManure(game.gameData.getManure() + removeAmount);
            game.gameData.setManureInBarn(game.gameData.getManureInBarn() - removeAmount);
            game.gameData.setActionsDone(game.gameData.getActionsDone() + 1);
            uiText = game.myBundle.get("shovelManurePartial");
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    resetInputProcessor();
                    remove();
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
                    resetInputProcessor();
                    remove();
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
        uiText = game.myBundle.get("tutorialBarn") + "\n";
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    game.gameData.setBarnVisited(true);
                } else {
                    game.gameData.setAllVisited(true);
                }
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
        cow1S.dispose();
        cow2S.dispose();
        cow3S.dispose();
        milkingMachine.dispose();
    }

    @SuppressWarnings("RedundantCast")
    public boolean getRec(String name) {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get(name));
        boolean action = playerAction(r);
        return player.getRectangle().overlaps(r) && action;
    }

    public void cowRender(TextureRegion cow, Rectangle spawn) {
        spawn.width = cow1.getWidth()/cowSize / cowCols;
        spawn.height = cow1.getHeight()/cowSize / cowRows;
        batch.draw(cow, spawn.x, spawn.y, spawn.width, spawn.height);

    }

    public void cowSpawn(int cowCount) {
        //Can be uncommented to render every cow.
        //cowCount = 6;
        spawn = new Rectangle(0.0f, 0.0f, cowSize, cowSize);
        spawn.x = 2.2f;
        spawn.y = 8.1f;
        if (cowCount == 6) {
            cowRender(C3CurrentFrame, spawn);

            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 5) {
            cowRender(CB3CurrentFrame, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 4) {
            cowRender(C1CurrentFrame, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 3) {
            cowRender(CB2CurrentFrame, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 2) {
            cowRender(CB1CurrentFrame, spawn);
            spawn.y = spawn.y -1.5f;
            cowCount--;
        }
        if (cowCount == 1) {
            cowRender(C2CurrentFrame, spawn);
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


