package com.yandere.handlers;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yandere.Person.Person;

public class Player extends Person {
    public Player(Map<String, Animation<TextureRegion>> animations) {
        super("player", animations);
    }

    public InputAdapter getInputAdapter() {
        return new InputAdapter() {
            public boolean keyTyped(char keyCode) {
                switch (keyCode) {
                    case 'w':
                    case 'W':
                        if (getDirection() == Direction.Top)
                            setPosition(getPosition().add(0, 120 * Gdx.graphics.getDeltaTime()));
                        setDirection(Direction.Top);
                        return true;
                    case 'd':
                    case 'D':
                        if (getDirection() == Direction.Right)
                            setPosition(getPosition().add(120 * Gdx.graphics.getDeltaTime(), 0));
                        setDirection(Direction.Right);
                        return true;
                    case 's':
                    case 'S':
                        if (getDirection() == Direction.Bottom)
                            setPosition(getPosition().add(0, -120 * Gdx.graphics.getDeltaTime()));
                        setDirection(Direction.Bottom);
                        return true;
                    case 'a':
                    case 'A':
                        if (getDirection() == Direction.Left)
                            setPosition(getPosition().add(-120 * Gdx.graphics.getDeltaTime(), 0));
                        setDirection(Direction.Left);
                        return true;
                }
                return false;
            }
        };
    }
}
