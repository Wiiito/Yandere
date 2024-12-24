package com.yandere;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yandere.handlers.TextureHandler;

public class main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameInterface scene;

    @Override
    public void create() {
        TextureHandler.instantiate();

        scene = new GameInterface();
        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        scene.update();

        batch.begin();
        scene.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
