package datatypes;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.SequencePlayer;

public class Player {
	
	private Header header;
	private Body body;
	private final int ticksPerQuarterNote, beatsPerMinute;
	private SequencePlayer seqPlayer;
	private MusicSequenceScheduler scheduler = new MusicSequenceScheduler(this);
	
	public Player(Header header, Body body, int beatsPerMinute, int ticksPerQuarterNote) 
			throws MidiUnavailableException, InvalidMidiDataException {
		this.header = header;
		this.body = body;
		this.ticksPerQuarterNote = ticksPerQuarterNote;
		this.beatsPerMinute = beatsPerMinute;
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
	
	/**
	 * Schedules the body to be played on the SequencePlayer associated with this Player object
	 */
	public void scheduleBody() {
		body.schedule(scheduler);
	}
}
