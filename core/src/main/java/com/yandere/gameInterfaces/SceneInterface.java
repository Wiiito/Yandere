package com.yandere.gameInterfaces;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SceneInterface {
    private ArrayList<GameObject> gameObjects;
    protected OrthographicCamera camera;
    protected InputMultiplexer inputs;

    private PriorityQueue<GameObject> onScreenObjects;

    // TODO - REMOVE
    private BitmapFont removeFpsCounter = new BitmapFont();

    public SceneInterface() {
        gameObjects = new ArrayList<>();

        // Camera
        camera = new OrthographicCamera(256, 256);

        // Input handler
        inputs = new InputMultiplexer();

        Gdx.input.setInputProcessor(inputs);

        onScreenObjects = new PriorityQueue<>();
    }

    public void addObject(GameObject object) {
        if (gameObjects.isEmpty()) {
            gameObjects.add(object);
            return;
        }
        gameObjects.add(object);
    }

    public void update() {
        onScreenObjects.clear();
        camera.update();

        for (GameObject gameObject : gameObjects) {
            gameObject.update();

            if (camera.frustum.pointInFrustum(gameObject.getPosition().x, gameObject.getPosition().y, 0)) {
                onScreenObjects.add(gameObject);
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);

        for (GameObject gameObject : onScreenObjects) {
            gameObject.render(batch);
        }

        // TODO - REMOVE
        removeFpsCounter.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
                camera.position.x - camera.viewportWidth / 2,
                camera.position.y + camera.viewportHeight / 2);
    }

    public void dispose() {
        for (GameObject gameObject : gameObjects) {
            gameObject.dispose();
        }
    }
}
