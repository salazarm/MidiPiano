package visitors;

import java.util.ArrayList;
import java.util.List;

import sound.SequencePlayer;
import datatypes.Body;
import datatypes.Chord;
import datatypes.MusicSequence;
import datatypes.Note;
import datatypes.Player;
import datatypes.Rest;
import datatypes.Tuplet;
import datatypes.Visitor;
import datatypes.Voice;

public class MusicSequenceScheduler implements Visitor<Void> {
	
	private final Player player;
	private final Duration duration;
	private final SequencePlayer seqPlayer;
	
	public MusicSequenceScheduler(Player player) {
	    //if(player.getSeqPlayer() == null) System.out.println("wrong init");
	    
		this.player = player;
		this.seqPlayer = player.getSeqPlayer();
		duration = new Duration(this.player);
	}
	
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Schedules a note on the Player
	 * @param note Note object to be scheduled
	 * @return null
	 */
	@Override
	public Void onNote(Note note) {
		/* Examines the key signature of this abc file to determine whether any
		 * additional accidentals have to be applied to the note. */
	    
//		KeySignature ks = this.player.getHeader().getKeySignature();
//		int[] accidentals = ks.getKeyAccidentals();
//		int curNote = getCurNoteAsInt(Character.toUpperCase(note.getBaseNote()));
//		note.getNotePitch().accidentalTranspose(accidentals[curNote]);
	    
	    //System.out.println("onNote: "+note.getStartTick()+" for "+this.duration);

		this.seqPlayer.addNote(note.getNotePitch().toMidiNote(), 
				note.getStartTick(), note.accept(this.duration));
		return null;
	}
	
	/**
	 * Returns the corresponding values for baseNote arguments:
	 * A = 1, B = 2, C = 3, D = 4, E = 5, F = 6, G = 7
	 * @param baseNote char uppercase representation of the baseNote, must be uppercase
	 * character between A-G inclusive
	 * @return int index in the KeySignature accidentals array of that baseNote
	 */
	private int getCurNoteAsInt(char baseNote) {
		return (int) (baseNote-64);
	}

	/**
	 * Schedules a chord on the Player
	 * @param chord Chord object to be scheduled
	 * @return null
	 */
	@Override
	public Void onChord(Chord chord) {
		for(Note note : chord.getNotes()) {
			note.setStartTick(chord.getStartTick());
			note.accept(this);
		}
		return null;
	}

	@Override
	public Void onRest(Rest rest) {
		return null;
	}

	/**
	 * Schedules a Tuplet on the Player
	 * @param tuplet Tuplet object to schedule on the Player
	 * @return null
	 */
	@Override
	public Void onTuplet(Tuplet tuplet) {
		List<Note> notesCorrectDuration = correctDuration(tuplet.getNotes(), tuplet.accept(this.duration));

		tuplet.incrementCurTick(tuplet.getStartTick());
		for (Note note: notesCorrectDuration) {
			note.setStartTick(tuplet.getCurTick());
			note.accept(this);
			tuplet.incrementCurTick(note.accept(this.duration));
		}
		return null;
	}
	
	/**
	 * Returns the List of Notes with their durations (and noteMultipliers) modified to
	 * their values when they are to be scheduled to be played as part of a Tuplet 
	 * @param notes List of Notes objects, this List is not modified
	 * @param tupletDuration int duration of the Tuplet the notes are a part of, in ticks
	 * @return List of Notes objects with their noteMultiplier corrected as specified above
	 */
	private List<Note> correctDuration(List<Note> notes, int tupletDuration) {
		int totalNoteDuration = 0;
		List<Note> correctDurationNotes = new ArrayList<Note>();
		for (Note note: notes) {
			totalNoteDuration += note.accept(this.duration);
		}
		for (Note note: notes) {
			double ratio = ((float)note.accept(this.duration))/totalNoteDuration;
			double ticksThisNote = (ratio*tupletDuration);
			double noteMultiplier = ((float)ticksThisNote)/(this.getPlayer().getHeader().getDefaultNoteLength() 
				* 4 * this.getPlayer().getTicksPerQuarterNote());
			
			correctDurationNotes.add(new Note(note.getBaseNote(), note.getOctaveModifier(), 
					note.getAccidentalModifier(), noteMultiplier));
		}
		return correctDurationNotes;
	}

	/**
	 * Schedules a Voice on the Player
	 * @param voice Voice object to be scheduled
	 * @return null
	 */
	@Override
	public Void onVoice(Voice voice) {
		for(MusicSequence musicSequence : voice.getMusicSequences()) {
			musicSequence.setStartTick(voice.getCurTick());
			musicSequence.accept(this);
			voice.incrementCurTick(musicSequence.accept(this.duration));
		}
		return null;
	}

	/**
	 * Schedules a Body on the Player
	 * @param body Body object to be scheduled
	 * @return null
	 */
	@Override
	public Void onBody(Body body) {
		for(Voice voice : body.getVoiceList()) {
			voice.setStartTick(body.getStartTick());
			voice.accept(this);
		}
		return null;
	}
}
