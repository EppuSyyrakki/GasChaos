package fi.tuni.tiko;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Maps {

    Player player;

    int TILE_WIDTH = 120;
    int TILE_HEIGHT = 120;

    public void MapField(Player player, TiledMap tiledMap) {

        this.player = new Player();
        // Tiled Map load
        tiledMap = new TmxMapLoader().load("fields2.tmx");


    }

    @SuppressWarnings("RedundantCast")
    public void checkCollisions(TiledMap tiledMap) {

        MapLayer collisionLayer = (MapLayer)tiledMap.getLayers().get("exit");

        // All the rectangles of the layer
        MapObjects mapObjects = collisionLayer.getObjects();

        // add to RectangleObjects to an array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate all of the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();
            Rectangle rectangle = scaleRect(tmp, 1 / 100f);

            // scale down rectangle using world dimensions.
            if (player.getBoundingRectangle().overlaps(rectangle)) {
                System.out.println("Crash");
            }
        }
    }

    private Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangle = new Rectangle();
        rectangle.x      = r.x * scale;
        rectangle.y      = r.y * scale;
        rectangle.width  = r.width * scale;
        rectangle.height = r.height * scale;
        return rectangle;
    }
}
