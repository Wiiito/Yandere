package com.yandere;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yandere.Person.Person;
import com.yandere.Person.PersonHandler;
import com.yandere.gameInterfaces.SceneInterface;
import com.yandere.handlers.MapHandler;
import com.yandere.handlers.Player;
import com.yandere.handlers.TextureHandler;
import com.yandere.lib.TimeObserver;

public class GameInterface extends SceneInterface {
    private Player player;
    private PersonHandler personHandler;
    private MapHandler mapHanlder;

    private BitmapFont font = new BitmapFont();

    public GameInterface() {
        TimeObserver.getInstance();
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
        float deltaTIme = Gdx.graphics.getDeltaTime();

        TimeObserver.update(deltaTIme * 25);

        camera.position.set(player.getPosition(), 0);
        super.update(deltaTIme);

        mapHanlder.render(camera);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        font.draw(batch, TimeObserver.getTime().toString(), camera.position.x - camera.viewportWidth / 2,
                camera.position.y + camera.viewportHeight / 2 - 16);
    }
}
