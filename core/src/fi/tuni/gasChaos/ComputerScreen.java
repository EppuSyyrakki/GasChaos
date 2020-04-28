package fi.tuni.gasChaos;

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
            userInterface.dialogFocus = true;
            setActionInputActive(false);
            uiText = getBankDialogText();
            Dialog d = new Dialog(game.myBundle.get("postDialogTitle"), userInterface.skin) {
                protected void result(Object object) {
                    game.gameData.saveGame();
                    resetInputProcessor();
                    remove();
                }
            };
            userInterface.createDialog(d, uiText, false);
        }
    }

    private String getBankDialogText() {
        int milkMoney = moneyFromMilk();
        int grainMoney = game.gameData.getGrainSold() * game.gameData.MONEY_FROM_GRAIN;
        float floatMethaneMoney = game.gameData.getMethaneSold() * game.gameData.MONEY_FROM_METHANE;
        int methaneMoney = (int)floatMethaneMoney;
        float floatManureMoney = game.gameData.getManureSold() * game.gameData.MONEY_FROM_MANURE;
        int manureMoney = (int)floatManureMoney;
        int gardenMoney = game.gameData.getGardenSold() * game.gameData.MONEY_FROM_GARDEN;
        int fertilizerMoney = (game.gameData.getNSold() * game.gameData.MONEY_FROM_N)
                + (game.gameData.getPSold() * game.gameData.MONEY_FROM_P);
        int totalIncome = milkMoney + grainMoney + methaneMoney + manureMoney + gardenMoney
                + fertilizerMoney;
        int fieldPenalty = 0;
        int debtPayment = 0;
        int interest = 0;
        if (game.gameData.getCurrentTurn() % game.gameData.TURNS_BETWEEN_PAYMENTS == 0) {
            if (game.gameData.isFieldPenalty()) {
                fieldPenalty = game.gameData.PENALTY_PAYMENT;
            }
            debtPayment = game.gameData.getDebtPayment();
            interest = calculateDebtInterest();
        }

        int totalExpenses = calculateElectricity() + calculateFieldRent() + calculatePetrol()
                + interest + debtPayment;
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
        text += game.myBundle.format("electricity", calculateElectricity()) + "\n";
        text += game.myBundle.format("fieldRent", calculateFieldRent()) + "\n";
        text += game.myBundle.format("petrol", calculatePetrol()) + "\n\n";

        if (debtPayment != 0) {
            text += game.myBundle.format("debtPayment", debtPayment) + "\n";
            text += game.myBundle.format("interest", interest) + "\n";
        }

        if (fieldPenalty != 0) {
            text += game.myBundle.format("fieldPenalty", fieldPenalty) + "\n";
        }

        text += game.myBundle.format("incomeProject",
                (totalIncome - totalExpenses)) + "\n";
        text += game.myBundle.format("balanceProject",
                (game.gameData.getMoney() + (totalIncome - totalExpenses))) + "\n";
        return text;
    }

    private int moneyFromMilk() {
        int milkAmount = 0;
        ArrayList<Cow> tmpCowList = game.gameData.getCowList();

        for (Cow cow : tmpCowList) {
            if (cow.isEatenLastTurn()) {  // if cow not eaten, no milk
                int milkFromCow = cow.getMilk(game.gameData.getMilkingMachineLevel());

                if (game.gameData.getManureInBarn() > game.gameData.MANURE_DANGER) {
                    milkFromCow -= (milkFromCow / 3);
                }
                milkAmount +=milkFromCow;
            }
        }
        int milkSold = milkAmount * game.gameData.MONEY_FROM_MILK;
        return milkSold;
    }

    private int calculateDebtInterest() {
        float floatInterest = (float)game.gameData.getDebt() * game.gameData.getInterest();
        return (int)floatInterest;
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
                } else {
                    game.gameData.setAllVisited(true);
                }
                resetInputProcessor();
                remove();
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
        setActionInputActive(true);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

}
