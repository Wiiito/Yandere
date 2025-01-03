package com.yandere.handlers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.yandere.lib.MapGrid;

public class MapHandler {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int currentLayer = 0;
    private int[] underPLayer = { 1, 2, 3, 4, 5 };
    private int[] walls = { 6, 7 };
    private int wallViewRender = 0;

    // TODO - BETTER MAP GRID
    private MapGrid mapGrid;

    public MapHandler() {
        tiledMap = new TmxMapLoader().load("map/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        mapGrid = new MapGrid(getMapTileLayer(currentLayer));
    }

    // Needs to be passed as gridPosition
    public boolean collides(Vector2 fromPosition, Vector2 toPosition) {
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(currentLayer);
        Cell cell = tileLayer.getCell((int) toPosition.x, (int) toPosition.y);
        Cell currentCell = tileLayer.getCell((int) fromPosition.x, (int) fromPosition.y);
        TiledMapTile tile = cell.getTile();

        Vector2 desiredDirection = toPosition.cpy().sub(fromPosition);

        // Colisoes dentro pra fora
        if (currentCell.getTile().getProperties().get("blockDirections") != null) {
            int rotation = !currentCell.getFlipVertically() ? currentCell.getRotation() : 2;
            // Dentro da casa com uma parede e tentando andar para o lado da parede
            if ((int) currentCell.getTile().getProperties().get("blockDirections") == 1) {
                switch (rotation) {
                    case 0:
                        if (desiredDirection.idt(Vector2.Y))
                            return true;
                        break;
                    case 1:
                        if (desiredDirection.idt(Vector2.X.cpy().scl(-1)))
                            return true;
                        break;
                    case 2:
                        if (desiredDirection.idt(Vector2.Y.cpy().scl(-1)))
                            return true;
                        break;
                    case 3:
                        if (desiredDirection.idt(Vector2.X))
                            return true;
                        break;
                }
            } else {
                // Dentro de ma casa com duas paredes tentando andar para o lado de uma
                switch (rotation) {
                    case 0:
                        if (desiredDirection.idt(Vector2.Y) || desiredDirection.idt(Vector2.X))
                            return true;
                        break;
                    case 1:
                        if (desiredDirection.idt(Vector2.X.cpy().scl(-1)) ||
                                desiredDirection.idt(Vector2.Y))
                            return true;
                        break;
                    case 2:
                        if (desiredDirection.idt(Vector2.Y.cpy().scl(-1)) ||
                                desiredDirection.idt(Vector2.X.cpy().scl(-1)))
                            return true;
                        break;
                    case 3:
                        if (desiredDirection.idt(Vector2.X) ||
                                desiredDirection.idt(Vector2.Y.cpy().scl(-1)))
                            return true;
                        break;
                }
            }
        }

        // Colisoes fora pra dentro
        if ((boolean) tile.getProperties().get("walkable")) {
            Object blockDirections = tile.getProperties().get("blockDirections");
            if (blockDirections == null)
                return false;

            int rotation = !cell.getFlipVertically() ? cell.getRotation() : 2;

            if ((int) blockDirections == 1) {
                // Uma só parede
                switch (rotation) {
                    case 0:
                        if (desiredDirection.idt(Vector2.Y.cpy().scl(-1)))
                            return true;
                        break;
                    case 1:
                        if (desiredDirection.idt(Vector2.X))
                            return true;
                        break;
                    case 2:
                        if (desiredDirection.idt(Vector2.Y))
                            return true;
                        break;
                    case 3:
                        if (desiredDirection.idt(Vector2.X.cpy().scl(-1)))
                            return true;
                        break;
                }
            } else {
                // Duas paredes
                switch (rotation) {
                    case 0:
                        if (desiredDirection.idt(Vector2.Y.cpy().scl(-1))
                                || desiredDirection.idt(Vector2.X.cpy().scl(-1)))
                            return true;
                        break;
                    case 1:
                        if (desiredDirection.idt(Vector2.X) ||
                                desiredDirection.idt(Vector2.Y.cpy().scl(-1)))
                            return true;
                        break;
                    case 2:
                        if (desiredDirection.idt(Vector2.Y) ||
                                desiredDirection.idt(Vector2.X))
                            return true;
                        break;
                    case 3:
                        if (desiredDirection.idt(Vector2.X.cpy().scl(-1)) ||
                                desiredDirection.idt(Vector2.Y))
                            return true;
                        break;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    public Cell getCell(Vector2 position) {
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(currentLayer);
        Cell cell = tileLayer.getCell((int) position.x, (int) position.y);

        return cell;
    }

    public boolean isInsideWall(Vector2 gridPosition) {
        for (int wallLayer : walls)
            if (getMapTileLayer(wallLayer).getCell((int) gridPosition.x, (int) gridPosition.y) != null)
                return true;
        return false;
    }

    public TiledMapTileLayer getMapTileLayer(int layer) {
        return (TiledMapTileLayer) tiledMap.getLayers().get(layer);
    }

    // TODO - Tira isso DAQUI
    public ArrayList<Vector2> getSiplifiedPath(Vector2 startPos, Vector2 finalPos) {
        return mapGrid.getSimplfiedPath(startPos, finalPos);
    }

    public int getHeight() {
        MapProperties props = tiledMap.getProperties();
        return props.get("height", Integer.class);
    }

    public int getWidth() {
        MapProperties props = tiledMap.getProperties();
        return props.get("width", Integer.class);
    }

    public void renderUnderPLayer(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render(underPLayer);
    }

    public void renderAbovePlayer(OrthographicCamera camera, Vector2 playerGridPosition) {
        float width = camera.viewportWidth * camera.zoom;
        float height = camera.viewportHeight * camera.zoom;
        float w = width * Math.abs(camera.up.y) + height * Math.abs(camera.up.x);
        float h = height * Math.abs(camera.up.y) + width * Math.abs(camera.up.x);
        mapRenderer.setView(camera.combined, camera.position.x - w / 2,
                camera.position.y + wallViewRender + 16, w, h / 2 - wallViewRender);

        for (int wallLayer : walls) {
            if (getMapTileLayer(wallLayer).getCell((int) playerGridPosition.x, (int) playerGridPosition.y) != null) {
                wallViewRender = 64;
                break;
            } else {
                wallViewRender = 0;
            }
        }

        mapRenderer.render(walls);
    }
}
