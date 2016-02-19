package com.theironyard.minicraft;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import javafx.beans.WeakInvalidationListener;

public class Minicraft extends ApplicationAdapter {
    FitViewport viewPort;
    final int WIDTH = 100;
    final int HEIGHT = 100;
    Animation walkUp;
    Animation walkDown;

    SpriteBatch batch;
    TextureRegion down, up, right, left, stand, tree, flippedUp, flippedDown;
    TextureRegion zUp, zDown, zRight, zLeft;
    float time;
    float x = 0;
    float y = 0;
    float xv = 0;
    float yv = 0;
    float zombieX = 100;
    float zombieY = 100;
    float zombieXV = 0;
    float zombieYV = 0;

    final float MAX_VELOCITY = 1000;

    @Override
    public void create () {
        batch = new SpriteBatch();

        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Texture tiles = new Texture("tiles.png");
        TextureRegion[][] grid = TextureRegion.split(tiles, 16, 16);
        tree = grid[1][0];
        down = grid[6][0];
        up = grid[6][1];
        zUp = grid[6][4];
        zDown = grid[6][5];
        zRight = grid[6][7];
        zLeft = new TextureRegion(zRight);
        zLeft.flip(true,false);

        flippedUp =  new TextureRegion(up);
        flippedUp.flip(true, false);

        flippedDown = new TextureRegion(down);
        flippedDown.flip(true, false);

        right = grid[6][3];
        left = new TextureRegion(right);
        left.flip(true, false);

        stand = grid[6][2];

        walkUp = new Animation(0.1f, up, flippedUp);
        walkDown = new Animation(0.1f, down, flippedDown);
        walkUp.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        walkDown.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
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
        zombieX += zombieXV * Gdx.graphics.getDeltaTime();
        zombieY += zombieYV * Gdx.graphics.getDeltaTime();

       if(x < 0 ){
            x = Gdx.graphics.getWidth()-WIDTH;
        }
        if (x > (Gdx.graphics.getWidth()-WIDTH)){
            x = 1;
        }
        if(y < 0 ){
            y = (Gdx.graphics.getHeight() - HEIGHT);
        }
        if (y > Gdx.graphics.getHeight() - HEIGHT){
            y = 1;
        }
        if(zombieX < 0 ){
            zombieX = Gdx.graphics.getWidth()-WIDTH;
        }
        if (zombieX > (Gdx.graphics.getWidth()-WIDTH)){
            zombieX = 1;
        }
        if(zombieY < 0 ){
            zombieY = (Gdx.graphics.getHeight() - HEIGHT);
        }
        if (zombieY > Gdx.graphics.getHeight() - HEIGHT){
            zombieY = 1;
        }

        time += Gdx.graphics.getDeltaTime();


        TextureRegion img;
        TextureRegion zImg;

        if (Math.abs(yv) > Math.abs(xv)) {
            if (yv > 1) {
                img = walkUp.getKeyFrame(time, true);
                zImg = zUp;
            } else {
                img = walkDown.getKeyFrame(time, true);
                zImg = zDown;
            }
        } else {
            if (xv >= 0) {
                img = right;
                zImg = zLeft;
            } else {
                img = left;
                zImg = zRight;
            }
        }
        if (yv == 0){
            img = up;
        }


        Gdx.gl.glClearColor(0, 0.5f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
//        if (xv >= 0) {
//            batch.draw(img, x, y, WIDTH, HEIGHT);
//        } else {
//            batch.draw(img, x + WIDTH, y, WIDTH * -1, HEIGHT);
//        }
        batch.draw(img, x, y, WIDTH, HEIGHT);
        batch.draw(zImg, zombieX, zombieY, WIDTH, HEIGHT);

        batch.end();
    }
    void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){ //WASD controls
            yv = MAX_VELOCITY;
            zombieYV = MAX_VELOCITY *-1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
            yv = MAX_VELOCITY * -1;
            zombieYV = MAX_VELOCITY;
        }
        if (Gdx.input.isKeyPressed((Input.Keys.RIGHT)) || Gdx.input.isKeyPressed(Input.Keys.D)){
            xv = MAX_VELOCITY;
            zombieXV = MAX_VELOCITY * -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
            xv = MAX_VELOCITY * -1;
            zombieXV = MAX_VELOCITY;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)){
            yv = MAX_VELOCITY* 4;
        }

        xv *= 0.3;//dampening
        yv *= 0.3;
        zombieYV *= 0.3f;
        zombieXV *= 0.3f;
    }
    @Override
    public void resize(int width, int height){
        viewPort.update(width, height);
    }
}
