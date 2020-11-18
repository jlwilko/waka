package ghost;

import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class Whim extends Ghost{

    public Whim(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, long frightenedLength, Map<String, PImage> sprites){
        super(x,y,sprite,speed,game,modeLengths,frightenedLength, sprites);
        this.sodaSprite = sprites.get("wsoda");
    }
    
    public void setTarget(){
        if (this.mode == GhostMode.CHASE || this.mode == GhostMode.SODA){
            int dx = this.game.getPlayerX()+2*this.game.getPlayerVelX() - this.game.getChaserX();
            int dy = this.game.getPlayerY()+2*this.game.getPlayerVelY() - this.game.getChaserY();
            
            this.targetX = this.game.getChaserX() + 2*dx;
            this.targetY = this.game.getChaserY() + 2*dy;
            LimitRange.limit(this.targetX, 0, 27);
            LimitRange.limit(this.targetY, 0, 35);
        } else {
            this.targetX = Corner.BOTTOMRIGHT.x;
            this.targetY = Corner.BOTTOMRIGHT.y;
        }
    }
}