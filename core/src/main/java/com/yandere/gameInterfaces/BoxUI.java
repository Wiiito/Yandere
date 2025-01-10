package com.yandere.gameInterfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BoxUI {
	private Texture HelpBox;
	private boolean isInDialog = false;

	private static BoxUI BoxUI;

	private BoxUI() {
		this.HelpBox = new Texture(Gdx.files.internal("interface/BoxE.jpg"));
		
		
	}

	public static void instantiate() {
		// Colocando uam variavel pra instanciar pra nao ter q passar um if todo frame
		BoxUI = new BoxUI();
	}

	public static BoxUI getBoxUI() {
		return BoxUI;
	}

	public boolean getIsInDialog() {
		return this.isInDialog;
	}

	public void showDialog() {
		this.isInDialog = true;

	}

	public void update() {
		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			this.isInDialog = false;
		}
	}

	public void render(SpriteBatch batch, float screenPositionX, float screenPositionY) {
		if (isInDialog) {
			batch.draw(HelpBox, screenPositionX, screenPositionY);
		
		}
	}
}
