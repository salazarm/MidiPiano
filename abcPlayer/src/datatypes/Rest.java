package datatypes;

public class Rest extends MusicSequence {

	private final double noteMultiplier;
	
	/**
	 * Creates a Rest object
	 * @param noteMultiplier double modifier of the note length. A passed value of 1 indicates
	 * that note length is equal to the default note length specified in the Header 
	 * associated with the Player object that this Note belongs to.
     */
	public Rest(double noteMultiplier) {
		this.noteMultiplier = noteMultiplier;
	}

	public double getNoteMultiplier() {
		return this.noteMultiplier;
	}
	
	public <R> R accept(Visitor<R> v)
    {
        return v.onRest(this);
    }
}
