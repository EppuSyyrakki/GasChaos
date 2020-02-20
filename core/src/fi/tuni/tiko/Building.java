package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Building extends Farm {
    private String name;
    private Texture iconSource;     // Pääruudun kuvakkeen eri versiot (jos päivittyy)
    private TextureRegion icons;    // kuvakkeet osina
    private Sprite icon;            // Pääruudun nappi

    public Building(String name) {
        this.name = name;
    }

    public void ActionResolver(String name) {   // Ottaa aliluokista toimintoja vastaan

    }
}

