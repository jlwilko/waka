package ghost;

import processing.core.PApplet;

public class App extends PApplet {

    public static final int WIDTH = 448;
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

        // Load images
        // this.ghost = new Ghost(224, 280, this.loadImage("src/main/resources/ghost.png"));
        // papplet treats images slightly different to files


    }

    public void draw() { 

        this.game.tick(this);
        background(0, 0, 0);

        this.game.draw(this);

        // this.ghost.tick();

        // this.ghost.draw(this);

        // subdivide into draw methods and tick emthods

    }

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }

}
