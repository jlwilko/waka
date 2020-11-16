package ghost;

public class LimitRange {
    public static int limit(int a, int min, int max){
        if (a > max){
            a = max;
        } else if (a < min){
            a = min;
        }
        return a;
    }
}