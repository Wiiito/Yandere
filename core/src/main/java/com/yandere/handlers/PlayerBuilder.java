// Facade
package com.yandere.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.yandere.gameInterfaces.GameObject.Direction;

public class PlayerBuilder {
    Map<String, Animation<TextureRegion>> animationsMap = new HashMap<>();
    Map<String, Float> animationsDelay = new HashMap<>();

    public void loadPlayerData() {
        AnimationBuilder animationBuilder = new AnimationBuilder();
        JsonReader jsonReader = new JsonReader();
        JsonValue playerData = jsonReader.parse(Gdx.files.internal("data/npcs.json")).get("player");

        for (JsonValue textureValue = playerData
                .get("textures").child; textureValue != null; textureValue = textureValue.next) {
            String textureName = textureValue.getString("name");

            // Procura o diretorio direções no json, caso não encontre, coloca todas as
            // direções
            ArrayList<Direction> directions = new ArrayList<>();
            if (textureValue.get("directions") == null) {
                for (Direction direction : Direction.values()) {
                    directions.add(direction);
                }
            } else {
                for (JsonValue directionsOnJson = textureValue
                        .get("directions").child; directionsOnJson != null; directionsOnJson = directionsOnJson.next) {
                    directions.add(Direction.valueOf(directionsOnJson.toString()));
                }
            }

            // Carrega todos os tempos presentes para cada animação
            ArrayList<Float> timings = new ArrayList<>();
            // Basicamente usando o erro como if, caso o valor timing não ser um float, cai
            // na exception
            try {
                Float timingFloat = textureValue.getFloat("timing");
                for (int i = 0; i < directions.size(); i++) {
                    timings.add(timingFloat);
                }
            } catch (Exception e) {
                for (JsonValue timingsOnJson = textureValue
                        .get("timing").child; timingsOnJson != null; timingsOnJson = timingsOnJson.next) {
                    timings.add(timingsOnJson.asFloat());
                }
            }

            try {
                float animationDelay = textureValue.getFloat("delay");
                animationsDelay.put(textureName, animationDelay);
            } catch (Exception e) { // Simplismente nao adiciona ao array
            }

            // Carrega todas as texturas presentes na animação
            // Primeiro, pega cada atlas, para cada atlas, carrega as direções presentes na
            // textura
            for (int i = 0; i < directions.size(); i++) {
                for (JsonValue textureAtlas = textureValue
                        .get("textureAtlas").child; textureAtlas != null; textureAtlas = textureAtlas.next) {
                    TextureRegion textureRegion = TextureHandler.getAtlas()
                            .findRegion(textureAtlas.toString() + directions.get(i));
                    animationBuilder.addTextureRegion(textureRegion);
                }
                Pixmap animationPixmap = animationBuilder.getPixmap();
                Animation<TextureRegion> currentAnimation = new Animation<>(timings.get(i),
                        TextureHandler.textureRegionFromTexture(new Texture(animationPixmap), 16, 24));
                animationsMap.put(textureName + directions.get(i), currentAnimation);
                animationBuilder.reset();
            }
        }
    }

    public Map<String, Animation<TextureRegion>> getPLayerAnimations() {
        return this.animationsMap;
    }

    public Map<String, Float> getPlayerAnimationsDelay() {
        return this.animationsDelay;
    }
}
