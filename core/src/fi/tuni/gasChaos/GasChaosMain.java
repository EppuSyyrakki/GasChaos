package fi.tuni.gasChaos;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;


public class GasChaosMain extends Game {
	SpriteBatch batch;
	OrthographicCamera camera;
	boolean mercy = true;

	GameData gameData;
	MenuScreen menuScreen;
	FarmScreen farmScreen;
	HomeScreen homeScreen;
	FieldScreen fieldScreen;
	BarnScreen barnScreen;
	GardenScreen gardenScreen;
	GasTankScreen gasTankScreen;
	ComputerScreen computerScreen;
	UpgradeScreen upgradeScreen;
	BuySellScreen buySellScreen;
	HighScore highScoreScreen;
	LoseScreen loseScreen;
	I18NBundle myBundle;
	Locale locale;

	@Override
	public void create () {

		myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
				langCheck(), "UTF-8");

		gameData = new GameData();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		farmScreen = new FarmScreen(batch, camera, this);
		homeScreen = new HomeScreen(batch, camera, this);
		fieldScreen = new FieldScreen(batch, camera, this);
		barnScreen = new BarnScreen(batch, camera, this);
		gardenScreen = new GardenScreen(batch, camera, this);
		gasTankScreen = new GasTankScreen(batch, camera, this);
		computerScreen = new ComputerScreen(batch, camera, this);
		upgradeScreen = new UpgradeScreen(batch, camera, this);
		buySellScreen = new BuySellScreen(batch, camera, this);
		loseScreen = new LoseScreen(camera, this);
		menuScreen = new MenuScreen(camera, this);

		setMenuScreen();
		//setHighScoreScreen();
		//setFarmScreen();
	}

	public Locale langCheck () {
		// for debugging purposes, will change locale argument to
		// java.util.Locale.getDefault() at the end of development
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

	@SuppressWarnings("EmptyMethod")
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		farmScreen.dispose();
		fieldScreen.dispose();
		gardenScreen.dispose();
		gasTankScreen.dispose();
		//highScoreScreen.dispose();
		homeScreen.dispose();
		loseScreen.dispose();
		upgradeScreen.dispose();
		buySellScreen.dispose();
		menuScreen.dispose();

	}

	public void setFarmScreen() {
		setScreen(farmScreen);
	}

	public void setHomeScreen() {
		setScreen(homeScreen);
	}

	public void setNewTurn() {

		farmScreen.player.setRX(2);
		farmScreen.player.setRY(5);
		farmScreen.player.matchX(2);
		farmScreen.player.matchY(5);
		homeScreen.setNewTurn(true);
		if ((gameData.getMoney() + gameData.getGrainSold() + gameData.getMethaneSold()
				+ gameData.getManureSold() + gameData.getGardenSold()
				+ gameData.getNSold() + gameData.getPSold()) >= 0) {
			mercy = true;
		} else {
			mercy = false;
		}

		if (gameData.getDebt() <= 0 && !gameData.victory) {
			gameData.setInterest(0f);
			gameData.setDebtPayment(0);
			gameData.victory = true;
			highScoreScreen = new HighScore(camera, this);
			setHighScoreScreen();
		} else if (gameData.getMoney() < 0 && !mercy) {
			setLoseScreen();
			gameData.defeatMenu = true;
			gameData.defeat = true;
		} else {
			homeScreen.resetInputProcessor();
			setScreen(homeScreen);
			mercy = true;
		}
		gameData.updateResources();

		gameData.saveGame();

	}

	public void setComputerScreen() {
		setScreen(computerScreen);
	}

	public void setMenuScreen () {
		setScreen(menuScreen);
		gameData.menu = true;
	}

	public void setBarnScreen () {
		setScreen(barnScreen);
	}

	public void setGardenScreen () {
		setScreen(gardenScreen);
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

	public void setLoseScreen() {
		setScreen(loseScreen);
	}

	public void setGameData(GameData data) {
		this.gameData = data;
	}

	public void quitGame() {
		gameData.saveGame();
		Gdx.app.exit();
	}
}
