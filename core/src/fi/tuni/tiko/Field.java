package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

public class Field extends Building {
    private final int MANURE_PER_FIELD = 50;

    public Field() {
        background = new Texture("fieldBackground.png");
    }

    /**
     * Increases field[n] from 1 to 2. Blocked if not 1.
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
     * Sets fieldGrowth to 2 in field[n] and fieldFertilizer[n] to 5. Blocked if
     * fieldFertilizer[n] > 0 or if field[n] is 0 or 1 (not rented / sown)
     */
    public GameData actionFertilizeField(GameData data, int n) {
        int[] tmpFields = data.getFields();
        int[] tmpFieldGrowth = data.getFieldGrowth();
        int[] tmpFieldFertilizer = data.getFieldFertilizer();

        if (tmpFields[n] == 0 || tmpFields[n] == 1) {
            // TODO block action, can't fertilize that field UI message
        } else if (tmpFieldFertilizer[n] > 0) {
            // TODO block action, that field already fertilized UI message
        } else if (data.getManure() < MANURE_PER_FIELD) {
            // TODO block action, not enough manure to fertilize
        } else {
            tmpFieldFertilizer[n] = 5;
            tmpFieldGrowth[n] = 2;
            data.setManure(data.getManure() - MANURE_PER_FIELD);
            // TODO field fertilized UI message
            data.setActionsDone(data.getActionsDone() + 1);
        }
        data.setFieldFertilizer(tmpFieldFertilizer);
        data.setFieldGrowth(tmpFieldGrowth);
        return data;
    }

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
     * Increase all data.fields[] that are not 0, 1 or 30 by data.fieldGrowth. fieldGrowth is 2 if
     * fieldFertilizer > 0. Else fieldGrowth is 1.
     */
    public GameData update(GameData data) {
        int[] tmpFields = data.getFields();
        int[] tmpFieldGrowth = data.getFieldGrowth();
        int[] tmpFieldFertilizer = data.getFieldFertilizer();

        for (int i = 0; i < tmpFields.length; i++) {

            if (tmpFields[i] > 1 && tmpFields[i] < 30) {    // fields growing
                tmpFields[i] += tmpFieldGrowth[i];
            }

            if (tmpFieldFertilizer[i] > 0) {    // fertilizer decreasing
                tmpFieldFertilizer[i]--;
            }

            if (tmpFieldFertilizer[i] == 0) {   // if fertilizer = 0, growth = 1
                tmpFieldGrowth[i] = 1;
            }
        }
        data.setFields(tmpFields);
        data.setFieldGrowth(tmpFieldGrowth);
        data.setFieldFertilizer(tmpFieldFertilizer);
        return data;
    }

    public void dispose() {
        background.dispose();
        iconSource.dispose();
    }
}
