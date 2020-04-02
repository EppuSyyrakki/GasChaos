package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TopBar {

    Stage stage = new Stage();
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    Table topTable = new Table();
    Label moneyLabel = new Label("Money: ", skin);
    Label cowsLabel = new Label("Cows: ", skin);

    public TopBar() {
        stage.addActor(topTable);
        Gdx.input.setInputProcessor(stage);
        topTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 16);
        topTable.setPosition(0,Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 16));
        topTable.setDebug(true);
        topTable.setSkin(skin);
        topTable.add(moneyLabel);
        topTable.add(cowsLabel);
        moneyLabel.setScale(20f);
        cowsLabel.setScale(20f);
    }

    public void update(GameData data) {
        moneyLabel.setText("Money: " + data.getMoney());
        cowsLabel.setText("Cows: " + data.getCowAmount());
    }
}
