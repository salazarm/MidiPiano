package player;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

import datatypes.Player;

public class ParserTest
{
    Parser parser;
    Player player;
    
    class FakeLexer extends Lexer
    {
        FakeLexer(ArrayList<Token> header, ArrayList<Token> body)
        {
            this.headerTokens = header;
            this.bodyTokens = body;
        }
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
    
    @Test //(expected = RuntimeException.class)
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
        body.add(new Token("c","c"));
        body.add(new Token("c","c"));
        body.add(new Token("|]","|]"));
        body.add(new Token("C","C"));
        body.add(new Token("c","c"));
        body.add(new Token("[1","[1"));
        body.add(new Token("D","D"));
        body.add(new Token("[2","[2"));
        body.add(new Token("E","E"));
        body.add(new Token(":|",":|"));
        body.add(new Token("f","f"));
        body.add(new Token("|:","|:"));
        body.add(new Token("f","f"));
        body.add(new Token(":|",":|"));
        // 7 + 
        parser = new Parser(new FakeLexer(header, body));
        player = parser.parse();
        player.schedule();
        player.play();
        
        assertEquals(14,player.getBody().getVoiceList().get(0).getMusicSequences().size());
    }
}
