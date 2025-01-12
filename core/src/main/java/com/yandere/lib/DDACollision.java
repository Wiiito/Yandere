// Conceito de desing pattern q n lembro o nome mas é o msm do AStar de quebrar algoritimos
//complexos em varias classes e englobar eles em uma unica classe (MapHandler)

package com.yandere.lib;

import com.badlogic.gdx.math.Vector2;

public class DDACollision {
    // Fonte: https://lodev.org/cgtutor/raycasting.html
    public boolean traceRay(Vector2 gridStartingPos, Vector2 gridEndingPos, MapGrid mapGrid) {
        Vector2 rayDirection = gridEndingPos.cpy().sub(gridStartingPos).nor();
        Vector2 mapCheck = new Vector2(gridStartingPos);
        Vector2 rayLength = new Vector2();
        Vector2 step = new Vector2();

        Vector2 raySizeInDirection = new Vector2((float) Math.sqrt(1 + Math.pow(rayDirection.y / rayDirection.x, 2)),
                (float) Math.sqrt(1 + Math.pow(rayDirection.x / rayDirection.y, 2)));

        // Initial rayLenght and ray direction on grid with step
        if (rayDirection.x < 0) {
            step.x = -1;
            rayLength.x = (gridStartingPos.x - mapCheck.x) * raySizeInDirection.x;
        } else if (rayDirection.x == 0) {
            step.x = 0;
            rayLength.x = 0;
        } else {
            step.x = 1;
            rayLength.x = (mapCheck.x - gridStartingPos.x) * raySizeInDirection.x;
        }

        if (rayDirection.y < 0) {
            step.y = -1;
            rayLength.y = (gridStartingPos.y - mapCheck.y) * raySizeInDirection.y;
        } else if (rayDirection.y == 0) {
            step.y = 0;
            rayLength.y = 0;
        } else {
            step.y = 1;
            rayLength.y = (mapCheck.y - gridStartingPos.y) * raySizeInDirection.y;
        }

        boolean finalTile = false;
        while (!finalTile) {
            if (rayLength.x < rayLength.y) {
                mapCheck.x += step.x;
                rayLength.x += raySizeInDirection.x;
            } else {
                mapCheck.y += step.y;
                rayLength.y += raySizeInDirection.y;
            }

            if (!mapGrid.getChamber(mapCheck).getWalkable()) {
                return true;
            }

            if (mapCheck.idt(gridEndingPos)) {
                finalTile = true;
            }
        }

        return false;
    }
}
