package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Location {
    final float WORLD_WIDTH = 9f;
    final float WORLD_HEIGHT = 16f;
    float blackness;
    float fadeSpeed = 2f;
    boolean fadeIn;
    SpriteBatch batch;
    OrthographicCamera camera;
    Texture background;
    Texture blackTexture = new Texture("black.png");
    Sprite black = new Sprite(blackTexture);
    GasChaosMain game;
    Screen parent;

    public void fadeFromBlack() {
        blackness -= Gdx.graphics.getDeltaTime() * fadeSpeed;

        if (blackness <= 0) {
            fadeIn = false;
            blackness = 0;
        }
    }
}
