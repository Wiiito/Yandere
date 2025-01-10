package com.yandere.Weapons;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private Animation<TextureRegion> currentWeaponAnimation;
    private Map<String, Animation<TextureRegion>> animations;

    public Weapon(Map<String, Animation<TextureRegion>> animations) {
        this.animations = animations;
    }

    public void setAnimation(String animationName) {
        this.currentWeaponAnimation = animations.get(animationName);
    }

    public TextureRegion getFrame(float animationTime) {
        return currentWeaponAnimation.getKeyFrame(animationTime, true);
    }
}
