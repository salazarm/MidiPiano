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

public class Duration implements Visitor<Integer> {
	
	private final Player player;
	
	public Duration(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Calculates and returns the duration of this Note in the associated Player 
	 * in ticks.
	 * @return duration of this Note in ticks in associated Player 
	 */
	@Override
	public Integer onNote(Note note) {
		/* The following calculates the duration of this Note according to the following
		 * formula:
		 * duration = Note Multiplier * Default Note Length * Number of Ticks Per Note
		 * The unit of duration is discrete "ticks".  
		 */
		return (int) (note.getNoteMultiplier() * this.getPlayer().getHeader().getDefaultNoteLength() 
				* 4 * this.getPlayer().getTicksPerQuarterNote());
	}

	/**
	 * Returns duration of chord, currently defined as the duration of the longest note
	 * in the chord, in case the different notes within the chord have different lengths.
	 * @return duration Duration of chord in ticks as defined above
	 */
	@Override
	public Integer onChord(Chord chord) {
		int duration = 0;
		for (Note note: chord.getNotes()) {
			duration = Math.max(duration, note.accept(this));
		}
		return duration;
	}

	/**
	 * Calculates and returns the duration of this Rest in the associated Player 
	 * in ticks.
	 * @return duration of this Rest in ticks in associated Player 
	 */
	@Override
	public Integer onRest(Rest rest) {
		/* The following calculates the duration of this Note according to the following
		 * formula:
		 * duration = Rest Note Multiplier * Default Note Length * Number of Ticks Per Note
		 * The unit of duration is discrete "ticks".  
		 */
		return (int) (rest.getNoteMultiplier() * this.getPlayer().getHeader().getDefaultNoteLength() 
				* 4 * this.getPlayer().getTicksPerQuarterNote()); 
	}

	/**
	 * Returns the duration of this Tuplet in ticks. See abc subset for definition of
	 * Tuplet duration.
	 * @return duration int representation of the duration of this Tuplet in ticks.
	 */
	@Override
	public Integer onTuplet(Tuplet tuplet) {
		int duration = 0;
		if(tuplet.getTupletNumber()==2) {
			for (Note note : tuplet.getNotes()) {
				duration += note.accept(this);
			}
			duration = (int) ((0.5*duration)*3);
		}
		else if(tuplet.getTupletNumber()==3) {
			for(Note note: tuplet.getNotes()) {
				duration += note.accept(this);
			}
			duration = (int) ((((double) (1/3)) * duration) * 2);
		}
		else if(tuplet.getTupletNumber()==4) {
			for(Note note: tuplet.getNotes()) {
				duration += note.accept(this);
			}
			duration = (int) ((((double) (1/4)) * duration) * 3);
		}
		return duration;
	}

	
	/**
	 * Used for checking each section is fulfilled.
	 * 
	 * Get duration of this Voice in ticks. Duration is defined as the sum of the durations
	 * of the MusicSequences that make up this Voice.
	 * @return duration int value of duration as defined above in ticks
	 */
    @Override
    public Integer onVoice(Voice voice) {
        int duration = 0, oneSection = player.getTicksPerSection(), checkPoint;
        
        checkPoint = oneSection;
        for (MusicSequence musicSequence : voice.getMusicSequences()) {
            duration += musicSequence.accept(this);
            if(duration == checkPoint) checkPoint += oneSection;
            else if(duration > checkPoint) throw new RuntimeException("not fulfilled section");
        }
        if(duration%oneSection != 0) throw new RuntimeException("not fulfilled section");
        return duration;
    }
	

	/**
	 * Used for checking all voices have the same length.
	 * 
	 * Returns duration of this Body, defined as the duration of the Voice with the longest
	 * duration in ticks.
	 * @return duration int value of duration in ticks, as defined above
	 */
	@Override
	public Integer onBody(Body body) {
		int duration = 0;
		Integer last = null;

		for (Voice voice : body.getVoiceList()) {
			duration = voice.accept(this);
			
			if(last!=null && duration!=last)
			    throw new RuntimeException("Voices have different lengths");
			
			last=duration;
		}
		return duration;
	}
}
