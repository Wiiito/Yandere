// Decorator
package com.yandere.handlers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yandere.gameInterfaces.Interactible;
import com.yandere.lib.DDACollision;
import com.yandere.lib.MapGrid;

public class MapHandler extends OrthogonalTiledMapRenderer {
	private int currentLayer = 0;
	private int layersCount = 10;
	private int[] underPLayer = { 1, 2, 3, 4, 5 };
	private int[] walls = { 6, 7 };
	private int[] abovePlayer = { 8 };
	private int[] objectsLayer = { 9 };

	private int[] actualUnderPlayer = underPLayer.clone();
	private int[] actualWalls = walls.clone();
	private int[] actualAbovePlayer = abovePlayer.clone();
	private int[] actualObjects = objectsLayer.clone();

	private ArrayList<MapGrid> mapGrid;
	private DDACollision rayTracingCollision;

	public MapHandler() {
		super(new TmxMapLoader().load("map/map.tmx"));
		mapGrid = new ArrayList<>();
		mapGrid.add(new MapGrid(getMapTileLayer(0)));
		mapGrid.add(new MapGrid(getMapTileLayer(this.layersCount)));
		rayTracingCollision = new DDACollision();
	}

	// Precisa passar como gridPosition (100 linhas de colisão pro jogador... kk,
	// sisteminha de andar dentro de parede pra fazer realista ficou chein de if...)
	public boolean collides(Vector2 fromPosition, Vector2 toPosition) {
		TiledMapTileLayer tileLayer = (TiledMapTileLayer) super.map.getLayers().get(currentLayer);
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
						if (desiredDirection.idt(Vector2.X.cpy().scl(-1)) || desiredDirection.idt(Vector2.Y))
							return true;
						break;
					case 2:
						if (desiredDirection.idt(Vector2.Y.cpy().scl(-1))
								|| desiredDirection.idt(Vector2.X.cpy().scl(-1)))
							return true;
						break;
					case 3:
						if (desiredDirection.idt(Vector2.X) || desiredDirection.idt(Vector2.Y.cpy().scl(-1)))
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
						if (desiredDirection.idt(Vector2.X) || desiredDirection.idt(Vector2.Y.cpy().scl(-1)))
							return true;
						break;
					case 2:
						if (desiredDirection.idt(Vector2.Y) || desiredDirection.idt(Vector2.X))
							return true;
						break;
					case 3:
						if (desiredDirection.idt(Vector2.X.cpy().scl(-1)) || desiredDirection.idt(Vector2.Y))
							return true;
						break;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	public Interactible interact(Vector2 gridPosition, boolean ispressed) {
		int x = 0;
		int y = 0;
		int objectWidth = 0;
		int objectHeight = 0;
		for (int objectLayer : actualObjects) {
			MapLayer layer = super.map.getLayers().get(objectLayer);

			for (MapObject object : layer.getObjects()) {
				if (object instanceof TiledMapTileMapObject) {
					final TiledMapTileMapObject tileObject = (TiledMapTileMapObject) object;
					final TiledMapTile tile = tileObject.getTile();

					x = Math.floorDiv((int) tileObject.getX(), 16);
					y = Math.floorDiv((int) tileObject.getY(), 16);

					objectWidth = Math.floorDiv(tile.getTextureRegion().getRegionWidth(), 16);
					objectHeight = Math.floorDiv(tile.getTextureRegion().getRegionHeight(), 16);

				}

				if (object instanceof RectangleMapObject) {
					final RectangleMapObject tileObject = (RectangleMapObject) object;
					final Rectangle tile = tileObject.getRectangle();

					x = Math.floorDiv((int) tile.getX(), 16);
					y = Math.floorDiv((int) tile.getY(), 16);

					objectWidth = Math.floorDiv((int) tile.getWidth(), 16);
					objectHeight = Math.floorDiv((int) tile.getHeight(), 16);
				}

				if (gridPosition.x >= x && gridPosition.x < x + objectWidth && gridPosition.y >= y
						&& gridPosition.y < y + objectHeight) {
					Interactible interactible = new Interactible();
					interactible.type = Interactible.Type.valueOf(object.getProperties().get("Type", String.class));
					if (ispressed) {
						if (object.getProperties().get("Name", String.class) != null) {
							interactible.name = object.getProperties().get("Name", String.class);
						}
						interactible.dialog = object.getProperties().get("Dialog", String.class);

						if (object instanceof TiledMapTileMapObject) {
							// Talves considere mandar esse objeto para o jogador, pra dropar o item caso
							// troque
							layer.getObjects().remove(object);
						}
					}
					return interactible;
				}
			}
		}
		return null;
	}

	public boolean isInsideWall(Vector2 gridPosition) {
		for (int wallLayer : actualWalls)
			if (getMapTileLayer(wallLayer).getCell((int) gridPosition.x, (int) gridPosition.y) != null)
				return true;
		return false;
	}

	// Gets
	public Cell getCell(Vector2 position) {
		TiledMapTileLayer tileLayer = (TiledMapTileLayer) super.map.getLayers().get(currentLayer);
		Cell cell = tileLayer.getCell((int) position.x, (int) position.y);

		return cell;
	}

	public TiledMapTileLayer getMapTileLayer(int layer) {
		return (TiledMapTileLayer) super.map.getLayers().get(layer);
	}

	public MapGrid getMapGrid(int layer) {
		return this.mapGrid.get(layer);
	}

	public DDACollision getRayTracingTest() {
		return this.rayTracingCollision;
	}

	public int getHeight() {
		MapProperties props = super.map.getProperties();
		return props.get("height", Integer.class);
	}

	public void setCurrentFloor(int layer) {
		this.currentLayer = layer;
		for (int i = 0; i < underPLayer.length; i++) {
			this.actualUnderPlayer[i] = underPLayer[i] + layer;
		}
		for (int i = 0; i < walls.length; i++) {
			this.actualWalls[i] = walls[i] + layer;
		}
		for (int i = 0; i < abovePlayer.length; i++) {
			this.actualAbovePlayer[i] = abovePlayer[i] + layer;
		}
		for (int i = 0; i < objectsLayer.length; i++) {
			this.actualObjects[i] = objectsLayer[i] + layer;
		}
	}

	public int getWidth() {
		MapProperties props = super.map.getProperties();
		return props.get("width", Integer.class);
	}

	public int getLayerCount() {
		return this.layersCount;
	}

	public Vector2 getClosestAlarm(Vector2 fromPosition, int layer) {
		MapLayer mapLayer = super.map.getLayers().get(layer + this.layersCount - 1);

		Vector2 alarmGridPosition = null;
		float closestDistance = Float.MAX_VALUE;

		for (MapObject object : mapLayer.getObjects()) {
			if (object.getProperties().get("Type", String.class).equals(new String("Alarm"))) {
				RectangleMapObject alarm = (RectangleMapObject) object;
				Rectangle rectangle = alarm.getRectangle();

				int alarmGridPosX = (int) rectangle.getX() / 16;
				int alarmGridPosY = (int) rectangle.getY() / 16;

				Vector2 currentAlarmGridPosition = new Vector2(alarmGridPosX, alarmGridPosY);
				Vector2 vectorDistance = fromPosition.cpy().sub(currentAlarmGridPosition);

				float distance = (float) Math.sqrt(Math.pow(vectorDistance.x, 2) + Math.pow(vectorDistance.y, 2));

				if (closestDistance > distance) {
					closestDistance = distance;
					alarmGridPosition = currentAlarmGridPosition;
				}
			}
		}
		return alarmGridPosition;
	}

	// Rendering map
	public void renderUnderPLayer(OrthographicCamera camera) {
		super.setView(camera);
		super.render(actualUnderPlayer);
	}

	public void renderWalls(OrthographicCamera camera, Vector2 playerGridPosition) {
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		float w = width * Math.abs(camera.up.y) + height * Math.abs(camera.up.x);
		float h = height * Math.abs(camera.up.y) + width * Math.abs(camera.up.x);

		boolean insideWall = false;
		int wallViewRender = 0;

		// Margens
		int leftInsideWalls = 0;
		int rightInsideWalls = 0;

		for (int wallLayer : actualWalls) {
			if (getMapTileLayer(wallLayer).getCell((int) playerGridPosition.x, (int) playerGridPosition.y) != null) {
				insideWall = true;
			}
		}

		if (insideWall) {
			int counter = 1;
			int insideLeftWalls = 0;
			int insideRightWalls = 0;

			// Calculando quantas paredes existem na esquerda
			for (int i = 0; i > -9; i--) {
				int emptyWallsCounter = 0;
				for (int wallLayer : actualWalls) {
					if (getMapTileLayer(wallLayer).getCell(Math.max((int) playerGridPosition.x + i, 0),
							(int) playerGridPosition.y) != null) {
						insideLeftWalls++;
						break;
					} else {
						emptyWallsCounter++;
					}
				}
				if (emptyWallsCounter >= walls.length) {
					break;
				}
			}

			// Calculando quantas paredes existem na direita
			for (int i = 0; i < 8; i++) {
				int emptyWallsCounter = 0;
				for (int wallLayer : actualWalls) {
					if (getMapTileLayer(wallLayer).getCell(Math.min((int) playerGridPosition.x + i, this.getWidth()),
							(int) playerGridPosition.y) != null) {
						insideRightWalls++;
						break;
					} else {
						emptyWallsCounter++;
					}
				}
				if (emptyWallsCounter >= walls.length) {
					break;
				}
			}

			// Logica de desenho
			if (insideRightWalls >= 8 && insideLeftWalls >= 8) { // Dentro de uma parede que completa a tela
				for (int wallLayer : actualWalls) {
					while (getMapTileLayer(wallLayer).getCell((int) playerGridPosition.x,
							(int) playerGridPosition.y + counter) != null) {
						counter++;
					}
					wallViewRender = 16 * counter;
				}
			} else { // Dentro de parede incompleta
				leftInsideWalls = insideLeftWalls * 16;
				rightInsideWalls = insideRightWalls * 16 + 16;
				int lower = 0;
				for (int wallLayer : actualWalls) {
					int insideCounter = 1;
					while (getMapTileLayer(wallLayer).getCell((int) playerGridPosition.x,
							(int) playerGridPosition.y - insideCounter) != null) {
						insideCounter++;
					}
					lower = Math.max(insideCounter, lower);
				}
				wallViewRender = -16 * lower;
			}
		} else { // Fora de parede
			int higher = 10;
			for (int wallLayer : actualWalls) {
				int counter = 1;
				while (getMapTileLayer(wallLayer).getCell((int) playerGridPosition.x,
						(int) playerGridPosition.y - counter) == null && counter < 10) {
					counter++;
				}
				higher = Math.min(counter, higher);
			}
			wallViewRender = -16 * higher + 16;
		}

		// left side render
		super.setView(camera.combined, camera.position.x - w / 2, camera.position.y + wallViewRender + 16,
				w / 2 - leftInsideWalls, h / 2 - wallViewRender);
		super.render(actualWalls);

		// Right side render
		super.setView(camera.combined, camera.position.x + rightInsideWalls, camera.position.y + wallViewRender + 16,
				w / 2 - rightInsideWalls, h / 2 - wallViewRender);
		super.render(actualWalls);
	}

	public void renderObject(MapObject object, SpriteBatch batch) {
		if (object instanceof TiledMapTileMapObject) {
			final TiledMapTileMapObject tileObject = (TiledMapTileMapObject) object;
			final TiledMapTile tile = tileObject.getTile();

			final float x = tileObject.getX();
			final float y = tileObject.getY();

			final TextureRegion region = tile.getTextureRegion();
			batch.draw(region, x, y, region.getRegionWidth(), region.getRegionHeight());
		}
	}

	public void renderObjects(SpriteBatch batch) {
		for (int objectLayer : actualObjects) {
			MapLayer layer = super.map.getLayers().get(objectLayer);
			for (MapObject object : layer.getObjects()) {
				renderObject(object, batch);
			}
		}
	}

	public void renderAbovePlayer(OrthographicCamera camera) {
		super.setView(camera);
		super.render(actualAbovePlayer);
	}
}
