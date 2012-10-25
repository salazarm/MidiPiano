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
	
	/**
	 * Creates a MusicSequenceScheduler for the given Player. Also stores a SequencePlayer
	 * for the Player and creates a new Duration visitor for the Player.
	 * @param player Player object representing desired player as above
	 */
	public MusicSequenceScheduler(Player player) {	    
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
		this.seqPlayer.addNote(note.getNotePitch().toMidiNote(), 
				note.getStartTick(), note.accept(this.duration));
		return null;
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

	/**
	 * Schedules nothing for a Rest object
	 * @param rest Rest object
	 */
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
		    if(musicSequence == null) continue;
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
