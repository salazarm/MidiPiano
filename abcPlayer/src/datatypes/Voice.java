package datatypes;

import java.util.ArrayList;
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
        this.musicSequences = new ArrayList<MusicSequence>();
    }
    
    /**
     * Adds passed MusicSequence to the List of MusicSequences that make up this Voice
     * @param musicSequence MusicSequence to add to this Voice
     */
    public void add(MusicSequence musicSequence) {
        if(closed) throw new RuntimeException("This voice is already closed with doubleBar");
        this.musicSequences.add(musicSequence);
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
