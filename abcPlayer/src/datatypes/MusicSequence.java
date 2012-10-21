package datatypes;

public interface MusicSequence {
	public int getDuration(); // Returns duration of this MusicSequence in ticks
	
	public <R> R accept(Visitor<R> v);	

}
