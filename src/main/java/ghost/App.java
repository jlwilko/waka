package ghost;

import processing.core.PApplet;

public class App extends PApplet {

    public static final int WIDTH = 449;
    public static final int HEIGHT = 576;
    public static final String CONFIG_FILENAME = "config.json";
    
    // private Ghost ghost;
    public Game game;
    // private Map map;

    public App() {
        //Set up your objects
        this.game = new Game(CONFIG_FILENAME, this);
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        frameRate(60);
        this.game.setup(this);
    }

    public void draw() { 
        this.game.tick(this);
        background(0, 0, 0);
        this.game.draw(this);
    }

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }


    public void keyReleased(){
        if (key == ' '){
            this.game.toggleDebug();
        }
    }
}
