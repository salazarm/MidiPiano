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
    static class Composer
    {
        int t = 0;
        int unit;
        SequencePlayer player;
        Composer(SequencePlayer player, int unit)
        {
            this.player= player;
            this.unit = unit;
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
        public void addTuplet(String pitches)
        {
            int i=0,len = unit / pitches.length();
            for(char pitch : pitches.toCharArray())
            {
                player.addNote(new Pitch(pitch).toMidiNote(), t, len);
                t += len;
                i++;
            }
        }
        public void addTuplet(String pitches, int transpose[])
        {
            int i=0,len = unit / pitches.length();
            for(char pitch : pitches.toCharArray())
            {
                player.addNote(new Pitch(pitch).transpose(transpose[i]).toMidiNote(), t, len);
                t += len;
                i++;
            }
        }
        public void addRest(double length)
        {
            t+=unit * length;
        }
    }
    
    SequencePlayer player;
    Composer composer;

    @Test
    public void test_piece1()
    {
        try {
            // there is no special meaning in 24,
            // but all note lengths should be a divisor of 24.
            player = new SequencePlayer(120, 24);
            composer = new Composer(player, 24);

            composer.addOne('C',1);
            composer.addOne('C',1);
            composer.addOne('C',3/4.);
            composer.addOne('D',1/4.);
            composer.addOne('E',1);
            
            composer.addOne('E',3/4.);
            composer.addOne('D',1/4.);
            composer.addOne('E',3/4.);
            composer.addOne('F',1/4.);
            composer.addOne('G',2);
            
            composer.addTuplet("DDD",new int[]{Pitch.OCTAVE,Pitch.OCTAVE,Pitch.OCTAVE});
            composer.addTuplet("GGG");
            composer.addTuplet("EEE");
            composer.addTuplet("CCC");
            
            composer.addOne('G',3/4.);
            composer.addOne('F',1/4.);
            composer.addOne('E',3/4.);
            composer.addOne('D',1/4.);
            composer.addOne('C',2);

            //assertEquals(player.toString(), "C C C3/4 D/4 E | E3/4 D/4 E3/4 F/4 G2 | (3DDD (3GGG (3EEE (3CCC | G3/4 F/4 E3/4 D/4 C2 ||");            
            
            player.play();

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
            composer = new Composer(player, 24);

            // [eF^]/
            composer.addOne('E',1/2.,+Pitch.OCTAVE);
            composer.addHere('F',1/2.,SHARP);
            // [eF^]/
            composer.addOne('E',1/2.,+Pitch.OCTAVE);
            composer.addHere('F',1/2.,SHARP);
            // rest
            composer.addRest(1);
            // [eF^]/
            composer.addOne('E',1/2.,+Pitch.OCTAVE);
            composer.addHere('F',1/2.,SHARP);
            // rest
            composer.addRest(1);
            // [cF^]/
            composer.addOne('C',1/2.,+Pitch.OCTAVE);
            composer.addHere('F',1/2.,SHARP);
            // [eF^]/
            composer.addOne('E',1/2.,+Pitch.OCTAVE);
            composer.addHere('F',1/2.,SHARP);
            // [GBg]
            composer.addOne('G',1);
            composer.addHere('B',1);
            composer.addHere('G',1,+Pitch.OCTAVE);
            // rest
            composer.addRest(1);
            // G
            composer.addOne('G', 1.0);
            // rest
            composer.addRest(1);

            
            composer.addOne('C',3/2.,Pitch.OCTAVE);
            composer.addOne('G',1/2.);
            composer.addRest(1);
            composer.addOne('E',1);
            composer.addOne('E',1/2.);
            composer.addOne('A',1);
            composer.addOne('B',1);
            composer.addOne('B',1/2.,FLAT);
            composer.addOne('A',1);
            
            composer.addTuplet("GEG",new int[]{0,Pitch.OCTAVE,Pitch.OCTAVE});
            composer.addOne('A',1,Pitch.OCTAVE);
            composer.addOne('F',1/2.,Pitch.OCTAVE);
            composer.addOne('G',1/2.,Pitch.OCTAVE);
            composer.addRest(1/2.);
            composer.addOne('E',1);
            composer.addOne('C',1/2.,Pitch.OCTAVE);
            composer.addOne('D',1/2.,Pitch.OCTAVE);
            composer.addOne('C',3/4.,Pitch.OCTAVE);

//            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 7, 1);
            //assertEquals(player.toString(), "C C C3/4 D/4 E | E3/4 D/4 E3/4 F/4 G2 | (3DDD (3GGG (3EEE (3CCC | G3/4 F/4 E3/4 D/4 C2 ||");            
            
            player.play();

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
