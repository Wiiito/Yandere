// Builder
package com.yandere.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yandere.Schedule.Schedule;
import com.yandere.handlers.MapHandler;

public class PersonBuilder {
    private String name;
    private Map<String, Animation<TextureRegion>> animations;
    private MapHandler map;
    private Vector2 startingPos;
    private PriorityQueue<Schedule> npcSchedule;

    public PersonBuilder() {
        this.animations = new HashMap<>();
        this.npcSchedule = new PriorityQueue<>();
    }

    public PersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder addAnimation(String animationName, Animation<TextureRegion> animation) {
        this.animations.put(animationName, animation);
        return this;
    }

    public PersonBuilder setMapHandler(MapHandler map) {
        this.map = map;
        return this;
    }

    public PersonBuilder setSchedule(PriorityQueue<Schedule> schedule) {
        this.npcSchedule = schedule;
        return this;
    }

    public PersonBuilder setPosition(Vector2 pos) {
        this.startingPos = new Vector2(pos);
        return this;
    }

    public Person build() {
        Person person = new Person(this.name, this.animations, this.map);
        person.setGridPosition(startingPos);
        person.snapToGrid();
        return person;
    }

    public Npc buildNpc() {
        Npc npc = new Npc(this.name, this.animations, this.map, this.npcSchedule);
        npc.setGridPosition(startingPos);
        npc.snapToGrid();
        return npc;
    }

    public void dispose() {
        this.name = null;
        this.animations.clear();
        this.npcSchedule.clear();
    }
}
