package com.yandere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.yandere.Person.Npc;
import com.yandere.Person.Person;
import com.yandere.Person.PersonHandler;
import com.yandere.gameInterfaces.GameObject;
import com.yandere.gameInterfaces.SceneInterface;
import com.yandere.gameInterfaces.GameObject.Direction;
import com.yandere.handlers.AnimationBuilder;
import com.yandere.handlers.MapHandler;
import com.yandere.handlers.Player;
import com.yandere.handlers.TextureHandler;
import com.yandere.lib.TimeObserver;

public class GameInterface extends SceneInterface {
    private Player player;
    private PersonHandler personHandler;
    private MapHandler mapHanlder;
    private PriorityQueue<GameObject> beforeWall;

    private BitmapFont font = new BitmapFont();

    // TODO - DO BETTER
    Map<String, Animation<TextureRegion>> animationsMap;

    private void loadPlayerAnimations() {
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
                this.animationsMap.put(textureName + directions.get(i), currentAnimation);
                animationBuilder.reset();
            }
        }
    }

    public GameInterface() {
        TimeObserver.getInstance();
        animationsMap = new HashMap<>();

        // TODO - REMOVE
        loadPlayerAnimations();

        mapHanlder = new MapHandler();

        player = new Player(animationsMap, mapHanlder);
        player.setGridPosition(new Vector2(77, 33));
        player.snapToGrid();
        addObject(player);

        personHandler = new PersonHandler(mapHanlder);
        for (Person person : personHandler.getPersons()) {
            addObject(person);
        }

        inputs.addProcessor(player.getInputAdapter());

        beforeWall = new PriorityQueue<>();
    }

    public void update() {
        float deltaTIme = Gdx.graphics.getDeltaTime();
        this.beforeWall.clear();

        TimeObserver.update(deltaTIme * 25);

        camera.position.set(player.getPosition(), 0);
        super.update(deltaTIme);

        Iterator<GameObject> onScreenIterator = super.onScreenObjects.iterator();
        while (onScreenIterator.hasNext()) {
            GameObject o = onScreenIterator.next();

            int xPosition = Math.floorDiv((int) o.getPosition().x, 16);
            int yPosition = Math.floorDiv((int) o.getPosition().y, 16);

            if (o instanceof Npc) {
                Person person = (Person) o;
                if (person.getDirection() == Direction.Bottom) {
                    yPosition--;
                }
            }

            Vector2 objectPositionOnGrid = new Vector2(xPosition, yPosition);

            if (mapHanlder.isInsideWall(objectPositionOnGrid)) {
                beforeWall.add(o);
                onScreenIterator.remove();
            }
        }

        mapHanlder.renderUnderPLayer(camera);
    }

    @Override
    public void render(SpriteBatch batch) {
        for (GameObject gameObject : beforeWall) {
            gameObject.render(batch);
        }

        batch.end();
        mapHanlder.renderWalls(camera, player.getGridPosition());
        batch.begin();

        super.render(batch);

        mapHanlder.renderAbovePlayer(camera);

        font.draw(batch, TimeObserver.getTime().toString(), camera.position.x - camera.viewportWidth / 2,
                camera.position.y + camera.viewportHeight / 2 - 16);
    }
}
