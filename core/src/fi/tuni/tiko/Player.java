package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

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
    boolean inputActive = false;    // not able to move player while fading screen

    public void player() {
        texture = new Texture("player.png");
        float width = texture.getWidth()/200f;
        float height = texture.getHeight()/200f;
        rectangle = new Rectangle(0.0f, 0.0f, width, height);
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

    public void playerTouch () {

        camera = new OrthographicCamera();

        if(Gdx.input.isTouched()) {

            // move player on touch
            int realX = Gdx.input.getX();
            int realY = Gdx.input.getY();

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

        // X axis
        if(targetX > rectangle.x + speed) {
            rectangle.x = rectangle.x + speed * Gdx.graphics.getDeltaTime();

        } if(targetX < rectangle.x - speed) {
            rectangle.x = rectangle.x - speed * Gdx.graphics.getDeltaTime();
        } else if ((targetX > rectangle.x && targetX < rectangle.x + speed) ||
                   (targetX < rectangle.x && targetX > rectangle.x - speed)) {
            setX(targetX);

        }

    }
}
