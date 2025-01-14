package com.yandere.handlers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TextureHandler {
    private static TextureHandler thisHandler;
    private TextureAtlas atlas;
    private TextureAtlas weaponAtlas;

    private TextureHandler() {
        atlas = new TextureAtlas("Personagem.atlas");
        weaponAtlas = new TextureAtlas("TodasArmas.atlas");
    }

    public static TextureHandler getInstance() {
        if (thisHandler == null) {
            thisHandler = new TextureHandler();
        }
        return thisHandler;
    }

    public TextureAtlas getPersonAtlas() {
        return this.atlas;
    }

    public TextureAtlas getWeaponsAtlas() {
        return this.weaponAtlas;
    }

    public Pixmap pixmapFromTextureRegion(TextureRegion textureRegion) {
        TextureData textureData = textureRegion.getTexture().getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        Pixmap pixmap = new Pixmap(
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight(),
                textureData.getFormat());

        pixmap.drawPixmap(
                textureData.consumePixmap(), 0, 0, textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        return pixmap;
    }

    public Array<Sprite> spritesFromTexture(Texture texture, int frameHeight, int frameWidth) {
        Array<Sprite> sprites = new Array<>();

        for (int y = 0; y < texture.getHeight(); y = y + frameHeight) {
            for (int x = 0; x < texture.getWidth(); x = x + frameWidth) {
                sprites.add(new Sprite(new TextureRegion(texture, x, y, frameWidth, frameHeight)));
            }
        }

        return sprites;
    }

    public Array<TextureRegion> textureRegionFromTexture(Texture texture, int frameWidth, int frameHeight) {
        Array<TextureRegion> textures = new Array<>();

        for (int y = 0; y < texture.getHeight(); y = y + frameHeight) {
            for (int x = 0; x < texture.getWidth(); x = x + frameWidth) {
                textures.add(new TextureRegion(texture, x, y, frameWidth, frameHeight));
            }
        }

        return textures;
    }
}
