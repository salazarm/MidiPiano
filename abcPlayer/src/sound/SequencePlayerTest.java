package sound;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

/**
 * Practice test for piece1.abc and piece2.abc
 * 
 * @author Donggu Kang
 *
 */
public class SequencePlayerTest
{    
    // Helper class to add notes
    static class Composer
    {
        public int t = 0;
        int unit, sectionLength;
        SequencePlayer player;
        
        /*
         * @param player where notes are addded to
         * @param unit ticks per 1unit
         * @param unitsPerSection # of default notes in one section 
         */
        Composer(SequencePlayer player, int unit, int unitsPerSection)
        {
            this.player= player;
            this.unit = unit;
            this.sectionLength = unit * unitsPerSection;
        }
        public void addOne(char pitch, double length)
        {
            player.addNote(new Pitch(pitch).toMidiNote(), t, (int)(unit * length));
            t+=unit * length;
        }
        public void addOne(char pitch, double length, int transpose)
        {
            player.addNote(new Pitch(pitch).transpose(transpose).toMidiNote(), t, (int)(unit * length));
            t+=unit * length;
        }
        public void addHere(char pitch, double length)
        {
            player.addNote(new Pitch(pitch).toMidiNote(), t - (int)(unit*length), (int)(unit * length));
        }
        public void addHere(char pitch, double length, int transpose)
        {
            player.addNote(new Pitch(pitch).transpose(transpose).toMidiNote(), t - (int)(unit*length), (int)(unit * length));
        }
        public void addTuplet(String pitches, double length)
        {
            int i=0,len=(int)(length * unit / pitches.length());
            if(pitches.length()==2) len*=3;
            else if(pitches.length()==3) len*=2;
            else if(pitches.length()==4) len*=3;

            for(char pitch : pitches.toCharArray())
            {
                player.addNote(new Pitch(pitch).toMidiNote(), t, len);
                t += len;
                i++;
            }
        }
        public void addTuplet(String pitches, double length, int transpose[])
        {
            int i=0,len=(int)(length * unit / pitches.length());
            if(pitches.length()==2) len*=3;
            else if(pitches.length()==3) len*=2;
            else if(pitches.length()==4) len*=3;

            for(char pitch : pitches.toCharArray())
            {
                player.addNote(new Pitch(pitch).transpose(transpose[i]).toMidiNote(), t, len);
                t += len;
                i++;
            }
        }
        public void addRest(double length)
        {
            t += unit * length;
        }
        public void padSection()
        {
            if(t%sectionLength>0)
                t += sectionLength - t%sectionLength;
        }
        public void verifySection()
        {
            assertTrue(t/sectionLength+1 + " th section is not completed : "+t, t%sectionLength==0);
        }
    }
    
    SequencePlayer player;
    Composer composer;

    @Test
    public void test_piece1()
    {
        try {
            // there is no special meaning isn 24,
            // but all note lengths should be a divisor of 24.
            player = new SequencePlayer(120, 24);
            composer = new Composer(player, 24, 4);

            composer.addOne('C',1);
            composer.addOne('C',1);
            composer.addOne('C',3/4.);
            composer.addOne('D',1/4.);
            composer.addOne('E',1);
            composer.verifySection();
            
            composer.addOne('E',3/4.);
            composer.addOne('D',1/4.);
            composer.addOne('E',3/4.);
            composer.addOne('F',1/4.);
            composer.addOne('G',2);
            composer.verifySection();
            
            composer.addTuplet("DDD",1/2.,new int[]{Pitch.OCTAVE,Pitch.OCTAVE,Pitch.OCTAVE});
            composer.addTuplet("GGG",1/2.);
            composer.addTuplet("EEE",1/2.);
            composer.addTuplet("CCC",1/2.);
            composer.verifySection();
            
            composer.addOne('G',3/4.);
            composer.addOne('F',1/4.);
            composer.addOne('E',3/4.);
            composer.addOne('D',1/4.);
            composer.addOne('C',2);
            composer.verifySection();
           
            
            player.play();
            
            /* After implementing player,
             * We can compare this player with the player from reading piece*.abc.
             */
            
            // assertEquals(player.toString(), "C C C3/4 D/4 E | E3/4 D/4 E3/4 F/4 G2 | (3DDD (3GGG (3EEE (3CCC | G3/4 F/4 E3/4 D/4 C2 ||");
            

            /*
             * Note: A possible weird behavior of the Java sequencer: Even if the
             * sequencer has finished playing all of the scheduled notes and is
             * manually closed, the program may not terminate. This is likely
             * due to daemon threads that are spawned when the sequencer is
             * opened but keep on running even after the sequencer is killed. In
             * this case, you need to explicitly exit the program with
             * System.exit(0).
             */
            // System.exit(0);

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
    
    final static int SHARP=1;
    final static int FLAT=-1;
    
    @Test
    public void test_piece2()
    {
        try {
            player = new SequencePlayer(200, 24);
            composer = new Composer(player, 24, 4);

            // [eF^]/
            composer.addOne('E',1/2.,+Pitch.OCTAVE);
            composer.addHere('F',1/2.,SHARP);
            // [eF^]/
            composer.addOne('E',1/2.,+Pitch.OCTAVE);
            composer.addHere('F',1/2.,SHARP);
            // rest
            composer.addRest(1/2.);
            // [eF^]/
            composer.addOne('E',1/2.,+Pitch.OCTAVE);
            composer.addHere('F',1/2.,SHARP);
            // rest
            composer.addRest(1/2.);
            // [cF^]/
            composer.addOne('C',1/2.,+Pitch.OCTAVE);
            composer.addHere('F',1/2.,SHARP);
            // [eF^]/
            composer.addOne('E',1,+Pitch.OCTAVE);
            composer.addHere('F',1,SHARP);
            composer.verifySection();
            
            
            // [GBg]
            composer.addOne('G',1);
            composer.addHere('B',1);
            composer.addHere('G',1,+Pitch.OCTAVE);
            composer.addRest(1);
            composer.addOne('G', 1.0);
            composer.addRest(1);
            composer.verifySection();

            composer.addOne('C',3/2.,Pitch.OCTAVE);
            composer.addOne('G',1/2.);
            composer.addRest(1);
            composer.addOne('E',1);
            composer.addOne('E',1/2.);
            composer.addOne('A',1);
            composer.addOne('B',1);
            composer.addOne('B',1/2.,FLAT);
            composer.addOne('A',1);
            composer.verifySection();
            
            composer.addTuplet("GEG",1.,new int[]{0,Pitch.OCTAVE,Pitch.OCTAVE});
            composer.addOne('A',1,Pitch.OCTAVE);
            composer.addOne('F',1/2.,Pitch.OCTAVE);
            composer.addOne('G',1/2.,Pitch.OCTAVE);
            composer.verifySection();
            
            composer.addRest(1/2.);
            composer.addOne('E',1,Pitch.OCTAVE);
            composer.addOne('C',1/2.,Pitch.OCTAVE);
            composer.addOne('D',1/2.,Pitch.OCTAVE);

            composer.addOne('B',3/4., Pitch.OCTAVE);
            composer.padSection();
            composer.verifySection();

            player.play();
            
            /* After implementing player,
             * We can compare this player with the player from reading piece*.abc.
             */
            
            //assertEquals(player.toString(), "C C C3/4 D/4 E | E3/4 D/4 E3/4 F/4 G2 | (3DDD (3GGG (3EEE (3CCC | G3/4 F/4 E3/4 D/4 C2 ||");

            /*
             * Note: A possible weird behavior of the Java sequencer: Even if the
             * sequencer has finished playing all of the scheduled notes and is
             * manually closed, the program may not terminate. This is likely
             * due to daemon threads that are spawned when the sequencer is
             * opened but keep on running even after the sequencer is killed. In
             * this case, you need to explicitly exit the program with
             * System.exit(0).
             */
            // System.exit(0);

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}
