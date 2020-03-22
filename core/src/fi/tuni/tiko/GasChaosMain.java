package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class GasChaosMain extends ApplicationAdapter {
	SpriteBatch batch;
	Home home;
	Barn barn;
	Field field;
	Garden garden;
	GasTank gasTank;
	Farm farm;
	GameData gameData;
	Player player;
	Maps maps;
	TiledMapRenderer tiledMapRenderer;
	TiledMap tiledMap;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		farm = new Farm();
		home = new Home();
		barn = new Barn();
		field = new Field();
		garden = new Garden();
		gasTank = new GasTank();
		gameData = new GameData();
		player = new Player();
		maps = new Maps();
		debugger();
		// maps.MapField(player, tiledMap);
		tiledMap = new TmxMapLoader().load("fields2.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 100f);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

		batch.begin();

		// batch.draw(farm.background, 0, 0);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		farm.dispose();
		home.dispose();
		barn.dispose();
		field.dispose();
		garden.dispose();
		gasTank.dispose();
	}

	public void debugger () {
		Gdx.app.log("Other", "y = " + gameData.getActionsDone());

		int[] field = gameData.getFields();
		int[] fieldGrowth = gameData.getFields();
		int[] fieldFertilizer = gameData.getFields();
		int[] fieldRent = gameData.getFields();
		String fieldLog = "";
		String fieldGrowthLog = "";
		String fieldFertilizerLog = "";
		String fieldRentLog = "";

		for (int i = 0; i < field.length; i++) {
			fieldLog = fieldLog + ", " + field[i];
		}
		for (int i = 0; i < fieldGrowth.length; i++) {
			fieldGrowthLog = fieldGrowthLog + ", " + fieldGrowth[i];
		}
		for (int i = 0; i < fieldFertilizer.length; i++) {
			fieldFertilizerLog = fieldFertilizerLog + ", " + fieldFertilizer[i];
		}
		for (int i = 0; i < fieldRent.length; i++) {
			fieldRentLog = fieldRentLog + ", " + field[i];
		}

		// Upgrades
		Gdx.app.log("Upgrade", "Solar level = " + gameData.getSolarPanelLevel());
		Gdx.app.log("Upgrade", "Gas Collector level = " + gameData.getGasCollectorLevel());
		Gdx.app.log("Upgrade", "Milking machine level = " + gameData.getMilkingMachineLevel());
		Gdx.app.log("Upgrade", "Tractor level = " + gameData.getTractorLevel());
		Gdx.app.log("Upgrade", "Gas generator level = " + gameData.getGasGeneratorLevel());

		// Manure and Methane
		Gdx.app.log("Resource", "Manure = " + gameData.getManure());
		Gdx.app.log("Resource", "Manure max = " + gameData.getManureMax());
		Gdx.app.log("Resource", "Methane = " + gameData.getMethane());
		Gdx.app.log("Resource", "Methane max = " + gameData.getMethaneMax());

		// Fields
		Gdx.app.log("Resource array", "Fields = " + fieldLog);
		Gdx.app.log("Resource array", "Field growth = " + fieldGrowthLog);
		Gdx.app.log("Resource array", "Field fertilizer = " + fieldFertilizerLog);
		Gdx.app.log("Resource array", "Fields rented = " + fieldRentLog);

		// Cows
		Gdx.app.log("Resource", "Cow list = " + gameData.getCowList());
		Gdx.app.log("Resource", "Cows bought = " + gameData.getCowsBought());
		Gdx.app.log("Resource", "feed = " + gameData.getFeed());
		Gdx.app.log("Resource", "Feed max = " + gameData.getFeedMax());

		// Economy
		Gdx.app.log("Economy", "Grain sold = " + gameData.getGrainSold());
		Gdx.app.log("Economy", "Methane sold = " + gameData.getMethaneSold());
		Gdx.app.log("Economy", "Manure sold = " + gameData.getManureSold());
		Gdx.app.log("Economy", "Garden sold = " + gameData.getGardenSold());
		Gdx.app.log("Economy", "Feed bought = " + gameData.getFeedBought());
		Gdx.app.log("Economy", "Current turn = " + gameData.getCurrentTurn());
		Gdx.app.log("Economy", "Money = " + gameData.getMoney());

		// Bought check
		Gdx.app.log("Is bought", "Is basic solar bought = " + gameData.isSolarPanelBasicBought());
		Gdx.app.log("Is bought", "Is adv solar bought = " + gameData.isSolarPanelAdvBought());
		Gdx.app.log("Is bought", "Is adv gas collector bought = " + gameData.isGasCollectorAdvBought());
		Gdx.app.log("Is bought", "Is adv milking machine bought = " + gameData.isMilkingMachineAdvBought());
		Gdx.app.log("Is bought", "Is adv tractor bought = " + gameData.isTractorAdvBought());
		Gdx.app.log("Is bought", "Is tractor gas bought = " + gameData.isTractorGasBought());
		Gdx.app.log("Is bought", "Is gas generator bought = " + gameData.isGasGeneratorBought());

	}
}
