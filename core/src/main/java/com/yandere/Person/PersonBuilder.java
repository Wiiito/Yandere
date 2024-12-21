package com.yandere.Person;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PersonBuilder {
    private String name;
    private Map<String, Animation<TextureRegion>> animations;

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

    public Person build() {
        return new Person(this.name, animations);
    }

    public void dispose() {
        this.name = null;
        this.animations.clear();
    }
}
