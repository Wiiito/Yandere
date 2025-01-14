package com.yandere.gameInterfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yandere.lib.AStar;

public class MenuInterface {

    private boolean isInMenu = true;

    private Texture menuImage;
    private Texture button;

    private Music menuMusic;

    private long start = 0;
    private long end = 0;

    private float[] yPosButton, xPosButton;

    private int currentOption;

    public MenuInterface() {

        this.menuImage = new Texture(Gdx.files.internal("menuAssets/menuImage.png"));
        this.button = new Texture(Gdx.files.internal("menuAssets/BlackShape.png"));
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/OstMenu.ogg"));

        yPosButton = new float[3];
        xPosButton = new float[3];

        yPosButton[0] = 370;
        yPosButton[1] = 260;
        yPosButton[2] = 145;

        xPosButton[0] = 217;
        xPosButton[1] = 221;
        xPosButton[2] = 220;

        currentOption = 0;

    }

    public boolean isInMenu() {
        return isInMenu;
    }

    public void setIsInMenu() {
        this.isInMenu = !isInMenu;
    }

    public void update() {

        end = System.currentTimeMillis();

        long elapsedTime = end - start;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && elapsedTime > 100) {

            if (currentOption < xPosButton.length - 1) {
                currentOption++;
            } else {
                currentOption = 0;
            }

            start = System.currentTimeMillis();
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && elapsedTime > 100) {
            if (currentOption == 0) {
                currentOption = xPosButton.length - 1;
            } else {
                currentOption--;
            }

            start = System.currentTimeMillis();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {

            switch (currentOption) {
                case 0:
                    isInMenu = false;
                    menuMusic.pause();
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    break;
                case 1:

                    break;
                case 2:
                    AStar.threadPool.shutdown();
                    Gdx.app.exit();
                    break;
                default:
                    break;
            }

        }

    }

    public void render(SpriteBatch batch) {
        batch.draw(menuImage, 0, 0);
        batch.draw(button, xPosButton[currentOption], yPosButton[currentOption], 781 / 4, 261 / 4);
        menuMusic.play();
    }

}
