import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.File;
import java.util.function.Consumer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
*
*This class represents a JavaFX scene used for loading a SCENARIO-ID.txt file.
*It includes a text field for entering a scenario ID, an "OK" button for confirming the input,
*and an error alert message in case the ID entered is invalid or the scenario file does not exist.
*@author Koutentakis Stavros
*
*@param TextField  Text field for entering scenario ID
*/

public class LoadScene {
    static TextField ID = new TextField();

    /**
    * Displays the JavaFX scene for loading a scenario file and returns the scenario ID entered by the user
    * to the calling method using a callback function.
    *
    * @param callback The callback function to pass the scenario ID entered by the user to the calling method
    */
    public static void display(Consumer<String> callback) {

        // Create a new stage for the load scene
        Stage loadStage = new Stage();
        loadStage.setTitle("Load Scene");

        // Create a label and text field for entering scenario ID
        Label idLabel = new Label("Enter SCENARIO-ID:");
        HBox hbox1 = new HBox(10, idLabel, ID);

        // Create an "OK" button to confirm the input
        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Get the scenario ID entered by the user
                final String scenarioId = ID.getText();

                try{
                    // Find the directory containing the scenario files
                    String parentDir = new File(".").getAbsoluteFile().getParent();
                    File file = new File(parentDir, "/medialab");
                    File[] filelist = file.listFiles();

                    // Search for the scenario file with the corresponding ID
                    for (File f : filelist){
                        if (f.getName().equals("SCENARIO-"+ scenarioId +".txt")){
                            loadStage.close();
                            callback.accept(scenarioId);
                            return;
                        } //epilekse to file k kane return
                    }
                    // If the scenario ID is invalid or the file does not exist, throw an exception
                    throw new InvalidValueException("SCENARIO-ID not found");
                }catch (InvalidValueException ex){

                    // Display an error alert message
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid input");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                    ButtonType result = alert.getResult();
                    if (result == ButtonType.OK) {
                        // Do not create the file or close the stage if pressed OK on error message
                        return;}
                            }
            }
        });

        // Create a layout for the scene and add the label, text field and button to it
        VBox layout = new VBox(hbox1, okButton);

        // Create a new scene for the layout and set its size
        Scene scene = new Scene(layout, 500, 200);

        // Set the scene of the create stage and show it
        loadStage.setScene(scene);
        loadStage.show();
    }
}

