package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import processing.core.PApplet;
import processing.core.PImage;

public class Board{ 
    /** The board (2D array) for the game  */
    private Tile[][] board;
    /** The game reference for the applet instance  */
    private Game game;
    /** The number of fruits in the instance */
    private int fruitNumber;

    /** Map of the TileSprites for the board tiles  */
    private Map<String,PImage> tileSprites;

    /**
     * Constructor for a board object 
     * @param filename The filename to read the map from
     * @param game A reference to the game object 
     * @param app A reference to the app for drawing and app references
     * @param spriteMap The map of sprites for the board tiles 
     */
    public Board(String filename, Game game, PApplet app, Map<String, PImage> spriteMap){
        this.board = new Tile[36][28]; 
        this.game = game;

        this.tileSprites = spriteMap;
        File file = new File(filename);
        try{
            Scanner sc = new Scanner(file);
            int row = 0;
            while (sc.hasNext()){

                String[] line = sc.nextLine().split("");
                int column = 0;

                for (String string : line) {
                    PImage sprite = spriteMap.get(string);
                    if (string.equals("9")){
                        ;
                    }
                    int id = sprite != null ? Integer.parseInt(string) : 0;
                    this.board[row][column] = new Tile(sprite, column, row, id);
                    if (this.isFruitTile(column, row)){
                        fruitNumber++;
                    }

                    column++;
                }

                row++;

            }

            sc.close();
            
        } catch (FileNotFoundException e){
            System.out.printf("Map file not found.\n");
            
        }

    }

    /**
     * Draw method for a game object
     * @param app App reference for drawing and applet methods
     */
    public void draw(PApplet app){
        for(int i = 0; i < this.board.length; i++){
            for(int j = 0; j < this.board[i].length; j++){
                this.board[i][j].draw(app);
            }
        }
    }

    /**
     * Tick method for progressing logic by a frame 
     * @param app App reference for drawing and applet methods 
     * @return Boolean value of whether the game has been won yet
     */
    public boolean tick(PApplet app){
        int x = game.getPlayerX();
        int y = game.getPlayerY();
        int subX = game.getPlayerSubX();
        int subY = game.getPlayerSubY();

        // System.out.printf("Fruit remaining: %d\n", this.fruitNumber);
        if ((subX == 0 && subY == 0) && isFruitTile(x, y)){
            if (isSuperFruitTile(x,y)){
                this.game.startFrightenedMode();
            } else if (isSodaCanTile(x, y)){
                this.game.startSodaMode();
            }
            board[y][x] = new Tile(tileSprites.get("0"), x, y, 0);
            this.fruitNumber--;
            // System.out.printf("Remaining fruit: %d", this.fruitNumber);
            
        }

        return this.fruitNumber > 0 ? false : true;
    }

    /**
     * Returns the amount of remaining fruit in the game 
     * @return The number of fruit tiles still to be collected 
     */
    public int getRemaininigFruit(){
        return this.fruitNumber;
    }

    /**
     * Returns a boolean if the current tile is a fruit or is empty 
     * @param Xcoord The x value of the tile to be checked 
     * @param Ycoord The y value of the tile to be checked 
     * @return Returns whether the tile is a fruit tile or is empty 
     */
    public boolean checkBoardTile(int Xcoord, int Ycoord){
        return (this.board[Ycoord][Xcoord].getID() == 0 || this.isFruitTile(Xcoord, Ycoord));
    }
    
    /**
     * Returns a boolean if the current tile is a fruit
     * @param Xcoord The x value of the tile to be checked 
     * @param Ycoord The y value of the tile to be checked 
     * @return Returns whether the tile is a fruit tile
     */
    public boolean isFruitTile(int Xcoord, int Ycoord){
        int tileID = this.board[Ycoord][Xcoord].getID();
        return (tileID == 7 || tileID == 8 || tileID == 9);
    }

    /**
     * Returns a boolean if the current tile is a superfruit
     * @param Xcoord The x value of the tile to be checked 
     * @param Ycoord The y value of the tile to be checked 
     * @return Returns whether the tile is a soda can
     */
    public boolean isSuperFruitTile(int Xcoord, int Ycoord){
        int tileID = this.board[Ycoord][Xcoord].getID();
        return (tileID == 8);
    }

    /**
     * Returns a boolean if the current tile is a soda can
     * @param Xcoord The x value of the tile to be checked 
     * @param Ycoord The y value of the tile to be checked 
     * @return Returns whether the tile is a soda can 
     */
    public boolean isSodaCanTile(int Xcoord, int Ycoord){
        int tileID = this.board[Ycoord][Xcoord].getID();
        return (tileID == 9);
    }

    /**
     * Restart the game in the event of the Win/Game Over 
     * @param filename The file to read the map from to reset the game object 
     */
    public void restart(String filename){
        File file = new File(filename);
        try{
            Scanner sc = new Scanner(file);
            int row = 0;
            while (sc.hasNext()){

                String[] line = sc.nextLine().split("");
                int column = 0;

                for (String string : line) {

                    PImage sprite = tileSprites.get(string);
                    // Set the id, unless the sprite is null
                    int id = sprite != null ? Integer.parseInt(string) : 0;
                    this.board[row][column] = new Tile(sprite, column, row, id);
                    // Check if the tile is counted as a fruit 
                    if (id == 7 || id == 8 || id == 9){
                        fruitNumber++;
                    }
                    column++;
                }
                row++;
            }

            sc.close();
            
        } catch (FileNotFoundException e){
            System.out.printf("Map file not found.\n");
            
        }
    }
}