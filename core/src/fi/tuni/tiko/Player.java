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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

@SuppressWarnings({"IntegerDivisionInFloatingPointContext", "RedundantCast", "rawtypes", "unchecked", "SameParameterValue"})
public class Player extends Sprite{

    private OrthographicCamera camera;
    Texture textureStand;
    Texture textureRight;
    Texture textureUp;
    Texture textureDown;
    Rectangle rectangle;
    Rectangle recFuture;
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
    final int FRAME_STAND_COLS = 1;
    final int FRAME_STAND_ROWS = 1;
    final int FRAME_RIGHT_COLS = 9;
    final int FRAME_RIGHT_ROWS = 1;
    final int FRAME_UP_COLS = 4;
    final int FRAME_UP_ROWS = 1;
    final int FRAME_DOWN_COLS = 4;
    final int FRAME_DOWN_ROWS = 1;

    public void player(float size) {
        textureStand = new Texture("player/player.png");
        textureRight = new Texture("player/playerRight.png");
        textureUp = new Texture("player/playerUp.png");
        textureDown = new Texture("player/playerDown.png");
        float width = textureStand.getWidth() / FRAME_STAND_COLS / size;
        float height = textureStand.getHeight() / FRAME_STAND_ROWS / size;
        rectangle = new Rectangle(0.0f, 0.0f, width, height);
        recFuture = new Rectangle(0.0f, 0.0f, width, height);
        setSize(width, height);
        speedX = 0.1f;
        speedY = 0.1f;
        speed = 1.45f;
        targetX = getRX();
        targetY = getRY();
        lastX = getRX();
        lastY = getRY();

        // Create texture regions.
        TextureRegion[][] tmpStand = TextureRegion.split(
                textureStand,
                textureStand.getWidth() / FRAME_STAND_COLS,
                textureStand.getHeight() / FRAME_STAND_ROWS);

        TextureRegion[][] tmpRight = TextureRegion.split(
                textureRight,
                textureRight.getWidth() / FRAME_RIGHT_COLS,
                textureRight.getHeight() / FRAME_RIGHT_ROWS);

        TextureRegion[][] tmpUp = TextureRegion.split(
                textureUp,
                textureUp.getWidth() / FRAME_UP_COLS,
                textureUp.getHeight() / FRAME_UP_ROWS);

        TextureRegion[][] tmpDown = TextureRegion.split(
                textureDown,
                textureDown.getWidth() / FRAME_DOWN_COLS,
                textureDown.getHeight() / FRAME_DOWN_ROWS);

        // Texture regions part 2
        TextureRegion[] framesRight = transformTo1D(tmpRight, FRAME_RIGHT_ROWS, FRAME_RIGHT_COLS);
        TextureRegion[] framesUp = transformTo1D(tmpUp, FRAME_UP_ROWS, FRAME_UP_COLS);
        TextureRegion[] framesDown = transformTo1D(tmpDown, FRAME_DOWN_ROWS, FRAME_DOWN_COLS);
        TextureRegion[] framesStand = transformTo1D(tmpStand, FRAME_STAND_ROWS, FRAME_STAND_COLS);

        // Sets animation frames to animations
        walkStandAnimation = new Animation(18 / 60f, framesStand);
        walkRightAnimation = new Animation(6 / 60f, framesRight);
        walkUpAnimation = new Animation(16 / 60f, framesUp);
        walkDownAnimation = new Animation(16 / 60f, framesDown);
    }

    public int getFRAME_STAND_COLS() {
        return FRAME_STAND_COLS;
    }

    public int getFRAME_STAND_ROWS() {
        return FRAME_STAND_ROWS;
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

    public int getFRAME_RIGHT_ROWS() {
        return FRAME_RIGHT_ROWS;
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
        Rectangle recFutureRight = getRectangle();
        recFutureRight.x = recFutureRight.x + getSpeed() * Gdx.graphics.getDeltaTime();
        return recFutureRight;
    }

    public Rectangle getRectangleLeft() {
        Rectangle recFutureLeft = getRectangle();
        recFutureLeft.x = recFutureLeft.x - getSpeed() * Gdx.graphics.getDeltaTime();
        return recFutureLeft;
    }

    public Rectangle getRectangleDown() {
        Rectangle recFutureDown = getRectangle();
        recFutureDown.y = recFutureDown.y - getSpeed() * Gdx.graphics.getDeltaTime();
        return recFutureDown;
    }

    public Rectangle getRectangleUp() {
        Rectangle recFutureUp = getRectangle();
        recFutureUp.y = recFutureUp.y + getSpeed() * Gdx.graphics.getDeltaTime();
        return recFutureUp;
    }

    public void draw(SpriteBatch batch) {
        playerFrames();
        batch.draw(currentFrame, getRX(), getRY(), getWidth(), getHeight());
    }

    public void playerTouch(SpriteBatch batch) {


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 9, 16);
        batch.setProjectionMatrix(camera.combined);

        if (inputActive) {
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

    @SuppressWarnings("ConstantConditions")
    public void playerMovement (TiledMap tiledMap) {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 9, 16);
        checkCollisions(tiledMap);

        // It's a dumpster fire but a working one.
        if (inputActive) {
            // X axis
            if (targetX == (getRX() + getWidth() / 2f)) {
                setLastX(getRX());
            } if (targetX > (getRX() + getWidth() / 2f) && right) {
                if (targetX > ((getRX() + getWidth() / 2f) + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastX(getRX());
                    setRX(getRX() + getSpeed() * Gdx.graphics.getDeltaTime());
                    if (overlap(tiledMap, getRectangle())) {
                        setRX(getLastX());
                    }
                } else if (targetX <= ((getRX() + getWidth() / 2) + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastX(getRX());
                    setRX(getRX());
                    if (overlap(tiledMap, getRectangle())) {
                        setRX(getLastX());
                    }
                }
            } if (targetX < (getRX() + getWidth() / 2f) && left) {
                if (targetX < ((getRX() + getWidth() / 2f) - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastX(getRX());
                    setRX(getRX() - getSpeed() * Gdx.graphics.getDeltaTime());
                    if (overlap(tiledMap, getRectangle())) {
                        setRY(getLastY());
                    }
                } else if (targetX >= ((getRX() + getWidth() / 2f) - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastX(getRX());
                    setRX(getRX());
                    if (overlap(tiledMap, getRectangle())) {
                        setRX(getLastX());
                    }
                }
            }

            // Y axis
            if (targetY == (getRY() + getHeight() / 2f)) {
                setLastY(getRY());
            } if (targetY > (getRY() + getHeight() / 2f)&& up) {
                if (targetY > ((getRY() + getHeight() / 2f) + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastY(getRY());
                    setRY(getRY() + getSpeed() * Gdx.graphics.getDeltaTime());
                    if (overlap(tiledMap, getRectangle())) {
                        setRY(getLastY());
                    }
                } else if (targetY <= ((getRY() + getHeight() / 2f) + getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastY(getRY());
                    setRY(getRY());
                    if (overlap(tiledMap, getRectangle())) {
                        setRY(getLastY());
                    }
                }
            } if (targetY < (getRY() + getHeight() / 2f) && down) {
                if (targetY < ((getRY() + getHeight() / 2f) - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastY(getRY());
                    setRY(getRY() - getSpeed() * Gdx.graphics.getDeltaTime());
                    if (overlap(tiledMap, getRectangle())) {
                        setRY(getLastY());
                    }
                } else if (targetY >= ((getRY() + getHeight() / 2f) - getSpeed() * Gdx.graphics.getDeltaTime())) {
                    setLastY(getRY());
                    setRY(getRY());
                    if (overlap(tiledMap, getRectangle())) {
                        setRY(getLastY());
                    }
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

            touched = rectangle.contains(touchPos.x, touchPos.y);
        }
        return touched;

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
            Rectangle rectangle = scaleRect(tmp, 1 / 120f);

            if (getRectangle().overlaps(rectangle)) {
                System.out.println("collision detected");
            }
        }

        up = true;
        down = true;
        left = true;
        right = true;

        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, 1 / 120f);

            if (getRectangleUp().overlaps(rectangle)) {
                up = false;
                //System.out.println("up false");
            }
            if (getRectangleDown().overlaps(rectangle)) {
                down = false;
                //System.out.println("down false");
            }
            if (getRectangleLeft().overlaps(rectangle)) {
                left = false;
                //System.out.println("left false");
            }
            if (getRectangleRight().overlaps(rectangle)) {
                right = false;
                //System.out.println("right false");
            }
        }
    }

    public boolean overlap(TiledMap tiledMap, Rectangle fut) {

        boolean future = true;

        MapLayer collisionObjectLayer = (MapLayer) tiledMap.getLayers().get("RectangleCollision");

        // all of the layer
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // add to array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, 1 / 120f);

            if (fut.overlaps(rectangle)) {
                future = true;
                break;
            } else {
                future = false;
            }
        }
        return future;
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

    // Sets and flips animation frames
    private void playerFrames() {
        stateTime += Gdx.graphics.getDeltaTime();
        // If player is going left, flip textures and set walkRightAnimation.
        // Also flips walk down and standing frames to make character direction more consistent visually.
        if ((getRX() - getLastX()) > 0.003f && inputActive) {
            for (TextureRegion textureRegion : walkRightAnimation.getKeyFrames()) {
                if (textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            for (TextureRegion textureRegion : walkStandAnimation.getKeyFrames()) {
                if (textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            for (TextureRegion textureRegion : walkDownAnimation.getKeyFrames()) {
                if (!textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
        } else if ((getLastX() - getRX()) > 0.003f  && inputActive) {   // Same as above but to the left
            for (TextureRegion textureRegion : walkRightAnimation.getKeyFrames()) {
                if (!textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            for (TextureRegion textureRegion : walkStandAnimation.getKeyFrames()) {
                if (!textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            for (TextureRegion textureRegion : walkDownAnimation.getKeyFrames()) {
                if (textureRegion.isFlipX()) textureRegion.flip(true, false);
            }
            currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
        } else if ((getRY() - getLastY()) > 0.003f  && inputActive) {   // Sets walk up animation.
            currentFrame = walkUpAnimation.getKeyFrame(stateTime, true);
        } else if ((getLastY() - getRY()) > 0.003f  && inputActive) {   // Sets walk down animation
            currentFrame = walkDownAnimation.getKeyFrame(stateTime, true);
        } else {    // Sets idle frame if player isn't moving or inputActive is off.
            setLastX(getRX());
            setLastY(getRY());
            currentFrame = walkStandAnimation.getKeyFrame(stateTime, true);
        }
    }
}