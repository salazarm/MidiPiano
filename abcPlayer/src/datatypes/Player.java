package datatypes;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.SequencePlayer;

public class Player {
	
	private Header header;
	private Body body;
	private final int ticksPerQuarterNote, beatsPerMinute;
	private SequencePlayer seqPlayer;
	
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
	
	public void schedule(final int voice)
    {
	    final int [] timeline = new int[header.getVoiceNames().length];
	    
        class Schedule implements Visitor<Void>
        {
            public Void onNote(Note note)
            {
                int len = note.getDuration();
                seqPlayer.addNote(note.getNotePitch().toMidiNote(), timeline[voice], len);
                timeline[voice] += len;
                return null;
            }
            public Void onChord(Chord chord)
            {
                int len = chord.getDuration();
                List<Note> notes = chord.getNotes();
                seqPlayer.addNote(notes.get(0).getNotePitch().toMidiNote(), timeline[voice], len);
                seqPlayer.addNote(notes.get(1).getNotePitch().toMidiNote(), timeline[voice], len);
                seqPlayer.addNote(notes.get(2).getNotePitch().toMidiNote(), timeline[voice], len);
                timeline[voice] += len;
                return null;
            }
            public Void onRest(Rest rest)
            {
                timeline[voice] += rest.getDuration();
                return null;                
            }
            public Void onRepeat(Repeat repeat)
            {
                return null;
            }
            public Void onTuplet(Tuplet tuplet)
            {
                int i, len = tuplet.getDuration();
                List<Note> notes = tuplet.getNotes();
                for(i=0;i<notes.size();i++)
                {
                    seqPlayer.addNote(notes.get(0).getNotePitch().toMidiNote(), timeline[voice], len);
                    timeline[voice] += len;
                }
                return null;
            }
        };
    }
}
