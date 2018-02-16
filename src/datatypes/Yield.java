package datatypes;

public class Yield<T,S> extends Step{

	public Yield(T e, S s){
		this.elem = e;
		this.state = s;
	}

	public String toString(){
		return "I'm a datatypes.Yield\n" + "Elem: " + this.elem + "\nState: " + this.state + "\n";
	}
}