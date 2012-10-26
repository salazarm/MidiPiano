package datatypes;

import java.util.List;

public class Tuplet extends MusicSequence {

	private final int tupletNumber;
	private final List<Note> notes;
	private int curTick = 0;

	/**
	 * Creates a Tuplet object
	 * @param notes List of Notes that compose this tuplet. It should be a list of 2, 3, or 4 notes.
	 * @throws RuntimeException in case of invalid tuplet number
	 */
	public Tuplet(List<Note> notes) {
	    tupletNumber = notes.size();
		if (tupletNumber!=2 && tupletNumber!=3 && tupletNumber!=4) {
			throw new RuntimeException(
					String.format("Invalid tuplet number: %s", tupletNumber));
		}
		this.notes = notes;
	}

	public List<Note> getNotes() {
		return this.notes;
	}

	public int getTupletNumber() {
		return this.tupletNumber;
	}

	/**
	 * Increments the current tick of this MusicSequence by 1
	 * @param increment int ticks to increment by 
	 */
	public void incrementCurTick(int increment) {
		this.curTick += increment;
	}

	public int getCurTick() {
		return this.curTick;
	}

	public <R> R accept(Visitor<R> v)
    {
        return v.onTuplet(this);
    }
}
