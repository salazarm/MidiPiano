package player;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import player.Token.Type;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import datatypes.Accidental;
import datatypes.Body;
import datatypes.Header;
import datatypes.Note;
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
	
	// Read [ (^, b) (Basenote) (Octave up/down) multiplier ]
	/**
	 * 
	 * @param lastAccidental 
	 * @return
	 */
	
	private double readMultiplier()
	{
	    Token token = lexer.nextBody();
        String str = token.getValue();
	    int split = str.indexOf('/');
        
        if(split == -1) // not fraction
            return Integer.parseInt(str);
        else // fraction
        {
            int numerator,denominator;
            
            if(split>0) numerator = Integer.parseInt(str.substring(0, split)); else numerator=1;
            if(split<str.length()-1) denominator = Integer.parseInt(str.substring(split+1, str.length())); else denominator=2;
            return ((double) numerator) / denominator;
        }
	}
	private Note readNote()
	{
	    Token token = lexer.nextBody();
	    Accidental accidental;
	    char note;
	    int octave;
	    double multiplier;
	    
	    // Read accidental if exist
	    if(token.getType() == Type.ACCIDENTAL)
	        accidental = new Accidental(token.getValue());
	    else
	    {
	        accidental = null;
//	        accidental = last one; TODO:
	        token = lexer.nextBody();
	    }

	    // Read basenote
	    if(token.getType()!=Type.BASENOTE)
	        throw new RuntimeException("readNote: This is not basenote!");
	    note = token.getValue().charAt(0);
	    
	    // Read octave modifiers
	    if(lexer.peekBody().getType()==Type.OCTAVE)
	    {
	        token = lexer.nextBody();
	        String str = token.getValue();
	        
	        if(str.charAt(0)==',')
	        {
	            // validate!
	            for(char chr : str.toCharArray()) if(chr!=',')
	                throw new RuntimeException("bad char in octave modifier");
	            octave = -str.length();
	        }
	        else
	        {
	            // validate!
                for(char chr : str.toCharArray()) if(chr!='\'')
                    throw new RuntimeException("bad char in octave modifier");
                octave = str.length();
	        }
	    }
	    else octave = 0;
	    
	    
	   // Read 1/4, /3, 5/, ..
       if(lexer.peekBody().getType()==Type.NOTEMULTIPLIER)
           multiplier = readMultiplier();
       else
           multiplier = 1;
	    
	    return new Note(note, octave, accidental, multiplier);
	}
	private Header parseHeader()
	{
//	    Header header = new Header();
		List<Token> headerTokens = this.lexer.getHeader();
		if(headerTokens.get(0).getType()!=Token.Type.INDEX) {
			throw new ParseException("Header must start with index number");
		}
		String index = headerTokens.get(0).getValue();
	    
	    //return header();
		throw new NotImplementedException();
	}
	private Body parseBody()
    {
//        Body body = new Body();
        //return body();
        throw new NotImplementedException();
    }
	
	
	// Q. any chance Lexer is exposed to outsdie?
	public Lexer getLexer() {
		return this.lexer;
	}
}
