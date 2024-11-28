package com.yandere;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yandere.Person.Person;

public class main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    Person person;

    @Override
    public void create() {
        TextureHandler.instantiate();

        batch = new SpriteBatch();

        camera = new OrthographicCamera(64, 64);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        person = new Person();
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        batch.setProjectionMatrix(camera.combined);
        person.update();

        batch.begin();
        person.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
