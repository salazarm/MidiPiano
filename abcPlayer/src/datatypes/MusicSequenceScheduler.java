package datatypes;

import sound.SequencePlayer;

public class MusicSequenceScheduler implements Visitor<Void> {
	
	private final Player player;
	private final Duration duration;
	private final SequencePlayer seqPlayer;
	
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
		seqPlayer.addNote(note.getNotePitch().toMidiNote(), 
				note.getStartTick(), note.getDuration(this.duration));
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
			note.schedule(this);
		}
		return null;
	}

	@Override
	public Void onRest(Rest rest) {
		return null;
	}

	/**
	 * Schedules a repeated MusicSequence on the Player
	 * @param repeat Repeat object to be scheduled
	 * @return null
	 */
	@Override
	public Void onRepeat(Repeat repeat) {
		repeat.incrementCurTick(repeat.getStartTick());
		for(MusicSequence firstPass : repeat.getSequences()) {
			firstPass.setStartTick(repeat.getCurTick());
			firstPass.schedule(this);
			repeat.incrementCurTick(firstPass.getDuration(this.duration));
		}
		for(MusicSequence secondPass : repeat.getSecondPass()) {
			secondPass.setStartTick(repeat.getCurTick());
			secondPass.schedule(this);
			repeat.incrementCurTick(secondPass.getDuration(this.duration));
		}
		return null;
	}

	@Override
	public Void onTuplet(Tuplet tuplet) {
		// TODO Auto-generated method stub
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
			musicSequence.setStartTick(voice.getCurTick());
			musicSequence.schedule(this);
			voice.incrementCurTick(musicSequence.getDuration(this.duration));
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
			voice.schedule(this);
		}
		return null;
	}
}
