package player;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import datatypes.Body;
import datatypes.Header;
import datatypes.Player;

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
	    Header header = new Header();
	    

	    
	    return header;
	}
	private Body parseBody()
    {
        Body body = new Body();
        return body;
    }
	
	
	// Q. any chance Lexer is exposed to outsdie?
	public Lexer getLexer() {
		return this.lexer;
	}

}
