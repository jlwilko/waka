package ghost;

import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class Ignorant extends Ghost{
    /**
     * Creates an instance of the Ignorant Ghost
     * @param x the x position of the ghost in tile coordinates
     * @param y the y position of the ghost in tile coordinates 
     * @param sprite the current sprite of the ghost
     * @param speed the number of pixels the ghost moves per frame
     * @param game reference to the game this ghost is a part of 
     * @param modeLengths the lengths of SCATTER and CHASE mode for this ghost in frames
     * @param frightenedLength the length of FRIGHTENED and SODA mode for this ghost in frames 
     * @param sprites a map of all possible ghost sprites
     */
    public Ignorant(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, long frightenedLength, Map<String, PImage> sprites){
        super(x,y,sprite,speed,game,modeLengths,frightenedLength, sprites);
        this.sodaSprite = sprites.get("isoda");
    }
    
    /**
     * sets the target appropriate to the algorithm for the Ignorant ghost. <p>
     * Is the ghost is more than 8 units (straight-line distance) from Waka, then Ignorant acts 
     * as a chaser. If Ignorant is closer than or equal to 8 units away, Ignorant will try behave
     * as in scatter mode <p>
     * Scatter Target = bottom left corner
     */
    public void setTarget(){
        if (this.mode == GhostMode.CHASE || this.mode == GhostMode.SODA){
            double dist = this.calculateDistanceToTarget(this.x, this.y, this.game.getPlayerX(), this.game.getPlayerY());
            if (dist > 8){
                this.targetX = this.game.getPlayerX();
                this.targetY = this.game.getPlayerY();
            } else {
                this.targetX = Corner.BOTTOMLEFT.x;
                this.targetY = Corner.BOTTOMLEFT.y;
            }
            LimitRange.limit(this.targetX, 0, 27);
            LimitRange.limit(this.targetY, 0, 35);

        } else {
            this.targetX = Corner.BOTTOMLEFT.x;
            this.targetY = Corner.BOTTOMLEFT.y;
        }
    }
}