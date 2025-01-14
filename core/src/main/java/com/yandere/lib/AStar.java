package com.yandere.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.badlogic.gdx.math.Vector2;
import com.yandere.gameInterfaces.AStarCallback;

public class AStar {
    MapGrid mapGrid;
    public static ExecutorService threadPool;

    public AStar(MapGrid mapGrid) {
        this.mapGrid = mapGrid;
        threadPool = Executors.newFixedThreadPool(1);
    }

    public void findPath(Vector2 startPos, Vector2 endPos, AStarCallback callback) {
        threadPool.execute(new Algorithm(startPos, endPos, callback));
    }

    private ArrayList<Vector2> retracePath(MapGrid.Chamber startNode, MapGrid.Chamber lastChamber) {
        ArrayList<Vector2> path = new ArrayList<>();
        MapGrid.Chamber currentChamber = lastChamber;

        while (currentChamber != startNode) {
            path.add(currentChamber.getWorldPos());
            currentChamber = currentChamber.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    public static ArrayList<Vector2> simplifyPath(ArrayList<Vector2> completeList) {
        ArrayList<Vector2> simpleList = new ArrayList<>();
        Vector2 oldDirection = Vector2.Zero;

        for (int i = 1; i < completeList.size(); i++) {
            if (completeList.get(i) == completeList.get(completeList.size() - 1)) {
                simpleList.add(completeList.get(i));
                continue;
            }

            Vector2 newDirection = new Vector2(completeList.get(i - 1).x - completeList.get(i).x,
                    completeList.get(i - 1).y - completeList.get(i).y);

            if (oldDirection.idt(newDirection)) {
                continue;
            }
            simpleList.add(completeList.get(i - 1));
            oldDirection = newDirection;
        }

        return simpleList;
    }

    class Algorithm implements Runnable {
        AStarCallback callback;
        Vector2 startPos;
        Vector2 endPos;

        public Algorithm(Vector2 startPos, Vector2 endPos, AStarCallback callback) {
            this.startPos = startPos;
            this.endPos = endPos;
            this.callback = callback;
        }

        @Override
        public void run() {
            PriorityQueue<MapGrid.Chamber> openSet;
            ArrayList<MapGrid.Chamber> closedSet;
            openSet = new PriorityQueue<>();
            closedSet = new ArrayList<>();
            MapGrid.Chamber startChamber = mapGrid.getChamber(startPos);
            MapGrid.Chamber endChamber = mapGrid.getChamber(endPos);

            openSet.add(startChamber);

            while (!openSet.isEmpty()) {
                MapGrid.Chamber currentChamber = openSet.poll();
                closedSet.add(currentChamber);

                if (currentChamber == endChamber) {
                    callback.pathFind(retracePath(startChamber, endChamber));
                }

                for (MapGrid.Chamber neighbour : mapGrid.getNeighbours(currentChamber)) {
                    if (!neighbour.getWalkable() || closedSet.contains(neighbour)) {
                        continue;
                    }

                    int newMovementCostToNeighbour = currentChamber.getGCost() + 1;
                    if (newMovementCostToNeighbour < neighbour.getGCost() || !openSet.contains(neighbour)) {
                        neighbour.setGCost(newMovementCostToNeighbour);
                        neighbour.setHCost(mapGrid.getDistance(neighbour, endChamber));
                        neighbour.setParent(currentChamber);

                        if (!openSet.contains(neighbour)) {
                            openSet.add(neighbour);
                        }
                    }
                }
            }
        }
    }
}
