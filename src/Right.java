public class Right<T> extends Either{
  T elem;

  public Right(T e){
    this.elem = e;
  }

  public T fromRight(){
    return this.elem;
  }
}
