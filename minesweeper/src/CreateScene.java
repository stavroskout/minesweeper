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
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/*This class is responsible for creating different game variations 
 * when clicking on the button 'Create' of the Menu
 */

public class CreateScene {

    public static void display() {
        Stage createStage = new Stage();
        createStage.setTitle("Create Scene");

        // Create the text fields
        TextField ID = new TextField();
        TextField Level = new TextField();
        TextField Mines = new TextField();
        TextField Supermine = new TextField();
        TextField TimeLimit = new TextField();



        Label idLabel = new Label("Enter SCENARIO-ID:");
        Label lvlLabel = new Label("Enter Level 1(easy) or 2(hard):");
        Label minesLabel = new Label("Enter number of mines:");
        Label SupermineLabel = new Label("Enter 0 for no supermine 1 for supermine:");
        Label timeLabel = new Label("Enter time limit in seconds:");

        HBox hbox1 = new HBox(10, idLabel, ID);
        HBox hbox2 = new HBox(10, lvlLabel, Level);
        HBox hbox3 = new HBox(10, minesLabel, Mines);
        HBox hbox4 = new HBox(10, SupermineLabel, Supermine);
        HBox hbox5 = new HBox(10, timeLabel, TimeLimit);


        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                //GET the params
                final String scenarioId = ID.getText();
                final int level = Integer.parseInt(Level.getText());
                final int numMines = Integer.parseInt(Mines.getText());
                final int hasSupermine = Integer.parseInt(Supermine.getText());
                final int timeLimit = Integer.parseInt(TimeLimit.getText());
                try{
                    //create file SCENARIO-ID.txt
                if (level != 2 && level != 1) {
                    throw new InvalidValueException("Invalid level: " + level);
                }
                if (level==1 && (numMines<9 || numMines>11) ) {
                    throw new InvalidValueException("Number of mines should be between 9 and 11 on easy level");
                }
                if (level==2 && (numMines<35 || numMines>45)) {
                    throw new InvalidValueException("Number of mines should be between 35 and 45 on hard level");
                }
                if ( hasSupermine!=0 && hasSupermine != 1) {
                    throw new InvalidValueException("Supermine field should be 0 for 'no' or 1 for 'yes' ");
                }
                if (level ==1 && hasSupermine == 1) {
                    throw new InvalidValueException("Cannot have supermine on easy level");
                }
                if (level==1 && (timeLimit<120 || timeLimit>180)) {
                    throw new InvalidValueException("Time limit should be between 120 and 180 on easy level");
                }
                if (level==2 && (timeLimit<240 || timeLimit>360)) {
                    throw new InvalidValueException("Time limit shuld be between 240 and 360 on hard level");
                }
                String parentDir = new File(".").getAbsoluteFile().getParent();
                File file = new File(parentDir, "medialab/SCENARIO-"+scenarioId + ".txt");
                try{
                FileWriter writer = new FileWriter(file);
                writer.write( level + "\n");
                writer.write( numMines + "\n");
                writer.write(timeLimit + "\n");
                writer.write(hasSupermine + "");
                writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                // Close the stage when the "OK" button is clicked
                createStage.close();
                }catch (InvalidValueException ex){
                    Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid input");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            // Check if the user clicked the "OK" button on the alert
        ButtonType result = alert.getResult();
        if (result == ButtonType.OK) {
            // Do not create the file or close the stage
            return;}
                }
            }
        });

        // Add the text fields to a layout
        VBox layout = new VBox(hbox1, hbox2, hbox3, hbox4, hbox5, okButton);

        // Set the layout as the root of the scene
        Scene scene = new Scene(layout, 500, 200);

        // Set the scene of the create stage and show it  
        createStage.setScene(scene);
        createStage.show();
    }
}