package com.yandere.Person;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.yandere.TextureHandler;
import com.yandere.gameInterfaces.GameObject;

public class Person extends GameObject {
    private Map<String, Animation<TextureRegion>> animations;
    private Map<String, Float> animationsTiming;
    private Animation<TextureRegion> currentAnimation;
    private float elapsedTime = 0;
    private Direction direction = Direction.Bottom;

    private void setAnimations() {
        for (GameObject.Direction textureDirection : GameObject.Direction.values()) {
            Pixmap combinedPixmap;

            TextureRegion corpoTexture = TextureHandler.getAtlas().findRegion("IdleCorpo" + textureDirection);
            TextureRegion cabecaTexture = TextureHandler.getAtlas().findRegion("IdleCabeca" + textureDirection);

            combinedPixmap = new Pixmap(cabecaTexture.getRegionWidth(), cabecaTexture.getRegionHeight(),
                    Pixmap.Format.RGBA8888);

            combinedPixmap.drawPixmap(TextureHandler.pixmapFromTextureRegion(corpoTexture), 0, 0);
            combinedPixmap.drawPixmap(TextureHandler.pixmapFromTextureRegion(cabecaTexture), 0, 0);

            animations.put("Idle" + textureDirection,
                    new Animation<>(animationsTiming.get("Idle" + textureDirection),
                            TextureHandler.textureRegionFromTexture(new Texture(combinedPixmap), 16, 24)));

            combinedPixmap.dispose();
        }
    }

    public Person() {
        animations = new HashMap<>();
        animationsTiming = new HashMap<>();

        // TODO - Sistema de arquivos dos npc
        animationsTiming.put("IdleBottom", 0.12f);
        animationsTiming.put("IdleLeft", 0.12f);
        animationsTiming.put("IdleRight", 0.12f);
        animationsTiming.put("IdleTop", 0.30f);
        animationsTiming.put("WalkBottom", 0.15f);
        animationsTiming.put("WalkLeft", 0.15f);
        animationsTiming.put("WalkRight", 0.15f);
        animationsTiming.put("WalkTop", 0.15f);

        this.setAnimations();

        sprite = new Sprite();
        sprite.setSize(16, 24);
        sprite.setPosition(0, 0);
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
        currentAnimation = animations.get("Idle" + direction);
        elapsedTime += Gdx.graphics.getDeltaTime();
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY());
    }
}
