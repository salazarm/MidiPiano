package datatypes;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import sound.SequencePlayer;
import visitors.MusicSequenceScheduler;

public class Player {
	
	private Header header;
	private Body body;
	private final int ticksPerQuarterNote, beatsPerMinute;
	private SequencePlayer seqPlayer;
	private MusicSequenceScheduler scheduler = new MusicSequenceScheduler(this);
	
	/**
	 * Creates a Player object that schedules and plays a series of MusicSequences, stored in
	 * the Voices List of the Body associated with the Player.
	 * @param header Header object containing required header info for an abc file
	 * @param body Body object containing a List of Voices that makes up the body of this abc file
	 * @param beatsPerMinute int value for beats per minute
	 * @param ticksPerQuarterNote int value for ticks per quarter note
	 * @throws MidiUnavailableException
	 * @throws InvalidMidiDataException
	 */
	public Player(Header header, Body body) 
			throws MidiUnavailableException, InvalidMidiDataException {
		this.header = header;
		this.body = body;
		//header.getDefaultNoteLength()
		//header.getTempo()
		/*
		 * Input :
		 * (1) defaultNoteLength: 1/4, 1/8..
		 * (2) Tempo: (defaultNotes / min)
		 * 
		 * 1 beat = 1/4 note
		 * (3) beatsPerMinute:  Tempo * defaultNoteLength * 4 
		 * (4) ticksPerQuarterNote: freely chosen, as long as ticks for notes are integer.
		 * (3)x(4) = ticks per minute. (3)x(4) ticks <-> (tempo) default notes.
		 * One default note = [(3)*(4)/tempo] ticks = (4) * defaultNoteLength * 4
		 */
		this.beatsPerMinute = (int) (header.getTempo() * 4 * header.getDefaultNoteLength());
		// choose appropriately so all notes have integer ticks
		this.ticksPerQuarterNote = 48;

		this.seqPlayer = new SequencePlayer(beatsPerMinute, ticksPerQuarterNote);
	}
	
	public Header getHeader() {
		return this.header;
	}

	public Body getBody() {
		return this.body;
	}

	public SequencePlayer getSeqPlayer() {
		return this.seqPlayer;
	}

	public int getTicksPerQuarterNote() {
		return this.ticksPerQuarterNote;
	}

	public int getBeatsPerMinute() {
		return this.beatsPerMinute;
	}
	
	public int getTicksPerSection()
	{
	    // One default note = [(3)*(4)/tempo] ticks = (4) * defaultNoteLength * 4
	    // One section = (Meter / defaultNoteLength) default notes
	    return (int)(header.getMeter().getValue() * 4 * ticksPerQuarterNote);
	}
	
	/**
	 * Schedules the body to be played on the SequencePlayer associated with this Player object
	 */
	public void scheduleBody() {    
		body.accept(scheduler);
	}
	
	/**
	 * Plays currently scheduled MusicSequences in the SequencePlayer
	 * @throws MidiUnavailableException
	 */
	public void play() throws MidiUnavailableException {
		this.seqPlayer.play();
	}
}
