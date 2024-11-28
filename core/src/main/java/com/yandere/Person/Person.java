package com.yandere.Person;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.yandere.TextureHandler;
import com.yandere.gameInterfaces.GameObject;

public class Person extends GameObject {
    private Animation<Sprite> animation;
    private float elapsedTime = 0;

    public Person() {
        Pixmap combinedPixmap;

        TextureRegion corpoTexture = TextureHandler.getAtlas().findRegion("Corpo");
        TextureRegion cabecaTexture = TextureHandler.getAtlas().findRegion("Cabeca");

        combinedPixmap = new Pixmap(cabecaTexture.getRegionWidth(), cabecaTexture.getRegionHeight(),
                Pixmap.Format.RGBA8888);

        combinedPixmap.drawPixmap(TextureHandler.pixmapFromTextureRegion(corpoTexture), 0, 0);
        combinedPixmap.drawPixmap(TextureHandler.pixmapFromTextureRegion(cabecaTexture), 0, 0);

        sprite = new Sprite(new Texture(combinedPixmap));
        animation = new Animation<>(0.12f, TextureHandler.spritesFromTexture(new Texture(combinedPixmap), 32, 32));

        combinedPixmap.dispose();

        sprite.setPosition(0, 0);
    }

    public void update() {
        sprite = animation.getKeyFrame(elapsedTime, true);
        elapsedTime += Gdx.graphics.getDeltaTime();
    }
}
