package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;

public class UserInterface {
    boolean dialogFocus;
    boolean preDialogResult;
    Stage stage = new Stage();
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    Table topTable = new Table();
    Label topMoneyLabel = new Label("", skin);
    Label topCowsLabel = new Label("", skin);
    Label questionLabel = new Label("", skin);  // text of pre-action dialog
    Label resultLabel = new Label("", skin);    // text of post-action dialog
    Label confirmLabel = new Label("", skin);
    Label cancelLabel = new Label("", skin);
    Button confirmButton = new Button(skin);
    Button cancelButton = new Button(skin);
    I18NBundle myBundle;

    public UserInterface(I18NBundle myBundle) {
        this.myBundle = myBundle;
        stage.addActor(topTable);
        // setup not-changing texts according to language
        topMoneyLabel.setText(myBundle.get("money"));
        topCowsLabel.setText(myBundle.get("cows"));
        confirmLabel.setText(myBundle.get("confirm"));
        cancelLabel.setText(myBundle.get("cancel"));
        // setup top bar of UI
        topTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 16);
        topTable.setPosition(0,Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 16));
        topTable.setDebug(true);
        topTable.setSkin(skin);
        topTable.add(topMoneyLabel);
        topTable.add(topCowsLabel);
        // add text to dialog buttons
        confirmButton.add(confirmLabel);
        cancelButton.add(cancelLabel);
        // prevent dialog from drawing on default
        dialogFocus = false;
    }

    public void render(GameData data) {
        topMoneyLabel.setText("Money: " + data.getMoney());
        topCowsLabel.setText("Cows: " + data.getCowAmount());
        stage.draw();
    }

    public void configPreDialog(Dialog dialog, String text) {
        Gdx.input.setInputProcessor(stage);
        dialog.pad(20);
        dialog.text(text);
        dialog.button(myBundle.get("confirm"), true); //sends "true" as the result
        dialog.button(myBundle.get("cancel"), false); //sends "false" as the result
        dialog.show(stage);
    }

    public void configPostDialog(Dialog dialog, String text) {
        Gdx.input.setInputProcessor(stage);
        dialog.pad(20);
        dialog.text(text);
        dialog.button(myBundle.get("confirm"), true);
        dialog.show(stage);
    }
}
