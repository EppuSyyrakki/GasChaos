package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

public class Farm {
    Texture background;
    Building barn;
    Building gasChamber;
    Building home;
    Building garden;

    public Farm () {
        barn = new Building("barn");
        gasChamber = new Building("gasChamber");
        home = new Building("home");
        garden = new Building("garden");
    }
}
