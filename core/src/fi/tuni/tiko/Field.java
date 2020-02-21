package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

public class Field extends Building {
    private int[] fieldGrowth = new int[] {1, 1, 1, 1, 1, 1};

    public Field() {
        background = new Texture("fieldBackground.png");

    }

    /**
     * Increases data.grainSold by data.field[number] and resets data.field[number] to 1.
     * Blocks action if data.field[number] is less than 15 -> grain not ripe.
     */
    public GameData actionSowField(GameData data, int number) {

        return data;
    }

    /**
     * Sets fieldGrowth to 2 in data.field[number]. Blocks action if that
     */
    public GameData actionFertilizeField(GameData data, int number) {

        return data;
    }

    public GameData actionReapField(GameData data, int fieldNumber) {

        return data;
    }

    public void update() {

    }

    public void dispose() {
        background.dispose();
        iconSource.dispose();
    }
}
