package datatypes;

public class Rest extends MusicSequence {

	private final double noteMultiplier;
	private final Player player;
	
	/**
	 * Creates a Rest object
	 * @param noteMultipler double modifier of the note length. A passed value of 1 indicates
	 * that note length is equal to the default note length specified in the Header 
	 * associated with the Player object that this Note belongs to.
	 * @param player Player object that this Note is to be played by
     */
	public Rest(double noteMultiplier, Player player) {
		this.noteMultiplier = noteMultiplier;
		this.player = player;
	}

	public double getNoteMultiplier() {
		return this.noteMultiplier;
	}

	public Player getPlayer() {
		return this.player;
	}

	public <R> R accept(Visitor<R> v)
    {
        return v.onRest(this);
    }
}
