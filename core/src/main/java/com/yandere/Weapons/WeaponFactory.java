package com.yandere.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.yandere.gameInterfaces.GameObject.Direction;
import com.yandere.handlers.TextureHandler;

public class WeaponFactory {
    private ArrayList<String> playerAnimationsName;
    private Map<String, ArrayList<Float>> animationsTimings;

    public WeaponFactory() {
        playerAnimationsName = new ArrayList<>();
        animationsTimings = new HashMap<>();

        JsonReader jsonReader = new JsonReader();
        JsonValue playerData = jsonReader.parse(Gdx.files.internal("data/npcs.json")).get("player");

        for (JsonValue textureValue = playerData
                .get("textures").child; textureValue != null; textureValue = textureValue.next) {
            playerAnimationsName.add(textureValue.getString("name"));

            // Carrega todos os tempos presentes para cada animação
            ArrayList<Float> timings = new ArrayList<>();
            // Basicamente usando o erro como if, caso o valor timing não ser um float, cai
            // na exception
            try {
                float timingFloat = textureValue.getFloat("timing");
                for (@SuppressWarnings("unused") // pqp, n quero ter q fazer for de gnt e o vs fica enchendo o saco
                Direction direction : Direction.values()) {
                    timings.add(timingFloat);
                }
            } catch (Exception e) {
                for (JsonValue timingsOnJson = textureValue
                        .get("timing").child; timingsOnJson != null; timingsOnJson = timingsOnJson.next) {
                    timings.add(timingsOnJson.asFloat());
                }
            }

            animationsTimings.put(textureValue.getString("name"), timings);
        }
    }

    public Weapon getWeapon(String weaponName) {
        Weapon weapon = null;
        switch (weaponName) {
            case "Extintor":
                weapon = getExtintor();
                break;
            case "Espada":
                weapon = getEspada();
                break;
        }
        return weapon;
    }

    public Weapon getExtintor() {
        Map<String, Animation<TextureRegion>> animations = new HashMap<>();
        for (String animationName : playerAnimationsName) {
            if (animationName.contains("Weapon")) // Pulando animações de arma, meio obvio o motivo
                continue;

            for (int i = 0; i < Direction.values().length; i++) {
                TextureRegion textureRegion = TextureHandler.getInstance().getWeaponsAtlas()
                        .findRegion(animationName + "Sword" + Direction.values()[i]); // TODO - MUDAR PARA EXTINTOR
                                                                                      // QUANDO A ANIMAÇÃO ESTIVER
                                                                                      // PRONTA
                Animation<TextureRegion> currentAnimation = new Animation<>(animationsTimings.get(animationName).get(i),
                        TextureHandler.getInstance().textureRegionFromTexture(
                                new Texture(TextureHandler.getInstance().pixmapFromTextureRegion(textureRegion)),
                                24,
                                26));
                animations.put(animationName + Direction.values()[i], currentAnimation);
            }
        }
        Weapon extintor = new Weapon(animations);
        return extintor;
    }

    public Weapon getEspada() {
        Map<String, Animation<TextureRegion>> animations = new HashMap<>();
        for (String animationName : playerAnimationsName) {
            if (animationName.contains("Weapon")) // Pulando animações de arma, meio obvio o motivo
                continue;

            for (int i = 0; i < Direction.values().length; i++) {
                TextureRegion textureRegion = TextureHandler.getInstance().getWeaponsAtlas()
                        .findRegion(animationName + "Sword" + Direction.values()[i]);
                Animation<TextureRegion> currentAnimation = new Animation<>(animationsTimings.get(animationName).get(i),
                        TextureHandler.getInstance().textureRegionFromTexture(
                                new Texture(TextureHandler.getInstance().pixmapFromTextureRegion(textureRegion)),
                                24,
                                26));
                animations.put(animationName + Direction.values()[i], currentAnimation);
            }
        }
        Weapon extintor = new Weapon(animations);
        return extintor;
    }
}
