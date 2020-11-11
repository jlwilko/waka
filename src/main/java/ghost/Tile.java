package ghost;

import processing.core.PApplet;
import processing.core.PImage;

//  Maybe have a tile public class/abstract class / interface 
//  That way we just call the tile interface methods 

public class Tile{
    private PImage sprite;
    private int x;
    private int y;
    private int id;

    public Tile(PImage sprite, int x, int y, int id){
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getID(){
        return this.id;
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