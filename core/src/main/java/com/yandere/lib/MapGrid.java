// Adapter
package com.yandere.lib;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.yandere.gameInterfaces.AStarCallback;

public class MapGrid {
    TiledMapTileLayer map;
    Vector2 worldSize;
    ArrayList<ArrayList<Chamber>> collisions;
    AStar pathFiding;

    public MapGrid(TiledMapTileLayer map) {
        this.map = map;
        worldSize = new Vector2(map.getWidth(), map.getHeight());

        collisions = new ArrayList<>();

        for (int x = 0; x < worldSize.x; x++) {
            collisions.add(new ArrayList<>());
            for (int y = 0; y < worldSize.y; y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getTile().getProperties().containsKey("blockDirections")
                        || !cell.getTile().getProperties().get("walkable", Boolean.class)) {
                    collisions.get(x).add(new Chamber(new Vector2(x, y), false));
                } else {
                    collisions.get(x).add(new Chamber(new Vector2(x, y), true));
                }
            }
        }

        this.pathFiding = new AStar(this);
    }

    public Chamber getChamber(Vector2 position) {
        return getChamber((int) position.x, (int) position.y);
    }

    public Chamber getChamber(int x, int y) {
        return this.collisions.get(x).get(y);
    }

    public void getSimplfiedPath(Vector2 startPosition, Vector2 endPosition, AStarCallback callback) {
        this.pathFiding.findPath(startPosition, endPosition, callback);
    }

    public ArrayList<Chamber> getNeighbours(Chamber chamber) {
        ArrayList<Chamber> neighbours = new ArrayList<>();
        // -1,0
        // 1,0
        // 0,-1
        // 0,1
        if (!(chamber.getWorldPos().x - 1 < 0)) {
            neighbours.add(getChamber((int) chamber.getWorldPos().x - 1, (int) chamber.getWorldPos().y));
        }
        if (!(chamber.getWorldPos().x + 1 > worldSize.x - 1)) {
            neighbours.add(getChamber((int) chamber.getWorldPos().x + 1, (int) chamber.getWorldPos().y));
        }
        if (!(chamber.getWorldPos().y - 1 < 0)) {
            neighbours.add(getChamber((int) chamber.getWorldPos().x, (int) chamber.getWorldPos().y - 1));
        }
        if (!(chamber.getWorldPos().y + 1 > worldSize.y - 1)) {
            neighbours.add(getChamber((int) chamber.getWorldPos().x, (int) chamber.getWorldPos().y + 1));
        }

        return neighbours;
    }

    public int getDistance(Chamber cA, Chamber cB) {
        int xDistance = (int) Math.abs(cA.getWorldPos().x - cB.getWorldPos().x);
        int yDistance = (int) Math.abs(cA.getWorldPos().y - cB.getWorldPos().y);

        return xDistance + yDistance;
    }

    class Chamber implements Comparable<Chamber> {
        private Vector2 worldPos;
        private boolean walkable;
        private int gCost;
        private int hCost;
        private Chamber parent;

        public Chamber(Vector2 worldPos, boolean walkable) {
            this.worldPos = worldPos;
            this.walkable = walkable;
        }

        public boolean getWalkable() {
            return walkable;
        }

        public void setGCost(int gCost) {
            this.gCost = gCost;
        }

        public int getGCost() {
            return this.gCost;
        }

        public void setHCost(int hCost) {
            this.hCost = hCost;
        }

        public int getHCost() {
            return this.hCost;
        }

        public int getFCost() {
            return this.gCost + this.hCost;
        }

        public Vector2 getWorldPos() {
            return this.worldPos;
        }

        public void setParent(Chamber parent) {
            this.parent = parent;
        }

        public Chamber getParent() {
            return this.parent;
        }

        public int compareTo(Chamber anotherChamber) {
            if (this.getFCost() > anotherChamber.getFCost() || this.getFCost() == anotherChamber.getFCost()
                    && this.getHCost() > anotherChamber.getHCost()) {
                return 1;
            }
            return -1;
        }
    }
}
