package com.yandere.handlers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationBuilder {
    private Pixmap combinedPixmap;
    private ArrayList<TextureRegion> loadedTextures;

    public AnimationBuilder() {
        loadedTextures = new ArrayList<>();
    }

    public void addTextureRegion(TextureRegion textureRegion) {
        loadedTextures.add(textureRegion);
    }

    public void reset() {
        loadedTextures.clear();
        combinedPixmap.dispose();
    }

    public Pixmap getPixmap() {
        combinedPixmap = new Pixmap(loadedTextures.get(0).getRegionWidth(), loadedTextures.get(0).getRegionHeight(),
                Pixmap.Format.RGBA8888);

        for (TextureRegion textureRegion : loadedTextures) {
            combinedPixmap.drawPixmap(TextureHandler.pixmapFromTextureRegion(textureRegion), 0, 0);
        }

        return combinedPixmap;
    }
}
