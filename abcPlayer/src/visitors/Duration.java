package visitors;

import java.util.List;

import datatypes.Body;
import datatypes.Chord;
import datatypes.MusicSequence;
import datatypes.Note;
import datatypes.Player;
import datatypes.Repeat;
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
	 * Returns the duration of this Repeat in ticks.
	 * @return duration int representation of the duration of this Repeat, which is calculated
	 * as the duration of each of the musicSequences on both the first and second passes of the 
	 * Repeat
	 */
	@Override
	public Integer onRepeat(Repeat repeat) {
		int duration = 0;
		for (int i = 0; i < repeat.getSequences().size(); i++) {
			duration += repeat.getSequences().get(i).accept(this) + repeat.getSecondPass().get(i).accept(this);
		}
		return duration;
	}

	/**
	 * Returns the duration of this Tuplet in ticks. See abc subset for definition of
	 * Tuplet duration.
	 * @return duration int representation of the duration of this Tuplet in ticks.
	 */
	@Override
	public Integer onTuplet(Tuplet tuplet) {
		int duration = 0;
        for (Note note : tuplet.getNotes())
            duration += note.accept(this);

		if(tuplet.getTupletNumber()==2)
			duration = (int) (duration * 3./2);
		else if(tuplet.getTupletNumber()==3)
			duration = (int) (duration * 2./3);
		else if(tuplet.getTupletNumber()==4)
			duration = (int) (duration * 3./4);
		else
		    throw new RuntimeException("The length of tuplet should be 2 ~ 4");

		return duration;
	}

	
	/**
	 * Get duration of this Voice in ticks. Duration is defined as the sum of the durations
	 * of the MusicSequences that make up this Voice.
	 * @return duration int value of duration as defined above in ticks
	 */
	@Override
    public Integer onVoice(Voice voice) {
	    int i,n = voice.getMusicSequences().size();
        int duration = 0, oneSection = player.getTicksPerSection(), checkPoint;
        List<MusicSequence> seq = voice.getMusicSequences();
        
        checkPoint = oneSection;
        for(i=0;i<n;++i)
        {
            duration += seq.get(i).accept(this);
            
// measurement checking
//            if(seq.get(i)!=null)
//            {
//                if(duration >= checkPoint) throw new RuntimeException("not fulfilled section");
//                duration += seq.get(i).accept(this);
//            }
//            else // barline
//            {
//                if(duration == checkPoint)
//                    checkPoint += oneSection;
//                else
//                    throw new RuntimeException("wrong position of barline");
//            }
        }

        //if(duration%oneSection != 0 || seq.get(n-1)!=null)
        //    throw new RuntimeException("not fulfilled last section " + duration + " " + oneSection);

        return duration;
    }

	/**
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
