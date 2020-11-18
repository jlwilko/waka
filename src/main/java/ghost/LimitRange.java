package ghost;

public class LimitRange {
    /**
     * Limits the input integer between the values of max and min
     * @param a the number to be limited
     * @param min the minimum output number
     * @param max the maximum output number 
     * @return Returns the final number, limited to between min and max
     */
    public static int limit(int a, int min, int max){
        if (a > max){
            a = max;
        } else if (a < min){
            a = min;
        }
        return a;
    }
}