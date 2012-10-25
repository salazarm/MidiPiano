package visitors;

import datatypes.Body;
import datatypes.Chord;
import datatypes.MusicSequence;
import datatypes.Note;
import datatypes.Player;
import datatypes.Rest;
import datatypes.Tuplet;
import datatypes.Visitor;
import datatypes.Voice;

import sound.SequencePlayer;

public class MusicSequenceScheduler implements Visitor<Void> {
	
	private final Player player;
	private final Duration duration;
	private final SequencePlayer seqPlayer;
	private int currentTick;
	
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
		this.seqPlayer.addNote(note.getPitch().toMidiNote(), 
				currentTick, note.accept(this.duration));
		return null;
	}

	/**
	 * Schedules a chord on the Player
	 * @param chord Chord object to be scheduled
	 * @return null
	 */
	@Override
	public Void onChord(Chord chord) {
		for(Note note : chord.getNotes())
			note.accept(this);

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
	    int tick = currentTick, oneNote;
	    oneNote = (int) (tuplet.getNotes().get(0).accept(duration)*Tuplet.ratio[tuplet.getTupletNumber()]);
		
		for (Note note: tuplet.getNotes()) {
			note.accept(this);
			currentTick += oneNote;
		}
		
		currentTick = tick;
		return null;
	}

	/**
	 * Schedules a Voice on the Player
	 * @param voice Voice object to be scheduled
	 * @return null
	 */
	@Override
	public Void onVoice(Voice voice) {
		for(MusicSequence musicSequence : voice.getMusicSequences()) {
			musicSequence.accept(this);
			currentTick += musicSequence.accept(this.duration);
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
		    currentTick = 0;
			voice.accept(this);
		}
		return null;
	}
}
