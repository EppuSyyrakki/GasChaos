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
    boolean up;
    boolean down;
    boolean left;
    boolean right;

    public void player() {
        texture = new Texture("player.png");
        float width = texture.getWidth()/150f;
        float height = texture.getHeight()/150f;
        rectangle = new Rectangle(0.0f, 0.0f, width, height);
        setSize(width, height);
        speedX = 0.1f;
        speedY = 0.1f;
        speed = 1.45f;
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
    public float getRX() {
        return rectangle.x;
    }

    public float getRY() {
        return rectangle.y;
    }

    // Set X/Y
    public void setRX(float x) {
        this.rectangle.x = x;
    }

    public void setRY(float y) {
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

    public Rectangle getRectangleUp() {
        Rectangle recFuture = getRectangle();
        recFuture.x = recFuture.x + speed * Gdx.graphics.getDeltaTime();
        return recFuture;
    }

    public Rectangle getRectangleDown() {
        Rectangle recFuture = getRectangle();
        recFuture.x = recFuture.x - speed * Gdx.graphics.getDeltaTime();
        return recFuture;
    }

    public Rectangle getRectangleLeft() {
        Rectangle recFuture = getRectangle();
        recFuture.y = recFuture.y - speed * Gdx.graphics.getDeltaTime();
        return recFuture;
    }

    public Rectangle getRectangleRight() {
        Rectangle recFuture = getRectangle();
        recFuture.y = recFuture.y + speed * Gdx.graphics.getDeltaTime();
        return recFuture;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(getTexture(), getRX(), getRY(), getWidth(), getHeight());
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
            if (targetX == getRX()) {
                //System.out.println("same X");
            } else if (targetX > getRX() && up == true) {
                if (targetX > (getRX() + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setRX(getRX() + getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetX <= (getRX() + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setRX(getRX());
                }
            } else if (targetX < getRX() && down == true) {
                if (targetX < (getRX() - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setRX(getRX() - getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetX >= (getRX() - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setRX(getRX());
                }
            }

            // Y axis
            if (targetY == getRY()) {
                //System.out.println("same Y");
            } else if (targetY > getRY() && right == true) {
                if (targetY > (getRY() + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setRY(getRY() + getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetY <= (getRY() + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setRY(getRY());
                }
            } else if (targetY < getRY() && left == true) {
                if (targetY < (getRY() - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setRY(getRY() - getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetY >= (getRY() - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setRY(getRY());
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

        setX(getRY());
        setY(getRY());

        Rectangle bounding = getBoundingRectangle();
        //System.out.println(bounding.x + "" + bounding.y);

        MapLayer collisionObjectLayer = (MapLayer)tiledMap.getLayers().get("RectangleCollision");

        // all of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // add to array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, 1 / 120f);

            if (getRectangle().overlaps(rectangle)) {
                System.out.println("placeholder");
            }
        }

        up = true;
        down = true;
        left = true;
        right = true;
        // wallcheck going up
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, 1 / 120f);

            if (getRectangleUp().overlaps(rectangle)) {
                up = false;
            }
            if (getRectangleDown().overlaps(rectangle)) {
                down = false;
            }
            if (getRectangleLeft().overlaps(rectangle)) {
                left = false;
            }
            if (getRectangleRight().overlaps(rectangle)) {
                right = false;
            }

        }
    }

    private Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangleScale = new Rectangle();
        rectangleScale.x      = r.x * scale;
        rectangleScale.y      = r.y * scale;
        rectangleScale.width  = r.width * scale;
        rectangleScale.height = r.height * scale;
        return rectangleScale;
    }
}