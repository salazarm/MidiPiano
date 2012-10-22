package datatypes;

import java.util.List;
import exception.InvalidInputException;

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
	public Tuplet(int tupletNumber, List<Note> notes) {
		if (tupletNumber!=2 && tupletNumber!=3 && tupletNumber!=4) {
			throw new InvalidInputException(
					String.format("Invalid tuplet number: %s", tupletNumber));
		}
		this.tupletNumber = tupletNumber;
		if (notes.size()!=this.tupletNumber) {
			throw new InvalidInputException("Wrong number of notes in Tuplet.");
		}
		else {
			this.notes = notes;
		}
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

	@Override
	public int getDuration(Visitor<Integer> v) {
		return v.onTuplet(this);
	}

	@Override
	public void schedule(Visitor<Void> v) {
		v.onTuplet(this);
	}
}
