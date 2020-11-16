package ghost;

import java.util.List;

import processing.core.PImage;

public class Ignorant extends Ghost{
    public Ignorant(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, PImage frightenedSprite){
        super(x,y,sprite,speed,game,modeLengths,frightenedSprite);
    }
    
    public void setTarget(){
        if (this.mode == GhostMode.CHASE){
            double dist = this.calculateDistanceToTarget(this.x, this.y, this.game.getPlayerX(), this.game.getPlayerY());
            if (dist > 8){
                this.targetX = this.game.getPlayerX();
                this.targetY = this.game.getPlayerY();
            } else {
                this.targetX = Corner.BOTTOMLEFT.x;
                this.targetY = Corner.BOTTOMLEFT.y;
            }
        } else {
            this.targetX = Corner.BOTTOMLEFT.x;
            this.targetY = Corner.BOTTOMLEFT.y;
        }
    }
}