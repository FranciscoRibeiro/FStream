package util;

public class Right<T> extends Either{

  public Right(T e){
    this.right = e;
  }

  public T fromRight(){
    return (T) this.right;
  }
}