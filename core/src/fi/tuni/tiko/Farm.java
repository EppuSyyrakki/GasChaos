package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

public class Farm {
    Texture background;

    public Farm () {
        background = new Texture("farmBackground.png");
    }

    public void dispose() {
        background.dispose();
    }
}
