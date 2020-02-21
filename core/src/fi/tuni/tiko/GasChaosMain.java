package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GasChaosMain extends ApplicationAdapter {
	SpriteBatch batch;
	Home home;
	Barn barn;
	Field field;
	Garden garden;
	GasTank gasTank;
	Farm farm;
	GameData gameData;
	
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
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		batch.draw(farm.background, 0, 0);

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
}
