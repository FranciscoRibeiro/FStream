public class Left<T> extends Either{

  public Left(T e){
    this.left = e;
  }

  public T fromLeft(){
    return (T) this.left;
  }
}