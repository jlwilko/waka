package ghost;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Ghost extends Entity{

    /** current mode of the Ghost  */
    protected GhostMode mode;
    /** Old mode of the ghost */
    protected GhostMode oldMode;
    /** Target x for this ghost  */
    protected int targetX;
    /** Target y for this ghost */
    protected int targetY;
    /** Frames since the current mode started */
    protected int framesSinceChange;
    /** List of modeLengths for SCATTER and CHASE */
    protected List<Long> modeLengths;
    /** Counter for which mode we are currently in */
    protected int modeIndex;
    /** Image for the frightened sprite for the ghost  */
    protected PImage frightenedSprite;
    /** Image for the dead sprite for the ghost  */
    protected PImage deadSprite;
    /** Image for the normal sprite for the ghost  */
    protected PImage normalSprite;
    /** Image for the soda sprite for the ghost  */
    protected PImage sodaSprite;
    /** counter for the number of frames passed in frightened mode */
    protected int frightenedFrames;
    /** Length of frightened mode */
    protected long frightenedLength;
    /** boolean value if the ghost is currently alive */
    protected boolean alive;

    /**
     * Constructor for the Ghost Class 
     * @param x Tile x position of the ghost 
     * @param y Tile y position of the ghost
     * @param sprite Sprite for the ghost 
     * @param speed Pixels moved per frame 
     * @param game Reference to the game object
     * @param modeLengths The lengths of the SCATTER and CHASE modes
     * @param frightenedLength The length in seconds of the FRIGHTENED mode
     * @param sprites the map of sprites for the ghost to utilise 
     */
    public Ghost(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, long frightenedLength, Map<String, PImage> sprites){
        super(x, y, sprite, speed, game);
        this.xOff = -6;
        this.mode = GhostMode.SCATTER;
        this.targetX = 1;
        this.targetY = 1;
        this.framesSinceChange = 0;
        this.modeLengths = modeLengths;
        this.frightenedLength = frightenedLength;
        this.frightenedSprite = sprites.get("f");
        this.normalSprite = sprite;
        this.deadSprite = sprites.get("d");
        this.frightenedFrames = 0;

        this.alive = true;
    }

    /**
     * Restarts the board upon the win/game over screen being shown 
     */
    public void restart(){
        super.restart();
        this.modeIndex = 0;
        this.framesSinceChange = 0;
        this.alive = true;
        // Need to reset both modes to make sure the object has been completely reset
        this.unfrighten();
        this.unsoda();
        this.mode = GhostMode.SCATTER;
    }

    /**
     * Sets the next movement of the Ghost based on the current target location 
     */
    public void setNextMovement(App App){
        this.setTarget();
        Pair arr[] = new Pair[4];

        // calculate a pair for the distance and direction in eeach of the four directions 
        arr[0] = new Pair(calculateDistanceToTarget(this.x + Direction.LEFT.xVel, 
                        this.y + Direction.LEFT.yVel, targetX, targetY), Direction.LEFT);
        arr[1] = new Pair(calculateDistanceToTarget(this.x + Direction.RIGHT.xVel,
                        this.y + Direction.RIGHT.yVel, targetX, targetY),Direction.RIGHT);
        arr[2] = new Pair(calculateDistanceToTarget(this.x + Direction.DOWN.xVel, 
                        this.y + Direction.DOWN.yVel, targetX, targetY),Direction.DOWN);
        arr[3] = new Pair(calculateDistanceToTarget(this.x + Direction.UP.xVel, 
                        this.y + Direction.UP.yVel, targetX, targetY),Direction.UP);

        // Sort the array by distance 
        Arrays.sort(arr);

        int i = 0;
        if (this.mode == GhostMode.FRIGHTENED){
            Random gen = new Random();
            int rand = gen.nextInt(4);
            Direction dir = arr[rand].direction;
            while(dir == this.movement.opposite() || !this.canMove(dir)){
                rand = gen.nextInt(4);
                dir = arr[rand].direction;
            }
            this.nextMovement = dir;
            return;
        }

        // Loop through the array until we find a value that is not opposite the current direction 
        // or not walk into a wall
        while (this.movement == arr[i].direction.opposite() || !this.canMove(arr[i].direction)){
            i++;
            if (i==4){
                this.nextMovement = this.movement.opposite();
                return;
            }
        }
        // update the movement direction for the ghost
        this.nextMovement = arr[i].direction;
    }

    /**
     * Progresses the logic for one frame 
     */
    public boolean tick(App app){
        boolean res = super.tick(app);
        this.framesSinceChange++;
        // System.out.printf("frames = %d, swapping at %d\n", this.framesSinceChange, (long)app.frameRate*game.modeLengths.get(modeIndex));
        // System.out.printf("Mode = %s", this.mode);
        
        if (this.mode == GhostMode.FRIGHTENED || this.mode == GhostMode.SODA){
            this.frightenedFrames++;
            if (this.frightenedFrames > this.frightenedLength*60){
                this.unfrighten();
                this.unsoda();
            }
        }

        long currentModeLength = (long) 60*game.modeLengths.get(modeIndex);
        if (this.framesSinceChange >= currentModeLength){
            this.framesSinceChange %= currentModeLength;
            this.modeIndex++;
            this.modeIndex %= this.modeLengths.size();
            this.mode = this.mode.change();
        }
        return res;

    }

    /**
     * Draws the Ghost sprite to the screen of the applet 
     * @param app The reference to the applet for the drawing methods 
     * @param debug Boolean value for whether to show the debug lines 
     */
    public void draw(PApplet app, boolean debug){
        super.draw(app);
        if (debug){
            app.stroke(255);
            app.line(x*16+8+this.subX, y*16+8+this.subY, targetX*16+8, targetY*16+8);
        }
    }

    public abstract void setTarget();
    
    /**
     * Calculates the distance from the current position to the given target position
     * @param x The x location of the current entity 
     * @param y The y location of the current entity 
     * @param targetX The x location of the target Entity 
     * @param targetY The y location of the target Entity
     * @return A double of the straight line distance between the two entities
     */
    public double calculateDistanceToTarget(int x, int y, int targetX, int targetY){
        Square sqr = (dist) -> (dist*dist);
        int dx = sqr.apply(x - targetX);
        int dy = sqr.apply(y - targetY);
        return Math.sqrt(dx + dy);
    }

    /**
     * Sets the mode to the frightened and begins the frightened counter
     */
    public void frighten(){
        this.frightenedFrames = 0;
        this.oldMode = this.mode;
        this.mode = GhostMode.FRIGHTENED;
        this.sprite = frightenedSprite;
    }

    /**
     * Sets the mode back to the old mode, resets the frightened counter
     */
    public void unfrighten(){
        this.mode = this.oldMode;
        this.sprite = normalSprite;
        this.frightenedFrames = 0;
    }

    /**
     * Sets the mode to the soda mode and begins the soda counter
     */
    public void soda(){
        this.frightenedFrames = 0;
        if (this.mode == GhostMode.CHASE || this.mode == GhostMode.SCATTER){
            this.oldMode = this.mode;
        }
            this.mode = GhostMode.SODA;
            this.sprite = sodaSprite;
    }

    /**
     * Sets the mode back to the old mode, resets the soda counter
     */
    public void unsoda(){
        if (this.mode == GhostMode.CHASE || this.mode == GhostMode.SCATTER){
            this.mode = this.oldMode;
        }
        this.sprite = normalSprite;
        this.frightenedFrames = 0;
    }

    /**
     * Kills the entity, setting the sprite to nothing 
     */
    public void kill(){
        this.alive = false;
        this.sprite = this.deadSprite;
    }

    /**
     * Returns whether the current entity is currently dead 
     * @return Boolean value of the dead status of the entity 
     */
    public boolean isDead(){
        return !this.alive;
    }

}
interface Square{
    int apply(int x);
}

class Pair implements Comparable<Pair>{
    double distance;
    Direction direction; 

    public Pair(double distance, Direction direction){
        this.distance = distance;
        this.direction = direction;
    }

    public int compareTo(Pair other){
        return (this.distance > other.distance)? 1 : -1;
    }
}

enum GhostMode{
    CHASE{
        public GhostMode change(){
            return GhostMode.SCATTER;
        }
    },
    SCATTER{
        public GhostMode change(){
            return GhostMode.CHASE;
        }
    },
    FRIGHTENED{
        public GhostMode change(){
            return GhostMode.FRIGHTENED;
        }
    },
    SODA{
        public GhostMode change(){
            return GhostMode.SODA;
        }
    };

    public abstract GhostMode change();
}

enum Corner{
    TOPLEFT(0,0),
    TOPRIGHT(27,0),
    BOTTOMLEFT(0,35),
    BOTTOMRIGHT(27,35);

    public int x;
    public int y;

    private Corner(int x, int y){
        this.x = x;
        this.y = y;
    }
}