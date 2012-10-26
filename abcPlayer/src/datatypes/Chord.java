package datatypes;

import java.util.List;

public class Chord extends MusicSequence {
	
	private final List<Note> notes;
	
	/**
	 * Creates a Chord object
	 * @param notes List of Notes in the chord, all Notes must be associated with the Player object passed to constructor
	 * @param player Player object associated with all Notes in the chord, and hence 
	 * the chord as well 
	 */
	public Chord(List<Note> notes) {
		this.notes = notes;
	}
	
	public List<Note> getNotes() {
		return this.notes;
	}
	
    public <R> R accept(Visitor<R> v)
    {
        return v.onChord(this);
    }
}
