package com.yandere.gameInterfaces;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public interface AStarCallback {
    public void pathFind(ArrayList<Vector2> path);
}
