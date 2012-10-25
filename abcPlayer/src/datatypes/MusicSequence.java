package datatypes;

public abstract class MusicSequence {
	
	private int startTick;	
	
	public void setStartTick(int startTick) {
		this.startTick = startTick;
	}
	public int getStartTick() {
		return this.startTick;
	}
	
	public abstract <R> R accept(Visitor<R> v);

}
