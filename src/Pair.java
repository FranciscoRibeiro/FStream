public class Pair<T,S> {
    T x;
    S y;

    public Pair(T x, S y){
        this.x = x;
        this.y = y;
    }

    public T getX(){
        return this.x;
    }

    public S getY(){
        return this.y;
    }

    @Override
    public String toString() {
        return "(" + x +
                ", " + y +
                ')';
    }
}
