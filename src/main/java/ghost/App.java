package ghost;

import processing.core.PApplet;

public class App extends PApplet {

    /**
     * Width of the window of the sketch
     */
    public static final int WIDTH = 448;
    /** 
     * Height of the window of the sketch
     */
    public static final int HEIGHT = 576;
    /**
     * The filename that the config values will be read in from
     */
    public static final String CONFIG_FILENAME = "config.json";
    
    /**
     * The game object for this instance of the app
     */
    public Game game;

    /**
     * Constructor for the PApplet instance
     */
    public App() {
        //Set up your objects
        this.game = new Game(CONFIG_FILENAME, this);
    }

    /**
     * Sets the height and width of the sketch to be 
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Sets the frame rate of the sketch to 60, and loads all the sprite resources for the sketch
     */
    public void setup() {
        frameRate(60);
        this.game.setup(this);
    }

    /**
     * Is called at the frame rate of 60 times per second <p>
     * Manages both the logical progression and drawing of the game 
     */
    public void draw() { 
        // Tick the game, then write the background, then draw the next frame 
        this.game.tick(this);
        background(0, 0, 0);
        this.game.draw(this);
    }

    /**
     * Main entry point for the sketch <p>
     * Inits the sketch and runs the program
     * @param args Unused
     */
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }


    /**
     * Called everytime a key is released. <p>
     * This is used for the debug mode toggling on spacebar key
     */
    public void keyReleased(){
        // if the key released is the spacebar 
        if (key == ' '){
            this.game.toggleDebug();
        }
    }
}
