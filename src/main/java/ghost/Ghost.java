package ghost;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Ghost extends Entity{

    protected GhostMode mode;
    protected GhostMode oldMode;
    protected int targetX;
    protected int targetY;
    protected int framesSinceChange;
    protected List<Long> modeLengths;
    protected int modeIndex;
    protected PImage frightenedSprite;
    protected PImage deadSprite;
    protected PImage normalSprite;
    protected PImage sodaSprite;
    protected int frightenedFrames;
    protected long frightenedLength;
    protected boolean alive;


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

    public void restart(){
        super.restart();
        this.modeIndex = 0;
        this.framesSinceChange = 0;
        this.alive = true;
        this.unfrighten();
        this.unsoda();
        this.mode = GhostMode.SCATTER;
    }

    public void setNextMovement(App App){
        this.setTarget();
        Pair arr[] = new Pair[4];

        arr[0] = new Pair(calculateDistanceToTarget(this.x + Direction.LEFT.xVel, 
                        this.y + Direction.LEFT.yVel, targetX, targetY), Direction.LEFT);
        arr[1] = new Pair(calculateDistanceToTarget(this.x + Direction.RIGHT.xVel,
                        this.y + Direction.RIGHT.yVel, targetX, targetY),Direction.RIGHT);
        arr[2] = new Pair(calculateDistanceToTarget(this.x + Direction.DOWN.xVel, 
                        this.y + Direction.DOWN.yVel, targetX, targetY),Direction.DOWN);
        arr[3] = new Pair(calculateDistanceToTarget(this.x + Direction.UP.xVel, 
                        this.y + Direction.UP.yVel, targetX, targetY),Direction.UP);

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
        while (this.movement == arr[i].direction.opposite() || !this.canMove(arr[i].direction)){
            i++;
            if (i==4){
                this.nextMovement = this.movement.opposite();
                return;
            }
        }
        this.nextMovement = arr[i].direction;
    }

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

    public void draw(PApplet app, boolean debug){
        super.draw(app);
        if (debug){
            app.stroke(255);
            app.line(x*16+8+this.subX, y*16+8+this.subY, targetX*16+8, targetY*16+8);
        }
    }

    public abstract void setTarget();
    
    public double calculateDistanceToTarget(int x, int y, int targetX, int targetY){
        Square sqr = (dist) -> (dist*dist);
        int dx = sqr.apply(x - targetX);
        int dy = sqr.apply(y - targetY);
        return Math.sqrt(dx + dy);
    }

    public void frighten(){
        this.frightenedFrames = 0;
        this.oldMode = this.mode;
        this.mode = GhostMode.FRIGHTENED;
        this.sprite = frightenedSprite;
    }

    public void unfrighten(){
        this.mode = this.oldMode;
        this.sprite = normalSprite;
        this.frightenedFrames = 0;
    }

    public void soda(){
        this.frightenedFrames = 0;
        this.oldMode = this.mode;
        this.mode = GhostMode.SODA;
        this.sprite = sodaSprite;
    }

    public void unsoda(){
        this.mode = this.oldMode;
        this.sprite = normalSprite;
        this.frightenedFrames = 0;
    }

    public void kill(){
        this.alive = false;
        this.sprite = this.deadSprite;
    }

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