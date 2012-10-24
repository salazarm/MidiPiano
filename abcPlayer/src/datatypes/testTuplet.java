package datatypes;

import java.util.ArrayList;

import org.junit.Test;

public class testTuplet
{
    Tuplet tuplet;
    ArrayList<Note> notes;
    @Test
    public void testDuration_2notes()
    {
        notes = new ArrayList<Note>();
        notes.add(new Note('A', 0, new Accidental(Accidental.Type.FLAT, "="), 5.0));
        notes.add(new Note('B', 0, new Accidental(Accidental.Type.FLAT, "="), 5.0));
        tuplet = new Tuplet(2, notes);
        //tuplet.getDuration();
    }
    
    @Test
    public void testDuration_3notes()
    {
        
    }
    
    @Test
    public void testDuration_4notes()
    {
        
    }
}
