package com.yandere.gameInterfaces;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class SceneInterface {
    protected ArrayList<GameObject> gameObjects;
    protected OrthographicCamera camera;
    protected InputMultiplexer inputs;

    protected PriorityQueue<GameObject> onScreenObjects;

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

    public void update(float deltaTime) {
        onScreenObjects.clear();
        camera.update();

        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);

            if (camera.frustum.boundsInFrustum(new Vector3(gameObject.getPosition().x, gameObject.getPosition().y, 0),
                    new Vector3(gameObject.getSize().x, gameObject.getSize().y, 1))) {
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
