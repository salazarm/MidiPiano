package player;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.Test;

/***
 * Testing Strategy:
 * 
 * For testing files: We made 2 string buffers. One with whitespace and one without whitespace. We iterated over the output Tokens
 * and used our Token's getValue() method (equivalent to a toString() method) to build a string representing all
 * of the tokens. Essentially this should be equivalent to filtering out whitespace from the original string representation.
 * This makes the test accurate and reliable.
 * 
 * Other testing: We added tests to ensure correct lexing of the different abc token types, as well as the token types
 * in interaction with each other. Some regression tests were added (voices with repeats, newlines). In addition, tests
 * were added to ensure that the Lexer throws an exception upon encountering invalid tokens. 
 */
public class LexerTest extends Lexer
{ 
    public LexerTest()
    {
        super();
    }
    
    @Test
    public void testFiles(){
        // Tests the files in the array, comparing as stated above
    	String[] testFiles = {
    			"sample_abc/fur_elise.abc", 
    			"sample_abc/invention.abc", 
    			"sample_abc/little_night_music.abc", 
    			"sample_abc/paddy.abc", 
    			"sample_abc/piece1.abc", 
    			"sample_abc/piece2.abc", 
    			"sample_abc/prelude.abc", 
    			"sample_abc/scale.abc",
    			"sample_abc/ExtraTestFiles/tuples.abc",
    			"sample_abc/ExtraTestFiles/sample88.abc"};
    	for (String c: testFiles){
    		compare(c);
    	}
    }
    
	private void compare(String testFile){
        BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(testFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        StringBuffer sb = new StringBuffer();
        StringBuffer cb = new StringBuffer();
        char[] buf = new char[1024];
        int numRead;
        try {
			while((numRead = br.read(buf)) != -1){
			    sb.append(buf, 0, numRead);
			    if(Pattern.matches("\\s", ""+numRead)){
			    	cb.append(buf,0,numRead);
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        String result = cb.toString();
        String lexToString = "";
        Lexer l = new Lexer(sb.toString());
        ArrayList<Token> headerTokens = l.getHeader();
        ArrayList<Token> bodyTokens = l.getBody();
        for (int i =0; i<headerTokens.size(); i++) {
        	lexToString.concat(headerTokens.get(i).getValue());
        }
        for (int i = 0; i<bodyTokens.size(); i++) {
        	lexToString.concat(bodyTokens.get(i).getValue());
        }
        assertEquals(lexToString,result);
	}
    
	// The next few methods test illegal headers, whether due to invalid tokens, invalid start 
	// or invalid end.
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
        new Lexer(inp);
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
    
    // This test ensures that a correct header is lexed correctly.
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
        assertEquals("test case3: X, T, M, L, K","3",list.get(0).getValue());
        assertEquals("test case3: X, T, M, L, K","Turkish March",list.get(1).getValue());
        assertEquals("test case3: X, T, M, L, K","W. Mozart",list.get(2).getValue());
        assertEquals("test case3: X, T, M, L, K","2/4",list.get(3).getValue());
        assertEquals("test case3: X, T, M, L, K","1/8",list.get(4).getValue());
        assertEquals("test case3: X, T, M, L, K","Am",list.get(5).getValue());
        
        list = processHeader("X:  25 \nT: Mr. title.special!character \n L: 4/2 \nM: 1/16\nK:  Cm \n\n");
        assertEquals("test case4: X, T, L, M, K",5,list.size());
        assertEquals("test case4: X, T, L, M, K","25",list.get(0).getValue());
        assertEquals("test case4: X, T, L, M, K","Mr. title.special!character",list.get(1).getValue());
        assertEquals("test case4: X, T, L, M, K","4/2",list.get(2).getValue());
        assertEquals("test case4: X, T, L, M, K","1/16",list.get(3).getValue());
        assertEquals("test case4: X, T, L, M, K","Cm",list.get(4).getValue());
        
        // Voice test
        list = processHeader(" X:  25 \nT: Mr. title \nV: v1 \nV:  v23 42v\nL: 4/2 \nM: 1/16\nK:  Cm \n\n");
        assertEquals("test case4: X, T, V, V, L, M, K",7,list.size());
        assertEquals("test case4: X, T, V, V, L, M, K","v1",list.get(2).getValue());
        assertEquals("test case4: X, T, V, V, L, M, K","v23 42v",list.get(3).getValue());
    }
    
    // Test for invalid note
    @Test(expected = RuntimeException.class)
    public void testProcessBody_wrongNote1()
    {
        processBody("A|B|C|Z||");
    }
    
    // Basic test of body lexing
    @Test
    public void testProcessBodyBasic()
    {
        List<Token> list;
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
    }
    
    // Test to ensure rest multiplers and newlines are handled correctly
    @Test
    public void testRestMultiplierNewline(){
    	List<Token> list;
        list = processBody(" \n z A3| B/1 \n g3 |C1/ a3 C1/ |\nD/ z1/2 z/ z/4 z/ \n z1/ z/4 z16/16|] ");
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
        assertEquals("body test2: 30",Token.Type.REST,list.get(30).getType());
        assertEquals("body test2: 30","z",list.get(30).getValue());
        assertEquals("body test2: 31",Token.Type.NOTEMULTIPLIER,list.get(31).getType());
        assertEquals("body test2: 31","16/16",list.get(31).getValue());
        assertEquals("body test2: 32",Token.Type.ENDMAJORSECTION,list.get(32).getType());
        assertEquals("body test2: 32","|]",list.get(32).getValue());
        assertEquals("body test2: # token",33,list.size());
    }
    
    // Test to ensure chords, tuplets and note modifiers (accidentals, octaves) are lexed correctly
    public void testAccidentalOctaveChordsTuplets(){    
    	List<Token> list;
        list = processBody(" [^c'__a'EG]5/ | [_e'__a'EG,]/ | (4^g''__A,,^B'' | (2G,/16A1/16 (3a4a4a4 ||");
        assertEquals("body test3: 0",Token.Type.CHORDSTART,list.get(0).getType());
        assertEquals("body test3: 1",Token.Type.ACCIDENTAL,list.get(1).getType());
        assertEquals("body test3: 2",Token.Type.BASENOTE,list.get(2).getType());
        assertEquals("body test3: 3",Token.Type.OCTAVE,list.get(3).getType());
        assertEquals("body test3: 4",Token.Type.ACCIDENTAL,list.get(4).getType());
        assertEquals("body test3: 4","_",list.get(4).getValue());
        assertEquals("body test3: 10",Token.Type.CHORDEND,list.get(10).getType());
        assertEquals("body test3: 10","]",list.get(10).getValue());
        assertEquals("body test3: 11",Token.Type.NOTEMULTIPLIER,list.get(11).getType());
        assertEquals("body test3: 11","5/",list.get(11).getValue());
        assertEquals("body test3: 14",Token.Type.ACCIDENTAL,list.get(14).getType());
        assertEquals("body test3: 16",Token.Type.OCTAVE,list.get(16).getType());
        assertEquals("body test3: 17",Token.Type.ACCIDENTAL,list.get(17).getType());
        assertEquals("body test3: 17","_",list.get(17).getValue());
        assertEquals("body test3: 19",Token.Type.BASENOTE,list.get(19).getType());
        assertEquals("body test3: 19","a",list.get(19).getValue());
        assertEquals("body test3: 27",Token.Type.TUPLET,list.get(27).getType());
        assertEquals("body test3: 27","(4",list.get(27).getValue());
        assertEquals("body test3: 30",Token.Type.OCTAVE,list.get(30).getType());
        assertEquals("body test3: 30","'",list.get(30).getValue());
        assertEquals("body test3: 42",Token.Type.TUPLET,list.get(42).getType());
        assertEquals("body test3: 42","(2",list.get(42).getValue());
        assertEquals("body test3: 49",Token.Type.BASENOTE,list.get(49).getType());
        assertEquals("body test3: 49","a",list.get(49).getValue());
        assertEquals("body test3: # token",56,list.size());
    }
    
    // Tests to ensure voices are lexed correctly
    public void testMultipleVoicesRepeats(){
    	List<Token> list;
        list = processBody(" A B C |: C D E | a b c | c d e :| e e e |]\nV: new voice\n"
                + "C B A |: E D C |[1 E E E :|[2 F F F ||");
        
        assertEquals("body test4: 3",Token.Type.REPEATSTART,list.get(3).getType());
        assertEquals("body test4: 3","|:",list.get(3).getValue());
        assertEquals("body test4: 15",Token.Type.REPEATEND,list.get(15).getType());
        assertEquals("body test4: 15",Token.Type.REPEATEND,list.get(15).getType());
        assertEquals("body test4: 20",Token.Type.VOICE,list.get(20).getType());
        assertEquals("body test4: 20","V: new voice",list.get(20).getValue());
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
