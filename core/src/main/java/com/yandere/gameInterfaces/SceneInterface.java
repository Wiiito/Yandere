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

    // POR DEUS QUE ISSO É NECESSARIO... JURO...EXPLICO MAIS PRA BAIXO
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
        GameObject gameObject = onScreenObjects.poll();
        while (gameObject != null) {
            gameObject.render(batch);
            gameObject = onScreenObjects.poll();
        }

        /*
         * SIMPLISMENTE O BUG MAIS SEM SENTIDO DO *MUNDO*
         * SE NÃO EXISTE ESSE DRAW, A ORDEM DE DESENHO MUDA, AS ARVORES
         * PASSAM A SER DESENHADAS ATRAS DO PERSONAGEM, 0 MOTIVOS
         * SÓ DEUS SABE O PQ, NAO TEM NEM SENTIDO, SIMPLISMENTE
         * UM DRAW DE UM TEXTO MUDA A ORDEM DE DESENHO????
         */
        removeFpsCounter.draw(batch, " ", 0, 0);
    }

    public void dispose() {
        for (GameObject gameObject : gameObjects) {
            gameObject.dispose();
        }
    }
}
