package com.yandere.gameInterfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameUi {
    private Texture dialogBox;
    private Texture nameBox;
    private boolean isInDialog = false;

    private String currentDialog = "";
    private String currentName = "";

    private BitmapFont font12;
    private BitmapFont font10;

    private static GameUi gameUi;

    private GameUi() {
        this.dialogBox = new Texture(Gdx.files.internal("interface/DialogBox.png"));
        this.nameBox = new Texture(Gdx.files.internal("interface/NameBox.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Pixel Digivolve.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();

        parameter.size = 12;
        font12 = generator.generateFont(parameter);

        parameter.size = 10;
        font10 = generator.generateFont(parameter);

        generator.dispose();
        font12.setColor(Color.WHITE);
        font10.setColor(Color.WHITE);
    }

    public static void instantiate() {
        // Colocando uam variavel pra instanciar pra nao ter q passar um if todo frame
        gameUi = new GameUi();
    }

    public static GameUi getGameUi() {
        return gameUi;
    }

    public void showDialog(String name, String dialog) {
        if (name.length() == 0) {
            showDialog(dialog);
            return;
        }
        this.isInDialog = true;
        currentName = name;
        currentDialog = dialog;
    }

    public void showDialog(String dialog) {
        this.isInDialog = true;
        currentName = "";
        currentDialog = dialog;
    }

    public boolean getIsInDialog() {
        return this.isInDialog;
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            this.isInDialog = false;
        }
    }

    public void render(SpriteBatch batch, float screenPositionX, float screenPositionY, float screenWidth,
            float screenHeight) {
        if (isInDialog) {
            batch.draw(dialogBox, screenPositionX, screenPositionY, screenWidth, screenHeight);
            if (!(currentName.length() == 0)) {
                batch.draw(nameBox, screenPositionX, screenPositionY, screenWidth, screenHeight);
            }
            font12.draw(batch, this.currentName, screenPositionX + 12, screenPositionY + 92);
            font10.draw(batch, this.currentDialog, screenPositionX + 16, screenPositionY + 74);
        }
        BoxUI.getBoxUI().render(batch, screenPositionX + screenWidth / 2 + 12,
                screenPositionY + screenHeight / 2 + 20);
    }
}
