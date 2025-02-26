// Singleton & Observer
package com.yandere.lib;

import java.util.ArrayList;

import com.yandere.Person.Npc;
import com.yandere.Person.Person;
import com.yandere.Person.PersonHandler;
import com.yandere.Schedule.Time;
import com.yandere.gameInterfaces.GameUi;

// Infelizmente o observer ja implementado do java, tem como metodo de atualização
//nome "update", o mesmo utilizado para atualizar os gameObjects...
// Por isso, é necessaria a implementação de uma classe observadora
public class TimeObserver {
    static private TimeObserver uniqueInstance;

    static private ArrayList<TimeListener> timeListeners;
    static private float seconds = 0.f;
    static private Time time;
    static private boolean updated;

    private TimeObserver() {
        timeListeners = new ArrayList<>();
        time = new Time();
        time.hour = 7;
    }

    public static TimeObserver getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new TimeObserver();
        }
        return uniqueInstance;
    }

    public static void addListener(TimeListener object) {
        timeListeners.add(object);
    }

    public static void update(float deltaTime) {
        seconds += deltaTime;

        if (seconds >= 20) {
            seconds -= 20;
            time.minutes++;
        }

        if (time.minutes >= 60) {
            time.minutes -= 60;
            time.hour++;
        }

        if (time.minutes % 10 == 0 && !updated) {
            for (TimeListener timeListener : timeListeners) {
                timeListener.timeChange(time);
            }
            updated = true;
        }

        if (time.minutes % 10 != 0 && updated)
            updated = false;

        if (time.minutes == 50 && time.hour == 12) {
            for (Person aux : PersonHandler.getPersons()) {

                String name = aux.getName();

                if (name.equalsIgnoreCase("Rival")) {

                    Npc rival = (Npc) aux;
                    if (rival.isDead()) {
                        GameUi.getGameUi().setIsEndGood();
                    } else {
                        GameUi.getGameUi().setIsEndBad();
                    }
                }

            }
        }
    }

    public static Time getTime() {
        return time;
    }
}
