
package com.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {
   final Flappy game;
   OrthographicCamera camera;
   Rectangle uwuchad;
   Texture uwuchadLogo;
      Vector3 touchPoint;
   MenuScreen(final Flappy gam){
      game = gam;
      camera = new OrthographicCamera();
      uwuchad = new Rectangle();
      uwuchadLogo = new Texture(Gdx.files.internal("uwuchad_button.png"));
      uwuchad.y = 800 - 10 - uwuchadLogo.getHeight();
      uwuchad.x = 480 - 10 - uwuchadLogo.getWidth() ;
      uwuchad.width = uwuchadLogo.getWidth();
      uwuchad.height = uwuchadLogo.getHeight();
      touchPoint = new Vector3();
      camera.setToOrtho(false, 480, 800);
   }
   @Override
   public void render(float delta) {
      ScreenUtils.clear(0, 0, 1, 1);
      camera.update();
      game.batch.setProjectionMatrix(camera.combined);
      game.batch.begin();
      game.batch.draw(game.cenarioImg,0,0);
      game.batch.draw(game.groundImg,0,0);
      game.batch.draw(uwuchadLogo,uwuchad.x,  uwuchad.y);
      game.font.draw(game.batch, "Flappy Bird" ,   480/2 - 40  , 800/2 );
      game.font.draw(game.batch, "Pressione [SPACE] para começar" ,   480/2 - 110  , 800/2 -20 );
      game.batch.end();
      if(Gdx.input.isTouched())
         touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      if (Gdx.input.isTouched() && uwuchad.contains(touchPoint.x, 800 - touchPoint.y)){
         Gdx.net.openURI("https://uwuchad.codeberg.page/");
         touchPoint.set(0, 0 , 0);
      }       else if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isTouched()){
       game.setScreen(new GameScreen(game));
       dispose();}
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
      uwuchadLogo.dispose();
   }
}
