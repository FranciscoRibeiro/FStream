import java.util.*;

public class Skip<S> extends Step{
	S state;

	public Skip(S s){
		this.state = s;
	}
}