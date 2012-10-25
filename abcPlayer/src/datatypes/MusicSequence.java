package datatypes;

public abstract class MusicSequence {	
	public abstract <R> R accept(Visitor<R> v);
	
	// This is not a visitor pattern
	//public abstract int getDuration(Visitor<Integer> v); // Returns duration of this MusicSequence in ticks
	//public abstract void schedule(Visitor<Void> v);
}
