// Factory based
package com.yandere.Person;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.yandere.Schedule.Schedule;
import com.yandere.Schedule.Time;
import com.yandere.gameInterfaces.GameObject.Direction;
import com.yandere.handlers.AnimationBuilder;
import com.yandere.handlers.MapHandler;
import com.yandere.handlers.TextureHandler;

public class PersonHandler {

    private ArrayList<Person> persons;

    private AnimationBuilder animationBuilder;
    private PersonBuilder personBuilder;

    private JsonReader jsonReader;
    private JsonValue npcsJson;
    private JsonValue schedulesJson;
    private JsonValue locationsJson;

    private Vector2 currentNpcChairLocation;

    public PersonHandler(MapHandler map) {

        jsonReader = new JsonReader();
        npcsJson = jsonReader.parse(Gdx.files.internal("data/npcs.json")).get("npcs");
        schedulesJson = jsonReader.parse(Gdx.files.internal("data/classes.json"));
        locationsJson = jsonReader.parse(Gdx.files.internal("data/locations.json"));

        personBuilder = new PersonBuilder();
        animationBuilder = new AnimationBuilder();

        persons = new ArrayList<>();

        for (JsonValue npc = npcsJson.child; npc != null; npc = npc.next) {
            personBuilder.setName(npc.getString("name"));

            loadAnimations(npc);

            personBuilder.setMapHandler(map);

            int startingPosX = npc.get("startPos").getInt("x");
            int startingPosY = npc.get("startPos").getInt("y");
            personBuilder.setPosition(new Vector2(startingPosX, startingPosY));

            currentNpcChairLocation = new Vector2();
            currentNpcChairLocation.x = npc.get("defaultChair").getInt("x");
            currentNpcChairLocation.y = npc.get("defaultChair").getInt("y");

            loadSchedule(npc);

            persons.add(personBuilder.buildNpc());
        }
        personBuilder.dispose();
    }

    private void loadSchedule(JsonValue npc) {
        String schedule = npc.getString("schedule");
        JsonValue classes = schedulesJson.get(schedule);
        PriorityQueue<Schedule> schedules = new PriorityQueue<>();

        for (JsonValue currentClass = classes.child; currentClass != null; currentClass = currentClass.next) {
            Time startingTime = new Time();
            startingTime.hour = currentClass.get("startingTime").getInt("hour");
            startingTime.minutes = currentClass.get("startingTime").getInt("minutes");

            String locationClass = currentClass.getString("location");
            JsonValue scheduleLocation = locationsJson.get(locationClass).get("classPositions")
                    .get((int) currentNpcChairLocation.x).get((int) currentNpcChairLocation.y);

            Vector2 position = new Vector2(scheduleLocation.getInt("x"), scheduleLocation.getInt("y"));
            Direction activityDirection = Direction.valueOf(locationsJson.get(locationClass).getString("direction"));

            String animation = currentClass.getString("animation");

            Schedule currentSchedule = new Schedule();
            currentSchedule.startingTime = startingTime;
            currentSchedule.position = position;
            currentSchedule.activityDirection = activityDirection;
            currentSchedule.animation = animation;
            schedules.add(currentSchedule);
        }

        personBuilder.setSchedule(schedules);
    }

    private void loadAnimations(JsonValue npc) {
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
                float timingFloat = textureValue.getFloat("timing");
                for (int i = 0; i < directions.size(); i++) {
                    timings.add(timingFloat);
                }
            } catch (Exception e) {
                for (JsonValue timingsOnJson = textureValue
                        .get("timing").child; timingsOnJson != null; timingsOnJson = timingsOnJson.next) {
                    timings.add(timingsOnJson.asFloat());
                }
            }

            // Carregando delay entre animações
            try {
                float animationDelay = textureValue.getFloat("delay");
                personBuilder.addAnimationDelay(textureName, animationDelay);
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
                personBuilder.addAnimation(textureName + directions.get(i), currentAnimation);
                animationBuilder.reset();
            }
        }
    }

    public ArrayList<Person> getPersons() {
        return this.persons;
    }
}
