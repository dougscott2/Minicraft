package com.theironyard.minicraft;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Minicraft extends ApplicationAdapter {
    FitViewport viewPort;
    final int WIDTH = 100;
    final int HEIGHT = 100;

    SpriteBatch batch;
    TextureRegion down, up, right, left, stand;

    float time;
    float x = 0;
    float y = 0;
    float xv = 0;
    float yv = 0;

    final float MAX_VELOCITY = 1000;

    @Override
    public void create () {
        batch = new SpriteBatch();
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Texture tiles = new Texture("tiles.png");
        TextureRegion[][] grid = TextureRegion.split(tiles, 16, 16);
        down = grid[6][0];
        up = grid[6][1];
        //right = grid[6][2];
        //left = grid[6][3];
        right = grid[6][3];
        left = new TextureRegion(right);
        left.flip(true, false);
        stand = grid[6][2];
    }
    @Override
    public void render () {
        move();
        draw();
    }
    void draw() {
        float oldX = x;
        float oldY = y;
        x += xv * Gdx.graphics.getDeltaTime();
        y += yv * Gdx.graphics.getDeltaTime();

        if(x < 0 || x > (Gdx.graphics.getWidth()-WIDTH)){
            x = oldX;
        }
        if(y < 0 || y > (Gdx.graphics.getHeight()-HEIGHT)){
            y = oldY;
        }
        time = Gdx.graphics.getDeltaTime();
        TextureRegion img;
        if (Math.abs(yv) > Math.abs(xv)) {
            if (yv >= 0) {
                img = up;
            } else {
                img = down;
            }
        } else {
            if (Math.abs(xv) >= 0) {
                img = right;
            } else {
                img = left;
            }
        }
        Gdx.gl.glClearColor(0, 0.5f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (xv >= 0) {
            batch.draw(img, x, y, WIDTH, HEIGHT);
        } else {
            batch.draw(img, x + WIDTH, y, WIDTH * -1, HEIGHT);
        }
        batch.end();
    }


    void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){ //WASD controls
            yv = MAX_VELOCITY;

        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
            yv = MAX_VELOCITY * -1;

        }
        if (Gdx.input.isKeyPressed((Input.Keys.RIGHT)) || Gdx.input.isKeyPressed(Input.Keys.D)){
            xv = MAX_VELOCITY;

        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
            xv = MAX_VELOCITY * -1;
        }

        xv *= 0.3;//dampening
        yv *= 0.3;
    }
    @Override
    public void resize(int width, int height){
        viewPort.update(width, height);
        // camera.setToOrtho(true, width, height);
    }
}
