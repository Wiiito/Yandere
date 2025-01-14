package com.yandere.gameInterfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameUi {
    private Texture dialogBox;
    private Texture nameBox;
    private Texture HudBox;
    private Texture WeaponHud;
    private Texture EquipedHud;
    private Texture[] badEndFrames;

    private Animation<Texture> badEndAnimation;

    private Music endSound;
    private Music gameSoundtrack;

    private float timer = 0;

    private boolean isInDialog = false;
    private boolean getSword = false;
    private boolean isEquiped = false;
    private boolean isEndBad = false;

    private String currentDialog = "";
    private String currentName = "";

    private BitmapFont font12;
    private BitmapFont font10;

    private static GameUi gameUi;

    private GameUi() {
        this.dialogBox = new Texture(Gdx.files.internal("interface/DialogBox.png"));
        this.nameBox = new Texture(Gdx.files.internal("interface/NameBox.png"));
        this.HudBox = new Texture(Gdx.files.internal("interface/HudBox.png"));
        this.WeaponHud = new Texture(Gdx.files.internal("weaponHud/sword.png"));
        this.EquipedHud = new Texture(Gdx.files.internal("weaponHud/outline.jpg"));

        this.endSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/endAudio.ogg"));
        this.gameSoundtrack = Gdx.audio.newMusic(Gdx.files.internal("sounds/OstYandere.ogg"));

        loadEndTextures();

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

    public void changeSwordState() {
        this.getSword = true;
    }

    public void setIsEndBad() {
        if (!this.isEndBad)
            this.isEndBad = !isEndBad;
    }

    public void IsEquiped() {
        this.isEquiped = !isEquiped;
    }

    private void loadEndTextures() {

        this.badEndFrames = new Texture[55];

        for (int i = 0; i < 55; i++) {
            this.badEndFrames[i] = new Texture(Gdx.files.internal("framesAtumalaca/frame" + i + ".gif"));
        }

        this.badEndAnimation = new Animation<Texture>(0.08f, badEndFrames);
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
        if (getSword) {
            batch.draw(HudBox, screenPositionX - 10, screenPositionY - 5, 203 / 2, 76 / 2);
            batch.draw(WeaponHud, screenPositionX + 10, screenPositionY + 7, 500 / 30, 500 / 30);
            if (isEquiped) {
                batch.draw(EquipedHud, screenPositionX + 7.9f, screenPositionY + 6, 500 / 26, 500 / 26);
            }
        }

        if (isEndBad) {

            gameSoundtrack.pause();

            timer += Gdx.graphics.getDeltaTime();

            Texture currentFrame = badEndAnimation.getKeyFrame(timer, true);

            batch.draw(currentFrame, screenPositionX, screenPositionY, screenWidth, screenHeight);

            if (currentFrame.equals(badEndFrames[0])) {
                endSound.play();
            }

            font12.draw(batch, "KKKKKKKKKKKKKKKKKKKKKKKKKKKKK", screenPositionX + 12, screenPositionY + 92);
        } else {
            gameSoundtrack.play();
        }

    }

}
