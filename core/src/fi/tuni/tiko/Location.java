package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Location {
    final float WORLD_WIDTH = 9f;
    final float WORLD_HEIGHT = 16f;
    final float WORLD_SCALE = 1 / 120f;
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

    public Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangleScale = new Rectangle();
        rectangleScale.x      = r.x * scale;
        rectangleScale.y      = r.y * scale;
        rectangleScale.width  = r.width * scale;
        rectangleScale.height = r.height * scale;
        return rectangleScale;
    }

    public float getWorldScale() {
        return WORLD_SCALE;
    }
}
