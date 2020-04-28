package fi.tuni.gasChaos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * MainClass is just for demo purposes in this project.
 */
public class HighScore extends ApplicationAdapter implements HighScoreListener, Screen {
    final OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    final GasChaosMain game;
    final Label.LabelStyle labelStyle;
    final TextButton.TextButtonStyle buttonStyle;
    TextField.TextFieldStyle textFieldStyle;
    final UserInterface userInterface;
    private TextField nameField;
    final Texture textCursor;
    final Texture background;

    private Table content;

    public HighScore(OrthographicCamera camera, GasChaosMain game) {
        this.game = game;
        camera.setToOrtho(false, 9, 16);
        this.camera = camera;
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(false);
        HighScoreServer.fetchHighScores(this);
        userInterface = new UserInterface(game.myBundle);
        background = new Texture("ui/highScore.png");
        textCursor = new Texture("props/cursor.png");
        NinePatch patchUp = new NinePatch(new Texture(Gdx.files.internal("props/buttonUp.png")),
                5, 5, 5, 5);
        NinePatch patchDown = new NinePatch(new Texture(Gdx.files.internal("props/buttonDown.png")),
                5, 5, 5, 5);

        labelStyle = new Label.LabelStyle(userInterface.font, Color.WHITE);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = userInterface.font;
        buttonStyle.up = new NinePatchDrawable(patchUp);
        buttonStyle.down = new NinePatchDrawable(patchDown);


        otherSetup();
    }

    @Override
    public void create () {
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(false);
        HighScoreServer.fetchHighScores(this);

        otherSetup();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().setProjectionMatrix(camera.combined);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, 9f, 16f);
        stage.getBatch().end();

        stage.draw();
    }

    @Override
    public void hide() {

    }

    @Override
    public void receiveHighScore(List<HighScoreEntry> highScores) {
        //Gdx.app.log("HighScore", "Received new high scores successfully");
        updateScores(highScores);
    }

    @Override
    public void receiveSendReply(Net.HttpResponse httpResponse) {
        //Gdx.app.log("HighScore", "Received response from server: " + httpResponse.getStatus().getStatusCode());
        HighScoreServer.fetchHighScores(this);
    }

    @Override
    public void failedToRetrieveHighScores(Throwable t) {
        //Gdx.app.error("HighScore", "Something went wrong while getting high scores", t);
    }

    @Override
    public void failedToSendHighScore(Throwable t) {
        //Gdx.app.error("HighScore", "Something went wrong while sending a high scoreField entry", t);
    }

    private void otherSetup() {
        skin = new Skin();
        skin = new Skin (Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage();
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = userInterface.font;
        textFieldStyle.focusedFontColor = Color.WHITE;
        NinePatch patchText = new NinePatch
                (new Texture(Gdx.files.internal("props/textFieldBackground.png")),
                1, 1, 1, 1);
        textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(textCursor));
        textFieldStyle.background = new NinePatchDrawable(patchText);
        content = new Table();
        createTable();
        stage.addActor(content);
    }

    private ArrayList<Label> scoreLabels;

    private void updateScores(List<HighScoreEntry> scores) {
        int i = 0;
        for (HighScoreEntry e : scores) {
            String entry = e.getScore() + " - " + e.getName();
            scoreLabels.get(i).setText(entry);
            i++;
        }
    }

    private void createTable() {
        content.setFillParent(true);
        content.add(new Label((game.myBundle.format("highScoreTitle")), labelStyle)).colspan(2);

        scoreLabels = new ArrayList<>();

        for (int n = 0; n < 10; n++) {
            content.row();
            Label l = new Label("", labelStyle);
            content.add(l).colspan(2);
            scoreLabels.add(l);
        }

        TextButton fetch = new TextButton((game.myBundle.format("fetchScore")), buttonStyle);
        fetch.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fetchHighScores();
            }
        });

        TextButton newHighScore = new TextButton((game.myBundle.format("addScore")),
                buttonStyle);
        newHighScore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createNewScore();
                exitScore();
            }
        });

        content.row();
        content.add(fetch).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(new Label(game.myBundle.format("paidDebt",
                scoreCount()), labelStyle)).colspan(2);
        content.row();
        content.add(new Label((game.myBundle.format("submitScoreText")), labelStyle)).colspan(2);
        content.row();
        nameField = new TextField("", skin);
        nameField.setWidth(800);
        nameField.setStyle(textFieldStyle);
        content.add(nameField).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(newHighScore).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(new Label((game.myBundle.format("keepPlaying")), labelStyle)).colspan(2);
        content.row();
        // Literally just the fastest way to pad the content up
        // to prevent the text box from rendering under the phone keyboard.
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
    }

    private void fetchHighScores() {
        HighScoreServer.fetchHighScores(this);
    }

    private int scoreCount() {
        int score = game.gameData.getMoney();

        // Add value of bought upgrades to score.
        if (game.gameData.getTractorLevel() == 2) {
            score = score + game.gameData.PRICE_OF_TRACTOR;
        } else if (game.gameData.getTractorLevel() == 3) {
            score = score + game.gameData.PRICE_OF_TRACTOR * 2;
        }

        if (game.gameData.getGasGeneratorLevel() == 1) {
            score = score + game.gameData.PRICE_OF_GENERATOR;
        }

        if (game.gameData.getMilkingMachineLevel() == 2) {
            score = score + game.gameData.PRICE_OF_MILKING;
        }

        if (game.gameData.getSolarPanelLevel() == 1) {
            score = score + game.gameData.PRICE_OF_SOLAR;
        } else if (game.gameData.getSolarPanelLevel() == 2) {
            score = score + game.gameData.PRICE_OF_SOLAR * 2;
        }

        if (game.gameData.getGasCollectorLevel() == 2) {
            score = score + game.gameData.PRICE_OF_COLLECTOR;
        }

        score = score + (game.gameData.PRICE_OF_COW * game.gameData.getCowAmount());

        return score;
    }

    private void createNewScore() {
        String name = nameField.getText();
        int score = scoreCount();

        HighScoreEntry scoreEntry = new HighScoreEntry(name, score);
        HighScoreServer.sendNewHighScore(scoreEntry, this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose () {
        background.dispose();
        skin.dispose();
        stage.dispose();
        userInterface.dispose();
        textCursor.dispose();
    }

    public void exitScore() {
        game.setHomeScreen();
        dispose();
    }
}