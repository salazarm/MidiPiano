package datatypes;

import java.util.List;

public class Chord implements MusicSequence {
	
	private final List<Note> notes;
	private final Player player;
	
	/**
	 * Creates a Chord object
	 * @param notes List of Notes in the chord, all Notes must be associated with the 
	 * Player object passed to constructor
	 * @param player Player object associated with all Notes in the chord, and hence 
	 * the chord as well 
	 */
	public Chord(List<Note> notes, Player player) {
		this.notes = notes;
		this.player = player;
	}
	
	public List<Note> getNotes() {
		return this.notes;
	}
	
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Returns duration of chord, currently defined as the duration of the longest note
	 * in the chord, in case the different notes within the chord have different lengths.
	 * @return duration Duration of chord in ticks as defined above
	 */
	@Override
	public int getDuration() {
		int duration = 0;
		for (Note note: this.notes) {
			duration = Math.max(duration, note.getDuration());
		}
		return duration;
	}

	@Override
    public <R> R accept(Visitor<R> v) {
        return v.onChord(this);
    }
}
