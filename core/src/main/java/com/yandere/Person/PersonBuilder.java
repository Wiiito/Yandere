package com.yandere.Person;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yandere.handlers.MapHandler;

public class PersonBuilder {
    private String name;
    private Map<String, Animation<TextureRegion>> animations;
    private MapHandler map;

    public PersonBuilder() {
        this.animations = new HashMap<>();
    }

    public PersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder addAnimation(String animationName, Animation<TextureRegion> animation) {
        this.animations.put(animationName, animation);
        return this;
    }

    public PersonBuilder setMap(MapHandler map) {
        this.map = map;
        return this;
    }

    public Person build() {
        return new Person(this.name, this.animations, this.map);
    }

    public void dispose() {
        this.name = null;
        this.animations.clear();
    }
}
