package player;

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
        header.add(new Token("K","C"));
        body = new ArrayList<Token>();
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
    public void testParser_simple2() throws MidiUnavailableException
    {
        ArrayList<Token> header, body;
        
        header = new ArrayList<Token>();
        header.add(new Token("X","2"));
        header.add(new Token("T","title"));
        header.add(new Token("K","C"));
        body = new ArrayList<Token>();
        body.add(new Token("A","A"));
        body.add(new Token("B","B"));
        body.add(new Token("C","C"));
        body.add(new Token("|","|"));
        body.add(new Token("A","A"));
        body.add(new Token("B","B"));
        body.add(new Token("C","C"));
        body.add(new Token("||","||"));
        parser = new Parser(new FakeLexer(header, body));
        player = parser.parse();
        player.schedule();
        player.play();
    }
}
