package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class UserInterface {
    boolean dialogFocus;
    Stage top = new Stage();
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    Table topTable = new Table();
    Label moneyLabel = new Label("Money: ", skin);
    Label cowsLabel = new Label("Cows: ", skin);
    Label dialogLabel = new Label("", skin);
    Label confirmLabel = new Label("OK", skin);
    Label cancelLabel = new Label("CANCEL", skin);
    Dialog dialog = new Dialog("Action:", skin);
    Button confirm = new Button(skin);
    Button cancel = new Button(skin);

    public UserInterface() {
        top.addActor(topTable);
        Gdx.input.setInputProcessor(top);
        topTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 16);
        topTable.setPosition(0,Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 16));
        topTable.setDebug(true);
        topTable.setSkin(skin);
        topTable.add(moneyLabel);
        topTable.add(cowsLabel);
        confirm.add(confirmLabel);
        cancel.add(cancelLabel);

        dialogFocus = false;
        dialog.button(confirm);
        dialog.button(cancel);
        dialog.text(dialogLabel);

    }

    public void render(GameData data) {
        moneyLabel.setText("Money: " + data.getMoney());
        cowsLabel.setText("Cows: " + data.getCowAmount());
        top.draw();

        if (dialogFocus) {
            dialog.show(top);
        }
    }
}
