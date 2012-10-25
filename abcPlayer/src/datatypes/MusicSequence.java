package datatypes;

public abstract class MusicSequence {
	
	private int startTick;	
	
	/**
	 * Sets the starting tick of this MusicSequence
	 * @param startTick desired int value of starting tick
	 */
	public void setStartTick(int startTick) {
		this.startTick = startTick;
	}
	public int getStartTick() {
		return this.startTick;
	}
	
	public abstract <R> R accept(Visitor<R> v);

}
