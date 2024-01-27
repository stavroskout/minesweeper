import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/*This class is responsible for creating the actual minesweeper
 * game. The constructor creates the game based on the
 * parameters that have been loaded from the user
 */

public class Grid{
    private  int X_TILES;
    private  int Y_TILES;
    final int W = 400;
    final int H = 400;
    private double TILE_SIZE;
    private int bombs;
    private int clickCounter;
    private boolean hasSuperMine;
    private int marked;
    private int visibles = 0; 
    Label m = new Label("Number of Marked Boxes:"+String.valueOf(marked));

    private Tile[][] grid;

    String parentDir = new File(".").getAbsoluteFile().getParent();
    File file = new File(parentDir, "mines.txt");


    public Grid(int level, int bombs, boolean hasSuperMine, int marked) {
          if (level == 1){
            this.X_TILES = 9;
            this.Y_TILES = 9;}
        else if (level==2){
            this.X_TILES = 16;
            this.Y_TILES = 16;
        }
        this.clickCounter=0;
        //this.termed=false;
        this.hasSuperMine = hasSuperMine;
        this.grid = new Tile[this.X_TILES][this.Y_TILES];
        this.TILE_SIZE = W/X_TILES;
        this.bombs=bombs;
        this.marked=marked;

    }
    
    VBox createGrid(){
        Pane root = new Pane();
        root.setPrefSize(W, H);

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = new Tile(x, y, false);

                grid[x][y] = tile;
                root.getChildren().add(tile);
            }
        }
        //put  the desired number of bombs in the grid
        for (int i=0; i <bombs; i++){
            int rx = (int) Math.floor(Math.random() * (X_TILES));
            int ry = (int) Math.floor(Math.random() * (Y_TILES));
            if(grid[rx][ry].value.getText()=="X" || grid[rx][ry].value.getText()=="S" ) i--;
            else {
                Tile tile = grid[rx][ry];
                tile.bomb = true; 
                tile.value.setText("X");
                //mexri edw
                //add 1 to all the neighbor tiles of the bomb
                if(withinrange(rx-1, X_TILES)&&withinrange(ry-1, Y_TILES) && !grid[rx-1][ry-1].bomb){grid[rx-1][ry-1].value.setText(String.valueOf(Integer.parseInt(grid[rx-1][ry-1].value.getText())+1));}
                if(withinrange(rx, X_TILES)&&withinrange(ry-1, Y_TILES) && !grid[rx][ry-1].bomb){grid[rx][ry-1].value.setText(String.valueOf(Integer.parseInt(grid[rx][ry-1].value.getText())+1));}
                if(withinrange(rx+1, X_TILES)&&withinrange(ry-1, Y_TILES) && !grid[rx+1][ry-1].bomb){grid[rx+1][ry-1].value.setText(String.valueOf(Integer.parseInt(grid[rx+1][ry-1].value.getText())+1));}
                if(withinrange(rx-1, X_TILES)&&withinrange(ry, Y_TILES) && !grid[rx-1][ry].bomb){grid[rx-1][ry].value.setText(String.valueOf(Integer.parseInt(grid[rx-1][ry].value.getText())+1));}
                if(withinrange(rx+1, X_TILES)&&withinrange(ry, Y_TILES) && !grid[rx+1][ry].bomb){grid[rx+1][ry].value.setText(String.valueOf(Integer.parseInt(grid[rx+1][ry].value.getText())+1));}
                if(withinrange(rx-1, X_TILES)&&withinrange(ry+1, Y_TILES) && !grid[rx-1][ry+1].bomb){grid[rx-1][ry+1].value.setText(String.valueOf(Integer.parseInt(grid[rx-1][ry+1].value.getText())+1));}
                if(withinrange(rx, X_TILES)&&withinrange(ry+1, Y_TILES) && !grid[rx][ry+1].bomb){grid[rx][ry+1].value.setText(String.valueOf(Integer.parseInt(grid[rx][ry+1].value.getText())+1));}
                if(withinrange(rx+1, X_TILES)&&withinrange(ry+1, Y_TILES) && !grid[rx+1][ry+1].bomb){grid[rx+1][ry+1].value.setText(String.valueOf(Integer.parseInt(grid[rx+1][ry+1].value.getText())+1));}
            }
            if (i==0 && hasSuperMine) {grid[rx][ry].value.setText("S"); grid[rx][ry].bomb=true;}

        }
        minestxt();
        m.setFont(new Font(25));
        VBox vbox = new VBox(m, root);
        return vbox;
    }

    private class Tile extends StackPane{
        private int x, y;
        private boolean bomb;
        //private boolean supermine=false;
        private boolean isOpen = false;

        private Rectangle piece = new Rectangle(TILE_SIZE-2, TILE_SIZE-2);
        private Text value = new Text();

        public Tile(int x, int y, boolean bomb){
            this.x=x;
            this.y=y;
            this.bomb=bomb;

            piece.setStroke(Color.WHITE);
            piece.setFill(Color.BLACK);

            value.setText(bomb ? "X" : "0");
            value.setFill(Color.BLACK);
            value.setFont(Font.font(25));

            value.setVisible(false);

            getChildren().addAll(piece, value);

            setTranslateY(y*TILE_SIZE);
            setTranslateX(x*TILE_SIZE);

            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    open(true, true);
                    //clickCounter++;
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (value.getText()=="S" && clickCounter<5){
                            value.setFill(Color.BLUE);
                            //An anoiksei egkyra supermine meiwse ton energo arithmo vomvwn
                            //bombs--;
                            //marked--;
                            for(int i=0; i<16; i++){
                                grid[x][i].open(false, false);
                                grid[i][y].open(false, false);
                            }
                        } 
                    if (piece.getFill() == Color.BLACK && marked<bombs || (value.getText()=="S" && clickCounter>4)) {
                        piece.setFill(Color.GREEN);
                        marked++;
                        m.setText("Number of Marked Boxes:" + marked);
                    }
                    else if (piece.getFill() == Color.GREEN) {
                        piece.setFill(Color.BLACK);
                        marked--;
                        m.setText("Number of Marked Boxes:" + marked);
                    }
                
            }
            });

        }
        public void open(boolean recursively, boolean click){
            if(!isOpen){
                isOpen = true;
                //if marked, unmarked it
                if(piece.getFill() == Color.GREEN) {marked--; m.setText("Number of Marked Boxes:" + marked);}
                //If not mine or sumermine, make color red
                if(value.getText()!="X" && value.getText()!="S") {value.setFill(Color.RED);} 
                //if 0, make it empty
                if(value.getText()=="0")value.setText("");
                piece.setFill(Color.WHITE);//white background
                //an anoigei bomb mesw supermine xrwmatise tin me prasino
                if(!recursively && value.getText()=="X") value.setFill(Color.BLUE);
                //telos proergasias prin anoiksoume to box

                //Open box
                value.setVisible(true);
                //an anoikse mesw click auksise to clickcounter
                if(click)clickCounter++;
                visibles++;
                //if only bombs have left stop timer and WIN
                if(Y_TILES*X_TILES - bombs ==visibles){MyTimer.stopTimer(false);}
                //anadromiki emfanisi tetragwnwn xwris vomves
                if(value.getText()=="" && recursively){
                    if(withinrange(x-1, X_TILES)&&withinrange(y-1, Y_TILES)) grid[x-1][y-1].open(true, false);
                    if(withinrange(x, X_TILES)&&withinrange(y-1, Y_TILES)) grid[x][y-1].open(true, false);
                    if(withinrange(x+1, X_TILES)&&withinrange(y-1, Y_TILES)) grid[x+1][y-1].open(true, false);
                    if(withinrange(x-1, X_TILES)&&withinrange(y, Y_TILES)) grid[x-1][y].open(true, false);
                    if(withinrange(x+1, X_TILES)&&withinrange(y, Y_TILES)) grid[x+1][y].open(true, false);
                    if(withinrange(x-1, X_TILES)&&withinrange(y+1, Y_TILES)) grid[x-1][y+1].open(true, false);
                    if(withinrange(x, X_TILES)&&withinrange(y+1, Y_TILES)) grid[x][y+1].open(true, false);
                    if(withinrange(x+1, X_TILES)&&withinrange(y+1, Y_TILES)) grid[x+1][y+1].open(true, false);
                }
                if(value.getText()=="X"){
                    if(!recursively){bombs--;}// marked++; m.setText("Number of Marked Boxes:" + marked);}
                    //termed=true;
                    else App.appterm(); 
                }
            }
        }
    }
    void minestxt(){
        try{
            FileWriter writer = new FileWriter(file);
            //writer.write( level + "\n");
            for (int i = 0; i < X_TILES; i++) {
                for (int j = 0; j < Y_TILES; j++) {
                    if(grid[i][j].value.getText()=="X")writer.write(j+","+i+","+"0\n");
                    if(grid[i][j].value.getText()=="S")writer.write(j+","+i+","+"1\n");
                }
            }
            writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
    }
    void term(){
        MyTimer.stopTimer(true); 
        for (int i = 0; i < X_TILES; i++) {
            for (int j = 0; j < Y_TILES; j++) {
                grid[i][j].isOpen=true;
                if(grid[i][j].value.getText()!="X" && grid[i][j].value.getText()!="S" )grid[i][j].value.setFill(Color.RED);
                if(grid[i][j].value.getText()=="0")grid[i][j].value.setText("");
                grid[i][j].value.setVisible(true);
                grid[i][j].piece.setFill(Color.WHITE);
                //visibles++;
        }
    }}
    
public boolean withinrange(int c, int h){
    if (c>=0 && c<h)return true; else return false;
}

}