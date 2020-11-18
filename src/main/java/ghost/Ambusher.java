package ghost;

import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class Ambusher extends Ghost{

    /**
     * Constructor for an Ambusher Ghost
     * @param x the x position of the Ghost in Tile coordinates
     * @param y the y position of the Ghost in Tile coordinates
     * @param sprite the current sprite of the Ghost
     * @param speed the number of pixels that the ghost moves per frame
     * @param game a reference to the game that this Ghost is a part of
     * @param modeLengths the number of frames of the modes of SCATTER and CHASE
     * @param frightenedLength the number of frames the ghost will be frightened for 
     * @param sprites a map containing the other sprites this ghost could have
     */
    public Ambusher(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, long frightenedLength, Map<String, PImage> sprites){
        // create a normal ghost
        super(x,y,sprite,speed,game,modeLengths,frightenedLength, sprites);
        // add the ambusher soda sprite to this ghost 
        this.sodaSprite = sprites.get("asoda");
    }
    
    /**
     * Sets the Target for an Ambusher. This ghost will target the position 4 tiles ahead
     * of Waka, or the top right corner of the screen if the ghost is in SCATTER mode <p>
     * The coordinates will always be on-screen
     */
    public void setTarget(){

        if (this.mode == GhostMode.CHASE || this.mode == GhostMode.SODA){
            // This ghost targets 4 spaces ahead of the current waka
            this.targetX = this.game.getPlayerX()+4*this.game.getPlayerVelX();
            this.targetY = this.game.getPlayerY()+4*this.game.getPlayerVelY();
            
            // Make sure the target location is within the bounds of the map
            LimitRange.limit(this.targetX, 0, 27);
            LimitRange.limit(this.targetY, 0, 35);
            
        } else {
            // If we are in SCATTER or FRIGHTENED mode, draw the target of this ghost 
            // as the top right corner 
            this.targetX = Corner.TOPRIGHT.x;
            this.targetY = Corner.TOPRIGHT.y;
        }
    }
}