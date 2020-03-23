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
        data.setActionsDone(data.getActionsDone() + 1);
        return data;
    }

    /**
     * Increase data.methaneSold by data.methane. data.methane reduction handled in GameData update.
     */
    public GameData actionSellGas(GameData data) {
        if (data.getMethane() > 0) {
            data.setMethaneSold(data.getMethane());
            data.setActionsDone(data.getActionsDone() + 1);
            // TODO methane sold UI message
        } else {
            // TODO no methane to sell UI message
        }

        return data;
    }

    public void dispose() {
        background.dispose();
        iconSource.dispose();
    }
}
