package com.yandere.gameInterfaces;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject implements Comparable<GameObject> {
    protected Sprite sprite;

    public enum Direction {
        Top, Right, Bottom, Left
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public Vector2 getSize() {
        return new Vector2(sprite.getWidth(), sprite.getHeight());
    }

    public void update(float deltaTime) {
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void dispose() {
    }

    public int compareTo(GameObject o) {
        if (this.sprite.getY() > o.getPosition().y) {
            return -1;
        }
        if (this.sprite.getY() < o.getPosition().y) {
            return 1;
        }
        return 0;
    }
}
