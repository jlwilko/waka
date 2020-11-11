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

    public int xVel;
    public int yVel;

    private Direction(int xVel, int yVel){
        this.xVel = xVel;
        this.yVel = yVel;
    }

    public abstract Direction opposite();
}
