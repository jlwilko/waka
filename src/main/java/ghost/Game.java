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
    private long speed;
    private long lives;
    private long startingLives;
    private String mapfile;

    // private Player player;
    public List<Ghost> ghosts;

    public Ghost chaser;

    public Player player;
    public Board board;
    public List<Long> modeLengths;
    public long frightenedLength;

    public boolean won;
    public boolean gameOver;
    private int framesSinceDelay;

    private boolean debug;

    private PFont font;
    private String displayText;

    private PImage playerSprite;


    public Game(String filename, PApplet app){
        String contents = readFile(filename);
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(contents);
            JSONObject jsonObj = (JSONObject)obj;

            this.lives = (long)(jsonObj.get("lives"));
            this.startingLives = this.lives;
            this.speed = (long)jsonObj.get("speed");
            this.modeLengths = getArrayFromJSON(jsonObj.get("modeLengths"));
            this.mapfile = (String)jsonObj.get("map");
            this.frightenedLength = (long)jsonObj.get("frightenedLength");
            
        } catch (ParseException e){
            System.out.printf("Invalid JSON file given.\n");
        }
        

        this.won = false;
        this.gameOver = false;
        this.framesSinceDelay = 0;

        this.debug = true;
    }

    public void setup(PApplet app){
        
        this.font = app.createFont("src/main/resources/PressStart2P-Regular.ttf",30);
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
        Map<String, PImage> ghostSprites = new HashMap<>();
        String[] files = {"src/main/resources/ambusher",
                        "src/main/resources/chaser",
                        "src/main/resources/ignorant",
                        "src/main/resources/whim"};
        String[] keys = {"a", "c", "i", "w"};
        for (int i = 0; i < files.length; i++){
            ghostSprites.put(keys[i], app.loadImage(files[i] + ".png"));
            ghostSprites.put(keys[i]+"soda", null);
        }
        ghostSprites.put("f", app.loadImage("src/main/resources/frightened.png"));
        ghostSprites.put("d", null);


        this.ghosts = findGhosts(mapfile, app, ghostSprites);
        this.player = findPlayer(mapfile,app);
        this.playerSprite = app.loadImage("src/main/resources/playerRight.png");

    }

    public List<Ghost> findGhosts(String mapfile, PApplet app, Map<String, PImage> spriteMap){
        List<Ghost> ghosts = new ArrayList<Ghost>();

        File file = new File(mapfile);
        try{
            Scanner sc = new Scanner(file);
            int row = 0;
            while (sc.hasNext()){

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

    public void draw(PApplet app){
        if (this.won || this.gameOver){
            app.background(0,0,0);
            app.textFont(this.font,30);                  // STEP 3 Specify font to be used
            app.fill(255);                         // STEP 4 Specify font color 
            app.textAlign(PApplet.CENTER);
            app.text(this.displayText,app.width/2,app.height/3);   // STEP 5 Display Text 
        } else {
        this.board.draw(app);
        for (Ghost g : this.ghosts){
                if (!g.isDead()){
                    g.draw(app, debug);
                }
        }
        this.player.draw(app);
        for(int i = 0, j = 8; i < this.lives; i++, j+=32){
            app.image(this.playerSprite, j, app.height-32);
        }
        }

        // draw all ghosts
        // draw Waka
    }

    public void tick(App app){
        
        if (!this.won && !this.gameOver){
            this.player.setNextMovement(app);
            this.player.tick(app);
            if (this.detectCollision()){
                this.loseLife();
                this.restart(false);
                return;
            }
            for (Ghost g : this.ghosts){
                g.setNextMovement(app);
                g.tick(app);
            }
            
            if (this.detectCollision()){
                this.loseLife();
                this.restart(false);
                return;
            }
            this.won = this.board.tick(app);
                
            if (this.won){
                this.displayText = "YOU WIN";
            }
        } else {
            this.framesSinceDelay++;
            
            if (this.framesSinceDelay > 60*10){
                // System.out.printf("restarting game now\n");
                this.restart(true);
            }
        }
    }

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

    public void startFrightenedMode(){
        System.out.printf("make ghosts scared\n");
        for (Ghost g : ghosts){
            g.frighten();
        }
    };

    public void startSodaMode(){
        for (Ghost g : ghosts){
            g.soda();
        }
    }

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

    public void loseLife(){
        this.lives--;
        this.gameOver = this.lives < 0;
        this.displayText = "GAME OVER";
    }

    public void toggleDebug(){
        this.debug = !this.debug;
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

    public int getPlayerVelX(){
        return this.player.movement.xVel;
    }

    public int getPlayerVelY(){
        return this.player.movement.yVel;
    }

    public int getChaserX(){
        return this.chaser.x;
    }

    public int getChaserY(){
        return this.chaser.y;
    }
}