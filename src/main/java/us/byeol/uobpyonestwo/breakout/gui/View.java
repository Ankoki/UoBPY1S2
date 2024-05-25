package us.byeol.uobpyonestwo.breakout.gui;

import javafx.event.EventHandler;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class View implements EventHandler<KeyEvent> {

    private static final Paint BACKGROUND_GRADIENT = new LinearGradient(0, 0, 0.2, 1.4, true, CycleMethod.NO_CYCLE,
                    new Stop(0.1, Color.rgb(166, 123, 91)),
                    new Stop(0.9, Color.rgb(254, 216, 177)));
    private static final File DATA_FILE;

    static {
        DATA_FILE = new File(System.getenv("APPDATA"), "UoBPY1S2" + File.separator + "breakout.txt"); // TODO fix C:\Users\**\AppData\Roaming\UoBPY1S2\breakout.txt (Access is denied) error.
    }

    private final Map<String, String> data = new ConcurrentHashMap<>();
    private final int width,
                    height;

    private final Pane breakoutPane = new Pane(),
                        menuPane = new Pane(),
                        endgamePane = new Pane();
    private final Scene breakoutScene = new Scene(this.breakoutPane),
                        menuScene = new Scene(this.menuPane),
                        endgameScene = new Scene(this.endgamePane);
    private final Canvas canvas;
    private final Text scoreText = new Text();
    private int highScore;

    private Controller controller;
    private Model model;

    public View(int width, int height) {
        Debug.trace("us.byeol.uobpyonestwo.breakout.gui.View::<constructor>");
        this.width = width;
        this.height = height;
        this.canvas = new Canvas(this.width, this.height);
        this.breakoutPane.setMinSize(this.width, this.height);
        this.breakoutPane.setMaxSize(this.width, this.height);
        this.scoreText.setId("score");
        this.menuScene.getStylesheets().add("breakout.css");
        this.breakoutScene.getStylesheets().add("breakout.css");
        this.endgameScene.getStylesheets().add("breakout.css");
        this.breakoutScene.setOnKeyPressed(this);
        this.endgameScene.setOnKeyPressed(this);
        this.loadData().thenRun(() -> this.highScore = Integer.parseInt(this.data.get("high-score")));
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
        this.wipeCanvas();
        this.menuPane.setId("Menu");
        this.menuPane.getChildren().add(canvas);
        Label title = new Label(": BREAKOUT :");
        Button play = new Button("Play Single-Player Easy");
        Button playHard = new Button("Play Single-Player Hard");
        Button twoPlayer = new Button("Play Two-Player Easy");
        Button twoPlayerHard = new Button("Play Two-Player Hard");
        Label highScore = new Label("High Score - " + this.highScore);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 75));
        title.setLayoutX((this.width / 2D) - 100);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(50);
        play.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        play.setLayoutX((this.width / 2D) - 300);
        play.setTextAlignment(TextAlignment.CENTER);
        play.setTranslateY(150);
        play.setMinSize(500, 75);
        play.setOnMouseClicked(event -> this.showGame(GameOptions.EASY));
        playHard.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        playHard.setLayoutX((this.width / 2D) - 308);
        playHard.setTextAlignment(TextAlignment.CENTER);
        playHard.setTranslateY(250);
        playHard.setMinSize(500, 75);
        playHard.setOnMouseClicked(event -> this.showGame(GameOptions.HARD));
        twoPlayer.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        twoPlayer.setLayoutX((this.width / 2D) - 285);
        twoPlayer.setTextAlignment(TextAlignment.CENTER);
        twoPlayer.setTranslateY(350);
        twoPlayer.setMinSize(500, 75);
        twoPlayer.setOnMouseClicked(event -> this.showGame(GameOptions.TWO_PLAYER_EASY));
        twoPlayerHard.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        twoPlayerHard.setLayoutX((this.width / 2D) - 293);
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
        Main.getPrimaryStage().setScene(this.menuScene);
        Main.getPrimaryStage().show();
    }


    /**
     * Shows the game screen.
     */
    public void showGame(GameOptions options) {
        this.wipeCanvas();
        this.breakoutPane.setId("Breakout");
        this.breakoutPane.getChildren().add(canvas);
        this.scoreText.setText("0");
        this.scoreText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        this.scoreText.setLayoutX(this.width / 2D);
        this.scoreText.setTranslateY(50);
        this.breakoutPane.getChildren().add(this.scoreText);
        this.breakoutPane.setBackground(new Background(new BackgroundFill(View.BACKGROUND_GRADIENT, null, null)));
        Main.getPrimaryStage().setScene(this.breakoutScene);
        Main.getPrimaryStage().show();
        this.model.startGame(options);
    }

    /**
     * Shows the end game screen.
     * 
     * @param score the score.
     * @param secondPlayerScore the second players score. If there is no second player, this will be -1.
     * @param options the options of the game that was played. This is so when you press 'play again' the same game mode will play.
     */
    public void showEndgame(int score, int secondPlayerScore, GameOptions options) { // TODO pretty formatting.
        this.wipeCanvas();
        this.endgamePane.setId("Endgame");
        this.endgamePane.getChildren().add(canvas);
        Text gameOver = new Text("Game Over!");
        gameOver.setLayoutX(this.width / 2D);
        this.endgamePane.getChildren().add(gameOver);
        Text firstScore = new Text("" + score);
        firstScore.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        firstScore.setLayoutX(this.width / 2D);
        firstScore.setTranslateY(200);
        if (score > this.highScore)
            this.setHighScore(score);
        if (secondPlayerScore == -1 && !options.isSinglePlayer()) {
            firstScore.setLayoutX(this.width / 4D);
            Text secondScore = new Text("" + secondPlayerScore);
            secondScore.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
            secondScore.setLayoutX((this.width / 4D) * 3);
            secondScore.setTranslateY(400);
            this.endgamePane.getChildren().add(secondScore);
        }
        this.endgamePane.getChildren().add(firstScore);
        Button replay = new Button("Play Again");
        replay.setLayoutX((this.width / 2D) - 308);
        replay.setTextAlignment(TextAlignment.CENTER);
        replay.setTranslateY(250);
        replay.setMinSize(500, 75);
        replay.setOnMouseClicked(event -> this.showGame(options));
        this.endgamePane.getChildren().add(replay);
        Button home = new Button("Home");
        home.setFont(Font.font("Segoe UI", FontWeight.BOLD, 50));
        home.setLayoutX((this.width / 2D) - 293);
        home.setTextAlignment(TextAlignment.CENTER);
        home.setTranslateY(450);
        home.setMinSize(500, 75);
        home.setOnMouseClicked(event -> this.showMenu());
        this.endgamePane.getChildren().add(home);
        Main.getPrimaryStage().setScene(this.endgameScene);
        Main.getPrimaryStage().show();
    }

    /**
     * Wipes the canvas, setting it back to the original background radius.
     */
    public void wipeCanvas() {
        GraphicsContext context = this.canvas.getGraphicsContext2D();
        context.setFill(View.BACKGROUND_GRADIENT);
        context.fillRect(0, 0, this.width, this.height);
        Main.getPrimaryStage().setScene(null);
        this.menuPane.getChildren().clear();
        this.breakoutPane.getChildren().clear();
        this.endgamePane.getChildren().clear();
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
        synchronized (model) { // This field is effectively final. We can ignore this warning.
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(View.BACKGROUND_GRADIENT);
            gc.fillRect(0, 0, this.width, this.height);
            this.displayGameObj(gc, this.model.getBall());
            this.displayGameObj(gc, this.model.getBat());
            if (!this.model.getOptions().isSinglePlayer()) {
                this.displayGameObj(gc, this.model.getSecondBall());
                this.displayGameObj(gc, this.model.getSecondBat());
            }
            for (GameObj brick : this.model.getBricks())
                if (brick.isVisible())
                    this.displayGameObj(gc, brick);
            scoreText.setText("" + this.model.getScore()); // TODO Multiplayer Scores.
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

    /**
     * Sets a new high score.
     *
     * @param highScore the new high score.
     */
    public void setHighScore(int highScore) {
        this.highScore = highScore;
        this.data.put("high-score", "" + highScore);
        this.saveData();
    }

    /**
     * Loads the persistent data of this game async.
     *
     * @return the completable future to run things once loaded.
     */
    public CompletableFuture<Void> loadData() {
        return CompletableFuture.runAsync(() -> {
            try {
                if (!View.DATA_FILE.exists()) {
                    View.DATA_FILE.mkdirs();
                    this.saveData();
                } else {
                    Scanner scanner = new Scanner(View.DATA_FILE);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] raw = line.split(": ");
                        if (raw.length != 2)
                            continue;
                        this.data.put(raw[0], raw[1]);
                    }
                }
            } catch (IOException ex) { Debug.error(ex.getMessage()); }
        });
    }

    /**
     * Saves the data of this game async.
     *
     * @return the completable future to run things once saved.
     */
    public CompletableFuture<Void> saveData() {
        return CompletableFuture.runAsync(() -> {
            try {
                if (!View.DATA_FILE.exists()) {
                    View.DATA_FILE.createNewFile();
                    try (FileWriter writer = new FileWriter(DATA_FILE, false)) { // Auto Closes the Writer.
                        writer.write("high-score: " + this.highScore); // Write the default data.
                    }
                } else {
                    try (FileWriter writer = new FileWriter(DATA_FILE, false)) {
                        for (Entry<String, String> entry : this.data.entrySet())
                            writer.write(entry.getKey() + ": " + entry.getValue());
                    }
                }
            } catch (IOException ex) { Debug.error(ex.getMessage()); }
        });
    }

}
