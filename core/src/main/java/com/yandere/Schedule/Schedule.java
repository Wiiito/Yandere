package com.yandere.Schedule;

import com.badlogic.gdx.math.Vector2;

public class Schedule implements Comparable<Schedule> {
    public Time startingTime;
    public Vector2 position;
    public String animation;

    @Override
    public int compareTo(Schedule anotherSchedule) {
        return startingTime.compareTo(anotherSchedule.startingTime);
    }
}
