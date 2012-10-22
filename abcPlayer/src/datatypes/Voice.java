package datatypes;

import java.util.List;

/**
 * A class represents a single melody line.
 */

public class Voice implements MusicSequence {
	
	private final String voiceName;
	private List<MusicSequence> musicSequences;
	private Player player;
	private int curTick = 0;
	
	/**
	 * Creates a Voice object associated with the passed Player.
	 * @param voiceName String name of Voice object to create.
	 * @param player Player that Voice is associated with.
	 */
	public Voice(String voiceName, Player player) {
		this.voiceName = voiceName;
		this.player = player;
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
	
	public Player getPlayer() {
		return this.player;
	}
	
	public int getCurTick() {
		return this.curTick;
	}
	
	/**
	 * Get duration of this Voice in ticks. Duration is defined as the sum of the durations
	 * of the MusicSequences that make up this Voice.
	 * @return duration int value of duration as defined above in ticks
	 */
	public int getDuration() {
		int duration = 0;
		for (MusicSequence musicSequence : this.musicSequences) {
			duration += musicSequence.getDuration();
		}
		return duration;
	}
	
	public void schedule(Visitor v) {
		v.onVoice(this);
	}
}
