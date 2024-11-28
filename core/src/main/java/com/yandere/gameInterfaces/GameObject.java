package com.yandere.gameInterfaces;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    protected Vector2 position;
    protected Sprite sprite;

    public void update() {
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
