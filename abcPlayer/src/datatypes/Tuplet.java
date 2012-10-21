package datatypes;

import java.util.List;
import exception.InvalidInputException;

public class Tuplet implements MusicSequence {
	
	private final int tupletNumber;
	private final List<Note> notes;
	private final Player player;
	
	/**
	 * Creates a Tuplet object
	 * @param tupletNumber Identifier of tuple type. Must be 2, 3 or 4.
	 * @param notes
	 * @param player
	 */
	public Tuplet(int tupletNumber, List<Note> notes, Player player) {
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
		this.player = player;
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void schedule(Visitor v) {
		v.onTuplet(this);
	}

}
