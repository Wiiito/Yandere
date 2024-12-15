package com.yandere.gameInterfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapInterface {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    public MapInterface() {
        tiledMap = new TmxMapLoader().load("map0.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }
}
