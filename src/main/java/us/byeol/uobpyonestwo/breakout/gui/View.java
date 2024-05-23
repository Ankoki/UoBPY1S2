package us.byeol.uobpyonestwo.breakout.gui;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import us.byeol.uobpyonestwo.breakout.Main;
import us.byeol.uobpyonestwo.breakout.game.Controller;
import us.byeol.uobpyonestwo.breakout.game.GameObj;
import us.byeol.uobpyonestwo.breakout.game.enums.GameOptions;
import us.byeol.uobpyonestwo.breakout.misc.Debug;

public class View implements EventHandler<KeyEvent> {

    private final int width;
    private final int height;

    private final Pane breakoutPane = new Pane();
    private final Pane menuPane = new Pane();
    private final Canvas canvas;
    private final Text infoText = new Text();

    private Controller controller;
    private Model model;

    public View(int width, int height) {
        Debug.trace("us.byeol.uobpyonestwo.breakout.gui.View::<constructor>");
        this.width = width;
        this.height = height;
        this.canvas = new Canvas(this.width, this.height);
        this.infoText.setId("score");
    }

    /**
     * Sets the controller for this view.
     *
     * @param controller the controller.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Sets the model for this view.
     *
     * @param model the model.
     */
    public void setModel(Model model) {
        this.model = model;
    }
    
    /**
     * Shows the menu screen.
     */
    public void showMenu() {
        this.menuPane.setId("Menu");
        this.menuPane.getChildren().add(canvas);
        Label title = new Label(": BREAKOUT :");
        Button play = new Button("Play Single-Player Easy");
        Button playHard = new Button("Play Single-Player Hard");
        Button twoPlayer = new Button("Play Two-Player Easy");
        Button twoPlayerHard = new Button("Play Two-Player Hard");
        Label highScore = new Label("High Score - 0"); // TODO
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 75));
        title.setLayoutX(this.width / 2D);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(50);
        play.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        play.setLayoutX((this.width / 2D) - 200);
        play.setTextAlignment(TextAlignment.CENTER);
        play.setTranslateY(150);
        play.setMinSize(500, 75);
        play.setOnMouseClicked(event -> this.showGame(GameOptions.EASY));
        playHard.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        playHard.setLayoutX((this.width / 2D) - 200);
        playHard.setTextAlignment(TextAlignment.CENTER);
        playHard.setTranslateY(250);
        playHard.setMinSize(500, 75);
        playHard.setOnMouseClicked(event -> this.showGame(GameOptions.HARD));
        twoPlayer.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        twoPlayer.setLayoutX((this.width / 2D) - 200);
        twoPlayer.setTextAlignment(TextAlignment.CENTER);
        twoPlayer.setTranslateY(350);
        twoPlayer.setMinSize(500, 75);
        twoPlayer.setOnMouseClicked(event -> this.showGame(GameOptions.TWO_PLAYER_EASY));
        twoPlayerHard.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        twoPlayerHard.setLayoutX((this.width / 2D) - 200);
        twoPlayerHard.setTextAlignment(TextAlignment.CENTER);
        twoPlayerHard.setTranslateY(450);
        twoPlayerHard.setMinSize(500, 75);
        twoPlayerHard.setOnMouseClicked(event -> this.showGame(GameOptions.TWO_PLAYER_HARD));
        this.menuPane.getChildren().add(title);
        this.menuPane.getChildren().add(play);
        this.menuPane.getChildren().add(playHard);
        this.menuPane.getChildren().add(twoPlayer);
        this.menuPane.getChildren().add(twoPlayerHard);
        this.menuPane.getChildren().add(highScore);
        Scene scene = new Scene(this.menuPane);
        scene.getStylesheets().add("breakout.css");
        Main.getPrimaryStage().setScene(scene);
        Main.getPrimaryStage().show();
    }


    /**
     * Shows the game screen.
     */
    public void showGame(GameOptions options) {
        this.breakoutPane.setId("Breakout");
        this.breakoutPane.getChildren().add(canvas);
        this.infoText.setText("0");
        this.infoText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        this.infoText.setLayoutX(this.width / 2D);
        this.infoText.setTranslateY(50);
        this.breakoutPane.getChildren().add(this.infoText);
        this.breakoutPane.setBackground(
                new Background(
                    new BackgroundImage(
                            new Image(Main.class.getResourceAsStream("/vines.jpg")),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.DEFAULT,
                            BackgroundSize.DEFAULT)));
        Scene scene = new Scene(this.breakoutPane);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("breakout.css");
        scene.setOnKeyPressed(this);
        Main.getPrimaryStage().setScene(scene);
        Main.getPrimaryStage().show();
        this.model.startGame(options);
    }

    /**
     * Shows the end game screen.
     * 
     * @param score the score.
     * @param secondPlayerScore the second players score. If there is no second player, this will be -1.
     */
    public void showEndgame(int score, int secondPlayerScore) {

    }

    /**
     * Handles the key interact event.
     *
     * @param event the event which occurred
     */
    public void handle(KeyEvent event) {
        controller.userKeyInteraction(event);
    }

    /**
     * Paints our panel.
     */
    public void drawPicture() {
        synchronized (model) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.WHITE); // TODO Find a way to wipe the canvas without putting a static colour. Maybe change to a gradient beige/cream background?
            gc.fillRect(0, 0, width, height);
            this.displayGameObj(gc, this.model.getBall());
            this.displayGameObj(gc, this.model.getBat());
            if (!this.model.getOptions().isSinglePlayer()) {
                this.displayGameObj(gc, this.model.getSecondBall());
                this.displayGameObj(gc, this.model.getSecondBat());
            }
            for (GameObj brick : this.model.getBricks())
                if (brick.isVisible())
                    this.displayGameObj(gc, brick);
            infoText.setText("" + this.model.getScore()); // TODO Multiplayer Scores.
        }
    }

    /**
     * Paints a game objective onto the panel.
     *
     * @param context the graphics context.
     * @param objective the objective.
     */
    public void displayGameObj(GraphicsContext context, GameObj objective) {
        context.setFill(objective.getPaint());
        context.fillRect(objective.getX(), objective.getY(), objective.getWidth(), objective.getHeight());
    }

}
