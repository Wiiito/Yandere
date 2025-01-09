package com.yandere.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.yandere.Person.Person;

public class Player extends Person {
    private boolean canMove = true;
    private float timeSinceLastDirection = 0.f; // Efeito pokemon, virar ao tocar, andar ao pressionar

    public Player(PlayerBuilder playerBuilder,
            MapHandler map) {
        super("player", playerBuilder.getPLayerAnimations(), playerBuilder.getPlayerAnimationsDelay(), map);
    }

    /*
     * Os cliques sao usados para mudar a direção e em caso de a direção ja estar
     * setada, move 1 quadrado.
     * "Duplicado" como keyDown ja que é impossivel um ser humano dar 1 clique de um
     * frame...
     */

    public InputAdapter getInputAdapter() {
        return new InputAdapter() {
            public boolean keyDown(int keyCode) {
                switch (keyCode) {
                    case Input.Keys.SHIFT_LEFT:
                        setSpeed(60);
                        return true;
                    case Input.Keys.W:
                        if (getDirection() == Direction.Top) {
                            move();
                        } else {
                            setDirection(Direction.Top);
                            timeSinceLastDirection = 0;
                        }
                        return true;
                    case Input.Keys.D:
                        if (getDirection() == Direction.Right) {
                            move();
                        } else {
                            setDirection(Direction.Right);
                            timeSinceLastDirection = 0;
                        }
                        return true;
                    case Input.Keys.S:
                        if (getDirection() == Direction.Bottom) {
                            move();
                        } else {
                            setDirection(Direction.Bottom);
                            timeSinceLastDirection = 0;
                        }
                        return true;
                    case Input.Keys.A:
                        if (getDirection() == Direction.Left) {
                            move();
                        } else {
                            setDirection(Direction.Left);
                            timeSinceLastDirection = 0;
                        }
                        return true;
                }
                return false;
            }

            public boolean keyUp(int keyCode) {
                switch (keyCode) {
                    case Input.Keys.SHIFT_LEFT:
                        setSpeed(40);
                        return true;
                }
                return false;
            }
        };

    }

    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceLastDirection += Gdx.graphics.getDeltaTime();

        if (canMove) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)
                    && timeSinceLastDirection > .1f) {
                setDirection(Direction.Top);
                move();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)
                    && timeSinceLastDirection > .1f) {
                setDirection(Direction.Right);
                move();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)
                    && timeSinceLastDirection > .1f) {
                setDirection(Direction.Bottom);
                move();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)
                    && timeSinceLastDirection > .1f) {
                setDirection(Direction.Left);
                move();
            }
        }
    }

    @Override
    protected void handleFloorChange(Vector2 positionOnGrid) {
        super.handleFloorChange(positionOnGrid);
        map.changePlayerFloor(super.getCurrentLayer());
    }

    @Override
    public void handleMovement(float deltaTime) {
        if (map.collides(gridPosition, desiredGridPosition))
            this.desiredGridPosition = gridPosition.cpy();
        super.handleMovement(deltaTime);
    }
}
