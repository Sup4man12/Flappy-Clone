package com.game.flappy;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
   Texture canoImg;
   Sprite cano;
   Sound pontoSound, batidaSound, asasSound;
   Animation<TextureRegion> puloAnimation;
   Texture birdImg;
   private static final int COLUNA = 3;
   float tempo;

   int cenarioPos;
   int groundPos;
   int ultimoCano ;
   int score;
   int altura;
   float velocidade;
   Rectangle pipe, pipe2;
   Array<Rectangle> pipes;
   Rectangle bird;
   final Flappy game;
   OrthographicCamera camera;
   GameScreen(final Flappy gam){
      game = gam;
      asasSound = Gdx.audio.newSound(Gdx.files.internal("asas.wav"));
      pontoSound = Gdx.audio.newSound(Gdx.files.internal("ponto.wav"));
      batidaSound = Gdx.audio.newSound(Gdx.files.internal("batida.wav"));
      altura = 0; 
      cenarioPos = 0;
      groundPos = 0;
      score = 0;
      canoImg = new Texture(Gdx.files.internal("cano.png"));
      cano = new Sprite(canoImg);
      cano.flip(false, true);
      cano.setRotation(0.25f);

      ultimoCano = 450;

      birdImg = new Texture(Gdx.files.internal("flappy.png"));
      TextureRegion tmp[][] = TextureRegion.split(birdImg, birdImg.getWidth() / COLUNA, birdImg.getHeight() );
      TextureRegion[] puloFrames = new TextureRegion[COLUNA * 1];
      int index = 0;
         for (int j= 0; j < COLUNA; j++){
            puloFrames[index++] = tmp[0][j];
         }
      puloAnimation = new Animation<TextureRegion>(0.085f, puloFrames);
      tempo = 0f;
      bird = new Rectangle();
      bird.x = 20;
      bird.y = 400;
      bird.width = 32;
      bird.height = 32;
      velocidade = 0;
      pipe = new Rectangle();
      pipes = new Array<Rectangle>();
      camera = new OrthographicCamera(480, 800);                          
      camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);    
      camera.setToOrtho(false, 480, 800);
      camera.update();

      spawnPipe(ultimoCano);
      pular();
   }
   private void space(){
      if (Gdx.input.isKeyJustPressed(Keys.SPACE)|| (Gdx.input.justTouched()) || altura > 0){
         pular();
      }
      if ((Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.justTouched()) && altura > 0) {
         bird.y += 15;
         altura = 1;
      }

   }
   private void pular(){
      if (altura == 1 )
         asasSound.play();
      velocidade = 0;
      altura ++;
      bird.y += altura;
      if (altura > 13){
         altura = 0;
      }
   }

   private void gravity(){
      if (bird.y > 0 + bird.height && altura == 0 )
         bird.y -=((200 + velocidade) * Gdx.graphics.getDeltaTime());
   }
   private void spawnPipe(int x){
      pipe = new Rectangle();
      pipe.x = x;
      pipe.y = MathUtils.random( (-canoImg.getHeight() + 90 ) , -40);
      pipe.height = canoImg.getHeight();
      pipe.width = canoImg.getWidth();
      pipe2 = new Rectangle();
      pipe2.x = x;
      pipe2.y = pipe.y + 800;
      pipe2.height = canoImg.getHeight();
      pipe2.width = canoImg.getWidth();
      pipes.add(pipe, pipe2);
   }
   @Override
   public void render(float delta) {
      ScreenUtils.clear(0, 0, 1, 1);
      gravity();
      space();
      camera.position.set(camera.position.x+=250 * Gdx.graphics.getDeltaTime(), camera.position.y, 0);
      bird.x+=250 *Gdx.graphics.getDeltaTime();
      camera.update();
      game.batch.setProjectionMatrix(camera.combined);
      Iterator<Rectangle> iter = pipes.iterator();
      tempo += Gdx.graphics.getDeltaTime(); 
      TextureRegion frameAtual = puloAnimation.getKeyFrame(tempo, true);
      game.batch.begin();
      game.batch.draw(game.cenarioImg, -20 + bird.x -   ++cenarioPos  , 0 );
      game.batch.draw(game.cenarioImg,  -20 +  bird.x + 480 - cenarioPos   , 0 );
      if (cenarioPos % 480 ==0)
         cenarioPos-=480;
      game.batch.draw(frameAtual, bird.x, bird.y);
      for (int a = 0; a < pipes.size ; a++){
         if(a % 2 == 0)
            game.batch.draw(canoImg, pipes.get(a).x, pipes.get(a).y);
         else
            game.batch.draw(cano, pipes.get(a).x, pipes.get(a).y);
         if(bird.overlaps(pipes.get(a)) || bird.y <= game.groundImg.getHeight() || bird.y > 800){
            game.setScreen(new GameOver(game));
            batidaSound.play();
            dispose();
         }
      }
      velocidade+=12;

      if(pipe.x < bird.x + 350){
         spawnPipe(ultimoCano + 350);
         if (bird.x > ultimoCano /2){
            score++;
            pontoSound.play();}
         ultimoCano += 350;
      }
      game.batch.draw(game.groundImg, -20 + bird.x -   (groundPos+=8)  , 0 );
      game.batch.draw(game.groundImg,  -20 +  bird.x + game.groundImg.getWidth() - groundPos   , 0 );
      if (groundPos % game.groundImg.getWidth() ==0)
         groundPos-=game.groundImg.getWidth();
      game.batch.end();
      game.secondLayer.begin();
      game.font.draw(game.secondLayer, "Metros: " + (int) bird.x,  20, 800 - 20 );
      game.font.draw(game.secondLayer, "Score: " + score,    20, 800 - 40 );
      game.secondLayer.end();
      while(iter.hasNext()){
         pipe = iter.next();
         if (bird.x -200 > pipe.x){
            iter.remove();
         }
         pipe2 = iter.next();
         if (bird.x -200 > pipe2.x){
            iter.remove();
         }
      }
   }

   @Override
   public void resize(int width, int height) {
   }

   @Override
   public void show() {
   }

   @Override
   public void hide() {
   }

   @Override
   public void pause() {
   }

   @Override
   public void resume() {
   }

   @Override
   public void dispose() {
      birdImg.dispose();
      canoImg.dispose();
      cano.getTexture().dispose();
      pontoSound.dispose();  
      asasSound.dispose();
      if (score > game.bestScore)
         game.bestScore = score;
   }
}
