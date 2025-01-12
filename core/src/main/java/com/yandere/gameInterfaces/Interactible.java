package com.yandere.gameInterfaces;

public class Interactible {
    public enum Type {
        Weapon, Dialog, Hide, Alarm
    }

    public Type type;
    public String dialog = "";
    public String name = "";
}