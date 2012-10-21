package datatypes;

import sound.Pitch;

public class Note implements MusicSequence {
	
	private final char baseNote;
	private final int octaveModifier;
	private final Accidental accidentalModifier;
	private final double noteMultiplier;
	private final Pitch notePitch;
	private final Player player;
	
	/**
	 * Creates a Note object
	 * @param baseNote char representing the base note
	 * @param octaveModifier integer represnting the number of octaves up or down from the base
	 * note. Passed value of 0 means no octave modification.
	 * @param accidentalModifier Accidental object representing the accidental applied to this
	 * Note. A passed value of null corresponds to no accidental. 
	 * @param noteMultiplier double Modifier of the note length. A passed value of 1 indicates
	 * that note length is equal to the default note length specified in the Header 
	 * associated with the Player object that this Note belongs to.
	 * @param player Player object that this Note is to be played by
	 */
	public Note(char baseNote, int octaveModifier, Accidental accidentalModifier, 
			double noteMultiplier, Player player) {
		this.baseNote = baseNote;
		this.octaveModifier = octaveModifier;
		this.accidentalModifier = setAccidentalModifier(accidentalModifier);
		this.noteMultiplier = noteMultiplier;
		this.notePitch = makePitch();
		this.player = player;
	}
	
	private Accidental setAccidentalModifier(Accidental accidentalModifier) {
		if(accidentalModifier==null) {
			return new Accidental(Accidental.Type.NEUTRAL, "=");
		}
		else {
			return accidentalModifier;
		}
	}
	
	private Pitch makePitch() {
		return new Pitch(this.baseNote).accidentalTranspose(accidentalModifier.getIntRep()).octaveTranspose(octaveModifier); 
	}
	
	/**
	 * Calculates and returns the duration of this Note in the associated Player 
	 * in ticks.
	 * @return duration of this Note in ticks in associated Player 
	 */
	@Override
	public int getDuration() {
		/* The following calculates the duration of this Note according to the following
		 * formula:
		 * duration = Note Multiplier * Default Note Length * Number of Ticks Per Note
		 * The unit of duration is discrete "ticks".  
		 */
		return (int) this.noteMultiplier * this.player.getHeader().getDefaultNoteLength() 
				* 4 * this.player.getTicksPerQuarterNote();
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
	
	public Player getPlayer() {
		return this.player;
	}
}
