package datatypes;

import sound.Pitch;

public class Note extends MusicSequence {
	
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
	 * @throws RuntimeException in case of invalid baseNote
	 */
	public Note(char baseNote, int octaveModifier, Accidental accidentalModifier, 
			double noteMultiplier) {
	    
	    if(baseNote>='a' && baseNote<='z') throw new RuntimeException("Note() : baseNote is not capital");
		this.baseNote = baseNote;
		this.octaveModifier = octaveModifier;
		this.accidentalModifier = setAccidentalModifier(accidentalModifier);
		this.noteMultiplier = noteMultiplier;
		this.notePitch = makePitch();
		
	}
	
	/**
	 * Decides and returns the accidentalModifier of this Note
	 * @param accidentalModifier Accidental object representing the accidentalModifier, value of
	 * null implies a neutral Note.
	 * @return Accidental object representing this Note's accidentalModifier
	 */
	private Accidental setAccidentalModifier(Accidental accidentalModifier) {		
		if(accidentalModifier==null) {
			return new Accidental("=");
		}
		else {
			return accidentalModifier;
		}
	}
	
	/**
	 * Makes a Pitch out of this Note
	 * @return Pitch object representing this Note
	 */
	private Pitch makePitch() {
		return new Pitch(this.baseNote).accidentalTranspose(accidentalModifier.getIntRep()).octaveTranspose(octaveModifier); 
	}

	
	public <R> R accept(Visitor<R> v)
	{
	    return v.onNote(this);
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
