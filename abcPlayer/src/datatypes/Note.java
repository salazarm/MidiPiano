package datatypes;

import sound.Pitch;

public class Note extends MusicSequence {
	
	private final double noteMultiplier;
	private final Pitch pitch;
	
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
	 */
	public Note(char baseNote, int octaveModifier, Accidental accidentalModifier, 
			double noteMultiplier) {
        if(accidentalModifier==null) accidentalModifier = new Accidental("=");
		this.noteMultiplier = noteMultiplier;
		this.pitch = new Pitch(baseNote).accidentalTranspose(accidentalModifier.getIntRep()).octaveTranspose(octaveModifier);
	}
	
	public <R> R accept(Visitor<R> v)
	{
	    return v.onNote(this);
	}

	public double getNoteMultiplier() {
		return this.noteMultiplier;
	}

	public Pitch getPitch() {
		return this.pitch;
	}
}
