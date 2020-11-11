package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public class Player extends Entity{


    public Player(int x, int y, PImage sprite, long speed, Game game){
        super(x, y, sprite, speed, game);

        // // Large tile position
        // this.x = x;
        // this.y = y;
        // this.sprite = sprite;

        // // No issues with overflow from this since speed will either be 1 or 2
        // this.speed = (int) speed;

        // // Position within each tile
        // this.subX = 0;
        // this.subY = 0;

        // // Player should initially be going left
        // this.movement = Direction.LEFT;
        // this.nextMovement = Direction.LEFT;

        // // Offset for the left facing sprite
        // this.xOff = -4;
        // this.yOff = -5;

        // this.game = game;
    }

    public int getTileX(){
        return x;
    }

    public int getTileY(){
        return y;
    }

    public Direction getMovementDirection(){
        return this.movement;
    }


    public void setNextMovement(App app){
        if (app.keyPressed){
            if (app.key == 'w' || app.key == PApplet.CODED && app.keyCode == PApplet.UP){
                this.nextMovement = Direction.UP;
            } else if (app.key == 'a' || app.key == PApplet.CODED && app.keyCode == PApplet.LEFT){
                this.nextMovement = Direction.LEFT;
            } else if (app.key == 's' || app.key == PApplet.CODED && app.keyCode == PApplet.DOWN){
                this.nextMovement = Direction.DOWN;
            } else if (app.key == 'd' || app.key == PApplet.CODED && app.keyCode == PApplet.RIGHT){
                this.nextMovement = Direction.RIGHT;
            }
        }
    }
    
}
