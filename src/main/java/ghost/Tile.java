package ghost;

import processing.core.PApplet;
import processing.core.PImage;

//  Maybe have a tile public class/abstract class / interface 
//  That way we just call the tile interface methods 

public class Tile{
    private PImage sprite;
    private int x;
    private int y;

    public Tile(PImage sprite, int x, int y){
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public void draw(PApplet app){
        if (this.sprite == null){
            return;
        }
        app.image(this.sprite, this.x*16, this.y*16);
    }

    // public String get(int type){
    //     return this.tileTypes[i];
    // }
}