package datatypes;

import java.util.List;

public class Tuplet extends MusicSequence {

	private final int tupletNumber;
	private final List<Note> notes;
	private int curTick = 0;

	/**
	 * Creates a Tuplet object
	 * @param tupletNumber Identifier of tuple type. Must be 2, 3 or 4.
	 * @param notes
	 * @param player
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
