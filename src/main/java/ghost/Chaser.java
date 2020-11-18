package ghost;

import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class Chaser extends Ghost{

    /**
     * Constructor for an Chaser Ghost
     * @param x the x position of the Ghost in Tile coordinates
     * @param y the y position of the Ghost in Tile coordinates
     * @param sprite the current sprite of the Ghost
     * @param speed the number of pixels that the ghost moves per frame
     * @param game a reference to the game that this Ghost is a part of
     * @param modeLengths the number of frames of the modes of SCATTER and CHASE
     * @param frightenedLength the number of frames the ghost will be frightened for 
     * @param sprites a map containing the other sprites this ghost could have
     */
    public Chaser(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, long frightenedLength, Map<String, PImage> sprites){
        super(x,y,sprite,speed,game,modeLengths,frightenedLength, sprites);
        this.sodaSprite = sprites.get("csoda");
    }
    
    /**
     * Sets the Target for a Chaser. This ghost will target the position of Waka, 
     * or the top left corner of the screen if the ghost is in SCATTER mode <p>
     * The coordinates will always be on-screen
     */
    public void setTarget(){
        if (this.mode == GhostMode.CHASE || this.mode == GhostMode.SODA){
            this.targetX = this.game.getPlayerX();
            this.targetY = this.game.getPlayerY();
        } else {
            this.targetX = Corner.TOPLEFT.x;
            this.targetY = Corner.TOPLEFT.y;
        }
    }

}