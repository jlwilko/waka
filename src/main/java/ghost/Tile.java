package ghost;

import processing.core.PApplet;
import processing.core.PImage;

//  Maybe have a tile public class/abstract class / interface 
//  That way we just call the tile interface methods 

public class Tile{
    /**
     * the sprite for this tile
     */
    private PImage sprite;
    /**
     * the tile x coordinate for this tile 
     */
    private int x;
    /**
     * the tile y coordinate for this tile 
     */
    private int y;
    /** 
     * the id of the tile, for comparison purposes in other classes 
     */
    private int id;

    /**
     * Constructs a Tile object 
     * @param sprite the sprite that should be shown for this tile 
     * @param x the x position of the tile 
     * @param y the y position of the tile 
     * @param id the id of the tile - a number between 0 and 9 
     */
    public Tile(PImage sprite, int x, int y, int id){
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.id = id;
    }

    /**
     * Getter for the ID property 
     * @return the ID property of the tile
     */
    public int getID(){
        return this.id;
    }

    /**
     * Draws the tile to the screen at the right location 
     * @param app The app screen to draw the tile to
     */
    public void draw(PApplet app){
        // IF there is not sprite associated with the tile, then skip drawing this cell of the board
        if (this.sprite == null){
            return;
        }
        app.image(this.sprite, this.x*16, this.y*16);
    }
}