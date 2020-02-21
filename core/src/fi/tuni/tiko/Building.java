package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Building {
    Texture background;
    Texture iconSource;     // source texture for icon containing all versions
    TextureRegion[] icons;  // different versions divided to regions
    Sprite icon;

    public void dispose() {
        background.dispose();
        iconSource.dispose();
    }
}
