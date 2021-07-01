package com.example.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;


public class Flappybird extends ApplicationAdapter {


	SpriteBatch batch;


	Texture background, gameOver;
	Texture[] birds;
	Texture[] pipes;

	Random randomGenerator;
	ShapeRenderer shapeRenderer;

	BitmapFont font, font2;


	int flapState = 0;
	int highScore = 0;
	int score = 0;
	float birdY = 0;
	float velocity = 0;
	float gravity = (float) 1.3;
	float maxTubeOffset;
	float tubeVelocity = (float) 6.8;
	Circle birdCircle;
	Rectangle[] topPipeRectangle, bottomPipeRectangle, gapRectangle;

	int numberOfTubes = 4;

	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	float[] gapSize = new float[numberOfTubes];

	int gameState = 0;
 	int gap = 400;
 	int scoringTube = 0;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background-day.png");

		gameOver = new Texture("gameover.png");

		birds = new Texture[3];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		font2 = new BitmapFont();
		font2.setColor(Color.GOLD);
		font2.getData().setScale(8);



		birds[0] = new Texture("yellowbird-upflapR1.png");
		birds[1] = new Texture("yellowbird-midflapR1.png");
		birds[2] = new Texture("yellowbird-downflapR1.png");


		pipes = new Texture[2];

		pipes[0] = new Texture("pipe-green.png");
		pipes[1] = new Texture("pipeup-green.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth()  * 3/4;

		birdCircle = new Circle();
		topPipeRectangle = new Rectangle[numberOfTubes];
		bottomPipeRectangle = new Rectangle[numberOfTubes];
		shapeRenderer = new ShapeRenderer();

		StartGame();





	}

	public void StartGame() {

		birdY = Gdx.graphics.getHeight()/2 - birds[flapState].getHeight()/2; //bird starts at centre of screen.

		for (int i = 0; i < numberOfTubes; i++ ) {

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth()/2 - pipes[0].getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topPipeRectangle[i] = new Rectangle();

			bottomPipeRectangle[i] = new Rectangle();



		}

	}

	@Override
	public void render () {


		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.VIOLET);

			if (gameState == 1) {

				if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {

					score++;



					Gdx.app.log("Score", String.valueOf(score));

					if (scoringTube < numberOfTubes - 1) {

						scoringTube++;

					} else {

						scoringTube = 0;

					}



				}



				if (Gdx.input.justTouched()) {

					velocity = -23;


				}

				for (int i = 0; i < numberOfTubes; i++ ) {

					if (tubeX[i] < - pipes[0].getWidth()) {

						tubeX[i] += numberOfTubes * distanceBetweenTubes;
						tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

					} else {

						tubeX[i] = tubeX[i] - tubeVelocity;


					}

					batch.draw(pipes[0], tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
					batch.draw(pipes[1], tubeX[i], Gdx.graphics.getHeight() / 2 - pipes[1].getHeight() - gap / 2 + tubeOffset[i]);
					birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight()/2 , birds[flapState].getWidth()/2);
					topPipeRectangle[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], pipes[0].getWidth(), pipes[0].getHeight());
					bottomPipeRectangle[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 - pipes[1].getHeight() - gap / 2 + tubeOffset[i], pipes[1].getWidth(), pipes[1].getHeight());

					// gapRectangle.set(tubeX[i] + 200, (Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]) - 400, pipes[0].getWidth() / 100 , 400); // (Height of top Pipe - 400) with the size of rectangle being size of gap i. e. 400

					// shapeRenderer.rect(gapRectangle.x, gapRectangle.y, gapRectangle.width, gapRectangle.height);


					//shapeRenderer.rect(topPipeRectangle.x, topPipeRectangle.y, topPipeRectangle.width, topPipeRectangle.height);
					//shapeRenderer.rect(bottomPipeRectangle.x, bottomPipeRectangle.y, bottomPipeRectangle.width, bottomPipeRectangle.height);


					// To check for Intersections/Collisions with pipes

					if (Intersector.overlaps(birdCircle, topPipeRectangle[i]) || Intersector.overlaps(birdCircle, bottomPipeRectangle[i])) {

						gameState = 2;

						if (score > highScore) {

							highScore = score;

						}

					}

					// To  check for Intersections / Collisions with score




				}



				if (birdY > 0) {

					velocity = velocity + gravity;
					birdY -= velocity;

				} else {

					gameState = 2;
					if (score > highScore) {

						highScore = score;

					}

				}



			} else if (gameState == 0 ) {

				if (Gdx.input.justTouched()) {

					gameState = 1;

				}

			} else if (gameState == 2) {

				batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight()/2 - gameOver.getWidth() / 2);
				font2.draw(batch, "High Score : " + String.valueOf(highScore), 100, 1500);


				if (Gdx.input.justTouched()) {

					gameState = 1;
					StartGame();
					score = 0;
					scoringTube = 0;
					velocity = 0;
					if (score > highScore) {

						highScore = score;

					}

				}

			}




			if (flapState == 0) {
				flapState = 1;
			} else if (flapState == 1) {
				flapState = 2;
			} else if (flapState == 2) {
				flapState = 0;
			}



			batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
			font.draw(batch, String.valueOf(score), 100, 200);
			batch.end();





			//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);


			//shapeRenderer.end();


	}

	@Override
	public void dispose () {


	}
}
