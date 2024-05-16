package us.byeol.uobpyonestwo.internal.handlers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import us.byeol.uobpyonestwo.misc.Debug;

/**
 * Class to manage and create the ATM.
 */
public class View {

    private static final int WINDOW_HEIGHT = 420;
    private static final int WINDOW_WIDTH = 500;

    private Label title;
    private TextField message;
    private TextArea reply;
    private ScrollPane scrollPane;
    private GridPane grid;
    private TilePane buttonPane;

    private Model model;
    private Controller controller;

    /**
     * Creates a new view.
     */
    public View() {
        Debug.trace("View::<constructor>");
    }


    /**
     * Starts the new ATM View with the given window.
     *
     * @param window the stage to put the view on.
     */
    public void start(Stage window) {
        Debug.trace("View::start");
        this.grid = new GridPane();
        this.grid.setId("Layout");
        this.buttonPane = new TilePane();
        this.buttonPane.setId("Buttons");
        this.title = new Label();
        this.grid.add( title, 0, 0);

        this.message = new TextField();
        this.message.setEditable(false);
        this.grid.add(message, 0, 1);

        this.reply = new TextArea();
        this.reply.setEditable(false);
        this.scrollPane = new ScrollPane();
        this.scrollPane.setContent(reply);
        this.grid.add(scrollPane, 0, 2);

        String[][] labels = {
                {"1",    "2",  "3",  "",  "Dep",  "Hst"},
                {"4",    "5",  "6",  "",  "W/D",  "W/D B"},
                {"7",    "8",  "9",  "",  "Bal",  "Fin"},
                {"CLR",  "0",  "",   "",  "Ovr",     "Ent"} };

        for (String[] row: labels)  {
            for (String label: row) {
                if (label.length() >= 1) {
                    Button button = new Button(label);
                    button.setOnAction(this::buttonClicked);
                    this.buttonPane.getChildren().add(button);
                } else
                    this.buttonPane.getChildren().add(new Text());
            }
        }
        grid.add(buttonPane,0,3);
        Scene scene = new Scene(grid, View.WINDOW_WIDTH, View.WINDOW_HEIGHT);
        scene.getStylesheets().add("atm.css");
        window.setScene(scene);
        window.show();
    }

    /**
     * Called only internally.
     * Passes the event to the correct process.
     *
     * @param event the button click event.
     */
    private void buttonClicked(ActionEvent event) {
        Button button = ((Button) event.getSource());
        if (controller != null) {
            String label = button.getText();
            Debug.trace("View::buttonClicked::label["+ label + "]");
            controller.process(label);
        }
    }

    /**
     * Updates the current view with the text from the model.
     */
    public void update() {
        if (this.model != null) {
            Debug.trace("View::update");
            this.title.setText(Model.getTitle());
            this.message.setText(this.model.getFirstDisplay());
            this.reply.setText(this.model.getSecondDisplay());
        }
    }

    /**
     * Sets the model of this view.
     *
     * @param model the new model.
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Sets the controller of this view.
     *
     * @param controller the new controller.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

}
