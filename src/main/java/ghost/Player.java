package ghost;

import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;

public class Player extends Entity{

    /**
     * The number of frames since the sprite has changed - for animating the eating effect
     */
    private int framesSinceChange;
    /**
     * The map of all sprites the player can have
     */
    private Map<Direction,PImage> sprites; 

    /**
     * Constructs a new Player
     * @param x the x coordinate of the player in tile coordinates
     * @param y the x coordinate of the player in tile coordinates
     * @param sprites A map of all possible sprites for the player 
     * @param speed The number of pixels that the player will move per frame
     * @param game A reference to the game that the player is a part of
     */
    public Player(int x, int y, Map<Direction,PImage> sprites, long speed, Game game){
        // create an entity
        super(x, y, sprites.get(Direction.LEFT), speed, game);
        // update the spritemap
        this.sprites = sprites; 
        // initialise the frames before changing as 0 
        this.framesSinceChange = 0;
    }

    /**
     * Returns the tile x coordinate of the player
     * @return the tile x coordinate of the player
     */
    public int getTileX(){
        return x;
    }

    /**
     * Returns the tile y coordinate of the player
     * @return the tile y coordinate of the player
     */
    public int getTileY(){
        return y;
    }

    /**
     * Updates the position and attributes of the player <p>
     * This is called 60 times per second
     * @param app The instance of the app that the player is in
     */
    public boolean tick(App app){
        // Normal entity tick method, need to store whether we moved or not for later
        boolean res = super.tick(app);
        // increment the frame counter for animating the eating 
        this.framesSinceChange++;
        
        // If we need to change sprite, change sprite 
        if (this.framesSinceChange >= 8){
            this.changeSprite();
            this.framesSinceChange = 0;
        }
        return res;

    }

    /**
     * Sets the sprite of the player to be pointing in the correct direction 
     */
    private void changeSprite(){
        if (this.sprite == this.sprites.get(Direction.NONE)){
            // IF we are currently not moving and we are showing the non moving sprite, 
            // continue the eating animation in the direction we were last moving 
            if (this.movement == Direction.NONE){
                this.sprite = this.sprites.get(this.lastMovement);
            } else {
                // If we are currently showing the closed movement, but actually moving, then show the appropriate sprite
                this.sprite = this.sprites.get(this.movement);
            }
        } else {
            // If we were not showing the closed sprite, then change to the closed sprite
            this.sprite = this.sprites.get(Direction.NONE);
        }
    }

    /**
     *  Sets the player back to the starting location <p>
     *  Utilised after player hits a ghost or the game restarts completely
     */
    public void restart(){
        super.restart();
        // Reset the direction of the player, to face left off the start 
        this.sprite = this.sprites.get(Direction.LEFT);
    }

    /**
     * Returns the direction that the player is currently moving  
     * @return A Direction datatype of the players current movement direction
     */
    public Direction getMovementDirection(){
        return this.movement;
    }

    /**
     * Sets the nextMovemement parameter of the player based on the currently pressed button <p> 
     * This input can be buffered so that the player turns at the next opportunity
     */
    public void setNextMovement(App app){
        if (app.keyPressed){
            if (app.key == 'w' || app.key == PApplet.CODED && app.keyCode == PApplet.UP){
                this.nextMovement = Direction.UP;
            } else if (app.key == 'a' || app.key == PApplet.CODED && app.keyCode == PApplet.LEFT){
                this.nextMovement = Direction.LEFT;
            } else if (app.key == 's' || app.key == PApplet.CODED && app.keyCode == PApplet.DOWN){
                this.nextMovement = Direction.DOWN;
            } else if (app.key == 'd' || app.key == PApplet.CODED && app.keyCode == PApplet.RIGHT){
                this.nextMovement = Direction.RIGHT;
            }
        }
    }
    
}
