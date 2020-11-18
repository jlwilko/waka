package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Entity {

    protected int x;
    protected int y;
    protected int subX;
    protected int subY;
    protected int xOff;
    protected int yOff;

    protected int startX;
    protected int startY;

    protected Direction lastMovement;
    protected Direction movement;
    protected Direction nextMovement;

    protected PImage sprite;
    protected long speed;

    protected Game game;


    public Entity(int x, int y, PImage sprite, long speed, Game game){
        // Large tile position
        this.x = x;
        this.y = y;
        this.sprite = sprite;

        this.startX = x;
        this.startY = y;
        // No issues with overflow from this since speed will either be 1 or 2
        this.speed = (int) speed;

        // Position within each tile
        this.subX = 0;
        this.subY = 0;

        // Player should initially be going left
        this.lastMovement = Direction.LEFT;
        this.movement = Direction.LEFT;
        this.nextMovement = Direction.LEFT;

        // Offset for the left facing sprite
        this.xOff = -4;
        this.yOff = -5;

        this.game = game;
    }

    public abstract void setNextMovement(App app);

    public void draw(PApplet app){
        if (this.sprite == null){
            return;
        }
        app.image(this.sprite, x*16+subX+xOff, y*16+subY+yOff);
    }

    
    public boolean tick(App app){
        // need to change the sprite every 8 frames too

        // if we are at a node, and may be able to make a decision
        if (this.decisionRequired()){

            // check if we are currently trying to move into a wall
            if (!this.canMove(this.movement)){
                // if so, we stop moving 
                this.lastMovement = this.movement;
                this.movement = Direction.NONE;
            } 
            // if the next movement buffered will allow us to move, set 
            // that as the new movement direction
            if (this.canMove(this.nextMovement)) {
                this.movement = this.nextMovement;

            }
        }
        return this.step(app);
    }

    public boolean step(App app){
        boolean moved = false;
        this.subX += this.movement.xVel * this.speed;
        int changeTile = 0;
        if (this.subX > 16){
            changeTile = 1;
            this.subX = 0;
        } else if (this.subX < 0){
            changeTile = -1;
            this.subX = 16;
        }
        moved |= changeTile != 0;
        this.x += changeTile;
        changeTile = 0;
        this.subY += this.movement.yVel * this.speed;
        if (this.subY > 16){
            changeTile = 1;
            this.subY = 0;
        } else if (this.subY < 0){
            changeTile = -1;
            this.subY = 16;
        }
        moved |= changeTile != 0;
        this.y += changeTile;
        return moved;
    }

    public boolean decisionRequired(){
        if (this.subX == 0 && this.subY == 0){
            return true;
        }
        return false;
    }

    public boolean canMove(Direction move){
        return this.game.checkBoardTile(x + move.xVel, y + move.yVel); 
    }

    public void restart(){
        this.x = this.startX;
        this.y = this.startY;
        this.subX = 0;
        this.subY = 0;
        this.movement = Direction.LEFT;
        this.nextMovement = Direction.LEFT;
    }
}