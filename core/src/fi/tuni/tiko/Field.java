package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

public class Field extends Building {
    private final int P_PER_FIELD = 50;
    private final int N_NER_FIELD = 50;

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
     * TODO Typpeen ja fosforiin perustuva lannoitus
     */
    public GameData actionFertilizeField(GameData data, int n) {
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
