package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Game {
    /** Pixels moved per frame for all entities  */
    private long speed;

    /** Number of lives that Waka currently has */
    private long lives;
    /** Number of lives that Waka starts with */
    private long startingLives;
    /** Map filename to recreate the map on game restarts */
    private String mapfile;

    /** list of the ghosts in the game */
    public List<Ghost> ghosts;
    /** The last chaser added to the game, for Whim to reference target position from */
    public Ghost chaser;

    /** The Waka object for the game */
    public Player player;
    /** The board object for the game  */
    public Board board;
    /** List of the modeLengths for SCATTER and CHASE mode for the ghosts */
    public List<Long> modeLengths;
    /** Time in seconds that ghosts stay frightened for when a SuperFruit is eaten */
    public long frightenedLength;
    /** Variable containing whether the game has been won */
    public boolean won;
    /** Variable containing if Waka has run out of lives */
    public boolean gameOver;
    /** Counter of frames since the GameOver/YouWin screen has been shown */
    private int framesSinceDelay;

    /** Whether the debug mode is active or not */
    private boolean debug;

    /** The font used to write the text at the end of the game */
    private PFont font;
    /** The text to be displayed when the game is over */
    private String displayText;

    /** The current sprite for the player to be displayed */
    private PImage playerSprite;


    /**
     * The constructor for a Game 
     * @param filename The filename from which the map information should be read
     * @param app The app sketch from which to access resource loading functions
     */
    public Game(String filename, PApplet app){
        // Read the entire file into memory 
        String contents = readFile(filename);
        JSONParser parser = new JSONParser();
        try{
            // Try to parse the object and cast it to a JSONObject 
            Object obj = parser.parse(contents);
            JSONObject jsonObj = (JSONObject)obj;

            // Extract the fields from the JSONObject into the game object 
            this.lives = (long)(jsonObj.get("lives"));
            this.startingLives = this.lives;
            this.speed = (long)jsonObj.get("speed");
            this.modeLengths = getArrayFromJSON(jsonObj.get("modeLengths"));
            this.mapfile = (String)jsonObj.get("map");
            this.frightenedLength = (long)jsonObj.get("frightenedLength");
            
        } catch (ParseException e){
            System.out.printf("Invalid JSON file given.\n");
        }
        

        // Set the instance variables that are not read from the JSON file
        this.won = false;
        this.gameOver = false;
        this.framesSinceDelay = 0;

        // Initially the game starts in debug mode 
        this.debug = true;
    }

    /**
     * Load all the resources and create the player/ghost/tile objects required to play the game 
     * @param app The app sketch from which to access resource loading functions
     */
    public void setup(PApplet app){
        
        this.font = app.createFont("src/main/resources/PressStart2P-Regular.ttf",30);

        // Put all the map sprites in PImage format into a map for easy access 
        Map<String, PImage> tileSprites = new HashMap<>();
        tileSprites.put("0", null);
        tileSprites.put("1", app.loadImage("src/main/resources/horizontal.png"));
        tileSprites.put("2", app.loadImage("src/main/resources/vertical.png"));
        tileSprites.put("3", app.loadImage("src/main/resources/upLeft.png"));
        tileSprites.put("4", app.loadImage("src/main/resources/upRight.png"));
        tileSprites.put("5", app.loadImage("src/main/resources/downLeft.png"));
        tileSprites.put("6", app.loadImage("src/main/resources/downRight.png"));
        tileSprites.put("7", app.loadImage("src/main/resources/fruit.png"));
        tileSprites.put("8", app.loadImage("src/main/resources/superFruit.png"));
        tileSprites.put("9", app.loadImage("src/main/resources/canwhite.png"));
        tileSprites.put("p", null);
        tileSprites.put("g", null);
        this.board = new Board(mapfile, this, app, tileSprites);

        // Put all the ghost sprites in PImage format into a map for easy access
        Map<String, PImage> ghostSprites = new HashMap<>();
        String[] files = {"src/main/resources/ambusher",
                        "src/main/resources/chaser",
                        "src/main/resources/ignorant",
                        "src/main/resources/whim"};
        String[] keys = {"a", "c", "i", "w"};
        // Loop through and add both the normal sprite and soda mode sprite to the map 
        for (int i = 0; i < files.length; i++){
            ghostSprites.put(keys[i], app.loadImage(files[i] + ".png"));
            ghostSprites.put(keys[i]+"soda", app.loadImage((files[i] + "soda.png")));
        }
        // Add the frightened sprite and the dead sprite to the map 
        ghostSprites.put("f", app.loadImage("src/main/resources/frightened.png"));
        ghostSprites.put("d", null);


        // Add the ghosts and the player to the game object 
        this.ghosts = findGhosts(mapfile, app, ghostSprites);
        this.player = findPlayer(mapfile,app);
        this.playerSprite = app.loadImage("src/main/resources/playerRight.png");

    }

    /**
     * Loop through the map file and create the appropriate ghosts, adding them to the ghosts list
     * in the game object 
     * @param mapfile The map file from which to read the ghost information from
     * @param app The app sketch from which to access resource loading functions
     * @param spriteMap The sprite map for all ghosts to source their sprites from 
     * @return Returns a list of the ghosts for this game instance
     */
    public List<Ghost> findGhosts(String mapfile, PApplet app, Map<String, PImage> spriteMap){
        List<Ghost> ghosts = new ArrayList<Ghost>();

        File file = new File(mapfile);
        try{
            Scanner sc = new Scanner(file);
            int row = 0;
            while (sc.hasNext()){

                // Break the line into single character strings 
                String[] line = sc.nextLine().split("");
                int column = 0;

                for (String string : line) {
                    switch (string){
                        case "a":
                            ghosts.add(new Ambusher(column,row,
                                    spriteMap.get(string), 
                                    this.speed, this, this.modeLengths, this.frightenedLength, spriteMap));
                            break;
                        case "c":
                            Chaser c = new Chaser(column, row, spriteMap.get(string), this.speed, 
                                this, this.modeLengths, this.frightenedLength, spriteMap);
                            ghosts.add(c);
                            this.chaser = c;
                            break;
                        case "i":
                            ghosts.add(new Ignorant(column,row,
                                    spriteMap.get(string), 
                                    this.speed, this, this.modeLengths, this.frightenedLength, spriteMap));
                            break;
                        case "w":
                            ghosts.add(new Whim(column,row,
                                    spriteMap.get(string), 
                                    this.speed, this, this.modeLengths, this.frightenedLength, spriteMap));
                            break;
                        default:
                            break;

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

    /**
     * Locates the player in the map, and constructs the Player object and places it into 
     * the game object 
     * @param mapfile The map file from which to read the ghost information from
     * @param app The app sketch from which to access resource loading functions
     * @return Returns the player object that has been constructed in the game object 
     */
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
                        // Construct the sprite Map for the player sprites 
                        Map<Direction,PImage> map = new HashMap<Direction,PImage>();
                        map.put(Direction.DOWN, app.loadImage("src/main/resources/playerDown.png"));
                        map.put(Direction.UP, app.loadImage("src/main/resources/playerUp.png"));
                        map.put(Direction.RIGHT, app.loadImage("src/main/resources/playerRight.png"));
                        map.put(Direction.LEFT, app.loadImage("src/main/resources/playerLeft.png"));
                        map.put(Direction.NONE, app.loadImage("src/main/resources/playerClosed.png"));
                        return new Player(column,row, map, this.speed, this);
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

    /**
     * Draws the game to the applet sketch 
     * @param app The app sketch from which to access resource loading functions
     */
    public void draw(PApplet app){
        // If we need to show the gameOver or Win screen
        if (this.won || this.gameOver){
            app.background(0,0,0);
            // Specify the font and colour of text on the screen
            app.textFont(this.font,30);                  // STEP 3 Specify font to be used
            app.fill(255);                         // STEP 4 Specify font color 
            // Center align the text
            app.textAlign(PApplet.CENTER);
            app.text(this.displayText,app.width/2,app.height/3);   // STEP 5 Display Text 
        } else {
            // Otherwise, draw the game normally 
            this.board.draw(app);
            // Draw all the ghosts 
            for (Ghost g : this.ghosts){
                if (!g.isDead()){
                    g.draw(app, debug);
                }
            }
            // Draw the player
            this.player.draw(app);

            // Draw the remaining lives on the screen
            for(int i = 0, j = 8; i < this.lives; i++, j+=28){
                app.image(this.playerSprite, j, app.height-32);
            }
        }
    }

    /**
     * Calculate the logic of the game for one frame 
     * @param app The sketch to access resource methods and drawing options from
     */
    public void tick(App app){
        
        // If we havent won, or lost, then simulate the next frame 
        if (!this.won && !this.gameOver){
            this.player.setNextMovement(app);
            this.player.tick(app);
            if (this.detectCollision()){
                this.loseLife();
                // Reset positions without restarting the map
                this.restart(false);
                return;
            }
            // Tick all the ghosts 
            for (Ghost g : this.ghosts){
                g.setNextMovement(app);
                g.tick(app);
            }
            // Check again for collisions
            if (this.detectCollision()){
                this.loseLife();
                this.restart(false);
                return;
            }
            // check that the game has not ended with that tick
            this.won = this.board.tick(app);
                
            if (this.won){
                this.displayText = "YOU WIN";
            }
        } else {
            // If we are displaying a game over / win screen, count another frame 
            this.framesSinceDelay++;
            
            if (this.framesSinceDelay > 60*10){
                // System.out.printf("restarting game now\n");
                this.restart(true);
            }
        }
    }

    /**
     * Reset the positions and status of all objects in the game 
     * @param gameFinished Boolean that determines the magnitude of the reset. IF true, full reset,
     *  if false, then only reset the players and ghosts 
     */
    public void restart(boolean gameFinished){
        this.framesSinceDelay = 0;
        for (Ghost g : this.ghosts){
            g.restart();
        }
        player.restart();
        if (gameFinished){
            board.restart(this.mapfile);
            this.won = false;
            this.gameOver = false;
            this.lives = this.startingLives;
        }
    }

    /**
     * Triggered when a superfruit is eaten, making all ghosts frightened 
     */
    public void startFrightenedMode(){
        for (Ghost g : ghosts){
            g.frighten();
        }
    };

    /**
     * Triggered when a soda can is eaten, making all ghosts partially invisible
     */
    public void startSodaMode(){
        for (Ghost g : ghosts){
            g.soda();
        }
    }

    /**
     * Determines whether a ghost has collided with the player 
     * @return Returns true if the player has collided with a ghost 
     */
    public boolean detectCollision(){
        for (Ghost g : this.ghosts){
            // if (g.calculateDistanceToTarget(g.x,g.y, this.player.x, this.player.y) <= 1){
            if (g.isDead() == true){
                continue;
            }
            if((this.getPlayerX() == g.x) && (this.getPlayerY() == g.y)){
                if (g.mode == GhostMode.FRIGHTENED){
                    g.kill();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Subtract a life from the player, checking if the game is ended by that life loss 
     */
    public void loseLife(){
        this.lives--;
        this.gameOver = this.lives < 0;
        this.displayText = "GAME OVER";
    }

    /**
     * Toggles the debug mode 
     */
    public void toggleDebug(){
        this.debug = !this.debug;
    }

    /**
     * Parses the array of modelengths from the JSON object and returns it 
     * @param array The JSON array created from the parsing 
     * @return The ArrayList of longs from the modeLengths 
     */
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

    /**
     *  Checks if the board tile at the given coordinates is moveable 
     * @param Xcoord the x tile position of the tile to be checked 
     * @param Ycoord the y tile position of the tile to be checked 
     * @return
     */
    public boolean checkBoardTile(int Xcoord, int Ycoord){
        return this.board.checkBoardTile(Xcoord, Ycoord);
    }

    /**
     * Reads all information in a file, appending it to a string
     * @param filename The file to read 
     * @return Returns the full string of all text in the file 
     */
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

    /**
     * Getter for the player position
     * @return The players X tile position
     */
    public int getPlayerX(){
        return this.player.x;
    }

    /**
     * Getter for the players position
     * @return The players Y tile position
     */
    public int getPlayerY(){
        return this.player.y;
    }

    /**
     * Getter for the players sub tile position
     * @return The players subtile X position
     */
    public int getPlayerSubX(){
        return this.player.subX;
    }

    /**
     * Getter for the players sub tile position
     * @return The players subtile Y position
     */
    public int getPlayerSubY(){
        return this.player.subY;
    }

    /**
     * Getter for the players velocity 
     * @return The players X velocity 
     */
    public int getPlayerVelX(){
        return this.player.movement.xVel;
    }

    /**
     * Getter for the players velocity 
     * @return The players Y velocity 
     */
    public int getPlayerVelY(){
        return this.player.movement.yVel;
    }

    /**
     * Getter for the chasers velocity 
     * @return The chasers X velocity 
     */
    public int getChaserX(){
        return this.chaser.x;
    }

    /**
     * Getter for the chasers velocity 
     * @return The chasers Y velocity 
     */
    public int getChaserY(){
        return this.chaser.y;
    }
}