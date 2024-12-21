package com.yandere.handlers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TextureHandler {
    private static TextureAtlas atlas;

    private TextureHandler() {
    }

    public static void instantiate() {
        if (atlas == null) {
            atlas = new TextureAtlas("Personagem.atlas");
        }
    }

    public static TextureAtlas getAtlas() {
        instantiate();
        return atlas;
    }

    public static Pixmap pixmapFromTextureRegion(TextureRegion textureRegion) {
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

    public static Array<Sprite> spritesFromTexture(Texture texture, int frameHeight, int frameWidth) {
        Array<Sprite> sprites = new Array<>();

        for (int y = 0; y < texture.getHeight(); y = y + frameHeight) {
            for (int x = 0; x < texture.getWidth(); x = x + frameWidth) {
                sprites.add(new Sprite(new TextureRegion(texture, x, y, frameWidth, frameHeight)));
            }
        }

        return sprites;
    }

    public static Array<TextureRegion> textureRegionFromTexture(Texture texture, int frameWidth, int frameHeight) {
        Array<TextureRegion> textures = new Array<>();

        for (int y = 0; y < texture.getHeight(); y = y + frameHeight) {
            for (int x = 0; x < texture.getWidth(); x = x + frameWidth) {
                textures.add(new TextureRegion(texture, x, y, frameWidth, frameHeight));
            }
        }

        return textures;
    }
}
