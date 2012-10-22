package datatypes;

import java.util.ArrayList;
import java.util.List;

public class Repeat extends MusicSequence {
	
	private final List<MusicSequence> sequences;
	private int curTick = 0;
	private final Player player;
	private final List<MusicSequence> alternateEnding;
	private final List<MusicSequence> secondPass; /* Contains the MusicSequences played in the
	                                                 second pass while repeating */
	
	/**
	 * Creates a Repeat object.
	 * @param sequences List of MusicSequences to be repeated, all MusicSequences in List must be 
	 * associated with the Player object passed to this constructor
	 * @param player Player object associated with all the MusicSequences in notes, and thus
	 * with this Repeat object 
	 * @param alternateEnding List of MusicSequences making up the ending of the Repeat on
	 * second pass. If passed value is null, same ending as last time is used. The size of this
	 * List is the number of MusicSequences replaced from the end of sequences
	 */
	public Repeat(List<MusicSequence> sequences, Player player, 
			List<MusicSequence> alternateEnding) {
		this.sequences = sequences;
		this.player = player;
		this.alternateEnding = alternateEnding;
		if (this.alternateEnding==null) {
			this.secondPass = this.sequences;
		}
		else {
			this.secondPass = makeSecondPass(alternateEnding);
		}
	}
	
	/**
	 * Determines the MusicSequences to be played on the second pass of this Repeat.
	 * @param alternateEnding List of MusicSequences that make up the alternate ending
	 * on the second pass. Must be non-null.
	 * @return secondPass List of MusicSequences making up the second pass on this Repeat.
	 */
	private List<MusicSequence> makeSecondPass(List<MusicSequence> alternateEnding) {
		List<MusicSequence> secondPass = new ArrayList<MusicSequence>();
		int endingSize = this.alternateEnding.size();
		for(int i = 0; i < (this.sequences.size()-endingSize+1); i++) {
			secondPass.add(sequences.get(i));
		}
		for (MusicSequence musicSequence : this.alternateEnding) {
			secondPass.add(musicSequence);
		}
		return secondPass;
	}

	public List<MusicSequence> getSequences() {
		return this.sequences;
	}
	
	public List<MusicSequence> getSecondPass() {
		return this.secondPass;
	}

	public Player getPlayer() {
		return this.player;
	}

	public List<MusicSequence> getAlternateEnding() {
		return this.alternateEnding;
	}
	
	public void incrementCurTick(int increment) {
		this.curTick += increment;
	}
	
	public int getCurTick() {
		return this.curTick;
	}
	
	@Override
	public int getDuration(Visitor<Integer> v) {
		return v.onRepeat(this);
	}

	@Override
	public void schedule(Visitor<Void> v) {
		v.onRepeat(this);
	}

}
