package ghost;

import java.util.List;

import processing.core.PImage;

public class Chaser extends Ghost{

    public Chaser(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, PImage frightenedSprite){
        super(x,y,sprite,speed,game,modeLengths,frightenedSprite);
    }
    
    public void setTarget(){
        if (this.mode == GhostMode.CHASE){
            this.targetX = this.game.getPlayerX();
            this.targetY = this.game.getPlayerY();
        } else {
            this.targetX = Corner.TOPLEFT.x;
            this.targetY = Corner.TOPLEFT.y;
        }
    }

}