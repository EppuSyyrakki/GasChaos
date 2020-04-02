package fi.tuni.tiko;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

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

    public void fadeFromBlack() {
        blackness -= Gdx.graphics.getDeltaTime() * fadeSpeed;

        if (blackness <= 0) {
            fadeIn = false;
            blackness = 0;
        }
    }

    /**
     * Returns first found rectangle from a MapLayer object.
     * @param layer MapLayer to get rectangle from
     * @return rectangle with gameworld coordinates
     */
    public Rectangle getCheckRectangle(MapLayer layer) {
        MapObjects mapObjects = layer.getObjects();
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        Rectangle rectangle = scaleRect(rectangleObjects.get(0).getRectangle(), WORLD_SCALE);
        return rectangle;
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
