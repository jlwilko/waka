package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import processing.core.PApplet;
import processing.core.PImage;

public class Board{
    private Tile[][] board;
    private Game game;
    private int fruitNumber;

    private Map<String,PImage> tileSprites;

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


    public void draw(PApplet app){
        for(int i = 0; i < this.board.length; i++){
            for(int j = 0; j < this.board[i].length; j++){
                this.board[i][j].draw(app);
            }
        }

    }

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

    public int getRemaininigFruit(){
        return this.fruitNumber;
    }

    public boolean checkBoardTile(int Xcoord, int Ycoord){
        return (this.board[Ycoord][Xcoord].getID() == 0 || this.isFruitTile(Xcoord, Ycoord));
    }
    
    public boolean isFruitTile(int Xcoord, int Ycoord){
        int tileID = this.board[Ycoord][Xcoord].getID();
        return (tileID == 7 || tileID == 8 || tileID == 9);
    }

    public boolean isSuperFruitTile(int Xcoord, int Ycoord){
        int tileID = this.board[Ycoord][Xcoord].getID();
        return (tileID == 8);
    }

    public boolean isSodaCanTile(int Xcoord, int Ycoord){
        int tileID = this.board[Ycoord][Xcoord].getID();
        return (tileID == 9);
    }

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
                    int id = sprite != null ? Integer.parseInt(string) : 0;
                    this.board[row][column] = new Tile(sprite, column, row, id);
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