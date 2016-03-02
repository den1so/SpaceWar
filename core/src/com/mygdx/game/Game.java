package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Game implements Screen {

	final Drop game;
	OrthographicCamera cam;
	SpriteBatch batch;
	Texture pltImg;
	Texture BulletImg;
	Texture doodleImg;
    Texture fonImg;
    Rectangle fon;
	Rectangle doodle;
    Array<Rectangle> plts;
	Array<Rectangle> bullets;
	Vector3 touchPos;
	long lastPltTime;
	long lastBltTime;
	int score;
    int lifeScore = 3;
	//boolean shot = false;




	public  Game (final Drop gam) {
		this.game = gam;

		batch = new SpriteBatch();

		cam = new OrthographicCamera();
		cam.setToOrtho(false, 272, 408);

		touchPos = new Vector3();

		pltImg = new Texture("nlo.png");
		doodleImg = new Texture("ship_big.png");
		BulletImg = new Texture("bullet.png");
		//bulletImg = new Texture("bullet.png");


		doodle = new Rectangle();
		doodle.x = 272/2 - 58/2;
		doodle.y = 10;
		doodle.width = 58;
		doodle.height = 71;

        fonImg = new Texture("fon.png");
        fon = new Rectangle();
        fon.x = 0;
        fon.y = 0;
        fon.width = 272;
        fon.height = 408;

		plts = new Array<Rectangle>();
		spawnPltdrop();
		bullets = new Array<Rectangle>();


	}

	private void spawnPltdrop(){
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 272 - 48);
		raindrop.y = 408;
		raindrop.width =48;
		raindrop.height = 24;
		plts.add(raindrop);
		lastPltTime = TimeUtils.nanoTime();
	}

	private void bulletSpawn(float bulletX){
		Rectangle blt = new Rectangle();
		blt.x = bulletX + 24;
		blt.y = 81;
		blt.width =10;
		blt.height = 10;
		bullets.add(blt);
        lastBltTime = TimeUtils.nanoTime();
	}



	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();

		game.batch.setProjectionMatrix(cam.combined);
		game.batch.begin();
        game.batch.draw(fonImg, fon.x, fon.y);
		game.font.draw(game.batch, "Score: " + score, 0, 400);
        game.font.draw(game.batch,"Health: " + lifeScore, 200,400 );
		game.batch.draw(doodleImg, doodle.x, doodle.y);
		for(Rectangle plt: plts){
			game.batch.draw(pltImg, plt.x, plt.y);
		}

		for(Rectangle blt: bullets){
			game.batch.draw(BulletImg, blt.x, blt.y);
		}

		game.batch.end();

		if(Gdx.input.isTouched()){
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(touchPos);
			doodle.x = (int) (touchPos.x - 58/2);
            if(TimeUtils.nanoTime() - lastBltTime > 200000000 && touchPos.y <= 90 ) {
                bulletSpawn(doodle.x);
            }
		}



		if(doodle.x < 0) doodle.x = 0;
		if(doodle.x > 272-58) doodle.x = 272-58;


		if(TimeUtils.nanoTime() - lastPltTime > 2*1000000000){
			spawnPltdrop();
		}

		Iterator<Rectangle> iterator = plts.iterator();
        while(iterator.hasNext()) {
            Rectangle plt = iterator.next();
            plt.y -= 100 * Gdx.graphics.getDeltaTime();
            if (plt.y + 24 < 0) iterator.remove();
            if (plt.overlaps(doodle)) {
                lifeScore--;
                iterator.remove();
            }
        }

        Iterator<Rectangle> iterator1 = bullets.iterator();
        while (iterator1.hasNext()) {
                Rectangle blt = iterator1.next();
                blt.y += 100 * Gdx.graphics.getDeltaTime();
                if (blt.y + 10 > 398) iterator1.remove();
        }

        //
        Collosion();

        if(lifeScore <= 0){
            game.setScreen(new GameOver(game,score));
            dispose();
        }

	}

    private void Collosion() {
        Iterator<Rectangle> b = bullets.iterator();
        while(b.hasNext()) {
            Rectangle balls = b.next();
            Iterator<Rectangle> i = plts.iterator();
            while(i.hasNext()) {
                Rectangle enemies = i.next();

                if ((Math.abs(balls.x - enemies.x) <= (balls.width + enemies.width) / 2f)
                        && (Math.abs(balls.y - enemies.y) <= (balls.height + enemies.height) / 2f)) {
                    i.remove();
                    b.remove();
                    score++;
                }
            }
        }
    }

    @Override
	public void show() {

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

		pltImg.dispose();
		doodleImg.dispose();
        BulletImg.dispose();
		batch.dispose();
	}
}
