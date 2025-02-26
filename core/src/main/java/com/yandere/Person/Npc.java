package com.yandere.Person;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.yandere.Schedule.Schedule;
import com.yandere.Schedule.Time;
import com.yandere.gameInterfaces.AStarCallback;
import com.yandere.gameInterfaces.GameUi;
import com.yandere.gameInterfaces.ScareNpc;
import com.yandere.handlers.MapHandler;
import com.yandere.lib.AStar;
import com.yandere.lib.TimeListener;
import com.yandere.lib.TimeObserver;

public class Npc extends Person implements TimeListener, AStarCallback {
    private PriorityQueue<Schedule> schedule;
    private ArrayList<Vector2> currentPath;
    private Schedule currentSchedule;

    private boolean isScared = false;
    private float scaredTimer = 1.5f; // Responsavel pela pausa dramatica e choque pelo corpo
    private ScareNpc deadBody;
    private boolean isHid = false;
    private Vector2 closestAlarm;

    public Npc(String name, Map<String, Animation<TextureRegion>> animations, Map<String, Float> animatiosDelay,
            MapHandler map,
            PriorityQueue<Schedule> schedule) {
        super(name, animations, animatiosDelay, map);
        this.schedule = new PriorityQueue<Schedule>(schedule);

        TimeObserver.addListener(this);
    }

    public void scare() {
        closestAlarm = this.map.getClosestAlarm(this.gridPosition, super.getCurrentLayer());
        this.setSpeed(50);
        this.map.getMapGrid(getCurrentLayer() * (map.getLayerCount() - 1))
                .getSimplfiedPath(this.getGridPosition(), closestAlarm, this);

        this.scaredTimer = 0.f;
        this.isScared = true;
    }

    public boolean isScared() {
        return this.isScared;
    }

    public void kill() {
        this.deadBody = new ScareNpc(this.getGridPosition(), map, this.getCurrentLayer());
        this.setSpeed(40);
        this.snapToGrid();
    }

    public void hide() {
        this.isHid = true;
    }

    public void pathFind(ArrayList<Vector2> path) {
        this.currentPath = AStar.simplifyPath(path);
    }

    public boolean isDead() {
        if (deadBody != null) {
            return true;
        }
        return false;
    }

    @Override
    public void timeChange(Time currentTime) {
        if (!schedule.isEmpty() && schedule.peek().startingTime.compareTo(currentTime) == 0) {
            currentSchedule = schedule.poll();
        }
        if (currentSchedule != null && !isScared)
            map.getMapGrid(getCurrentLayer() * (map.getLayerCount() - 1)).getSimplfiedPath(
                    getGridPosition(),
                    currentSchedule.position, this);
    }

    @Override
    public void tileChanged() {
        super.tileChanged();
        if (this.isScared() && this.getGridPosition().idt(closestAlarm))
            GameUi.getGameUi().setIsEndBad();
    }

    @Override
    public void update(float deltaTime) {
        if (deadBody != null) { // Coloca a animação de morte aq
            if (isHid)
                return;
            deadBody.setGridPosition(this.getGridPosition());
            deadBody.update(deltaTime);
            this.handleMovement(deltaTime);
            return;
        }

        super.update(deltaTime);

        if (isScared) {
            scaredTimer += deltaTime;
        }

        if (currentPath != null && !currentPath.isEmpty()) {
            if (super.getGridPosition().idt(currentPath.get(0)))
                currentPath.remove(0);

            if (!currentPath.isEmpty() && scaredTimer > 1) {
                Vector2 directionVector = currentPath.get(0).cpy().sub(super.getGridPosition());

                if (directionVector.x > 0)
                    this.setDirection(Direction.Right);
                if (directionVector.x < 0)
                    this.setDirection(Direction.Left);
                if (directionVector.y > 0)
                    this.setDirection(Direction.Top);
                if (directionVector.y < 0)
                    this.setDirection(Direction.Bottom);

                move();
            } else { // Acabou o caminho
                if (currentSchedule.activityDirection != null && !isScared) {
                    this.setDirection(currentSchedule.activityDirection);
                    this.setState(State.valueOf(currentSchedule.animation));
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isHid)
            super.render(batch);
    }
}
