package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public class Player {

    private int tileX;
    private int tileY;
    private int xOffset;
    private int yOffset;
    private int dx;
    private int dy;

    private PImage sprite;


    public Player(int x, int y, PImage sprite){
        this.tileX = x;
        this.tileY = y;
        this.sprite = sprite;
        this.xOffset = -4;
        this.yOffset = -4;
        this.dx = 0;
        this.dy = 0;
    }


    public void draw(PApplet app){
        // draw the sprite for the class 
        // Handling graphcis 
        // Should generally be a single line, or couple of lines,
        // minimial logic

        app.image(this.sprite, tileX*16+xOffset, tileY*16+yOffset);

    }

    public void tick(PApplet app){
        
        
        if (app.keyPressed){
            if (app.key == 'w' || app.key == app.CODED && app.keyCode == app.UP){
                this.dy = 1;
                this.dx = 0;
            } else if (app.key == 'a' || app.key == app.CODED && app.keyCode == app.LEFT){
                this.dy = 0;
                this.dx = -1;
            } else if (app.key == 's' || app.key == app.CODED && app.keyCode == app.DOWN){
                this.dy = -1;
                this.dx = 0;
            } else if (app.key == 'd' || app.key == app.CODED && app.keyCode == app.RIGHT){
                this.dy = 0;
                this.dx = 1;
            }
        }
        
        this.xOffset += this.dx;
        int temp = this.xOffset/16;
        this.tileX += temp;
        this.xOffset %= 16;
        
        this.yOffset -= this.dy;
        temp = this.yOffset/16;
        this.tileY += temp;
        this.yOffset %= 16;
    }
    
}