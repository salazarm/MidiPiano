package datatypes;

import java.util.List;
import exception.InvalidInputException;

public class Tuplet extends MusicSequence {
	
	private final int tupletNumber;
	private final List<Note> notes;
	public static final double ratio[] = new double [] {0, 0, 3./2, 2./3, 3./4};
	// * 2)AB   2 notes in time of 3 -> 3/2
    // * 3)ABC  3       in time of 2 -> 2/3
    // * 4)ABCD 4       in time of 3 -> 3/4
	
	/**
	 * Creates a Tuplet object
	 * @param tupletNumber Identifier of tuple type. Must be 2, 3 or 4.
	 * @param notes
	 * @param player
	 */
	public Tuplet(List<Note> notes) {
	    tupletNumber = notes.size();
		if (tupletNumber!=2 && tupletNumber!=3 && tupletNumber!=4) {
			throw new InvalidInputException(
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

	public <R> R accept(Visitor<R> v)
    {
        return v.onTuplet(this);
    }
}
