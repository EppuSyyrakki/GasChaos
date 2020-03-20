package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

public class Field extends Building {
    private final int MAX_P_PER_FIELD = 8;
    private final int MAX_N_NER_FIELD = 80;

    public Field() {
        background = new Texture("fieldBackground.png");
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
     * Reduce given field to 1 (owned but not sown) and increase
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
     * TODO Ravinteiden määrän kautta tapahtuva kasvu.
     */
    public GameData update(GameData data) {
        return data;
    }

    public void dispose() {
        background.dispose();
        iconSource.dispose();
    }
}
