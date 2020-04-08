package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;

public class UserInterface {
    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();
    boolean dialogFocus;
    Stage stage = new Stage();
    Texture fontTexture = new Texture(Gdx.files.internal("ui/alternative.png"));
    BitmapFont font;
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    Table topTable = new Table();
    Label topMoneyLabel;
    Label topCowsLabel;
    Label topActionsLabel;
    I18NBundle myBundle;

    public UserInterface(I18NBundle myBundle) {
        this.myBundle = myBundle;
        fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("ui/alternative.fnt"),
                new TextureRegion(fontTexture), false);
        stage.addActor(topTable);
        // set labels to custom font
        topMoneyLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        topCowsLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        topActionsLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        // setup top bar of UI
        topTable.setSize(width, height / 16);
        topTable.setPosition(0,height - height / 16);
        topTable.setDebug(true);
        topTable.setSkin(skin);
        topTable.add(topMoneyLabel).width(width / 3);
        topTable.add(topCowsLabel).width(width / 3);
        topTable.add(topActionsLabel).width(width / 3);
        // set to true when dialog on screen, prevents player movement.
        dialogFocus = false;

    }

    public void render(GameData data) {
        topMoneyLabel.setText(myBundle.get("money") + data.getMoney());
        topCowsLabel.setText(myBundle.get("cows") + data.getCowAmount());
        topActionsLabel.setText(myBundle.get("actions")
                + (data.MAX_ACTIONS - data.getActionsDone()));
        stage.draw();
    }

    public void createDialog(Dialog dialog, String text, boolean preDialog) {
        Gdx.input.setInputProcessor(stage);
        Label label = new Label(text, new Label.LabelStyle(font, Color.WHITE));
        label.setWrap(true);
        dialog.setMovable(false);
        dialog.setSize(width * 0.75f, height * 0.3f);
        dialog.setPosition(width / 2 - dialog.getWidth() / 2, height / 2 - dialog.getHeight());
        dialog.getContentTable().add(label).width(dialog.getWidth() - 30f);
        dialog.button(myBundle.get("confirm"), true); //sends "true" as the result
        if (preDialog) {    // add cancel button only if the dialog is pre-action
            dialog.button(myBundle.get("cancel"), false); //sends "false" as the result
        }
        dialog.getButtonTable().pad(10f);
        dialog.pack();
        dialog.show(stage);
    }
}
