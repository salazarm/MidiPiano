package datatypes;

import java.util.List;

/**
 * A class represents a single melody line.
 */

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
	public void add(MusicSequence musicSequence) {
		this.musicSequences.add(musicSequence);
	}
	
	private int repeatLeft = 0, repeatSkip = 0;
	public void repeatStart() { repeatLeft = musicSequences.size(); }
	public void repeatSection(){repeatSkip = musicSequences.size(); }
	/**
	 * Expand repeats,
	 * by copying old sequence, assuming ADTs are immutable.
	 */
	public void repeatEnd()
    {
	    // Expand!!!
	    if(repeatLeft == 0) throw new RuntimeException("No repeat start found");
	    int i;
	    if(repeatSkip==0) // |: C D E F | G A B c :|
	    {
	        int repeatRight = musicSequences.size();
	        // [repeatLeft, repeatRight)
	        for(i=repeatLeft;i<repeatRight;++i)
	            musicSequences.add(musicSequences.get(i));
	    }
	    else // |: C D E F |[1 G A B c :|[2 F E D C |
	    {
	     // [repeatLeft, repeatSkip) 
            for(i=repeatLeft;i<repeatSkip;++i)
                musicSequences.add(musicSequences.get(i));
	    }
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

	public <R> R accept(Visitor<R> v)
    {
        return v.onVoice(this);
    }
}
