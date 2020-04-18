package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * MainClass is just for demo purposes in this project.
 */
public class HighScore extends ApplicationAdapter implements HighScoreListener, Screen {
    private Stage stage;
    private Skin skin;
    final GasChaosMain game;

    private Table content;

    public HighScore(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        System.out.println("ree");
        this.game = game;
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(true);
        HighScoreServer.fetchHighScores(this);

        otherSetup();
    }

    @Override
    public void create () {
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(true);
        HighScoreServer.fetchHighScores(this);

        otherSetup();
    }

    public void HighScore (SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(true);
        HighScoreServer.fetchHighScores(this);

        otherSetup();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

    }

    @Override
    public void hide() {

    }

    @Override
    public void receiveHighScore(List<HighScoreEntry> highScores) {
        Gdx.app.log("HighScore", "Received new high scores successfully");
        updateScores(highScores);
    }

    @Override
    public void receiveSendReply(Net.HttpResponse httpResponse) {
        Gdx.app.log("HighScore", "Received response from server: "
                + httpResponse.getStatus().getStatusCode());
        HighScoreServer.fetchHighScores(this);
    }

    @Override
    public void failedToRetrieveHighScores(Throwable t) {
        Gdx.app.error("HighScore",
                "Something went wrong while getting high scores", t);
    }

    @Override
    public void failedToSendHighScore(Throwable t) {
        Gdx.app.error("HighScore",
                "Something went wrong while sending a high scoreField entry", t);
    }

    private void otherSetup() {
        skin = new Skin();
        skin = new Skin (Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        content = new Table();
        createTable();
        stage.addActor(content);
        System.out.println("test1");
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

    private TextField nameField;
    private TextField scoreField;

    private void createTable() {
        content.setFillParent(true);
        content.add(new Label((game.myBundle.format("highScoreTitle")), skin)).colspan(2);

        scoreLabels = new ArrayList<>();


        for (int n = 0; n < 10; n++) {
            content.row();
            Label l = new Label("", skin);
            content.add(l).colspan(2);
            scoreLabels.add(l);
        }

        TextButton fetch = new TextButton((game.myBundle.format("fetchScore")), skin);
        fetch.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fetchHighScores();
            }
        });

        TextButton newHighScore = new TextButton((game.myBundle.format("addScore")), skin);
        newHighScore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createNewScore();
            }
        });

        content.row();
        content.add(fetch).colspan(2);
        content.row();
        content.add(new Label("", skin)).colspan(2);
        content.row();
        content.add(new Label(game.myBundle.format("paidDebt",
                game.gameData.getCurrentTurn()), skin)).colspan(2);
        content.row();
        content.add(new Label((game.myBundle.format("submitScoreText")), skin)).colspan(2);
        content.row();

        nameField = new TextField("", skin);

        content.add(nameField).colspan(2);

        content.row();
        content.add(new Label("", skin)).colspan(2);
        content.row();
        content.add(newHighScore).colspan(2);
    }

    private void fetchHighScores() {
        HighScoreServer.fetchHighScores(this);
    }

    private void createNewScore() {
        String name = nameField.getText();
        int score = game.gameData.getCurrentTurn();
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
        skin.dispose();
    }

    public void exit () {
        game.setHomeScreen();
    }
}