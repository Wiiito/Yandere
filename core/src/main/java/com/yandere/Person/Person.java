package com.yandere.Person;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yandere.gameInterfaces.GameObject;

public class Person extends GameObject {
    private String name;
    private Map<String, Animation<TextureRegion>> animations;
    private Animation<TextureRegion> currentAnimation;
    private float elapsedTime = 0;
    private Direction direction = Direction.Bottom;

    public Person(String name, Map<String, Animation<TextureRegion>> animations) {
        this.name = name;
        this.animations = new HashMap<>(animations);

        this.currentAnimation = animations.entrySet().iterator().next().getValue();

        sprite = new Sprite();
        sprite.setSize(currentAnimation.getKeyFrame(0).getRegionWidth(),
                currentAnimation.getKeyFrame(0).getRegionHeight());
        sprite.setPosition(0, 0);
    }

    public String getName() {
        return this.name;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setPosition(Vector2 position) {
        this.sprite.setPosition(position.x, position.y);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void update() {
        this.currentAnimation = animations.get("Idle" + direction);
        elapsedTime += Gdx.graphics.getDeltaTime();
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY());
    }
}
