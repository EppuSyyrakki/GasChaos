package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import java.util.ArrayList;

public class ComputerScreen extends Location implements Screen {

    public ComputerScreen(SpriteBatch batch, OrthographicCamera camera, GasChaosMain game) {
        super(game);
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.batch = batch;
        this.camera = camera;
        userInterface = new UserInterface(game.myBundle);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        tiledMap = new TmxMapLoader().load("maps/Computer.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_SCALE);
        background = new Texture("ground/computerBackground.png");
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        if (fadeIn) {
            fadeFromBlack();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || getUIRec("RectangleExit")) {
            game.setHomeScreen();
            game.homeScreen.player.match();
        }

        batch.begin();
        black.draw(batch, blackness);
        batch.end();

        if (!game.gameData.isComputerVisited() && !userInterface.dialogFocus) {
            computerTutorial();
        }

        userInterface.render(game.gameData);
        checkActionRectangles();
    }

    private void checkActionRectangles() {
        if (((Gdx.input.isKeyJustPressed(Input.Keys.BACK)) || getUIRec("RectangleExit"))
             && !userInterface.dialogFocus) {    // move back to home
            game.setHomeScreen();
        }

        if (getUIRec("RectangleUpgrades") && !userInterface.dialogFocus) {    // go to upgrades screen
            game.setUpgradeScreen();
        }

        if (getUIRec("RectangleBuySell") && !userInterface.dialogFocus) {    // go to buy/sell screen
            game.setBuySellScreen();
        }

        if (getUIRec("RectangleFinances") && !userInterface.dialogFocus) {
            uiText = getBankDialogText();
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    if (result) {
                        game.gameData.saveGame();
                        resetInputProcessor();
                        remove();
                    }
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    private String getBankDialogText() {
        int milkMoney = moneyFromMilk();
        int grainMoney = game.gameData.getGrainSold() * game.gameData.MONEY_FROM_GRAIN;
        int methaneMoney = game.gameData.getMethaneSold() * game.gameData.MONEY_FROM_METHANE;
        int manureMoney = game.gameData.getManureSold() * game.gameData.MONEY_FROM_MANURE;
        int gardenMoney = game.gameData.getGardenSold() * game.gameData.MONEY_FROM_GARDEN;
        int fertilizerMoney = (game.gameData.getNSold() * game.gameData.MONEY_FROM_N)
                + (game.gameData.getPSold() * game.gameData.MONEY_FROM_P);
        int totalIncome = milkMoney + grainMoney + methaneMoney + manureMoney + gardenMoney
                + fertilizerMoney;
        int totalExpenses = 0 - calculateDebtPayment() - calculateElectricity()
                - calculateElectricity() - calculateFieldRent() - calculatePetrol();
        String text = game.myBundle.format("balance", game.gameData.getMoney())  + "\n";
        text += game.myBundle.format("debt", game.gameData.getDebt()) + "\n\n";
        text += game.myBundle.get("income") + "\n";
        text += game.myBundle.format("milkSold", milkMoney) + "\n";
        text += game.myBundle.format("grainSold", grainMoney) + "\n";
        text += game.myBundle.format("methaneSold", methaneMoney) + "\n";
        text += game.myBundle.format("manureSold", manureMoney) + "\n";
        text += game.myBundle.format("gardenSold", gardenMoney) + "\n";
        text += game.myBundle.format("fertilizersSold", fertilizerMoney) + "\n\n";
        text += game.myBundle.get("expenses") + "\n";
        text += game.myBundle.format("debtPayment", calculateDebtPayment()) + "\n";
        text += game.myBundle.format("electricity", calculateElectricity()) + "\n";
        text += game.myBundle.format("fieldRent", calculateFieldRent()) + "\n";
        text += game.myBundle.format("petrol", calculatePetrol()) + "\n\n";
        text += game.myBundle.format("incomeProject",
                (totalIncome + totalExpenses)) + "\n";
        text += game.myBundle.format("balanceProject",
                (game.gameData.getMoney() + (totalIncome + totalExpenses))) + "\n";
        return text;
    }

    private int moneyFromMilk() {
        int milkSold = 0;
        ArrayList<Cow> tmpCowList = game.gameData.getCowList();
        for (Cow cow : tmpCowList) {
            if (cow.isEatenThisTurn()) {  // if cow not eaten, no milk, manure and methane produced
                int milkFromCow = cow.getMilk(game.gameData.getMilkingMachineLevel());
                if (game.gameData.getManureInBarn() > game.gameData.MANURE_DANGER) {
                    milkFromCow -= (milkFromCow / 3);
                }
                milkSold += milkFromCow;
            }
        }
        return milkSold * game.gameData.MONEY_FROM_MILK;
    }

    private int calculateDebtPayment() {
        int debtPaymentThisTurn = 0;
        if (game.gameData.getCurrentTurn() % 2 == 0) { // debt is paid on every other turn.
            float floatPayment = (float)game.gameData.getDebt() * game.gameData.getInterest()
                    + game.gameData.getDebtPayment();
            debtPaymentThisTurn = (int)floatPayment;
        }
        if (debtPaymentThisTurn > game.gameData.getDebt()) {
            debtPaymentThisTurn = game.gameData.getDebt();
        }
        return debtPaymentThisTurn;
    }

    private int calculateElectricity() {
        int electricity = game.gameData.getElectricity();
        int solarPanelLevel = game.gameData.getSolarPanelLevel();
        int gasGeneratorLevel = game.gameData.getGasGeneratorLevel();
        int electricityThisTurn = electricity;
        if (solarPanelLevel == 1) {
            electricityThisTurn = electricity - (electricity / 3);  // 67
        } else if (solarPanelLevel == 2) {
            electricityThisTurn = electricity - (electricity / 3) - (electricity / 3);  // 33
        }
        if (gasGeneratorLevel == 1) {
            electricityThisTurn = electricity - (electricity / 3);  // 0
        }
        return electricityThisTurn;
    }

    private int calculateFieldRent() {
        ArrayList<Field> fields = game.gameData.getFields();
        int fieldsRentThisTurn = 0;
        for (Field field : fields) {
            if (field.isRented()) {
                fieldsRentThisTurn += game.gameData.PRICE_OF_FIELD;
            }
        }
        return fieldsRentThisTurn;
    }

    private int calculatePetrol() {
        int tractorLevel = game.gameData.getTractorLevel();
        int petrol = game.gameData.getPetrol();
        int petrolThisTurn = petrol;
        if (tractorLevel == 2) {
            petrolThisTurn = petrol / 2;
        } else if (tractorLevel == 3) {
            petrolThisTurn = 0;
        }
        return petrolThisTurn;
    }

    private void computerTutorial() {
        userInterface.dialogFocus = true;
        uiText = game.myBundle.get("tutorialComputer") + "\n";
        Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    game.gameData.setComputerVisited(true);
                    userInterface.dialogFocus = false;
                    resetInputProcessor();
                    remove();
                }
            }
        };
        userInterface.showTutorial(d, uiText);
    }

    @Override
    public void show() {
        blackness = 1;
        fadeIn = true;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        sunsetTexture.dispose();
        blackTexture.dispose();
        userInterface.dispose();
        tiledMap.dispose();
    }

    private void resetInputProcessor() {
        userInterface.dialogFocus = false;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

}
