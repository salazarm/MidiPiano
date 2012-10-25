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
	    if(closed) throw new RuntimeException("This voice is already closed with doubleBar");
		this.musicSequences.add(musicSequence);
	}
	
	private int repeatLeft = -1, repeatSkip = -1;
	
	/**
	 * Mark the start of repeat.
	 * Used with repeatEnd()
	 */
	public void repeatStart()
	{
	    if(closed) throw new RuntimeException("This voice is already closed with doubleBar");
	    repeatLeft = musicSequences.size();
	}
	/**
     * Mark the '[1' of repeat.
     * Used with repeatEnd()
     */
	public void repeatSection()
	{
	    if(closed) throw new RuntimeException("This voice is already closed with doubleBar");
	    repeatSkip = musicSequences.size();
	}
	/**
	 * Expand a repeat, by copying old sequence, assuming music elements are immutable.
	 */
	public void repeatEnd()
    {
	    if(closed) throw new RuntimeException("This voice is already closed with doubleBar");

	    // Expand!!!
	    if(repeatLeft == -1) throw new RuntimeException("No repeat start found");
	    int i;
	    if(repeatSkip == -1) // |: C D E F | G A B c :|
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
	/**
	 * Used for checking the end barline || or |].
	 */
	private boolean closed = false;
	public void setClosed()
	{
	    if(closed) throw new RuntimeException("You can't close a voice more than once");
	    closed = true;
	}
	public boolean getClosed() { return closed; }
	
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
