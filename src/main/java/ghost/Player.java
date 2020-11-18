package ghost;

import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;

public class Player extends Entity{

    private int framesSinceChange;
    private Map<Direction,PImage> sprites; 

    public Player(int x, int y, Map<Direction,PImage> sprites, long speed, Game game){
        super(x, y, sprites.get(Direction.LEFT), speed, game);
        this.sprites = sprites; 
        this.framesSinceChange = 0;
    }

    public int getTileX(){
        return x;
    }

    public int getTileY(){
        return y;
    }

    public boolean tick(App app){
        boolean res = super.tick(app);
        // if (this.game.detectCollision()){
        //     this.game.loseLife();
        //     this.game.restart(false);
        // }
        this.framesSinceChange++;
        // System.out.printf("frames = %d\n", this.framesSinceChange);
        
        if (this.framesSinceChange >= 8){
            this.changeSprite();
            this.framesSinceChange = 0;
        }
        return res;

    }

    private void changeSprite(){
        if (this.sprite == this.sprites.get(Direction.NONE)){
            if (this.movement == Direction.NONE){
                this.sprite = this.sprites.get(this.lastMovement);
            } else {
                this.sprite = this.sprites.get(this.movement);
            }
        } else {
            this.sprite = this.sprites.get(Direction.NONE);
        }
    }

    public void restart(){
        super.restart();
        this.sprite = this.sprites.get(Direction.LEFT);
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
