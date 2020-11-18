package ghost;

import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class Ignorant extends Ghost{
    public Ignorant(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, long frightenedLength, Map<String, PImage> sprites){
        super(x,y,sprite,speed,game,modeLengths,frightenedLength, sprites);
        this.sodaSprite = sprites.get("isoda");
    }
    
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