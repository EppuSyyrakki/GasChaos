package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

public class GasTank extends Building {

    public GasTank() {
        background = new Texture("gasTankBackground.png");
    }

    /**
     * Decrease data.methane by half
     */
    public GameData actionOpenEmergencyValve(GameData data) {
        data.setMethane(data.getMethane() / 2);
        // TODO methane tank depressurized UI message
        return data;
    }

    /**
     * Increase data.methaneSold by data.methane and set data.methane to 0.
     */
    public GameData actionEmptyGasTank(GameData data) {
        if (data.getMethane() == 0) {
            data.setMethaneSold(data.getMethane());
            data.setMethane(0);
        }
        // TODO methane sold UI message
        return data;
    }

    public void dispose() {
        background.dispose();
        iconSource.dispose();
    }
}
