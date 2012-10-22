package datatypes;

import java.util.List;

public class Voice extends MusicSequence {
	
	private final String voiceName;
	private List<MusicSequence> musicSequences;
	private int curTick = 0;
	
	/**
	 * Creates a Voice object associated with the passed Player.
	 * @param voiceName String name of Voice object to create.
	 * @param player Player that Voice is associated with.
	 */
	public Voice(String voiceName) {
		this.voiceName = voiceName;
	}
	
	/**
	 * Adds passed MusicSequence to the List of MusicSequences that make up this Voice
	 * @param musicSequence MusicSequence to add to this Voice
	 */
	public void addToVoice(MusicSequence musicSequence) {
		this.musicSequences.add(musicSequence);
	}
	
	public String getVoiceName() {
		return this.voiceName;
	}
	
	public List<MusicSequence> getMusicSequences() {
		return this.musicSequences;
	}
	
	public int getCurTick() {
		return this.curTick;
	}
	
	public void incrementCurTick(int increment) {
		this.curTick += increment;
	}

	public int getDuration(Visitor<Integer> v) {
		return v.onVoice(this);
	}
	
	public void schedule(Visitor<Void> v) {
		v.onVoice(this);
	}
}
