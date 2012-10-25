package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

import datatypes.Player;

/**
 * Main entry point of your application.
 */
public class Main {

	/**
	 * Plays the input file using Java MIDI API and displays
	 * header information to the standard output stream.
	 * 
	 * <p>Your code <b>should not</b> exit the application abnormally using
	 * System.exit()</p>
	 * 
	 * @param path the name of input abc file
	 */
    public static String readFile(String path) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(path));
        StringBuffer sb = new StringBuffer();
        char[] buf = new char[1024];
        int numRead;

        while((numRead = br.read(buf)) != -1 )
            sb.append(buf, 0, numRead);
        
        br.close();
        
        return sb.toString();
    }
	public static void play(String file) throws IOException, MidiUnavailableException {
	    Lexer lexer = new Lexer(readFile(file));
	    Parser parser = new Parser(lexer);
	    Player player = parser.parse();
	    player.schedule();
	    System.out.printf("%s", player.getHeader().toString());
	    player.play();	  
	}
	
	/////// JUnit Tests ///////
	@Test(expected = IOException.class)
	public void testReadFile_wrongPath() throws IOException
	{
	    readFile("non_exist_hello_abc.abc");
	}
//	@Test
    public void testReadFile_sampleABC() throws IOException
    {
        String str;
        str = readFile("sample_abc/prelude.abc");
        assertEquals("len(prelude.abc) != len(readFile())", 1870, str.length());
        assertTrue("wrong prefix from readFile(prelude.abc)", str.startsWith("X:8628\nT:Prelude BWV 846 no. 1\nC:Johann Sebastian Bach"));
        assertTrue("wrong suffix from readFile(prelude.abc)", str.endsWith("V:3\nC,,16|C,,16|]\n"));
    }
	
	@Test(expected = IOException.class)
    public void testPlay_wrongPath() throws IOException, MidiUnavailableException
    {
        play("non_exist_hello_abc.abc");
    }
	
	@Test
    public void testPlay_playPiece1() throws IOException, MidiUnavailableException
    {
		play("sample_abc/ExtraTestFiles/octaves.abc");
		//play("sample_abc/fur_elise.abc");
	    //play("sample_abc/piece1.abc");
		//play("sample_abc/piece2.abc");
		//play("sample_abc/prelude.abc");
	    //play("sample_abc/scale.abc");
        //play("sample_abc/little_night_music.abc");
	    //play("sample_abc/invention.abc");
	    //play("sample_abc/paddy.abc");
    }

	//@Test
    public void testPlay_sampleABC() throws IOException, MidiUnavailableException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
        play("sample_abc/prelude.abc");
        
        
        assertEquals(
            "X:8628\n"+
            "T:Prelude BWV 846 no. 1\n"+
            "C:Johann Sebastian Bach\n"+
            "M:4/4\n"+
            "L:1/16\n"+
            "Q:280\n"+
            "V:1\n"+
            "V:2\n"+
            "V:3\n"+
            "K:C\n", out.toString());
        assertEquals("", err.toString());
        System.setOut(null);
        System.setErr(null);
    }
}
