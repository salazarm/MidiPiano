package player;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import datatypes.Body;
import datatypes.Header;
import datatypes.Player;
import exception.ParseException;

public class Parser {
	
	private final Lexer lexer;
	
	
	/**
	 * Creates Parser over passed Lexer.
	 * @param lexer Lexer object to parse
	 */
	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public Player parse()
	{
	    try
	    {
	        return new Player(parseHeader(), parseBody());
	    }
	    catch(InvalidMidiDataException e)
	    {
	        throw new RuntimeException("Invalid Midi Data. Check your abc file.");
	    }
	    catch( MidiUnavailableException e)
	    {
	        throw new RuntimeException("Midi Unavailable");
	    }
	        
	}
	private Header parseHeader()
	{
//	    Header header = new Header();
		List<Token> headerTokens = this.lexer.getHeader();
		if(headerTokens.get(0).getType()!=Token.Type.INDEX) {
			throw new ParseException("Header must start with index number");
		}
		String index = headerTokens.get(0).getValue();
	    

	    
	    return header;
	}
	private Body parseBody()
    {
//        Body body = new Body();
        return body;
    }
	
	
	// Q. any chance Lexer is exposed to outsdie?
	public Lexer getLexer() {
		return this.lexer;
	}
}