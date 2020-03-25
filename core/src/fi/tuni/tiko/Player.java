package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite{

    private OrthographicCamera camera;
    Texture texture;
    Rectangle rectangle;
    float speed;
    float rotation;
    float rotationRate;
    float speedX;
    float speedY;
    float targetX;
    float targetY;
    boolean inputActive = true;    // not able to move player while fading screen
    boolean upleft;
    boolean downleft;
    boolean upright;
    boolean downright;

    public void player() {
        texture = new Texture("player.png");
        float width = texture.getWidth()/100f;
        float height = texture.getHeight()/100f;
        rectangle = new Rectangle(0.0f, 0.0f, width, height);
        setSize(width, height);
        speedX = 0.1f;
        speedY = 0.1f;
        speed = 1.6f;
        targetX = rectangle.x;
        targetY = rectangle.y;
    }


    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    // Get X/Y
    public float getX() {
        return rectangle.x;
    }

    public float getY() {
        return rectangle.y;
    }

    // Set X/Y
    public void setX(float x) {
        this.rectangle.x = x;
    }

    public void setY(float y) {
        this.rectangle.y = y;
    }

    // Get dimensions
    public float getHeight() {
        return rectangle.height;
    }

    public float getWidth() {
        return rectangle.width;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotationRate() {
        return rotationRate;
    }

    public void setRotationRate(float rotationRate) {
        this.rotationRate = rotationRate;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getTargetX() {
        return targetX;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
    }

    public void playerTouch(SpriteBatch batch) {


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 9, 16);
        batch.setProjectionMatrix(camera.combined);

        if(Gdx.input.isTouched()) {


            // move player on touch
            float realX = Gdx.input.getX();
            float realY = Gdx.input.getY();

            // pixels to world resolution
            Vector3 touchPos = new Vector3(realX, realY, 0);
            camera.unproject(touchPos);

            setTargetX(touchPos.x);
            setTargetY(touchPos.y);

            // log
            //Gdx.app.log("render", "x = " + touchPos.x);
            //Gdx.app.log("render", "y = " + touchPos.y);
        }
    }

    public void playerMovement () {

        camera = new OrthographicCamera();

        if (inputActive == true) {
            // X axis
            if (targetX == getX()) {
                //System.out.println("same X");
            } else if (targetX > getX()) {
                if (targetX > (getX() + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setX(getX() + getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetX <= (getX() + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setX(getX());
                }
            } else if (targetX < getX()) {
                if (targetX < (getX() - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setX(getX() - getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetX >= (getX() - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setX(getX());
                }
            }

            // Y axis
            if (targetY == getY()) {
                //System.out.println("same Y");
            } else if (targetY > getY()) {
                if (targetY > (getY() + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setY(getY() + getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetY <= (getY() + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setY(getY());
                }
            } else if (targetY < getY()) {
                if (targetY < (getY() - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setY(getY() - getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetY >= (getY() - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setY(getY());
                }
            }
        }


        boolean moveDebug = false;

        if (moveDebug) {
            Gdx.app.log("render", "x = " + rectangle.x);
            Gdx.app.log("render", "y = " + rectangle.y);
            Gdx.app.log("render", "x target = " + targetX);
            Gdx.app.log("render", "y target = " + targetY);
            Gdx.app.log("render", "x speed = " + speedX);
            Gdx.app.log("render", "y speed = " + speedY);
        }
    }

    public void checkCollisions(TiledMap tiledMap) {

        MapLayer collisionObjectLayer = (MapLayer)tiledMap.getLayers().get("RectangleCollision");

        // all of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // add to array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, 120f);

            if (getBoundingRectangle().overlaps(rectangle)) {
                System.out.println("placeholder");
            }
        }
    }

    private Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangle = new Rectangle();
        rectangle.x      = r.x * scale;
        rectangle.y      = r.y * scale;
        rectangle.width  = r.width * scale;
        rectangle.height = r.height * scale;
        return rectangle;
    }



}
