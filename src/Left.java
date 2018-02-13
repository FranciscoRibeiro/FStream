import java.util.ArrayList;

public class Left<T> extends Either{
  T elem;

  public Left(T e){
    this.elem = e;
  }

  public T fromLeft(){
    return this.elem;
  }
}
