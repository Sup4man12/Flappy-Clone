package com.game.flappy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Flappy extends Game {

	SpriteBatch batch, secondLayer;
	BitmapFont font;
	int bestScore;

   Texture cenarioImg;
   Texture groundImg;
	public void create() {
		bestScore = 0;
		batch = new SpriteBatch();
		secondLayer = new SpriteBatch();
      cenarioImg = new Texture(Gdx.files.internal("cenario.png"));
      groundImg = new Texture(Gdx.files.internal("ground.png"));
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MenuScreen(this));
	}
	public void render() {
		super.render(); // important!
	}

	public void dispose() {
           groundImg.dispose();
           cenarioImg.dispose();
		batch.dispose();
		secondLayer.dispose();
		font.dispose();
	}
}
