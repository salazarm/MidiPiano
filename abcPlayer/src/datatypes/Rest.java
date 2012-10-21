package datatypes;

public class Rest implements MusicSequence {

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

	/**
	 * Calculates and returns the duration of this Rest in the associated Player 
	 * in ticks.
	 * @return duration of this Rest in ticks in associated Player 
	 */
	@Override
	public int getDuration() {
		/* The following calculates the duration of this Note according to the following
		 * formula:
		 * duration = Rest Note Multiplier * Default Note Length * Number of Ticks Per Note
		 * The unit of duration is discrete "ticks".  
		 */
		return (int) (this.noteMultiplier * this.player.getHeader().getDefaultNoteLength() 
				* 4 * this.player.getTicksPerQuarterNote());
	}

	@Override
	public void schedule(Visitor v) {
		 v.onRest(this);
	}
}
