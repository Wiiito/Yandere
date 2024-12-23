package com.yandere.gameInterfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yandere.Person.PersonHandler;
import com.yandere.handlers.Player;
import com.yandere.handlers.TextureHandler;

public class SceneInterface {
    private ArrayList<GameObject> gameObjects;
    private PersonHandler personHandler;
    private MapInterface mapInterface;
    private OrthographicCamera camera;
    private InputMultiplexer inputs;
    private Player player;

    // TODO - REMOVE
    private BitmapFont removeFpsCounter = new BitmapFont();

    public SceneInterface() {
        gameObjects = new ArrayList<>();

        Animation<TextureRegion> animations = new Animation<>(.12f, TextureHandler
                .textureRegionFromTexture(
                        new Texture(TextureHandler
                                .pixmapFromTextureRegion(TextureHandler.getAtlas().findRegion("WalkCorpoBottom"))),
                        16, 24));

        Map<String, Animation<TextureRegion>> animationsMap = new HashMap<>();
        animationsMap.put("WalkBottom", animations);

        player = new Player(animationsMap);
        player.setPosition(new Vector2(160, 160));
        player.setGridPosition(new Vector2(10, 10));
        addObject(player);

        mapInterface = new MapInterface();

        personHandler = new PersonHandler();

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

        camera.position.set(player.getPosition(), 0);

        camera.update();
        mapInterface.render(camera);
        personHandler.update();
    }

    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);

        for (GameObject gameObject : gameObjects) {
            gameObject.render(batch);
        }

        personHandler.render(batch);

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
