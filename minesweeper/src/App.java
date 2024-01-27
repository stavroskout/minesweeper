import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class App extends Application {
    String sId;
    int level, timeLimit;
    static int numMines;
    static String time, winner;
    boolean supermine;
    int markedMines=0;
    Node n;
    //MyTimer t = new MyTimer();
    static Grid g;
    MenuBar menuBar = new MenuBar();
    static File rounds = new File("./medialab/Rounds.txt");


    @Override
    public void start(Stage primaryStage) {
        // Create a menu bar
        // Create Menu
        Menu appMenu = new Menu("Application");
        Menu detailsMenu = new Menu("Details");
        //Items
        MenuItem createItem = new MenuItem("Create");
        MenuItem loadItem = new MenuItem("Load");
        MenuItem startItem = new MenuItem("Start");
        MenuItem exitItem = new MenuItem("Exit");

        MenuItem roundsItem = new MenuItem("Rounds");
        MenuItem solItem = new MenuItem("Solution");

        //add items to menu
        appMenu.getItems().addAll(createItem, loadItem, startItem, exitItem);
        detailsMenu.getItems().addAll(roundsItem, solItem);

        //ACTIONS for menu items
        createItem.setOnAction(event -> {
            CreateScene.display();
        });
       loadItem.setOnAction(event -> {
        LoadScene.display((scenarioId) -> {
            sId = scenarioId;//scenarioId is set to the value the user wants
        });});
        exitItem.setOnAction(event -> {
            Platform.exit();
        });
        solItem.setOnAction(event -> {
            appterm();
        });
        startItem.setOnAction(event-> {
            //read integers from SCENARIO-ID.txt
            String parentDir = new File(".").getAbsoluteFile().getParent();
            String filePath =parentDir + "/medialab/SCENARIO-"+sId+".txt";
            try (Scanner scanner = new Scanner(new File(filePath))) {
                int[] ints = new int[4];
                int i=0;
            while ((scanner.hasNextLine())) {
                String line = scanner.nextLine().trim();
                ints[i++] = Integer.parseInt(line);
                //assing values to integers
                level = ints[0];
                numMines = ints[1];
                timeLimit = ints[2];
                supermine = ints[3] > 0;
            }}catch (IOException e) {
                e.printStackTrace();
            }
            g = new Grid(level, numMines, supermine, markedMines);
            Pane pane = g.createGrid();
            MyTimer.countdownDurationInSeconds=timeLimit;
            MyTimer.startTimer();
            Text mines_text = new Text("Number of Mines:"+String.valueOf(numMines));
            mines_text.setFont(new Font(25));
            n= pane;
            VBox vbox= new VBox(mines_text,MyTimer.timerLabel);
            disp(vbox, primaryStage, false);
            }
            );

        // Add the menu to the menu bar
        menuBar.getMenus().addAll(appMenu, detailsMenu);
        Label l = new Label("Wellcome to Medialab Minesweeper\n\nTo create a new game with the parameters of your choice\npress `Application` and `Create`, enter your parameters and\ndefine the scenario id.\nThen select `Load` from Application and enter the scenario id\nof your choice.\nTo start the game select `Start`. ");
        VBox vbox = new VBox(l);
        disp(vbox, primaryStage, true);
        
    }
    static void appterm(){
        g.term();
    }

    public void disp(Node box, Stage primStage, boolean starting){
        BorderPane root = new BorderPane();
        VBox mainbox;
        if(!starting){mainbox = new VBox(box, n);}
        else {mainbox = new VBox(box);}
        root.setTop(menuBar);
        root.setCenter(mainbox);
        //root.setBottom(n);

        // Set the scene of the primary stage to use the layout
        Scene scene = new Scene(root, 400, 550);
        primStage.setTitle("Medialab Minesweeper");
        primStage.setScene(scene);
        primStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//to do:
//@@supermine ylopoihsh me right click
//@@RoundsItem me stoixeia last 5 games
