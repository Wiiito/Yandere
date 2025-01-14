package com.yandere;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yandere.gameInterfaces.MenuInterface;

public class main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameInterface scene;
    private MenuInterface menu;

    @Override
    public void create() {
        scene = new GameInterface();
        batch = new SpriteBatch();
        menu = new MenuInterface();
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        scene.update();

        batch.begin();
        if (menu.isInMenu()) {
            menu.render(batch);
            menu.update();
        } else {
            scene.render(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
