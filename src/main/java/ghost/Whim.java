package ghost;

import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class Whim extends Ghost{

    /**
     * Constructor for a Whim Ghost
     * @param x the x position of the Ghost in tile coordinates
     * @param y the y position of the Ghost in tile coordinates
     * @param sprite the current sprite of the Ghost
     * @param speed the pixels moved per frame 
     * @param game reference to the game this ghost is a part of
     * @param modeLengths lengths of the modes of SCATTER and CHASE
     * @param frightenedLength length of FRIGHTENED mode
     * @param sprites map of all possible sprites for this Ghost
     */
    public Whim(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, long frightenedLength, Map<String, PImage> sprites){
        // construct a normal ghost
        super(x,y,sprite,speed,game,modeLengths,frightenedLength, sprites);
        // add the specific soda sprite for this ghost to the attributes
        this.sodaSprite = sprites.get("wsoda");
    }
    
    /**
     * Sets the target of this Ghost. Based on the current mode <p>
     * A whim looks for the last chaser added to the game, and doubles the vector from that to the 
     * Waka. This position is then set to be the target position for the Whim
     */
    public void setTarget(){
        if (this.mode == GhostMode.CHASE || this.mode == GhostMode.SODA){
            // calculate the components for the vector from waka to the chaser
            int dx = this.game.getPlayerX()+2*this.game.getPlayerVelX() - this.game.getChaserX();
            int dy = this.game.getPlayerY()+2*this.game.getPlayerVelY() - this.game.getChaserY();
            
            // double the vector from the chaser to waka and add to the position of the waka to get the 
            // target location
            this.targetX = this.game.getChaserX() + 2*dx;
            this.targetY = this.game.getChaserY() + 2*dy;

            // ensure that the target is not out of bounds of the board
            LimitRange.limit(this.targetX, 0, 27);
            LimitRange.limit(this.targetY, 0, 35);
        } else {
            // If we are in SCATTER mode, or FRIGHTENED mode, the visible target location of this 
            // ghost is the bottom right corner of the map
            this.targetX = Corner.BOTTOMRIGHT.x;
            this.targetY = Corner.BOTTOMRIGHT.y;
        }
    }
}