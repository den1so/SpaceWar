package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


public class GameOver implements Screen {
    private  int lifeScore;
    OrthographicCamera camera;
    Texture fonImg;
    Rectangle fon;
    final Drop game;


    public GameOver(Drop gam, int score) {
        this.game = gam;
        this.lifeScore = score;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 272, 408);

        fonImg = new Texture("GameOver.png");
        fon = new Rectangle();
        fon.x = 0;
        fon.y = 0;
        fon.width = 272;
        fon.height = 408;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(fonImg, fon.x, fon.y);
        game.font.draw(game.batch, "YOUR SCORE: " + lifeScore, 50, 125);
        game.batch.end();

        if(Gdx.input.isTouched()){
            game.setScreen(new Game(game));
            dispose();
        }
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

    }
}
