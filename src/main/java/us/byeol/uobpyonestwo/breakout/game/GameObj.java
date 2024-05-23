package us.byeol.uobpyonestwo.breakout.game;

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import us.byeol.uobpyonestwo.breakout.game.enums.Direction;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GameObj {

    private boolean visible = true;
    private final int width;
    private final int height;
    private Paint colour;
    private final int value;
    private int health;

    private int topX;
    private int topY;
    private int dirX = 1;
    private int dirY = 1;

    /**
     * Creates a new game objective.
     *
     * @param topX the X position.
     * @param topY the Y position.
     * @param width the width.
     * @param height the height.
     * @param colour the colour.
     */
    public GameObj(int topX, int topY, int width, int height, Paint colour) {
        this(topX, topY, width, height, colour, -1);
    }

    /**
     * Creates a new game objective.
     *
     * @param topX the X position.
     * @param topY the Y position.
     * @param width the width.
     * @param height the height.
     * @param colour the colour.
     * @param value the value if hit.
     */
    public GameObj(int topX, int topY, int width, int height, Paint colour, int value) {
        this(topX, topY, width, height, colour, value, 1);
    }

    /**
     * Creates a new game objective.
     *
     * @param topX the X position.
     * @param topY the Y position.
     * @param width the width.
     * @param height the height.
     * @param colour the colour.
     * @param value the value if hit.
     * @param health the health of this objective.
     */
    public GameObj(int topX, int topY, int width, int height, Paint colour, int value, int health) {
        this.topX = topX;
        this.topY = topY;
        this.width = width;
        this.height = height;
        this.colour = colour;
        this.value = value;
        this.health = health;
    }

    /**
     * Gets the X position of this objective.
     *
     * @return the X position.
     */
    public int getX() {
        return this.topX;
    }

    /**
     * Gets the Y position of this objective.
     *
     * @return the Y position.
     */
    public int getY() {
        return this.topY;
    }

    /**
     * Gets the width of this objective.
     *
     * @return the width.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Gets the height of this objective.
     *
     * @return the height.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Gets the colour of this objective.
     *
     * @return the colour.
     */
    public Paint getPaint() {
        return this.colour;
    }

    /**
     * Gets the value of this objective.
     *
     * @return the value, or -1 if there is no value.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Whether this objective is visible or not.
     *
     * @return true if visible.
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the visibility of this objective.
     *
     * @return true if the damaged objective has been hidden.
     */
    public boolean damage() {
        health--;
        if (health <= 0)
            this.visible = false;
        else if (this.colour instanceof LinearGradient gradient) {
            List<Stop> stops = new ArrayList<>();
            for (Stop stop : gradient.getStops())
                stops.add(new Stop(stop.getOffset(), stop.getColor().darker()));
            this.colour = new LinearGradient(gradient.getStartX(),
                    gradient.getStartY(),
                    gradient.getEndX(),
                    gradient.getEndY(),
                    gradient.isProportional(),
                    gradient.getCycleMethod(),
                    stops);
            return false;
        } else
            return false;
        return true;
    }

    /**
     * Moves the objective in the given direction.
     *
     * @param direction the direction to move the objective.
     * @param units the units to move the objective by.
     */
    public void move(Direction direction, int units) {
        if (direction == Direction.X)
            topX += units * dirX;
        else
            topY += units * dirY;
    }

    /**
     * Changes the direction the objective should travel.
     */
    public void changeDirection(Direction direction) {
        if (direction == Direction.X)
            dirX = -dirX;
        else
            dirY = -dirY;
    }

    /**
     * Checks if the current objective has been hit by the given objective.
     *
     * @param obj the objective to check.
     * @return true if hit, else false.
     */
    public boolean hitBy(GameObj obj) {
        return !(topX >= obj.topX + obj.width ||
                        topX + width <= obj.topX ||
                        topY >= obj.topY + obj.height ||
                        topY + height <= obj.topY);
    }

}
