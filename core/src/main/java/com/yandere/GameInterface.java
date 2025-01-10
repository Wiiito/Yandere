package com.yandere;

import java.util.Iterator;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yandere.Person.Npc;
import com.yandere.Person.Person;
import com.yandere.Person.PersonHandler;
import com.yandere.gameInterfaces.BoxUI;
import com.yandere.gameInterfaces.GameObject;
import com.yandere.gameInterfaces.GameUi;
import com.yandere.gameInterfaces.SceneInterface;
import com.yandere.gameInterfaces.GameObject.Direction;
import com.yandere.handlers.MapHandler;
import com.yandere.handlers.Player;
import com.yandere.handlers.PlayerBuilder;
import com.yandere.lib.TimeObserver;

public class GameInterface extends SceneInterface {
    private Player player;
    private MapHandler mapHanlder;
    private PriorityQueue<GameObject> beforeWall;

    private BitmapFont font = new BitmapFont();

    public GameInterface() {
        TimeObserver.getInstance();
        GameUi.instantiate();
        BoxUI.instantiate();

        mapHanlder = new MapHandler();

        PlayerBuilder pb = new PlayerBuilder();
        pb.loadPlayerData();
        player = new Player(pb, mapHanlder);
        player.setGridPosition(new Vector2(124, 27));
        player.snapToGrid();
        addObject(player);

        PersonHandler personHandler;
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
        GameUi.getGameUi().update();
        BoxUI.getBoxUI().update();

        camera.position.set(player.getPosition(), 0);
        super.update(deltaTIme);

        Iterator<GameObject> onScreenIterator = super.onScreenObjects.iterator();
        while (onScreenIterator.hasNext()) {
            GameObject o = onScreenIterator.next();

            int xPosition = -Math.floorDiv((int) -o.getPosition().x, 16);
            int yPosition = -Math.floorDiv((int) -o.getPosition().y, 16);

            if (o instanceof Npc) {
                Person person = (Person) o;
                if (person.getDirection() == Direction.Top) {
                    yPosition--;
                }
                if (person.getCurrentLayer() != player.getCurrentLayer()) {
                    onScreenIterator.remove();
                }
            }

            if (!mapHanlder.isInsideWall(player.getGridPosition())) {
                Vector2 objectPositionOnGrid = new Vector2(xPosition, yPosition);
                if (mapHanlder.isInsideWall(objectPositionOnGrid)) {
                    beforeWall.add(o);
                    onScreenIterator.remove();
                }
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
        mapHanlder.renderObjects(batch);

        super.render(batch);

        mapHanlder.renderAbovePlayer(camera);

        GameUi.getGameUi().render(batch, camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
        
        BoxUI.getBoxUI().render(batch, player.getPosition().x - 40, player.getPosition().y);

        font.draw(batch,
                "TESTE", camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2);

        font.draw(batch, TimeObserver.getTime().toString(), camera.position.x - camera.viewportWidth / 2,
                camera.position.y + camera.viewportHeight / 2 - 16);
    }
}
