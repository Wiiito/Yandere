package com.yandere.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class MapHandler {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int currentLayer = 0;

    public MapHandler() {
        tiledMap = new TmxMapLoader().load("map0.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
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

    public TiledMapTileLayer getMapTileLayer(int layer) {
        return (TiledMapTileLayer) tiledMap.getLayers().get(layer);
    }

    public int getHeight() {
        MapProperties props = tiledMap.getProperties();
        return props.get("height", Integer.class);
    }

    public int getWidth() {
        MapProperties props = tiledMap.getProperties();
        return props.get("width", Integer.class);
    }

    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }
}
