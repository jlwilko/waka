package ghost;

public enum Direction {

    UP(0,-1){
        public Direction opposite(){
            return DOWN;
        }
    },DOWN(0,1){
        public Direction opposite(){
            return UP;
        }
    },LEFT(-1,0){
        public Direction opposite(){
            return RIGHT;
        }
    },RIGHT(1,0){
        public Direction opposite(){
            return LEFT;
        }
    },NONE(0,0){
        public Direction opposite(){
            return NONE;
        }
    };

    /**
     * velocity in the x direction
     */
    public int xVel;

    /** 
     * velocity in the y direction
     */
    public int yVel;

    /**
     * The direction of movement of an Entity 
     * @param xVel the velocity of the Entity in the x direction
     * @param yVel the velocity of the Entity in the y direction
     */
    private Direction(int xVel, int yVel){
        this.xVel = xVel;
        this.yVel = yVel;
    }

    /**
     * Returns the Direction that is the opposite of this direction. <p>
     * Direction.NONE returns Direction.NONE
     * @return the direction that opposes the given direction 
     */
    public abstract Direction opposite();
}
