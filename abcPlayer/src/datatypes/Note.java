package datatypes;

import sound.Pitch;

public class Note implements MusicSequence {
	
	private final char baseNote;
	private final int octaveModifier;
	private final Accidental accidentalModifier;
	private final double noteMultiplier;
	private final Pitch notePitch;
	
	/**
	 * Creates a Note object
	 * @param baseNote char representing the base note
	 * @param octaveModifier integer represnting the number of octaves up or down from the base
	 * note. Passed value of 0 means no octave modification.
	 * @param accidentalModifier Accidental object representing the accidental applied to this
	 * Note. A passed value of null corresponds to no accidental. 
	 * @param noteMultiplier double modifier of the note length. A passed value of 1 indicates
	 * that note length is equal to the default note length specified in the Header 
	 * associated with the Player object that this Note belongs to.
	 * @param player Player object that this Note is to be played by
	 */
	public Note(char baseNote, int octaveModifier, Accidental accidentalModifier, 
			double noteMultiplier) {
		this.baseNote = baseNote;
		this.octaveModifier = octaveModifier;
		this.accidentalModifier = setAccidentalModifier(accidentalModifier);
		this.noteMultiplier = noteMultiplier;
		this.notePitch = makePitch();
	}
	
	private Accidental setAccidentalModifier(Accidental accidentalModifier) {
		if(accidentalModifier==null) {
			// TODO: Check here to see what the key signature is
			return new Accidental(Accidental.Type.NEUTRAL, "=");
		}
		else {
			// TODO: And here
			return accidentalModifier;
		}
	}
	
	private Pitch makePitch() {
		return new Pitch(this.baseNote).accidentalTranspose(accidentalModifier.getIntRep()).octaveTranspose(octaveModifier); 
	}
	
	@Override
	public int getDuration(Visitor visitor) {
		return visitor.duration(this);
	}

	@Override
	public void schedule(Visitor visitor) {
		visitor.onNote(this);
	}
	
	public char getBaseNote() {
		return this.baseNote;
	}

	public int getOctaveModifier() {
		return this.octaveModifier;
	}

	public Accidental getAccidentalModifier() {
		return this.accidentalModifier;
	}

	public double getNoteMultiplier() {
		return this.noteMultiplier;
	}

	public Pitch getNotePitch() {
		return this.notePitch;
	}
}
