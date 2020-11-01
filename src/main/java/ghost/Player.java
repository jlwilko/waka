package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public class Player {

    private int tileX;
    private int tileY;
    private int xOffset;
    private int yOffset;

    private PImage sprite;


    public Player(int x, int y, PImage sprite){
        this.tileX = x;
        this.tileY = y;
        this.sprite = sprite;
        this.xOffset = -4;
        this.yOffset = -4;
    }


    public void draw(PApplet app){
        // draw the sprite for the class 
        // Handling graphcis 
        // Should generally be a single line, or couple of lines,
        // minimial logic

        app.image(this.sprite, tileX*16+xOffset, tileY*16+yOffset);

    }
    
}