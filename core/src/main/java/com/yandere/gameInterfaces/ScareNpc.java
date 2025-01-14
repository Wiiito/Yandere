package com.yandere.gameInterfaces;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.yandere.Person.Npc;
import com.yandere.Person.Person;
import com.yandere.Person.PersonHandler;
import com.yandere.handlers.MapHandler;

public class ScareNpc extends GameObject {
    private Vector2 gridPosition;
    private int currentFloor;
    private MapHandler map;

    private ArrayList<Person> possibleTargets = new ArrayList<>();

    public ScareNpc(Vector2 startingGridPosition, MapHandler map, int deathFloor) {
        this.map = map;
        this.gridPosition = startingGridPosition;
        this.currentFloor = deathFloor;
    }

    public void setFloor(int floor) {
        this.currentFloor = floor;
    }

    public void setGridPosition(Vector2 gridPosition) {
        this.gridPosition = gridPosition;
    }

    @Override
    public void update(float DeltaTime) {
        this.possibleTargets.clear();

        for (Person person : PersonHandler.getPersons()) {
            if (person.getCurrentLayer() != this.currentFloor)
                continue;

            if (person.getGridPosition().x >= Math.max(this.gridPosition.x - 8, 0)
                    && person.getGridPosition().x < Math.min(this.gridPosition.x + 8, this.map.getWidth())
                    && person.getGridPosition().y >= Math.max(this.gridPosition.y - 8, 0)
                    && person.getGridPosition().y < Math.min(this.gridPosition.y + 8, this.map.getHeight())) {
                this.possibleTargets.add(person);
            }
        }

        for (Person person : this.possibleTargets) {
            // Pula se ja foi assustado
            if (((Npc) person).isScared())
                continue;

            // Pessoa viu corpo
            if (!this.map.getRayTracingTest().traceRay(this.gridPosition, person.getGridPosition(),
                    map.getMapGrid(this.currentFloor))) {
                ((Npc) person).scare();
            }
        }
    }
}
