package com.yandere.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yandere.Person.Npc;
import com.yandere.Person.Person;
import com.yandere.Person.PersonHandler;
import com.yandere.Weapons.Weapon;
import com.yandere.Weapons.WeaponFactory;
import com.yandere.gameInterfaces.BoxUI;
import com.yandere.gameInterfaces.GameUi;
import com.yandere.gameInterfaces.Interactible;
import com.yandere.gameInterfaces.ScareNpc;

public class Player extends Person {
	private boolean canMove = true;
	private float timeSinceLastDirection = 0.f; // Efeito pokemon, virar ao tocar, andar ao pressionar
	private boolean isWeaponOut = false;
	private Weapon currentWeapon;
	private WeaponFactory weaponFactory;
	private ScareNpc weaponOutScareNpc;
	private Vector2 lastPosition;
	private Npc pullNpc;

	public Player(PlayerBuilder playerBuilder, MapHandler map) {
		super("player", playerBuilder.getPLayerAnimations(), playerBuilder.getPlayerAnimationsDelay(), map);
		this.setGridPosition(playerBuilder.getInitialPosition());
		this.snapToGrid();
		this.weaponFactory = new WeaponFactory();
		weaponOutScareNpc = new ScareNpc(this.getGridPosition(), map, getCurrentLayer());
	}

	public boolean isCloseToObject() {

		Interactible resultInteractible = map.interact(getGridPosition(), false);

		if (resultInteractible != null && resultInteractible.type == Interactible.Type.Weapon) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Os cliques sao usados para mudar a direção e em caso de a direção ja estar
	 * setada, move 1 quadrado. "Duplicado" como keyDown ja que é impossivel um ser
	 * humano dar 1 clique de um frame...
	 */

	public InputAdapter getInputAdapter() {
		return new InputAdapter() {
			public boolean keyDown(int keyCode) {
				if (canMove) {
					switch (keyCode) {
						case Input.Keys.SHIFT_LEFT:
							if (pullNpc == null)
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
						case Input.Keys.E:
							Interactible resultInteractible = map.interact(getGridPosition(), true);
							if (resultInteractible != null
									&& resultInteractible.type == Interactible.Type.Weapon) {
								GameUi.getGameUi().showDialog(resultInteractible.dialog);
								currentWeapon = weaponFactory.getWeapon(resultInteractible.name);
								GameUi.getGameUi().changeSwordState();
							} else if (resultInteractible != null) {
								GameUi.getGameUi().showDialog(resultInteractible.name, resultInteractible.dialog);
							}
							return true;
						case Input.Keys.C:
							isWeaponOut = !isWeaponOut;
							weaponOutScareNpc.setGridPosition(getGridPosition());
							GameUi.getGameUi().IsEquiped();
							return true;
						case Input.Keys.X:
							pullBody(getDirection());
							return true;
						case Input.Keys.SPACE:
							if (currentWeapon != null && isWeaponOut)
								hit(getDirection());
							return true;
					}
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

	public void pullBody(Direction direction) {
		if (this.pullNpc != null) {
			Interactible caixao = map.interact(this.getGridPosition(), false);
			if (caixao != null && caixao.type == Interactible.Type.Hide) {
				pullNpc.hide();
			}

			this.pullNpc = null;
			return;
		}

		Vector2 trueHitSquare = Vector2.Zero;
		switch (direction) {
			case Bottom:
				trueHitSquare = new Vector2(0, -1);
				break;
			case Right:
				trueHitSquare = new Vector2(1, 0);
				break;
			case Top:
				trueHitSquare = new Vector2(0, 1);
				break;
			case Left:
				trueHitSquare = new Vector2(-1, 0);
				break;
		}
		trueHitSquare.add(this.getGridPosition());

		for (Person personFromArray : PersonHandler.getPersons()) {
			Npc person = (Npc) personFromArray;
			if ((person.getGridPosition().idt(this.getGridPosition())
					|| person.getGridPosition().idt(trueHitSquare)) && person.isDead()) {
				this.pullNpc = person;
				break;
			}
		}
	}

	public void hit(Direction direction) {
		// Faz animação de bater (seta booleana pra true, no update se for true altera
		// animação)
		Vector2 trueHitSquare = Vector2.Zero;
		switch (direction) {
			case Bottom:
				trueHitSquare = new Vector2(0, -1);
				break;
			case Right:
				trueHitSquare = new Vector2(1, 0);
				break;
			case Top:
				trueHitSquare = new Vector2(0, 1);
				break;
			case Left:
				trueHitSquare = new Vector2(-1, 0);
				break;
		}
		trueHitSquare.add(this.getGridPosition());

		for (Person personFromArray : PersonHandler.getPersons()) {
			Npc person = (Npc) personFromArray;
			if (person.getGridPosition().idt(this.getGridPosition())
					|| person.getGridPosition().idt(trueHitSquare)) {
				person.kill();
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		timeSinceLastDirection += Gdx.graphics.getDeltaTime();

		if (pullNpc != null) {
			pullNpc.update(deltaTime);
			pullNpc.handleMovement(deltaTime);
		}

		if (this.currentWeapon != null && isWeaponOut) {
			super.currentAnimation = animations.get("Weapon" + this.getState().toString() + this.getDirection());
			this.currentWeapon.setAnimation(this.getState().toString() + this.getDirection());

			weaponOutScareNpc.setGridPosition(this.getGridPosition());
			weaponOutScareNpc.update(deltaTime);
		}

		if (isCloseToObject()) {
			BoxUI.getBoxUI().showDialog();
		}

		// Se tiver em dialogo, não se move
		this.canMove = !GameUi.getGameUi().getIsInDialog();

		if (canMove) {
			if (Gdx.input.isKeyPressed(Input.Keys.W) && timeSinceLastDirection > .1f) {
				setDirection(Direction.Top);
				move();
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D) && timeSinceLastDirection > .1f) {
				setDirection(Direction.Right);
				move();
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S) && timeSinceLastDirection > .1f) {
				setDirection(Direction.Bottom);
				move();
			}
			if (Gdx.input.isKeyPressed(Input.Keys.A) && timeSinceLastDirection > .1f) {
				setDirection(Direction.Left);
				move();
			}
		}
	}

	@Override
	public void move() {
		if (pullNpc != null) {
			pullNpc.setDesiredGrid(lastPosition);
		}
		super.move();
	}

	@Override
	public void tileChanged() {
		this.lastPosition = getGridPosition();
		super.tileChanged();
	}

	@Override
	protected void handleFloorChange(Vector2 positionOnGrid) {
		super.handleFloorChange(positionOnGrid);
		map.setCurrentFloor(super.getCurrentLayer());
		this.weaponOutScareNpc.setFloor(super.getCurrentLayer());
	}

	@Override
	public void handleMovement(float deltaTime) {
		if (map.collides(gridPosition, desiredGridPosition)) {
			this.desiredGridPosition = gridPosition.cpy();
		}
		super.handleMovement(deltaTime);
	}

	@Override
	public void render(SpriteBatch batch) {
		if (currentWeapon != null && isWeaponOut && this.getDirection() == Direction.Left) {
			batch.draw(currentWeapon.getFrame(super.elapsedTime), getPosition().x - 8, getPosition().y - 2);
		} else if (currentWeapon != null && isWeaponOut && this.getDirection() == Direction.Top) {
			batch.draw(currentWeapon.getFrame(super.elapsedTime), getPosition().x, getPosition().y - 2);
		}

		super.render(batch);

		if (currentWeapon != null && isWeaponOut
				&& (this.getDirection() == Direction.Bottom || this.getDirection() == Direction.Right)) {
			batch.draw(currentWeapon.getFrame(super.elapsedTime), getPosition().x - 8, getPosition().y - 2);
		}
	}
}
