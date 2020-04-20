package fi.tuni.tiko;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;


public class GasChaosMain extends Game {
	SpriteBatch batch;
	OrthographicCamera camera;

	GameData gameData;
	MenuScreen menuScreen;
	FarmScreen farmScreen;
	HomeScreen homeScreen;
	FieldScreen fieldScreen;
	BarnScreen barnScreen;
	GardenScreen gardenScreen;
	GasTankScreen gasTankScreen;
	GrandmotherScreen grandmotherScreen;
	ComputerScreen computerScreen;
	UpgradeScreen upgradeScreen;
	BuySellScreen buySellScreen;
	HighScore highScoreScreen;
	// Locale locale;
	// Locale defaultLocale;
	I18NBundle myBundle;
	Locale locale;


	@Override
	public void create () {

		myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
				langCheck(), "UTF-8");

		gameData = new GameData();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		menuScreen = new MenuScreen();
		farmScreen = new FarmScreen(batch, camera, this);
		homeScreen = new HomeScreen(batch, camera, this);
		fieldScreen = new FieldScreen(batch, camera, this);
		barnScreen = new BarnScreen(batch, camera, this);
		gardenScreen = new GardenScreen(batch, camera, this);
		gasTankScreen = new GasTankScreen(batch, camera, this);
		grandmotherScreen = new GrandmotherScreen(batch, camera, this);
		computerScreen = new ComputerScreen(batch, camera, this);
		upgradeScreen = new UpgradeScreen(batch, camera, this);
		buySellScreen = new BuySellScreen(batch, camera, this);
		highScoreScreen = new HighScore(batch, camera, this);

		//setHighScoreScreen();
		setFarmScreen();
	}

	public Locale langCheck () {
		String lang;
		// Gets current language from android.
		lang = java.util.Locale.getDefault().toString();
		//lang = "fi_FI";

		if (lang.equals("fi_FI")) {
			locale = new Locale("fi", "FI");
		} else {
			locale = new Locale("en", "US");
		}
		return locale;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void setFarmScreen() {
		setScreen(farmScreen);
	}

	public void setHomeScreen() {
		setScreen(homeScreen);
	}

	public void setNewTurn() {
		gameData.updateResources();
		farmScreen.player.setRX(2);
		farmScreen.player.setRY(5);
		farmScreen.player.matchX(2);
		farmScreen.player.matchY(5);
		homeScreen.setNewTurn(true);
		homeScreen.resetInputProcessor();
		setScreen(homeScreen);

	}

	public void setComputerScreen() {
		setScreen(computerScreen);
	}

	public void setMenuScreen () {
		setScreen(menuScreen);
	}

	public void setBarnScreen () {
		setScreen(barnScreen);
	}

	public void setGardenScreen () {
		setScreen(gardenScreen);
	}

	public void setGrandmotherScreen () {
		setScreen(grandmotherScreen);
	}

	public void setFieldScreen () {
		setScreen(fieldScreen);
	}

	public void setGasTankScreen () {
		setScreen(gasTankScreen);
	}

	public void setUpgradeScreen() {
		setScreen(upgradeScreen);
	}

	public void setBuySellScreen() {
		setScreen(buySellScreen);
	}

	public void setHighScoreScreen() {
		setScreen(highScoreScreen);
	}

	public void setGameData(GameData data) {
		this.gameData = data;
	}
}
