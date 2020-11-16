package ghost;

import java.util.List;

import processing.core.PImage;

public class Whim extends Ghost{

    public Whim(int x, int y, PImage sprite, long speed, Game game, List<Long> modeLengths, PImage frightenedSprite){
        super(x,y,sprite,speed,game,modeLengths,frightenedSprite);
    }
    
    public void setTarget(){
        if (this.mode == GhostMode.CHASE){
            int dx = this.game.getPlayerX()+2*this.game.getPlayerVelX() - this.game.getChaserX();
            int dy = this.game.getPlayerY()+2*this.game.getPlayerVelY() - this.game.getChaserY();
            
            this.targetX = this.game.getChaserX() + 2*dx;
            this.targetY = this.game.getChaserY() + 2*dy;
        } else if (this.mode == GhostMode.SCATTER){
            this.targetX = Corner.BOTTOMRIGHT.x;
            this.targetY = Corner.BOTTOMRIGHT.y;
        } else {
            // FRIGHTENED MODE
            
        }
    }
}