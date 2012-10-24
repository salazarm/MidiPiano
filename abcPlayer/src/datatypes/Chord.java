package datatypes;

import java.util.List;

public class Chord extends MusicSequence {
	
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

    public <R> R accept(Visitor<R> v)
    {
        return v.onChord(this);
    }
}
