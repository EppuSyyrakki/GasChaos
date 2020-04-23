package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

public class UserInterface {
    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();
    boolean dialogFocus;
    float uiScale;
    Stage stage = new Stage();
    Texture fontTextureNormal = new Texture(Gdx.files.internal("ui/normal.png"));
    Texture fontTextureLarge = new Texture(Gdx.files.internal("ui/large.png"));
    Texture barBgTexture = new Texture(Gdx.files.internal("ui/uiBarBg.png"));
    Sprite barBGSprite = new Sprite(barBgTexture);
    SpriteDrawable bgDrawable = new SpriteDrawable(barBGSprite);
    BitmapFont font;
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    Table topTable = new Table();
    Table topTable2 = new Table();
    Label topMoneyLabel;
    Label topCowsLabel;
    Label topActionsLabel;
    Label topGrainLabel;
    Label topFertilizersLabel;
    I18NBundle myBundle;
    TextButton confirmButton;
    TextButton cancelButton;
    TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();

    TextButton reapButton;
    TextButton sowButton;
    TextButton nButton;
    TextButton pButton;
    TextButton rentButton;
    TextButton unrentButton;
    final Texture buttonUpBackground;
    final Texture buttonDownBackground;

    public UserInterface(I18NBundle myBundle) {
        this.myBundle = myBundle;
        buttonUpBackground = new Texture("props/buttonUp.png");
        buttonDownBackground = new Texture("props/buttonDown.png");
        fontTextureNormal.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fontTextureLarge.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        if (height <= 1280) {
            font = new BitmapFont(Gdx.files.internal("ui/normal.fnt"),
                    new TextureRegion(fontTextureNormal), false);
            uiScale = 1;
        } else if (height > 1280) {
            font = new BitmapFont(Gdx.files.internal("ui/large.fnt"),
                    new TextureRegion(fontTextureLarge), false);
            uiScale = 1.5f;
        }
        buttonStyle.font = font;
        confirmButton = new TextButton(" " + myBundle.get("confirm") + " ", buttonStyle);
        cancelButton = new TextButton(" " +myBundle.get("cancel") + " ", buttonStyle);

        stage.addActor(topTable);
        stage.addActor(topTable2);

        // set labels to custom font
        topMoneyLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        topCowsLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        topActionsLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        topGrainLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        topFertilizersLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));

        // setup top bar of UI
        topTable.setSize(width, height / 28);
        topTable.setPosition(0,height - height / 28);
        topTable.setBackground(bgDrawable);
        topTable.add(topMoneyLabel).width(width / 3).align(Align.center);
        topTable.add(topCowsLabel).width(width / 3).align(Align.center);;
        topTable.add(topActionsLabel).width(width / 3).align(Align.center);;

        topTable2.setSize(width, height / 28);
        topTable2.setPosition(0,height - height / 14);
        topTable2.setBackground(bgDrawable);
        topTable2.add(topGrainLabel).width(width / 3).align(Align.center);;
        topTable2.add(topFertilizersLabel).align(Align.center);;

        // set to true when dialog on screen, prevents player movement.
        dialogFocus = false;

        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonUpBackground));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(buttonDownBackground));
        // set field dialog buttons
        reapButton = new TextButton("  " + myBundle.get("reap") + "  ", buttonStyle);
        sowButton = new TextButton("  " + myBundle.get("sow") + "  ", buttonStyle);
        nButton = new TextButton("  " + myBundle.get("addN") + "  ", buttonStyle);
        pButton = new TextButton("  " + myBundle.get("addP") + "  ", buttonStyle);
        rentButton = new TextButton("  " + myBundle.get("rent") + "  ", buttonStyle);
        unrentButton = new TextButton("  " + myBundle.get("stopRent") + "  ", buttonStyle);

    }

    public void render(GameData data) {
        topMoneyLabel.setText(" " + myBundle.get("money") + data.getMoney());
        topCowsLabel.setText(myBundle.get("cows") + data.getCowAmount());
        topActionsLabel.setText(myBundle.get("actions")
                + (data.MAX_ACTIONS - data.getActionsDone()));
        topGrainLabel.setText(myBundle.get("grain") + data.getGrain());
        topFertilizersLabel.setText(myBundle.get("fertilizers")
                + data.getFertilizerN() + "/" + data.getFertilizerP());
        stage.draw();
    }

    public void createDialog(Dialog dialog, String text, boolean preDialog) {
        Gdx.input.setInputProcessor(stage);
        Label label = new Label(text, new Label.LabelStyle(font, Color.WHITE));
        label.setWrap(true);
        dialog.setMovable(false);
        dialog.setSize(width * 0.75f, height * 0.3f* uiScale);
        dialog.setPosition(width / 2 - dialog.getWidth() / 2, height / 2 - dialog.getHeight());
        label.setWidth(dialog.getWidth() * 0.8f);
        dialog.getContentTable().add(label).width(dialog.getWidth() - 30f);
        dialog.button(confirmButton, true);

        if (preDialog) {    // add cancel button only if the dialog is pre-action
            dialog.button(cancelButton, false);
        }
        dialog.pad(20f, 20f, 20f, 20f);
        dialog.pack();
        dialog.show(stage);
    }

    public void createFieldDialog(Dialog dialog, String text, boolean[] actions) {
        Gdx.input.setInputProcessor(stage);
        Label label = new Label(text, new Label.LabelStyle(font, Color.WHITE));
        label.setWrap(true);
        dialog.setMovable(false);
        dialog.setSize(width * 0.75f, height * 0.3f);
        dialog.setPosition(width / 2 - dialog.getWidth() / 2, height / 2 - dialog.getHeight());
        dialog.getContentTable().add(label).width(dialog.getWidth() - 30f);

        if (actions[4]) { // owned, empty
            dialog.button(nButton,1);
            dialog.button(pButton, 2);
        }

        if (actions[0]) {
            dialog.button(rentButton, 3);
        } else if (actions[1]) {
            dialog.button(unrentButton, 4);
        }

        if (actions[2]) {
            dialog.button(sowButton, 5);
        } else if (actions[3]) {
            dialog.button(reapButton, 6);
        }

        dialog.button(cancelButton, 0);
        dialog.pad(20f, 20f, 20f, 20f);
        dialog.pack();
        dialog.show(stage);
    }
}
