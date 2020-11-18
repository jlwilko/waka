package ghost;

import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class Chaser extends Ghost{

    public Chaser(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, long frightenedLength, Map<String, PImage> sprites){
        super(x,y,sprite,speed,game,modeLengths,frightenedLength, sprites);
        this.sodaSprite = sprites.get("csoda");
    }
    
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