package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import processing.core.PApplet;

public class Game {
    private Board board;
    private long speed;
    private long lives;
    private String mapfile;

    private int fruitNumber;

    private List<Long> modeLengths;
    // private Player player;
    private List<Ghost> ghosts;
    private Player player;

    private boolean won;
    

    public Game(String filename, PApplet app){
        String contents = readFile(filename);
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(contents);
            JSONObject jsonObj = (JSONObject)obj;

            this.lives = (long)(jsonObj.get("lives"));
            this.speed = (long)jsonObj.get("speed");
            this.modeLengths = getArrayFromJSON(jsonObj.get("modeLengths"));
            this.mapfile = (String)jsonObj.get("map");
            // this.ghosts = this.map.ghosts;
            // this.player = this.map.player;
            
        } catch (ParseException e){
            System.out.printf("Invalid JSON file given.\n");
        }
        

        this.won = false;

    }

    public void setup(PApplet app){
        
        this.board = new Board(mapfile, this, app);
        this.ghosts = findGhosts(mapfile, app);
        this.player = findPlayer(mapfile,app);

    }

    public List<Ghost> findGhosts(String mapfile, PApplet app){
        List<Ghost> ghosts = new ArrayList<Ghost>();
        File file = new File(mapfile);
        try{
            Scanner sc = new Scanner(file);
            int row = 0;
            while (sc.hasNext()){

                String[] line = sc.nextLine().split("");
                int column = 0;

                for (String string : line) {
                    if (string.equals("g")){
                        ghosts.add(new Ghost(column,row,
                                   app.loadImage("src/main/resources/ghost.png"), 
                                   this.speed, this));
                    }
                    column++;
                }
                row++;
            }
            sc.close();
        } catch (FileNotFoundException e){
            System.out.printf("File not found :(\n");
        }
        return ghosts;
    }

    public Player findPlayer(String mapfile, PApplet app){
        File file = new File(mapfile);
        try{
            Scanner sc = new Scanner(file);
            int row = 0;
            while (sc.hasNext()){

                String[] line = sc.nextLine().split("");
                int column = 0;

                for (String string : line) {
                    if (string.equals("p")){
                        sc.close();
                        return new Player(column,row, 
                        app.loadImage("src/main/resources/playerClosed.png"), this.speed, this);
                    }
                    column++;
                }
                row++;
            }
            sc.close();
        } catch (FileNotFoundException e){
            System.out.printf("File not found :(\n");
        } 
        return null;

    }

    public void draw(PApplet app){
        this.board.draw(app);
        for (Ghost g : this.ghosts){
            g.draw(app);
        }
        this.player.draw(app);

        // draw all ghosts
        // draw Waka
    }

    public void tick(App app){
        if (!this.won){
        for (Ghost g : this.ghosts){
            g.setNextMovement(app);
            g.tick(app);
        }
        this.player.setNextMovement(app);
        // this.board.checkValidMove(this.player);
        this.player.tick(app);
        this.won = this.board.tick(app);
            
        } else {
            System.out.printf("You won, good work!\n");
            
        }

    }

    private List<Long> getArrayFromJSON(Object array){
        ArrayList<Long> ls = new ArrayList<Long>();
        JSONArray jsonArray = (JSONArray)array;
        if (jsonArray != null){
            for (int i = 0; i < jsonArray.size(); i++) {
                ls.add((long)jsonArray.get(i));
            }
        }
        return ls;
    }

    public boolean checkBoardTile(int Xcoord, int Ycoord){
        return this.board.checkBoardTile(Xcoord, Ycoord);
    }

    private String readFile(String filename){
        File file = new File(filename);
        StringBuilder string = new StringBuilder();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                string.append(sc.nextLine());
            }
            sc.close();
            return string.toString();
        } catch (FileNotFoundException e){
            System.out.printf("Error: File not Found");
            return "";
        }
    }

    public int getPlayerX(){
        return this.player.x;
    }

    public int getPlayerY(){
        return this.player.y;
    }

    public int getPlayerSubX(){
        return this.player.subX;
    }

    public int getPlayerSubY(){
        return this.player.subY;
    }
}