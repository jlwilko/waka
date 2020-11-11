package ghost;

import java.util.Arrays;

import processing.core.PImage;

public class Ghost extends Entity{

    public Ghost(int x, int y, PImage sprite, long speed, Game game){
        super(x, y, sprite, speed, game);
    }


    public void setNextMovement(App App){
        Pair arr[] = new Pair[4];

        arr[0] = new Pair(calculateDistanceToPlayer(this.x + Direction.LEFT.xVel, 
                        this.y + Direction.LEFT.yVel), Direction.LEFT);
        arr[1] = new Pair(calculateDistanceToPlayer(this.x + Direction.RIGHT.xVel,
                        this.y + Direction.RIGHT.yVel),Direction.RIGHT);
        arr[2] = new Pair(calculateDistanceToPlayer(this.x + Direction.DOWN.xVel, 
                        this.y + Direction.DOWN.yVel),Direction.DOWN);
        arr[3] = new Pair(calculateDistanceToPlayer(this.x + Direction.UP.xVel, 
                        this.y + Direction.UP.yVel),Direction.UP);

        Arrays.sort(arr);

        int i = 0;
        while (this.movement == arr[i].direction.opposite() || !this.canMove(arr[i].direction)){
            i++;
        }
        this.nextMovement = arr[i].direction;
    }
    
    private double calculateDistanceToPlayer(int x, int y){
        Square sqr = (dist) -> (dist*dist);
        int dx = sqr.apply(x - this.game.getPlayerX());
        int dy = sqr.apply(y - this.game.getPlayerY());
        return Math.sqrt(dx + dy);
    }


}
interface Square{
    int apply(int x);
}

class Pair implements Comparable<Pair>{
    double distance;
    Direction direction; 

    public Pair(double distance, Direction direction){
        this.distance = distance;
        this.direction = direction;
    }

    public int compareTo(Pair other){
        return (this.distance > other.distance)? 1 : -1;
    }
}