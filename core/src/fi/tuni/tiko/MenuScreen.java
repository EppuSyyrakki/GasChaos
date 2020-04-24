package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;

public class MenuScreen extends ApplicationAdapter implements Screen {
    final GasChaosMain game;
    final Texture background;
    private Stage stage;
    private Table content;
    OrthographicCamera camera;
    UserInterface userInterface;
    Skin skin;
    Label.LabelStyle labelStyle;
    TextButton.TextButtonStyle buttonStyle;

    public MenuScreen(OrthographicCamera camera, GasChaosMain game) {
        this.game = game;
        camera.setToOrtho(false, 9, 16);
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);

        NinePatch patchUp = new NinePatch(new Texture(Gdx.files.internal("props/buttonUp.png")),
                5, 5, 5, 5);
        NinePatch patchDown = new NinePatch(new Texture(Gdx.files.internal("props/buttonDown.png")),
                5, 5, 5, 5);

        background = new Texture("ui/menu.png");
        labelStyle = new Label.LabelStyle(userInterface.font, Color.WHITE);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = userInterface.font;
        buttonStyle.up = new NinePatchDrawable(patchUp);
        buttonStyle.down = new NinePatchDrawable(patchDown);

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
        // Enable if score background gets created
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, 9f, 16f);
        stage.getBatch().end();

        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.quitGame();
        }
    }

    private void otherSetup() {
        skin = new Skin();
        skin = new Skin (Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage();
        content = new Table();
        createTable();
        stage.addActor(content);
    }

    private void createTable() {
        content.setFillParent(true);

        TextButton newGame = new TextButton((game.myBundle.format("newGame")), buttonStyle);
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameData.menu) {
                    game.setFarmScreen();
                    game.gameData.menu = false;
                    //game.reset();
                }
            }
        });

        TextButton loadGame = new TextButton((game.myBundle.format("loadGame")),
                buttonStyle);
        loadGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameData.menu) {
                    game.gameData.loadGame();
                    game.setFarmScreen();
                    game.gameData.menu = false;
                }
            }
        });

        TextButton quitGame = new TextButton((game.myBundle.format("quitGame")), buttonStyle);
        quitGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameData.menu) {
                    game.quitGame();
                }
            }
        });

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
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(newGame).colspan(2)
                .prefHeight(Gdx.graphics.getHeight() / 18)
                .prefWidth(Gdx.graphics.getWidth() / 3.5f);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(loadGame).colspan(2)
                .prefHeight(Gdx.graphics.getHeight() / 18)
                .prefWidth(Gdx.graphics.getWidth() / 3.5f);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(new Label("", labelStyle)).colspan(2);
        content.row();
        content.add(quitGame).colspan(2)
                .prefHeight(Gdx.graphics.getHeight() / 18)
                .prefWidth(Gdx.graphics.getWidth() / 3.5f);
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

    public void dispose() {
        skin.dispose();
        background.dispose();
        stage.dispose();
    }
}
