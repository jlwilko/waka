package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public class Ghost{

    private int tileX;
    private int tileY;
    private int xOffset;
    private int yOffset;

    private PImage sprite;

    public Ghost(int x, int y, PImage sprite){
        this.tileX = x;
        this.tileY = y;
        this.sprite = sprite;
        this.xOffset = 2;
        this.yOffset = -6;
    }

    public void tick(){
        // Handles logic for the class 

    }

    public void draw(PApplet app){
        // draw the sprite for the class 
        // Handling graphcis 
        // Should generally be a single line, or couple of lines,
        // minimial logic

        app.image(this.sprite, tileX*16+xOffset, tileY*16+yOffset);

    }

}
