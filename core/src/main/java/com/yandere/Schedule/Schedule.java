package com.yandere.Schedule;

import com.badlogic.gdx.math.Vector2;
import com.yandere.gameInterfaces.GameObject.Direction;

public class Schedule implements Comparable<Schedule> {
    public Time startingTime;
    public Vector2 position;
    public String animation;
    public Direction activityDirection = Direction.Bottom;

    @Override
    public int compareTo(Schedule anotherSchedule) {
        return startingTime.compareTo(anotherSchedule.startingTime);
    }
}
