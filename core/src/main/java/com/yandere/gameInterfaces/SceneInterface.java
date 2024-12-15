package com.yandere.gameInterfaces;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yandere.handlers.Player;

public class SceneInterface {
    private ArrayList<GameObject> gameObjects;
    private MapInterface mapInterface;
    private OrthographicCamera camera;
    private InputMultiplexer inputs;
    private Player player;

    public SceneInterface() {
        gameObjects = new ArrayList<>();

        player = new Player();
        player.setPosition(new Vector2(160, 160));
        addObject(player);

        mapInterface = new MapInterface();

        // Camera
        camera = new OrthographicCamera(256, 256);

        // Input handler
        inputs = new InputMultiplexer();
        inputs.addProcessor(player.getInputAdapter());
        Gdx.input.setInputProcessor(inputs);
    }

    public void addObject(GameObject object) {
        if (gameObjects.isEmpty()) {
            gameObjects.add(object);
            return;
        }

        for (int i = 0; i < gameObjects.size(); i++) {
            if (object.getPosition().y < gameObjects.get(i).getPosition().y) {
                continue;
            }
            gameObjects.add(i, object);
            break;
        }
    }

    public void update() {
        for (GameObject gameObject : gameObjects) {
            gameObject.update();
        }
        camera.position.set(
                new Vector2(player.getPosition().x + player.getSize().x / 2,
                        player.getPosition().y + player.getSize().y / 4),
                0);
        camera.update();
        mapInterface.render(camera);
    }

    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);

        for (GameObject gameObject : gameObjects) {
            gameObject.render(batch);
        }
    }

    public void dispose() {
        for (GameObject gameObject : gameObjects) {
            gameObject.dispose();
        }
    }
}
