package com.yandere.Schedule;

// Struct do time, evitar usar o time do java pq é complicar atoa
// Desnecessario fazer set e get pra isso aq...
public class Time implements Comparable<Time> {
    public int hour;
    public int minutes;

    @Override
    public int compareTo(Time anotherTime) {
        if (hour < anotherTime.hour)
            return -1;

        if (hour > anotherTime.hour)
            return 1;

        if (minutes < anotherTime.minutes)
            return -1;

        if (minutes > anotherTime.minutes)
            return 1;

        return 0;
    }

    @Override
    public String toString() {
        return hour + ":" + String.format("%02d", minutes);
    }
}
