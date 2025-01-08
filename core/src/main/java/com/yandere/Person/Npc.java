package com.yandere.Person;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yandere.Schedule.Schedule;
import com.yandere.Schedule.Time;
import com.yandere.handlers.MapHandler;
import com.yandere.lib.TimeListener;
import com.yandere.lib.TimeObserver;

public class Npc extends Person implements TimeListener {
    private PriorityQueue<Schedule> schedule;
    private ArrayList<Vector2> currentPath;
    private Schedule currentSchedule;

    public Npc(String name, Map<String, Animation<TextureRegion>> animations, MapHandler map,
            PriorityQueue<Schedule> schedule) {
        super(name, animations, map);
        this.schedule = new PriorityQueue<Schedule>(schedule);

        TimeObserver.addListener(this);
    }

    @Override
    public void timeChange(Time currentTime) {
        if (!schedule.isEmpty() && schedule.peek().startingTime.compareTo(currentTime) == 0) {
            currentSchedule = schedule.poll();
        }
        if (currentSchedule != null)
            currentPath = map.getSiplifiedPath(getGridPosition(), currentSchedule.position, 0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (currentPath != null && !currentPath.isEmpty()) {
            if (super.getGridPosition().idt(currentPath.get(0)))
                currentPath.remove(0);

            if (!currentPath.isEmpty()) {
                Vector2 directionVector = currentPath.get(0).cpy().sub(super.getGridPosition());

                if (directionVector.x > 0)
                    this.setDirection(Direction.Right);
                if (directionVector.x < 0)
                    this.setDirection(Direction.Left);
                if (directionVector.y > 0)
                    this.setDirection(Direction.Top);
                if (directionVector.y < 0)
                    this.setDirection(Direction.Bottom);

                move();
            } else {
                if (currentSchedule.activityDirection != null) {
                    this.setDirection(currentSchedule.activityDirection);
                }
            }
        }
    }
}
