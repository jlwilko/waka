package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Entity {

    /** X tile position */
    protected int x;
    /** Y tile position */
    protected int y;
    /** subtile X position */
    protected int subX;
    /** subtile Y position */
    protected int subY;

    /** sprite offset in x */
    protected int xOff;
    /** sprite offset in y */
    protected int yOff;

    /** Starting x tile for the Entity */
    protected int startX;
    /** Startign y tile for the Entity */
    protected int startY;

    /** Direction of the last movement of the entity */
    protected Direction lastMovement;
    /** Direction of the current movement of the entity */
    protected Direction movement;
    /** Direction of the buffered movement of the entity */
    protected Direction nextMovement;

    /** The current sprite */
    protected PImage sprite;
    /** Pixels moved per frame */
    protected long speed;

    /** Reference to the game associated with this entity */
    protected Game game;


    /**
     * Constructs a new Entity 
     * @param x the x position in tile coordinates 
     * @param y the y position in tile coordinates 
     * @param sprite the current sprite of the entity 
     * @param speed the number of pixels moved per frame by the Entity 
     * @param game a reference to the game associated with this Entity 
     */
    public Entity(int x, int y, PImage sprite, long speed, Game game){
        // Large tile position
        this.x = x;
        this.y = y;
        this.sprite = sprite;

        this.startX = x;
        this.startY = y;
        // No issues with overflow from this since speed will either be 1 or 2
        this.speed = (int) speed;

        // Position within each tile
        this.subX = 0;
        this.subY = 0;

        // Player should initially be going left
        this.lastMovement = Direction.LEFT;
        this.movement = Direction.LEFT;
        this.nextMovement = Direction.LEFT;

        // Offset for the left facing sprite
        this.xOff = -4;
        this.yOff = -5;

        this.game = game;
    }

    /**
     * Method that determines how an entity will move
     * @param app The app sketch to utilise for tick methods;
     */
    public abstract void setNextMovement(App app);


    /**
     * Draw the entitys sprite to the screen   
     * @param app The app sketch to draw to 
     */
    public void draw(PApplet app){
        if (this.sprite == null){
            return;
        }
        app.image(this.sprite, x*16+subX+xOff, y*16+subY+yOff);
    }

    /**
     * Steps forward the logic for the Entity by one frame
     * @param app The app sketch to draw to
     * @return Whether the Entity actually moved to a new Tile in the map
     */
    public boolean tick(App app){

        // if we are at a node, and may be able to make a decision
        if (this.decisionRequired()){

            // check if we are currently trying to move into a wall
            if (!this.canMove(this.movement)){
                // if so, we stop moving 
                this.lastMovement = this.movement;
                this.movement = Direction.NONE;
            } 
            // if the next movement buffered will allow us to move, set 
            // that as the new movement direction
            if (this.canMove(this.nextMovement)) {
                this.movement = this.nextMovement;

            }
        }
        return this.step(app);
    }

    /**
     * Submethod of tick(), actually moves the Entity in the board
     * @param app The app sketch to draw to 
     * @return Whether the entity moved into a new tile object 
     */
    public boolean step(App app){
        boolean moved = false;
        this.subX += this.movement.xVel * this.speed;
        int changeTile = 0;

        // If the subX position is out of bounds of the Tile, update the position to a new tile 
        if (this.subX > 16){
            changeTile = 1;
            this.subX = 0;
        } else if (this.subX < 0){
            changeTile = -1;
            this.subX = 16;
        }

        // If we moved, update the boolean
        moved |= changeTile != 0;
        // If we moved, change the overall tile position to maintain position
        this.x += changeTile;
        changeTile = 0;
        this.subY += this.movement.yVel * this.speed;

        // Repeat the above for the y direction of movement
        if (this.subY > 16){
            changeTile = 1;
            this.subY = 0;
        } else if (this.subY < 0){
            changeTile = -1;
            this.subY = 16;
        }
        moved |= changeTile != 0;
        this.y += changeTile;
        return moved;
    }

    /**
     * Returns whether the entity can make a decision about its movement direction
     * @return whether the entity can change its movement direction
     */ 
    public boolean decisionRequired(){
        return (this.subX == 0 && this.subY == 0);
    }

    /**
     * Checks if this entity can move in the given direction
     * @param move a direction in which to move 
     * @return True if the entity can move onto the tile in that direction, false if not
     */
    public boolean canMove(Direction move){
        return this.game.checkBoardTile(x + move.xVel, y + move.yVel); 
    }

    /**
     * Resets the position and attributes of this Entity back to its initial values
     */
    public void restart(){
        this.x = this.startX;
        this.y = this.startY;
        this.subX = 0;
        this.subY = 0;
        this.movement = Direction.LEFT;
        this.nextMovement = Direction.LEFT;
    }
}