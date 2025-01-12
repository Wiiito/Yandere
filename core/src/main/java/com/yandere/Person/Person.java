// State
package com.yandere.Person;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yandere.gameInterfaces.GameObject;
import com.yandere.handlers.MapHandler;

public class Person extends GameObject {
    protected Map<String, Animation<TextureRegion>> animations;
    protected float elapsedTime = (float) Math.random();
    protected Animation<TextureRegion> currentAnimation;
    private Direction direction = Direction.Bottom;
    private Map<String, Float> animationsDelay;
    private float animationDelayTimer = 0;
    private State currentState;
    private String name;

    protected Vector2 desiredGridPosition;
    protected Vector2 gridPosition;
    private int currentLayer = 0;
    private float speed = 40;

    protected MapHandler map;

    public enum State {
        Idle, Walk, Run, Sit
    }

    public Person(String name, Map<String, Animation<TextureRegion>> animations, Map<String, Float> animatiosDelay,
            MapHandler map) {
        this.name = name;
        this.animations = new HashMap<>(animations);
        this.map = map;
        this.animationsDelay = new HashMap<>(animatiosDelay);

        this.currentAnimation = this.animations.entrySet().iterator().next().getValue();

        sprite = new Sprite();
        sprite.setSize(currentAnimation.getKeyFrame(0).getRegionWidth(),
                currentAnimation.getKeyFrame(0).getRegionHeight());
        sprite.setPosition(0, 0);

        currentState = State.Idle;

        gridPosition = Vector2.Zero;
        desiredGridPosition = Vector2.Zero;
    }

    public String getName() {
        return this.name;
    }

    public void setDirection(Direction direction) {
        if (gridPosition.x == desiredGridPosition.x && gridPosition.y == desiredGridPosition.y)
            this.direction = direction;
    }

    public void setPosition(Vector2 position) {
        this.sprite.setPosition(position.x, position.y);
    }

    public void setGridPosition(Vector2 position) {
        this.gridPosition = position;
        this.desiredGridPosition = new Vector2(position);
    }

    public Vector2 getGridPosition() {
        return this.gridPosition;
    }

    public void snapToGrid() {
        this.setPosition(gridPosition.cpy().scl(16));
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void move() {
        if (gridPosition.x == desiredGridPosition.x && gridPosition.y == desiredGridPosition.y) {
            switch (this.direction) {
                case Top:
                    desiredGridPosition = gridPosition.cpy().add(0, 1);
                    break;
                case Bottom:
                    desiredGridPosition = gridPosition.cpy().add(0, -1);
                    break;
                case Left:
                    desiredGridPosition = gridPosition.cpy().add(-1, 0);
                    break;
                case Right:
                    desiredGridPosition = gridPosition.cpy().add(1, 0);
                    break;
            }
        }
    }

    public int getCurrentLayer() {
        return this.currentLayer;
    }

    protected void handleFloorChange(Vector2 positionOnGrid) {
        if (this.map.getMapTileLayer(this.currentLayer).getCell((int) positionOnGrid.x, (int) positionOnGrid.y)
                .getTile()
                .getProperties()
                .get("changeFloor") != null) {
            this.currentLayer = this.map.getMapTileLayer(this.currentLayer)
                    .getCell((int) positionOnGrid.x, (int) positionOnGrid.y).getTile().getProperties()
                    .get("changeFloor",
                            Integer.class)
                    * map.getLayerCount();
        }
    }

    protected void handleMovement(float deltaTime) {
        if (!gridPosition.idt(desiredGridPosition)) {
            Vector2 movement = desiredGridPosition.cpy().sub(gridPosition).scl(speed)
                    .scl(deltaTime);
            Vector2 thisFrameMovement = getPosition().cpy().add(movement);

            int thisFrameGridX;
            int thisFrameGridY;

            // Float -> Int em java SIMPLISMENTE *COME* o bit de sinal... isso me deu uma
            // dor de cabeça tão grande...
            // Esse if é NECESSARIO, unico jeito dessa brindadeira ai funcionar sem ficar
            // todo bugado...
            if (direction == Direction.Bottom || direction == Direction.Left) {
                thisFrameGridX = -Math.floorDiv((int) -thisFrameMovement.x, 16);
                thisFrameGridY = -Math.floorDiv((int) -thisFrameMovement.y, 16);
            } else {
                thisFrameGridX = (int) thisFrameMovement.x / 16;
                thisFrameGridY = (int) thisFrameMovement.y / 16;
            }

            if (speed <= 40) {
                currentState = State.Walk;
            } else {
                currentState = State.Run;
            }

            if (desiredGridPosition.x == thisFrameGridX && desiredGridPosition.y == thisFrameGridY) { // Ultimo quadrado
                this.setGridPosition(new Vector2(thisFrameGridX, thisFrameGridY));
                snapToGrid();
                handleFloorChange(this.getGridPosition());
                setState(State.Idle);
            } else {
                this.setPosition(thisFrameMovement);
            }
        }
    }

    public void setState(State state) {
        this.currentState = state;
    }

    public State getState() {
        return this.currentState;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void update(float deltaTime) {
        this.currentAnimation = animations.get(currentState.toString() + direction);
        elapsedTime += deltaTime;

        float animationDuration = currentAnimation.getAnimationDuration();
        TextureRegion lastFrameTextureRegion = currentAnimation.getKeyFrame(animationDuration);

        // Se existe delay nessa animação
        if (animationsDelay.get(currentState.toString()) != null) {
            // Vendo se o frame atual é o ultimo frame da animação
            if (lastFrameTextureRegion
                    .equals(currentAnimation.getKeyFrame(elapsedTime - currentAnimation.getFrameDuration(), true))
                    || animationDelayTimer > 0) {
                elapsedTime = 0;
                this.animationDelayTimer += deltaTime;
            }
            if (this.animationDelayTimer >= animationsDelay.get(currentState.toString())) {
                this.animationDelayTimer = 0;
            }
        }

        handleMovement(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY());
    }
}