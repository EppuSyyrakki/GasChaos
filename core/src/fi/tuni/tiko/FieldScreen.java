package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FieldScreen extends Location implements Screen {

    public FieldScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game, Screen parent) {
        background = new Texture("fieldBackground.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        this.parent = parent;
        this.game = game;
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (fadeIn) {
            fadeFromBlack();
        }

        batch.begin();
        batch.draw(background, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        black.draw(batch, blackness);
        batch.end();

        if (false) {    // condition return to farm
            game.setScreen(parent);
        }
    }

    /**
     * Increases field[n] from 1 to 2 (empty to sown). Blocked if not 1.
     */
    public GameData actionSowField(GameData data, int n) {
        int[] tmpFields = data.getFields();

        if (tmpFields[n] == 1) {
            tmpFields[n] = 2;
            // TODO field has been sown UI message
            data.setActionsDone(data.getActionsDone() + 1);
        } else {
            // TODO block action, can't sow that field UI message
        }

        data.setFields(tmpFields);
        return data;
    }

    /**
     * Increases Nitrogen in data.fieldFertilizerN by amount. n is number of field.
     */
    public GameData actionFertilizeFieldN(GameData data, int n, int amount) {
        int[] tmpFieldFertilizerN = data.getFieldFertilizerN();
        tmpFieldFertilizerN[n] += amount;
        data.setFieldFertilizerN(tmpFieldFertilizerN);
        return data;
    }

    /**
     * Increases Phosphorous in data.fieldFertilizerP[n] by amount. n is number of field.
     */
    public GameData actionFertilizeFieldP(GameData data, int n, int amount) {
        int[] tmpFieldFertilizerP = data.getFieldFertilizerP();
        tmpFieldFertilizerP[n] += amount;
        data.setFieldFertilizerP(tmpFieldFertilizerP);
        return data;
    }

    /**
     * Reduce given field to 1 (owned but not sown) and increase data.grainSold
     */
    public GameData actionReapField(GameData data, int n) {
        int[] tmpFields = data.getFields();

        if (tmpFields[n] < 2) {
            // TODO block action, nothing to reap UI message
        } else if (tmpFields[n] > 2 && tmpFields[n] < 21) {
            // TODO block action, field not ripe yet UI message
        } else {
            data.setGrainSold(data.getGrainSold() + tmpFields[n]);
            tmpFields[n] = 0;
            // TODO grain reaped and sold UI message
            data.setActionsDone(data.getActionsDone() + 1);
        }
        return data;
    }

    /**
     * Set next 0 element to 1 in data.newFields array. Block if all fields rented.
     */
    public GameData actionRentNewField(GameData data) {
        int[] tmpFields = data.getFields();
        int[] tmpFieldsRented = data.getFieldsRented();
        int hasFields = 2;

        for (int i = 2; i < tmpFields.length; i++) {    // start at 2 because 0 and 1 are owned
            if (tmpFields[i] > 0) {
                hasFields++;
            }

            if (tmpFields[i] == 0) {
                tmpFieldsRented[i] = 1;
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO new field rented UI message
                break;
            }
        }

        if (hasFields >= data.MAX_FIELDS) {
            // TODO no fields available to rent UI message
        }
        data.setFieldsRented(tmpFieldsRented);
        return data;
    }

    /**
     * Set last index that is not 0 to 0 in data.fields array. Blocked if only indexes 0 and 1 != 0.
     */
    public GameData actionStopRentingField(GameData data) {
        int[] tmpFields = data.getFields();
        int[] tmpFieldsRented = data.getFieldsRented();
        boolean fieldGivenUp = false;

        for (int i = tmpFields.length; i > 1; i--) {    // 0 and 1 are owned, so not touched here
            if (tmpFields[i] != 0) {
                tmpFieldsRented[i] = -1;
                fieldGivenUp = true;
                data.setActionsDone(data.getActionsDone() + 1);
                // TODO last rented field given up UI message
                break;
            }
        }

        if (!fieldGivenUp) {
            // TODO action blocked, no fields rented
        }
        data.setFieldsRented(tmpFieldsRented);
        return data;
    }

    @Override
    public void show() {
        blackness = 1;
        fadeIn = true;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
