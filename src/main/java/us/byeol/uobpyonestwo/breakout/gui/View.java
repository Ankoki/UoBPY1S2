package us.byeol.uobpyonestwo.breakout.gui;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
                    new Stop(0.2, Color.valueOf("23242c")),
                    new Stop(0.8, Color.valueOf("33363d")));
    private static final File DATA_FILE;

    static {
        DATA_FILE = new File(System.getenv("APPDATA"), "UoBPY1S2" + File.separator + "breakout.txt"); // TODO fix C:\Users\**\AppData\Roaming\UoBPY1S2\breakout.txt (Access is denied) error.
    }

    private final Map<String, String> data = new ConcurrentHashMap<>();
    private final int width,
                    height;

    private final Pane breakoutPane = new StackPane(),
                        menuPane = new StackPane(),
                        endgamePane = new StackPane();
    private final Scene breakoutScene = new Scene(this.breakoutPane),
                        menuScene = new Scene(this.menuPane),
                        endgameScene = new Scene(this.endgamePane);
    private final Canvas canvas,
                         gameCanvas;
    private final Text scoreText = new Text();
    private int highScore;

    private Controller controller;
    private Model model;

    public View(int width, int height) {
        Debug.trace("us.byeol.uobpyonestwo.breakout.gui.View::<constructor>");
        this.width = width;
        this.height = height;
        this.canvas = new Canvas(this.width, this.height);
        this.gameCanvas = new Canvas(this.width, this.height);
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
     * Creates a new label.
     *
     * @param text the text to use.
     * @param size the size of the text.
     * @param bold whether it should be bold or not
     * @return the new label.
     */
    public Label createLabel(String text, int size, boolean bold) {
        Label label = new Label(text);
        label.setStyle("-fx-font: " + size + " monospaced;" + (bold ? "-fx-font-weight: bold;" : ""));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Color.WHITE);
        StackPane.setAlignment(label, Pos.TOP_CENTER);
        return label;
    }

    /**
     * Creates a new button.
     *
     * @param text the text.
     * @param size the size of the text.
     * @param onClick what to do when the button is clicked.
     * @return the button.
     */
    public Button createButton(String text, int size, Runnable onClick) {
        Button button = new Button(text);
        button.setStyle("-fx-font: " + size + " monospaced; -fx-border-radius: 15px 50px 15px 50px; -fx-background-color: #6d6875;");
        button.setTextFill(Color.WHITE);
        button.setMinSize(150, 50);
        button.setOnMouseClicked(event -> onClick.run());
        StackPane.setAlignment(button, Pos.TOP_LEFT);
        return button;
    }
    
    /**
     * Shows the menu screen.
     */
    public void showMenu() {
        this.wipeCanvas();
        this.menuPane.setId("Menu");
        this.menuPane.getChildren().add(canvas);
        Label title = this.createLabel("[ = BREAKOUT = ]", 100, true);
        title.setTranslateY(50);
        Label singlePlayer = this.createLabel("Single-Player", 40, false);
        singlePlayer.setTranslateY(200);
        Button singlePlayerEasy = this.createButton("Easy", 20, () -> this.showGame(GameOptions.EASY));
        singlePlayerEasy.setTranslateY(300);
        singlePlayerEasy.setTranslateX((this.width / 3D) + 10);
        Button singlePlayerHard = this.createButton("Hard", 20, () -> this.showGame(GameOptions.HARD));
        singlePlayerHard.setTranslateY(300);
        singlePlayerHard.setTranslateX(((this.width / 4D) * 2) + 10);
        Label twoPlayer = this.createLabel("Two-Player", 40, false);
        twoPlayer.setTranslateY(400);
        Button twoPlayerEasy = this.createButton("Easy", 20, () -> this.showGame(GameOptions.TWO_PLAYER_EASY));
        twoPlayerEasy.setTranslateY(500);
        twoPlayerEasy.setTranslateX((this.width / 3D) + 10);
        Button twoPlayerHard = this.createButton("Hard", 20, () -> this.showGame(GameOptions.TWO_PLAYER_HARD));
        twoPlayerHard.setTranslateY(500);
        twoPlayerHard.setTranslateX(((this.width / 4D) * 2) + 10);
        Label highScore = this.createLabel("High Score\n" + this.highScore, 30, true);
        highScore.setTranslateY(600);
        this.menuPane.getChildren().addAll(title,
                                           singlePlayer,
                                           singlePlayerEasy,
                                           singlePlayerHard,
                                           twoPlayer,
                                           twoPlayerEasy,
                                           twoPlayerHard,
                                           highScore
                                          );
        Main.getPrimaryStage().setScene(this.menuScene);
        Main.getPrimaryStage().show();
    }


    /**
     * Shows the game screen.
     */
    public void showGame(GameOptions options) {
        this.wipeCanvas();
        this.model.initialiseGame(options);
        this.breakoutPane.setId("Breakout");
        this.breakoutPane.getChildren().add(gameCanvas);
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
     * @param options the options of the game that was played. This is so when you press 'play again' the same game mode will play.
     */
    public void showEndgame(int score, GameOptions options) {
        boolean changed = false;
        if (score > this.highScore) {
            this.setHighScore(highScore);
            changed = true;
        }
        this.wipeCanvas();
        this.endgamePane.setId("Endgame");
        this.endgamePane.getChildren().add(canvas);
        Label title = this.createLabel("[ = GAME OVER = ]", 100, true);
        title.setTranslateY(50);
        Label firstScore = this.createLabel("Score", 40, true),
              fsValue = this.createLabel("" + score, 40, false);
        firstScore.setTranslateY(200);
        fsValue.setTranslateY(250);
        Button home = this.createButton("Home", 40, this::showMenu);
        home.setTranslateY(400);
        home.setTranslateX((this.width / 3D) + 10);
        Button replay = this.createButton("Replay", 40, () -> this.showGame(options));
        replay.setTranslateY(400);
        replay.setTranslateX(((this.width / 4D) * 2) + 10);
        Label highScore = this.createLabel((changed ? "New High Score!\n" : "High Score\n") + score, 40, changed);
        highScore.setTranslateY(500);
        this.endgamePane.getChildren().addAll(title,
                                              firstScore,
                                              fsValue,
                                              home,
                                              replay,
                                              highScore
                                             );
        Main.getPrimaryStage().setScene(this.endgameScene);
        Main.getPrimaryStage().show();
    }

    /**
     * Wipes the canvas, setting it back to the original background gradient.
     */
    public void wipeCanvas() {
        GraphicsContext context = this.canvas.getGraphicsContext2D();
        context.setFill(View.BACKGROUND_GRADIENT);
        context.fillRect(0, 0, this.width, this.height);
        context = this.gameCanvas.getGraphicsContext2D();
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
            GraphicsContext gc = gameCanvas.getGraphicsContext2D();
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
            scoreText.setText("" + this.model.getScore());
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
