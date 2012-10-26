package player;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

import sound.Pitch;
import datatypes.Note;
import datatypes.Player;

public class ParserTest
{
    Parser parser;
    Player player;
    ArrayList<Token> basicHeader;
    public ParserTest()
    {
        basicHeader = new ArrayList<Token>();
        basicHeader.add(new Token("X","2"));
        basicHeader.add(new Token("T","title"));
        basicHeader.add(new Token("Q","200"));
        basicHeader.add(new Token("M","4/4"));
        basicHeader.add(new Token("K","C"));
    }
    
    
    /**
     * class FakeLexer is used to create a mock Lexer,
     * for testing Parser class regardless of Lexer class's validity,
     * 
     * Its behavior is same with Lexer except it uses the tokenized abc data given by caller. 
     */
    class FakeLexer extends Lexer
    {
        /**
         * Use the given header and body token lists as lexed results.
         * 
         * @param header a list of lexed header tokens.  
         * @param body a list of lexed body tokens.
         */
        FakeLexer(ArrayList<Token> header, ArrayList<Token> body)
        {
            this.headerTokens = header;
            this.bodyTokens = body;
        }
    }
    
    @Test
    public void testParser_correctKeySignature1() throws MidiUnavailableException
    {
        ArrayList<Token> header, body;
        
        header = new ArrayList<Token>();
        header.add(new Token("X","2"));
        header.add(new Token("T","title"));
        header.add(new Token("Q","200"));
        header.add(new Token("M","4/4"));
        header.add(new Token("K","Eb")); // A, B, E flat
        
        body = new ArrayList<Token>();
        body.add(new Token("A","A"));
        body.add(new Token("B","B"));
        body.add(new Token("C","C"));
        body.add(new Token("D","D"));
        body.add(new Token("E","E")); // 4
        body.add(new Token("^","^"));
        body.add(new Token("E","E")); // 5
        body.add(new Token("E","E")); // 6
        body.add(new Token("=","="));
        body.add(new Token("E","E")); // 7
        body.add(new Token("|","|"));
        body.add(new Token("E","E")); // 8

        parser = new Parser(new FakeLexer(header, body));
        player = parser.parse();
        Note n;
        
        n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(0);
        assertEquals(n.getNotePitch(), (new Pitch('A').accidentalTranspose(-1)));
        n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(1);
        assertEquals(n.getNotePitch(), (new Pitch('B').accidentalTranspose(-1)));
        n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(2);
        assertEquals(n.getNotePitch(), (new Pitch('C').accidentalTranspose(0)));
        n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(3);
        assertEquals(n.getNotePitch(), (new Pitch('D').accidentalTranspose(0)));
        n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(4);
        assertEquals(n.getNotePitch(), (new Pitch('E').accidentalTranspose(-1)));
        n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(5);
        assertEquals(n.getNotePitch(), (new Pitch('E').accidentalTranspose(+1)));
        n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(6);
        assertEquals(n.getNotePitch(), (new Pitch('E').accidentalTranspose(+1)));
        n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(7);
        assertEquals(n.getNotePitch(), (new Pitch('E').accidentalTranspose(+0)));
        n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(8);
        assertEquals(n.getNotePitch(), (new Pitch('E').accidentalTranspose(-1)));
    }

    @Test
    public void testParser_correctAccidental1() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        body.add(new Token("_","_"));
        body.add(new Token("_","_"));
        body.add(new Token("c","c"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
        Note n = (Note) player.getBody().getVoiceList().get(0).getMusicSequences().get(0);
        assertEquals(n.getNotePitch(), (new Pitch('C').octaveTranspose(1).accidentalTranspose(-2)));
    }
    @Test
    public void testParser_correctAccidental2() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        body.add(new Token("=","="));
        body.add(new Token("c","c"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
    }
    
    @Test (expected=RuntimeException.class)
    public void testParser_wrongAccidental1() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        body.add(new Token("_","_"));
        body.add(new Token("^","^"));
        body.add(new Token("c","c"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
    }
    @Test (expected=RuntimeException.class)
    public void testParser_wrongAccidental2() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        body.add(new Token("_","_"));
        body.add(new Token("_","_"));
        body.add(new Token("_","_"));
        body.add(new Token("a","a"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
    }
    @Test (expected=RuntimeException.class)
    public void testParser_wrongAccidental3() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        body.add(new Token("a","a"));
        body.add(new Token("^","^"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
    }
    @Test (expected=RuntimeException.class)
    public void testParser_wrongAccidental4() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        body.add(new Token("^","^"));
        body.add(new Token("z","z"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
    }
    @Test (expected=RuntimeException.class)
    public void testParser_wrongAccidental5() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        body.add(new Token("=","="));
        body.add(new Token("=","="));
        body.add(new Token("z","z"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
    }
    @Test (expected=RuntimeException.class)
    public void testParser_wrongOctave1() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        body.add(new Token(",",","));
        body.add(new Token("a","a"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
    }
    @Test (expected=RuntimeException.class)
    public void testParser_wrongOctave2() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        
        body.add(new Token("d","d"));
        body.add(new Token(",",","));
        body.add(new Token("'","'"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
    }
    @Test (expected=RuntimeException.class)
    public void testParser_wrongOctave3() throws MidiUnavailableException
    {
        ArrayList<Token> body;
        
        body = new ArrayList<Token>();
        
        body.add(new Token("z","z"));
        body.add(new Token("'","'"));

        parser = new Parser(new FakeLexer((ArrayList<Token>)basicHeader.clone(), body));
        player = parser.parse();
    }
    
    @Test
    public void testParser_simple1() throws MidiUnavailableException
    {
        ArrayList<Token> header, body;
        
        header = new ArrayList<Token>();
        header.add(new Token("X","1"));
        header.add(new Token("T","title"));
        header.add(new Token("Q","100"));
        header.add(new Token("M","3/2"));
        header.add(new Token("L","1/1"));
        header.add(new Token("K","C"));
        body = new ArrayList<Token>();
        body.add(new Token("A","A"));
        body.add(new Token("/2","/2"));
        body.add(new Token("B","B"));
        body.add(new Token("/2","/2"));
        body.add(new Token("C","C"));
        body.add(new Token("/2","/2"));
        body.add(new Token("||","||"));
        parser = new Parser(new FakeLexer(header, body));
        player = parser.parse();
        player.schedule();
        player.play();
    }
    
    @Test
    public void testParser_simple2() throws MidiUnavailableException
    {
        ArrayList<Token> header, body;
        
        header = new ArrayList<Token>();
        header.add(new Token("X","2"));
        header.add(new Token("T","title"));
        header.add(new Token("Q","200"));
        header.add(new Token("K","C"));
        body = new ArrayList<Token>();
        body.add(new Token("A","A"));
        body.add(new Token("B","B"));
        body.add(new Token("C","C"));
        body.add(new Token("D","D"));
        body.add(new Token("|","|"));
        body.add(new Token("A","A"));
        body.add(new Token("B","B"));
        body.add(new Token("C","C"));
        body.add(new Token("D","D"));
        body.add(new Token("||","||"));
        parser = new Parser(new FakeLexer(header, body));
        player = parser.parse();
        player.schedule();
        player.play();
    }
    
    @Test
    public void testParser_simple3() throws MidiUnavailableException
    {
        ArrayList<Token> header, body;
        
        header = new ArrayList<Token>();
        header.add(new Token("X","2"));
        header.add(new Token("T","title"));
        header.add(new Token("Q","200"));
        header.add(new Token("M","4/8"));
        header.add(new Token("V","piano"));
        header.add(new Token("V","drum"));
        header.add(new Token("K","C"));
        body = new ArrayList<Token>();
        body.add(new Token("1V:piano","V:piano"));
        body.add(new Token("A","A"));
        body.add(new Token("B","B"));
        body.add(new Token("C","C"));
        body.add(new Token("A","A"));
        body.add(new Token("||","||"));
        body.add(new Token("1V:drum","V:drum"));
        body.add(new Token("B","B"));
        body.add(new Token("A","A"));
        body.add(new Token("B","B"));
        body.add(new Token("C","C"));
        body.add(new Token("||","||"));
        parser = new Parser(new FakeLexer(header, body));
        player = parser.parse();
        player.schedule();
        player.play();
    }
    @Test
    public void testParser_repeat1() throws MidiUnavailableException
    {
        ArrayList<Token> header, body;
        
        header = new ArrayList<Token>();
        header.add(new Token("X","2"));
        header.add(new Token("T","title"));
        header.add(new Token("Q","200"));
        header.add(new Token("M","4/4"));
        header.add(new Token("K","C"));
        
        body = new ArrayList<Token>();
        body.add(new Token("A","A"));
        body.add(new Token("C","C"));
        body.add(new Token("c","c"));
        body.add(new Token("||","||"));
        body.add(new Token("c","c"));
        body.add(new Token("C","C"));
        body.add(new Token("c","c"));
        body.add(new Token("|:","|:"));
        body.add(new Token("D","D"));
        body.add(new Token("D","D"));
        body.add(new Token("D","D"));
        body.add(new Token("|","|"));
        body.add(new Token("f","f"));
        body.add(new Token("f","f"));
        body.add(new Token("f","f"));
        body.add(new Token(":|",":|"));
        body.add(new Token("A","A"));

        parser = new Parser(new FakeLexer(header, body));
        player = parser.parse();
        player.schedule();
        player.play();
        assertEquals(19,player.getBody().getVoiceList().get(0).getMusicSequences().size());
    }
    @Test
    public void testParser_repeat2() throws MidiUnavailableException
    {
        ArrayList<Token> header, body;
        
        header = new ArrayList<Token>();
        header.add(new Token("X","2"));
        header.add(new Token("T","title"));
        header.add(new Token("Q","200"));
        header.add(new Token("M","4/4"));
        header.add(new Token("K","C"));
        
        body = new ArrayList<Token>();
        body.add(new Token("A","A"));
        body.add(new Token("C","C"));
        body.add(new Token(",",","));
        body.add(new Token(",",","));
        body.add(new Token("c","c"));
        body.add(new Token("c","c"));
        body.add(new Token("|]","|]"));
        body.add(new Token("C","C"));
        body.add(new Token("(2","(2"));
        body.add(new Token("_","_"));
        body.add(new Token("_","_"));
        body.add(new Token("A","A"));
        body.add(new Token("B","B"));
        body.add(new Token("[1","[1"));
        body.add(new Token("D","D"));
        body.add(new Token("[2","[2"));
        body.add(new Token("E","E"));
        body.add(new Token(":|",":|"));
        body.add(new Token("f","f"));
        body.add(new Token("|:","|:"));
        body.add(new Token("f","f"));
        body.add(new Token(":|",":|"));

        parser = new Parser(new FakeLexer(header, body));
        player = parser.parse();
        player.schedule();
        player.play();
        
        assertEquals(14,player.getBody().getVoiceList().get(0).getMusicSequences().size());
    }
}
