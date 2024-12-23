package com.yandere.Person;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.yandere.gameInterfaces.GameObject.Direction;
import com.yandere.handlers.AnimationBuilder;
import com.yandere.handlers.TextureHandler;

public class PersonHandler {
    private AnimationBuilder animationBuilder;
    private PersonBuilder personBuilder;

    private JsonReader jsonReader;
    private JsonValue base;

    private ArrayList<Person> persons;

    public PersonHandler() {
        jsonReader = new JsonReader();
        base = jsonReader.parse(Gdx.files.internal("npcs.json")).child;

        personBuilder = new PersonBuilder();
        animationBuilder = new AnimationBuilder();

        persons = new ArrayList<>();

        for (JsonValue npc = base.child; npc != null; npc = base.next) {
            personBuilder.setName(npc.getString("name"));

            // Carrega todas as animações
            for (JsonValue textureValue = npc
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
                    personBuilder.addAnimation(textureName + directions.get(i), currentAnimation);
                    animationBuilder.reset();
                }
            }
            persons.add(personBuilder.build());
        }
        personBuilder.dispose();
    }

    public void update() {
        for (Person person : persons) {
            person.update();
        }
    }

    public void render(SpriteBatch batch) {
        for (Person person : persons) {
            person.render(batch);
        }
    }
}
