package com.yandere;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yandere.Person.Person;
import com.yandere.Person.PersonHandler;
import com.yandere.gameInterfaces.SceneInterface;
import com.yandere.handlers.MapHandler;
import com.yandere.handlers.Player;
import com.yandere.handlers.TextureHandler;

public class GameInterface extends SceneInterface {
    private Player player;
    private PersonHandler personHandler;
    private MapHandler mapHanlder;

    public GameInterface() {
        Animation<TextureRegion> animations = new Animation<>(.12f, TextureHandler
                .textureRegionFromTexture(
                        new Texture(TextureHandler
                                .pixmapFromTextureRegion(TextureHandler.getAtlas().findRegion("WalkCorpoBottom"))),
                        16, 24));
        Map<String, Animation<TextureRegion>> animationsMap = new HashMap<>();
        animationsMap.put("WalkBottom", animations);

        mapHanlder = new MapHandler();

        player = new Player(animationsMap, mapHanlder);
        player.setPosition(new Vector2(160, 160));
        player.setGridPosition(new Vector2(10, 10));
        addObject(player);

        personHandler = new PersonHandler(mapHanlder);
        for (Person person : personHandler.getPersons()) {
            addObject(person);
        }

        inputs.addProcessor(player.getInputAdapter());
    }

    public void update() {
        camera.position.set(player.getPosition(), 0);
        super.update();

        mapHanlder.render(camera);
    }
}
