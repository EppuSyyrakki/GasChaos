package fi.tuni.gasChaos;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Location implements InputProcessor {
    final GasChaosMain game;
    final float WORLD_WIDTH = 9f;
    final float WORLD_HEIGHT = 16f;
    final float WORLD_SCALE = 1 / 120f;
    float blackness;
    final float sunsetTransparency = 0.2f;
    float fadeSpeed = 2f;
    boolean fadeIn;
    boolean actionInputActive = true;
    SpriteBatch batch;
    OrthographicCamera camera;
    Texture background;
    final Texture blackTexture = new Texture("props/black.png");
    final Texture sunsetTexture = new Texture("props/sunset.png");
    final Sprite black = new Sprite(blackTexture);
    final Sprite sunset = new Sprite(sunsetTexture);
    UserInterface userInterface;
    String uiText = "default";
    I18NBundle myBundle;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    final Sound chickenS = Gdx.audio.newSound(Gdx.files.internal("audio/chicken.mp3"));
    final Sound bird1S = Gdx.audio.newSound(Gdx.files.internal("audio/nightBird.mp3"));
    final Sound bird2S = Gdx.audio.newSound(Gdx.files.internal("audio/blackBird.mp3"));
    final Music noise = Gdx.audio.newMusic(Gdx.files.internal("audio/noise.mp3"));
    final Music forest = Gdx.audio.newMusic(Gdx.files.internal("audio/forest.mp3"));

    public Location(GasChaosMain game) {
        this.game = game;
    }

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
     * @return rectangle with game world coordinates
     */
    public Rectangle getCheckRectangle(MapLayer layer) {
        MapObjects mapObjects = layer.getObjects();
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        return scaleRect(rectangleObjects.get(0).getRectangle(), WORLD_SCALE);
    }

    public Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangleScale = new Rectangle();
        rectangleScale.x      = r.x * scale;
        rectangleScale.y      = r.y * scale;
        rectangleScale.width  = r.width * scale;
        rectangleScale.height = r.height * scale;
        return rectangleScale;
    }

    @SuppressWarnings("RedundantCast")
    public boolean getUIRec(String name) {
        Rectangle r = getCheckRectangle((MapLayer)tiledMap.getLayers().get(name));
        return playerAction(r);
    }

    public boolean playerAction(Rectangle rectangle) {
        boolean touched = false;

        // setActionInputActive() setter.
        // isActionInputActive() getter.
        if (actionInputActive) {
            if (Gdx.input.justTouched()) {

                // move player on touch
                float realX = Gdx.input.getX();
                float realY = Gdx.input.getY();

                // pixels to world resolution
                Vector3 touchPos = new Vector3(realX, realY, 0);
                camera.unproject(touchPos);

                touched = rectangle.contains(touchPos.x, touchPos.y);
            }
        }
        return touched;

    }

    public float getBlackness() {
        return blackness;
    }

    public void setBlackness(float blackness) {
        this.blackness = blackness;
    }

    public float getFadeSpeed() {
        return fadeSpeed;
    }

    public void setFadeSpeed(float fadeSpeed) {
        this.fadeSpeed = fadeSpeed;
    }

    public boolean isFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(boolean fadeIn) {
        this.fadeIn = fadeIn;
    }

    public boolean isActionInputActive() {
        return actionInputActive;
    }

    public void setActionInputActive(boolean actionInputActive) {
        this.actionInputActive = actionInputActive;
    }

    public float getWorldScale() {
        return WORLD_SCALE;
    }

    @Override
    public boolean keyDown(int keycode) {
        return keycode == Input.Keys.BACK;
    }

    public boolean keyUp(int keycode) {
        return keycode == Input.Keys.BACK;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void sunsetRender() {
        if (!game.gameData.isActionsAvailable()) {
            sunset.draw(batch, sunsetTransparency);
        }
    }

    public void sunsetSky(Texture sunsetForeground) {
        if (!game.gameData.isActionsAvailable()) {
            batch.draw(sunsetForeground, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }
}
