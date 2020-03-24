package fi.tuni.tiko;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

public class GasChaosMain extends Game {
	SpriteBatch batch;
	OrthographicCamera camera;

	GameData gameData;
	FarmScreen farmScreen;
	MenuScreen menuScreen;

	Player player;
	Maps maps;
	TiledMapRenderer tiledMapRenderer;
	TiledMap tiledMap;
	
	@Override
	public void create () {
		gameData = new GameData();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		menuScreen = new MenuScreen();

		// move this to menuScreen eventually and setScreen(menuScreen) too.
		farmScreen = new FarmScreen(batch, camera, this, menuScreen);
		setScreen(farmScreen);

		/*
		debugger();
		player = new Player();
		maps = new Maps();
		// maps.MapField(player, tiledMap);
		tiledMap = new TmxMapLoader().load("fields2.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 100f);
		*/

	}

	@Override
	public void render () {
		super.render();

		/*
		//maps.checkCollisions(tiledMap);
		tiledMapRenderer.render();

		Locale locale = new Locale("fi", "Finland");
		Locale defaultLocale = Locale.getDefault();
		I18NBundle myBundle =
				I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);

		//Localization example
		String locTest1 = myBundle.get("buyCowComplete");
		String locTest2 = myBundle.format("turnInfoExample", gameData.getCurrentTurn());
		System.out.println(locTest1);
		System.out.println(locTest2);
		//End of example
		*/
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}



}
