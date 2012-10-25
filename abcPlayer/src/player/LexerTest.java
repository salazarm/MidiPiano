package player;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * LexerTest
 * To test private methods, it extends Lexer
 * 
 */
public class LexerTest extends Lexer
{ 
    public LexerTest()
    {
        super();
    }
    
	@Test
	public void testString(){
		System.out.println(Pattern.matches("\\A[/0-9].*","2 z"));
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		StringBuffer sb = new StringBuffer();
		try {
		fis = new FileInputStream("sample_abc/fur_elise.abc");
		bis = new BufferedInputStream(fis);
		dis = new DataInputStream(bis);

		while (dis.available() != 0) {
		sb.append(dis.readLine());
		sb.append("\n");
		}
		fis.close();
		bis.close();
		dis.close();

		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
		String input = sb.toString();
		Lexer result = new Lexer(input);
//		for (Token c: result.getBody()){
//			System.out.println(c.getType() +":"+c.getValue());
//		}
//		for (Token c: result.getHeader()){
//			System.out.println(c.getValue());
//		}
	}
    
    @Test(expected = RuntimeException.class)
    public void testProcessHeader_wrongHeader1()
    {
        processHeader(":");
    }
    @Test(expected = RuntimeException.class)
    public void testProcessHeader_wrongHeader2()
    {
        processHeader("Z:A");
    }
    
    @Test(expected = RuntimeException.class)
    public void testProcessHeader_wrongHeader_oneLine()
    {
        processHeader("X:2 T:title K:C");
    }
    @Test(expected = RuntimeException.class)
    public void testProcessHeader_wrongHeader_notInOrder()
    {
        processHeader("K:G\nT:title\nX:4");
    }
    @Test(expected = RuntimeException.class)
    public void testProcessHeader_wrongHeader_onlyTwo()
    {
        processHeader("T:title\nX:4\n");
    }
    @Test(expected = RuntimeException.class)
    public void testProcessHeader_wrongHeader_NotEndedWithKey()
    {
    	String inp = "X:9\nT:title\nK:D\nM:2/4";
        Lexer l = new Lexer(inp);
    }
    @Test(expected = RuntimeException.class)
    public void testProcessHeader_wrongHeader_NotCapital()
    {
        processHeader("X:9\nT:title\nm:2/4\nK:Cm");
    }
    @Test(expected = RuntimeException.class)
    public void testProcessHeader_wrongHeader_wrongSpacicing()
    {
        processHeader("X:9\nT:title\nm:2 0/4\nK:C");
    }
    @Test(expected = RuntimeException.class)
    public void testProcessHeader_wrongHeader_noDenominator()
    {
        processHeader("X:9\nT:title\nM:5/\nK:C");
    }
    
    @Test
    public void testProcessHeader_correct()
    {
        List<Token> list;
        
        list = processHeader("X:2\nT:title\nK:C");
        assertEquals("test case1: X, T, K with no space",3,list.size());
        
        list = processHeader("X: 2\nT: title \n K: C");
        assertEquals("test case2: X, T, K with space",3,list.size());
        
        // Because of the free spacing in Title and Composer,
        // We should extract just after ':' until the end of the line.
        list = processHeader("X: 3\nT: Turkish March\nC: W. Mozart\nM: 2/4\nL: 1/8\nK: Am");
        assertEquals("test case3: X, T, M, L, K",6,list.size());
        assertEquals("test case3: X, T, M, L, K"," 3",list.get(0).getValue());
        assertEquals("test case3: X, T, M, L, K"," Turkish March",list.get(1).getValue());
        assertEquals("test case3: X, T, M, L, K"," W. Mozart",list.get(2).getValue());
        assertEquals("test case3: X, T, M, L, K"," 2/4",list.get(3).getValue());
        assertEquals("test case3: X, T, M, L, K"," 1/8",list.get(4).getValue());
        assertEquals("test case3: X, T, M, L, K"," Am",list.get(5).getValue());
        
        list = processHeader("X:  25 \nT: Mr. title.special!character \n L: 4/2 \nM: 1/16\nK:  Cm \n\n");
        assertEquals("test case4: X, T, L, M, K",5,list.size());
        assertEquals("test case4: X, T, L, M, K","  25 ",list.get(0).getValue());
        assertEquals("test case4: X, T, L, M, K"," Mr. title.special!character ",list.get(1).getValue());
        assertEquals("test case4: X, T, L, M, K"," 4/2 ",list.get(2).getValue());
        assertEquals("test case4: X, T, L, M, K"," 1/16",list.get(3).getValue());
        assertEquals("test case4: X, T, L, M, K","  Cm ",list.get(4).getValue());
        
        // Voice test
        list = processHeader(" X:  25 \nT: Mr. title \nV: v1 \nV:  v23 42v\nL: 4/2 \nM: 1/16\nK:  Cm \n\n");
        assertEquals("test case4: X, T, V, V, L, M, K",7,list.size());
        assertEquals("test case4: X, T, V, V, L, M, K"," v1 ",list.get(2).getValue());
        assertEquals("test case4: X, T, V, V, L, M, K","  v23 42v",list.get(3).getValue());
    }
    
    @Test(expected = RuntimeException.class)
    public void testProcessBody_wrongNote1()
    {
        processBody("A|B|C|Z||");
    }
    @Test
    public void testProcessBody_correct()
    {
        List<Token> list;
        
        // Basic
        list = processBody("  A| B |C|D|| ");
        assertEquals("body test1: 0",Token.Type.BASENOTE,list.get(0).getType());
        assertEquals("body test1: 0","A",list.get(0).getValue());
        assertEquals("body test1: 1",Token.Type.BARLINE,list.get(1).getType());
        assertEquals("body test1: 1","|",list.get(1).getValue());
        assertEquals("body test1: 2",Token.Type.BASENOTE,list.get(2).getType());
        assertEquals("body test1: 2","B",list.get(2).getValue());
        assertEquals("body test1: 3",Token.Type.BARLINE,list.get(3).getType());
        assertEquals("body test1: 4",Token.Type.BASENOTE,list.get(4).getType());
        assertEquals("body test1: 4","C",list.get(4).getValue());
        assertEquals("body test1: 5",Token.Type.BARLINE,list.get(5).getType());
        assertEquals("body test1: 5","|",list.get(5).getValue());
        assertEquals("body test1: 6",Token.Type.BASENOTE,list.get(6).getType());
        assertEquals("body test1: 6","D",list.get(6).getValue());
        assertEquals("body test1: 7",Token.Type.ENDMAJORSECTION,list.get(7).getType());
        assertEquals("body test1: 7","||",list.get(7).getValue());
        assertEquals("body test1: # token",8,list.size());

        // Rest / Multiplier / newline test
        //                        4            + 5        + 7                           + 17 = 33
        list = processBody(" \n z A3| B1/1 \n g3 |C1/ a3 C1/ |\nD/ z1/2 z/ z/4 z/ \n z1/ z/4 z16/16|] ");
        assertEquals("body test2: 0",Token.Type.REST,list.get(0).getType());
        assertEquals("body test2: 0","z",list.get(0).getValue());
        assertEquals("body test2: 1",Token.Type.BASENOTE,list.get(1).getType());
        assertEquals("body test2: 1","A",list.get(1).getValue());
        assertEquals("body test2: 2",Token.Type.NOTEMULTIPLIER,list.get(2).getType());
        assertEquals("body test2: 2","3",list.get(2).getValue());
        assertEquals("body test2: 3",Token.Type.BARLINE,list.get(3).getType());
        assertEquals("body test2: 3","|",list.get(3).getValue());
        assertEquals("body test2: 5",Token.Type.NOTEMULTIPLIER,list.get(5).getType());
        assertEquals("body test2: 5","/1",list.get(5).getValue());
        assertEquals("body test2: 7",Token.Type.NOTEMULTIPLIER,list.get(7).getType());
        assertEquals("body test2: 7","3",list.get(7).getValue());
        assertEquals("body test2: 10",Token.Type.NOTEMULTIPLIER,list.get(10).getType());
        assertEquals("body test2: 10","1/",list.get(10).getValue());
        assertEquals("body test2: 31",Token.Type.NOTEMULTIPLIER,list.get(31).getType());
        assertEquals("body test2: 31","z",list.get(31).getValue());
        assertEquals("body test2: 32",Token.Type.NOTEMULTIPLIER,list.get(32).getType());
        assertEquals("body test2: 32","16/16",list.get(32).getValue());
        assertEquals("body test2: 33",Token.Type.ENDMAJORSECTION,list.get(33).getType());
        assertEquals("body test2: 33","|]",list.get(33).getValue());
        assertEquals("body test2: # token",34,list.size());
        
        
        // Accidental / Octave / Chords / Tuplets
        list = processBody("[^c'__a'EG]5/ | [_e'__a'EG,]/ | (4^g''__A,,^B'' | (2G,/16A1/16 (3a4a4a4 ||");
        assertEquals("body test3: 0",Token.Type.CHORDSTART,list.get(0).getType());
        assertEquals("body test3: 1",Token.Type.ACCIDENTAL,list.get(1).getType());
        assertEquals("body test3: 2",Token.Type.BASENOTE,list.get(2).getType());
        assertEquals("body test3: 3",Token.Type.OCTAVE,list.get(3).getType());
        assertEquals("body test3: 4",Token.Type.ACCIDENTAL,list.get(4).getType());
        assertEquals("body test3: 4","__",list.get(4).getValue());
        assertEquals("body test3: 9",Token.Type.CHORDEND,list.get(9).getType());
        assertEquals("body test3: 9","]",list.get(9).getValue());
        assertEquals("body test3: 10",Token.Type.NOTEMULTIPLIER,list.get(10).getType());
        assertEquals("body test3: 10","5/",list.get(10).getValue());
        assertEquals("body test3: 13",Token.Type.ACCIDENTAL,list.get(13).getType());
        assertEquals("body test3: 15",Token.Type.ACCIDENTAL,list.get(15).getType());
        assertEquals("body test3: 16",Token.Type.ACCIDENTAL,list.get(16).getType());
        assertEquals("body test3: 16","__",list.get(16).getValue());
        assertEquals("body test3: 17",Token.Type.BASENOTE,list.get(17).getType());
        assertEquals("body test3: 17","a",list.get(17).getValue());
        assertEquals("body test3: 25",Token.Type.TUPLET,list.get(25).getType());
        assertEquals("body test3: 25","(4",list.get(25).getValue());
        assertEquals("body test3: 28",Token.Type.ACCIDENTAL,list.get(28).getType());
        assertEquals("body test3: 28","''",list.get(28).getValue());
        assertEquals("body test3: 36",Token.Type.TUPLET,list.get(36).getType());
        assertEquals("body test3: 36","(2",list.get(36).getValue());
        assertEquals("body test3: 42",Token.Type.TUPLET,list.get(42).getType());
        assertEquals("body test3: 42","(3",list.get(42).getValue());
        assertEquals("body test3: # token",50,list.size());
        
        // Multiple visitor / Repeats 
        list = processBody("A B C |: C D E | a b c | c d e :| e e e |]\nV: new voice\n"
                + "C B A |: E D C |[1 E E E :|[2 F F F ||");
        assertEquals("body test4: 3",Token.Type.REPEATSTART,list.get(3).getType());
        assertEquals("body test4: 3","|:",list.get(3).getValue());
        assertEquals("body test4: 15",Token.Type.REPEATEND,list.get(15).getType());
        assertEquals("body test4: 15",Token.Type.REPEATEND,list.get(15).getType());
        assertEquals("body test4: 20",Token.Type.VOICE,list.get(20).getType());
        assertEquals("body test4: 20"," new voice",list.get(20).getValue());
        assertEquals("body test4: 28",Token.Type.BARLINE,list.get(28).getType());
        assertEquals("body test4: 28","|",list.get(28).getValue());
        assertEquals("body test4: 29",Token.Type.REPEATSECTION,list.get(29).getType());
        assertEquals("body test4: 29","[1",list.get(29).getValue());
        assertEquals("body test4: 33",Token.Type.REPEATEND,list.get(33).getType());
        assertEquals("body test4: 33",":|",list.get(33).getValue());
        assertEquals("body test4: 34",Token.Type.REPEATSECTION,list.get(34).getType());
        assertEquals("body test4: 34","[2",list.get(34).getValue());
        
    }
}
