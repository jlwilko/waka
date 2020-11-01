package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import processing.core.PApplet;
import processing.core.PImage;

public class Board{
    // TODO decide what this should be
    private Tile[][] board;

    private List<Ghost> ghosts;
    private Player player;

    private Map<String,PImage> tileSprites;

    public Board(String filename, PApplet app){
        this.board = new Tile[36][28]; 

        createSpriteHashMap(app);

        File file = new File(filename);
        try{
            Scanner sc = new Scanner(file);
            int row = 0;
            while (sc.hasNext()){

                String[] line = sc.nextLine().split("");
                int column = 0;

                for (String string : line) {

                    PImage sprite = tileSprites.get(string);
                    this.board[row][column] = new Tile(sprite, column, row);

                    column++;
                }

                row++;

            }

            sc.close();
            
        } catch (FileNotFoundException e){
            System.out.printf("Map file not found.\n");
            
        }

    }

    private void createSpriteHashMap(PApplet app){
        Map<String, PImage> tileSprites = new HashMap<>();
        // TODO make these strings more descriptive 
        tileSprites.put("0", null);
        tileSprites.put("1", app.loadImage("src/main/resources/horizontal.png"));
        tileSprites.put("2", app.loadImage("src/main/resources/vertical.png"));
        tileSprites.put("3", app.loadImage("src/main/resources/upLeft.png"));
        tileSprites.put("4", app.loadImage("src/main/resources/upRight.png"));
        tileSprites.put("5", app.loadImage("src/main/resources/downLeft.png"));
        tileSprites.put("6", app.loadImage("src/main/resources/downRight.png"));
        tileSprites.put("7", app.loadImage("src/main/resources/fruit.png"));
        tileSprites.put("p", null);
        tileSprites.put("g", null);
        // TODO add the other player sprites here 
        this.tileSprites = tileSprites;
        return;
    }

    public void draw(PApplet app){
        for(int i = 0; i < this.board.length; i++){
            for(int j = 0; j < this.board[i].length; j++){
                this.board[i][j].draw(app);
            }
        }
    }

}