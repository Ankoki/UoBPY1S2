package us.byeol.uobpyonestwo.breakout.gui;

import javafx.scene.paint.*;
import javafx.application.Platform;
import us.byeol.uobpyonestwo.breakout.game.Controller;
import us.byeol.uobpyonestwo.breakout.game.enums.Direction;
import us.byeol.uobpyonestwo.breakout.game.GameObj;
import us.byeol.uobpyonestwo.breakout.game.enums.GameOptions;
import us.byeol.uobpyonestwo.breakout.game.enums.State;
import us.byeol.uobpyonestwo.breakout.misc.Debug;

import java.util.Random;

public class Model {

    private static final int B = 6;
    private static final int M = 40;

    private static final int BALL_SIZE = 30;
    private static int BRICK_WIDTH = 25; // This is not final so that we can make it easier to access hard mode.
    private static final int BRICK_HEIGHT = 50;

    private static final int BAT_MOVE = 5;
    private static final int BALL_MOVE = 3;

    private View view;
    private Controller controller;

    private GameObj ball;
    private GameObj secondBall;
    private GameObj[] bottomLayer,
                    middleLayer,
                    topLayer;
    private GameObj bat;
    private GameObj secondBat;
    private int score = 0;
    private int secondPlayerScore = 0;
    private GameOptions options;

    private State state = State.IDLE;
    private boolean fast = false;

    private final int width;
    private final int height;

    public Model(int width, int height) {
        Debug.trace("us.byeol.uobpyonestwo.breakout.gui.Model::<constructor>");
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the view for this model.
     *
     * @param view the view.
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Sets the controller for this model.
     *
     * @param controller the controller.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void startGame(GameOptions options) {
        this.initialiseGame(options);
        Thread thread = new Thread(this::runGame);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Initialises the game's fields.
     *
     * @param options the options of the game.
     */
    public void initialiseGame(GameOptions options) {
        this.options = options;
        this.score = 0;
        this.secondPlayerScore = 0;
        if (options.isEasy())
            Model.BRICK_WIDTH *= 2;
        this.ball = new GameObj(width / (options.isSinglePlayer() ? 2 : 3), height / 2, BALL_SIZE, BALL_SIZE, Color.RED);
        this.ball.setX(new Random().nextInt(0, this.width - (BALL_SIZE + 1)));
        this.bat = new GameObj(width / (options.isSinglePlayer() ? 2 : 3), height - BRICK_HEIGHT * 3 / 2, BRICK_WIDTH * 2,
                BRICK_HEIGHT / 4, Color.valueOf("e8a598"));
        if (!options.isSinglePlayer()) {
            this.secondBall = new GameObj((width / 3) * 2, height / 2, BALL_SIZE, BALL_SIZE, Color.BLUE);
            this.secondBall.changeDirection(Direction.X);
            this.secondBat = new GameObj((width / 3) * 2, height - BRICK_HEIGHT * 3 / 2, BRICK_WIDTH * 2,
                    BRICK_HEIGHT / 4, Color.valueOf("fec89a"));
        }
        int amount = width / BRICK_WIDTH;
        this.bottomLayer = new GameObj[amount];
        this.middleLayer = new GameObj[amount];
        this.topLayer = new GameObj[amount];
        for (int i = 0; i < amount; i++)
            bottomLayer[i] = new GameObj(BRICK_WIDTH * i, 200, BRICK_WIDTH, BRICK_HEIGHT,
                    new LinearGradient(0, 0, 0.2, 1.4, true, CycleMethod.NO_CYCLE,
                            new Stop(0.1, Color.valueOf("a2d2ff")),
                            new Stop(0.4, Color.valueOf("bde0fe")),
                            new Stop(0.5, Color.valueOf("a2d2ff"))),
                    50, 1);
        for (int i = 0; i < amount; i++)
            middleLayer[i] = new GameObj(BRICK_WIDTH * i, 150, BRICK_WIDTH, BRICK_HEIGHT,
                    new LinearGradient(0, 0, 0.2, 1.4, true, CycleMethod.NO_CYCLE,
                            new Stop(0.1, Color.valueOf("93ff96")),
                            new Stop(0.4, Color.valueOf("d0ffb7")),
                            new Stop(0.5, Color.valueOf("93ff96"))),
                    100, 2);
        for (int i = 0; i < amount; i++)
            topLayer[i] = new GameObj(BRICK_WIDTH * i, 100, BRICK_WIDTH, BRICK_HEIGHT,
                    new LinearGradient(0, 0, 0.2, 1.4, true, CycleMethod.NO_CYCLE,
                            new Stop(0.1, Color.valueOf("f191ac")),
                            new Stop(0.4, Color.valueOf("f4bbc9")),
                            new Stop(0.5, Color.valueOf("f191ac"))),
                    200, 3);
        if (!options.isEasy())
            Model.BRICK_WIDTH /= 2;
    }

    /**
     * Runs the game and starts the loop.
     */
    public void runGame() {
        try {
            Debug.trace("us.byeol.uobpyonestwo.breakout.gui.Model::runGame: Game starting");
            this.setState(State.RUNNING);
            while (this.getState() == State.RUNNING) {
                this.updateGame();
                this.modelChanged();
                Thread.sleep(this.isFast() ? 10 : 20); // Safe to sleep as we run this on a separate thread.
            }
            Debug.trace("us.byeol.uobpyonestwo.breakout.gui.Model::runGame: Game finished");
        } catch (Exception ex) { Debug.error("us.byeol.uobpyonestwo.breakout.gui.Model::runAsSeparateThread error: " + ex.getMessage()); }
    }

    /**
     * Updates the game.
     */
    public synchronized void updateGame() {
        ball.move(Direction.X, BALL_MOVE);
        ball.move(Direction.Y, BALL_MOVE);
        if (!this.options.isSinglePlayer()) {
            secondBall.move(Direction.X, BALL_MOVE);
            secondBall.move(Direction.Y, BALL_MOVE);
        }
        int x = ball.getX();
        int y = ball.getY();
        if (x >= width - B - BALL_SIZE)
            ball.changeDirection(Direction.X);
        if (x <= B)
            ball.changeDirection(Direction.X);
        if (y >= height - B - BALL_SIZE) {
            Platform.runLater(() -> this.view.showEndgame(this.score, -1, this.options));
            this.setState(State.FINISHED);
            return;
        }
        if (y <= M)
            ball.changeDirection(Direction.Y);

        if (!this.options.isSinglePlayer()) {
            x = secondBall.getX();
            y = secondBall.getY();
            if (x >= width - B - BALL_SIZE)
                secondBall.changeDirection(Direction.X);
            if (x <= B)
                secondBall.changeDirection(Direction.X);
            if (y >= height - B - BALL_SIZE) {
                Platform.runLater(() -> this.view.showEndgame(this.score, -1, this.options));
                this.setState(State.FINISHED);
                return;
            }
            if (y <= M)
                secondBall.changeDirection(Direction.Y);
        }

        boolean hit = false;
        for (GameObj brick : this.getBricks()) {
            if ((brick.isVisible() && brick.hitBy(ball)) || (!this.options.isSinglePlayer() && brick.hitBy(secondBall))) {
                hit = true;
                if (brick.damage()) {
                    if (brick.hitBy(ball))
                        this.increaseScore(brick.getValue());
                    else
                        this.increaseSecondPlayerScore(brick.getValue());
                }
                if (this.getBricksLeft() <= 0) {
                    this.setState(State.FINISHED);
                    Platform.runLater(() -> this.view.showEndgame(this.score, this.secondPlayerScore, this.options));
                }
            }
        }

        if (hit)
            ball.changeDirection(Direction.Y);
        if (ball.hitBy(bat))
            ball.changeDirection(Direction.Y);
        if (!this.options.isSinglePlayer()) {
            if (ball.hitBy(secondBat))
                ball.changeDirection(Direction.Y);
            if (secondBall.hitBy(bat) || secondBall.hitBy(secondBat))
                secondBall.changeDirection(Direction.Y);
        }
    }

    /**
     * Gets the amount of bricks left on the board.
     *
     * @return the amount of bricks.
     */
    public int getBricksLeft() {
        int visible = 0;
        for (GameObj objective : this.getBricks())
            visible += (objective.isVisible() ? 1 : 0);
        return visible;
    }

    /**
     * Triggers the view to update.
     */
    public synchronized void modelChanged() {
        Platform.runLater(view::drawPicture);
    }

    /**
     * Sets the state of this game.
     *
     * @param state the new state.
     */
    public synchronized void setState(State state) {
        this.state = state;
    }

    /**
     * Gets the state of the game.
     *
     * @return the state.
     */
    public synchronized State getState() {
        return this.state;
    }

    /**
     * Gets whether the game is in fast mode.
     *
     * @return true if fast.
     */
    public synchronized boolean isFast() {
        return this.fast;
    }

    /**
     * Sets the fast status of this game.
     *
     * @param fast the new state.
     */
    public synchronized void setFast(boolean fast) {
        this.fast = fast;
    }

    /**
     * Gets the bat of this game.
     *
     * @return the bat.
     */
    public synchronized GameObj getBat() {
        return this.bat;
    }

    /**
     * Gets the second bat of this game if present.
     *
     * @return the second bat.
     */
    public GameObj getSecondBat() {
        return this.secondBat;
    }

    /**
     * Gets the ball of this game.
     *
     * @return the ball.
     */
    public synchronized GameObj getBall() {
        return this.ball;
    }

    /**
     * Gets the second ball of this game if present.
     *
     * @return the second ball.
     */
    public GameObj getSecondBall() {
        return this.secondBall;
    }

    /**
     * Gets the options of this game.
     *
     * @return the options.
     */
    public GameOptions getOptions() {
        return this.options;
    }

    /**
     * Gets all the bricks in the game.
     *
     * @return the bricks.
     */
    public synchronized GameObj[] getBricks() {
        int amount = this.getBottomLayer().length + this.getMiddleLayer().length + this.getTopLayer().length;
        GameObj[] combined = new GameObj[amount];
        int layerSize = amount / 3;
        for (int i = 0; i < layerSize; i++)
            combined[i] = this.getBottomLayer()[i];
        for (int i = layerSize; i < layerSize * 2; i++)
            combined[i] = this.getMiddleLayer()[i - layerSize];
        for (int i = layerSize * 2; i < layerSize * 3; i++)
            combined[i] = this.getTopLayer()[i - layerSize * 2];
        return combined;
    }

    /**
     * Gets the bottom bricks of this game.
     *
     * @return the bricks on the bottom layer.
     */
    public synchronized GameObj[] getBottomLayer() {
        return this.bottomLayer;
    }

    /**
     * Gets the middle bricks of this game.
     *
     * @return the bricks on the middle layer.
     */
    public synchronized GameObj[] getMiddleLayer() {
        return middleLayer;
    }

    /**
     * Gets the top bricks of this game.
     *
     * @return the bricks on the top layer.
     */
    public synchronized GameObj[] getTopLayer() {
        return topLayer;
    }

    /**
     * Gets the score of this game.
     *
     * @return the score.
     */
    public synchronized int getScore() {
        return this.score;
    }

    /**
     * Gets the second player's score of this game.
     *
     * @return the second player's score.
     */
    public synchronized int getSecondScore() {
        return this.secondPlayerScore;
    }

    /**
     * Increases the score by the given amount.
     *
     * @param increase the amount.
     */
    public synchronized void increaseScore(int increase) {
        score += increase;
    }

    /**
     * Increases the score by the given amount.
     *
     * @param increase the amount.
     */
    public synchronized void increaseSecondPlayerScore(int increase) {
        secondPlayerScore += increase;
    }

    /**
     * Moves the bat in the given direction.
     *
     * @param direction the direction.
     */
    public synchronized void moveBat(int direction) {
        int dist = direction * BAT_MOVE;
        Debug.trace("us.byeol.uobpyonestwo.breakout.gui.Model::moveBat: Move bat = " + dist);
        bat.move(Direction.X, dist);
    }

    /**
     * Moves the second bat in the given direction.
     *
     * @param direction the direction.
     */
    public synchronized void moveSecondBat(int direction) {
        if (this.options.isSinglePlayer())
            return;
        int dist = direction * BAT_MOVE;
        Debug.trace("us.byeol.uobpyonestwo.breakout.gui.Model::moveBat: Move bat = " + dist);
        if (secondBat.getX() >= 0 && secondBat.getX() <= this.width)
            secondBat.move(Direction.X, dist);
    }

}   
    