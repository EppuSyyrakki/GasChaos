package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    Texture textureStand;
    Texture textureRight;
    Rectangle rectangle;
    float speed;
    float rotation;
    float rotationRate;
    float speedX;
    float speedY;
    float targetX;
    float targetY;
    float lastX;
    float lastY;
    float stateTime = 1.0f;
    boolean inputActive = true;    // not able to move player while fading screen
    boolean up;
    boolean down;
    boolean left;
    boolean right;
    TextureRegion currentFrame;
    Animation<TextureRegion> walkStandAnimation;
    Animation<TextureRegion> walkUpAnimation;
    Animation<TextureRegion> walkDownAnimation;
    Animation<TextureRegion> walkRightAnimation;
    int FRAME_STAND_COLS = 1;
    int FRAME_STAND_ROWS = 1;
    int FRAME_RIGHT_COLS = 9;
    int FRAME_RIGHT_ROWS = 1;

    public void player(float size) {
        textureStand = new Texture("player.png");
        textureRight = new Texture("playerRight.png");
        float widthRight = textureRight.getWidth() / size;
        float heightRight = textureRight.getHeight() / size;
        float width = textureStand.getWidth() / FRAME_STAND_COLS /size;
        float height = textureStand.getHeight() / FRAME_STAND_ROWS /size;
        rectangle = new Rectangle(0.0f, 0.0f, width, height);
        setSize(width, height);
        speedX = 0.1f;
        speedY = 0.1f;
        speed = 1.45f;
        targetX = getRX();
        targetY = getRY();
        lastX = getRX();
        lastY = getRY();

        // Animation
        TextureRegion[][] tmpStand = TextureRegion.split(
                textureStand,
                textureStand.getWidth() / FRAME_STAND_COLS,
                textureStand.getHeight() / FRAME_STAND_ROWS);

        TextureRegion[][] tmpRight = TextureRegion.split(
                textureRight,
                textureRight.getWidth() / FRAME_RIGHT_COLS,
                textureRight.getHeight() / FRAME_RIGHT_ROWS);

        TextureRegion[] framesRight = transformTo1D(tmpRight, 1, 9);
        TextureRegion[] framesStand = transformTo1D(tmpStand, 1, 1);

        walkStandAnimation = new Animation(18 / 60f, framesStand);
        walkRightAnimation = new Animation(6 / 60f, framesRight);
    }

    public int getFRAME_STAND_COLS() {
        return FRAME_STAND_COLS;
    }

    public void setFRAME_STAND_COLS(int FRAME_STAND_COLS) {
        this.FRAME_STAND_COLS = FRAME_STAND_COLS;
    }

    public int getFRAME_STAND_ROWS() {
        return FRAME_STAND_ROWS;
    }

    public void setFRAME_STAND_ROWS(int FRAME_STAND_ROWS) {
        this.FRAME_STAND_ROWS = FRAME_STAND_ROWS;
    }

    public Texture getTextureStand() {
        return textureStand;
    }

    public void setTextureStand(Texture textureStand) {
        this.textureStand = textureStand;
    }

    public Texture getTextureRight() {
        return textureRight;
    }

    public void setTextureRight(Texture textureRight) {
        this.textureRight = textureRight;
    }

    public float getLastX() {
        return lastX;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public boolean isInputActive() {
        return inputActive;
    }

    public void setInputActive(boolean inputActive) {
        this.inputActive = inputActive;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public int getFRAME_RIGHT_COLS() {
        return FRAME_RIGHT_COLS;
    }

    public void setFRAME_RIGHT_COLS(int FRAME_RIGHT_COLS) {
        this.FRAME_RIGHT_COLS = FRAME_RIGHT_COLS;
    }

    public int getFRAME_RIGHT_ROWS() {
        return FRAME_RIGHT_ROWS;
    }

    public void setFRAME_RIGHT_ROWS(int FRAME_RIGHT_ROWS) {
        this.FRAME_RIGHT_ROWS = FRAME_RIGHT_ROWS;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public Texture getTexture() {
        return textureRight;
    }

    public void setTexture(Texture texture) {
        this.textureRight = texture;
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

    public Rectangle getRectangleRight() {
        Rectangle recFuture = getRectangle();
        recFuture.x = recFuture.x + speed * Gdx.graphics.getDeltaTime();
        return recFuture;
    }

    public Rectangle getRectangleLeft() {
        Rectangle recFuture = getRectangle();
        recFuture.x = recFuture.x - speed * Gdx.graphics.getDeltaTime();
        return recFuture;
    }

    public Rectangle getRectangleDown() {
        Rectangle recFuture = getRectangle();
        recFuture.y = recFuture.y - speed * Gdx.graphics.getDeltaTime();
        return recFuture;
    }

    public Rectangle getRectangleUp() {
        Rectangle recFuture = getRectangle();
        recFuture.y = recFuture.y + speed * Gdx.graphics.getDeltaTime();
        return recFuture;
    }

    public void draw(SpriteBatch batch) {
        playerFrames();
        batch.draw(currentFrame, getRX(), getRY(), getWidth(), getHeight());
    }

    public void playerTouch(SpriteBatch batch) {


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 9, 16);
        batch.setProjectionMatrix(camera.combined);

        if (inputActive == true) {
            if (Gdx.input.isTouched()) {


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
    }

    public void playerMovement () {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 9, 16);

        if (inputActive == true) {
            // X axis
            if (targetX == (getRX() + getWidth() / 2f)) {
                setLastX(getRX());
            } else if (targetX > (getRX() + getWidth() / 2f) && right == true) {
                if (targetX > ((getRX() + getWidth() / 2f) + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastX(getRX());
                    setRX(getRX() + getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetX <= ((getRX() + getWidth() / 2) + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastX(getRX());
                    setRX(getRX());
                }
            } else if (targetX < (getRX() + getWidth() / 2f) && left == true) {
                if (targetX < ((getRX() + getWidth() / 2f) - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastX(getRX());
                    setRX(getRX() - getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetX >= ((getRX() + getWidth() / 2f) - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastX(getRX());
                    setRX(getRX());
                }
            }

            // Y axis
            if (targetY == (getRY() + getHeight() / 2f)) {
                setLastY(getRY());
            } else if (targetY > (getRY() + getHeight() / 2f)&& up == true) {
                if (targetY > ((getRY() + getHeight() / 2f) + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastY(getRY());
                    setRY(getRY() + getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetY <= ((getRY() + getHeight() / 2f) + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastY(getRY());
                    setRY(getRY());
                }
            } else if (targetY < (getRY() + getHeight() / 2f) && down == true) {
                if (targetY < ((getRY() + getHeight() / 2f) - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastY(getRY());
                    setRY(getRY() - getSpeed() * Gdx.graphics.getDeltaTime());
                } else if (targetY >= ((getRY() + getHeight() / 2f) - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastY(getRY());
                    setRY(getRY());
                }
            }
        }


        boolean moveDebug = false;

        if (moveDebug) {
            Gdx.app.log("render", "x = " + rectangle.x);
            Gdx.app.log("render", "y = " + rectangle.y);
            //Gdx.app.log("render", "x target = " + targetX);
            //Gdx.app.log("render", "y target = " + targetY);
            //Gdx.app.log("render", "x speed = " + speedX);
            //Gdx.app.log("render", "y speed = " + speedY);
        }
    }

    public boolean playerAction(SpriteBatch batch, Rectangle rectangle) {

        boolean touched = false;
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

            if (rectangle.contains(touchPos.x, touchPos.y)) {
                //System.out.println("boat");
                touched = true;
            } else {
                touched = false;

            }
        }
        return touched;

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
                System.out.println("collision detected");
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

    private TextureRegion[] transformTo1D(TextureRegion[][] tmp, int rows, int cols) {
        TextureRegion [] walkFrames
                = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        return walkFrames;
    }

    // Don't bother reading this, it's ugly but it works
    private void playerFrames() {
        stateTime += Gdx.graphics.getDeltaTime();
        if ((getRX() - getLastX()) > 0.003f) {
            for (TextureRegion textureRegion : walkRightAnimation.getKeyFrames()) {
                if (textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            for (TextureRegion textureRegion : walkStandAnimation.getKeyFrames()) {
                if (textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
        } else if ((getLastX() - getRX()) > 0.003f) {
            for (TextureRegion textureRegion : walkRightAnimation.getKeyFrames()) {
                if (!textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            for (TextureRegion textureRegion : walkStandAnimation.getKeyFrames()) {
                if (!textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
        } else if ((getRY() - getLastY()) > 0.003f) {
            currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
        } else if ((getLastY() - getRY()) > 0.003f) {
            currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
        } else {
            setLastX(getRX());
            setLastY(getRY());
            currentFrame = walkStandAnimation.getKeyFrame(stateTime, true);
        }
    }
}