package ghost;

import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class Ambusher extends Ghost{

    public Ambusher(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, long frightenedLength, Map<String, PImage> sprites){
        super(x,y,sprite,speed,game,modeLengths,frightenedLength, sprites);
        this.sodaSprite = sprites.get("asoda");
    }
    
    public void setTarget(){
        if (this.mode == GhostMode.CHASE || this.mode == GhostMode.SODA){
            this.targetX = this.game.getPlayerX()+4*this.game.getPlayerVelX();
            this.targetY = this.game.getPlayerY()+4*this.game.getPlayerVelY();
            LimitRange.limit(this.targetX, 0, 27);
            LimitRange.limit(this.targetY, 0, 35);
            
        } else {
            this.targetX = Corner.TOPRIGHT.x;
            this.targetY = Corner.TOPRIGHT.y;
        }
    }
}