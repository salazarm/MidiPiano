package player;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import player.Token.Type;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import datatypes.Accidental;
import datatypes.Body;
import datatypes.Chord;
import datatypes.Header;
import datatypes.Note;
import datatypes.Player;
import datatypes.Rest;
import datatypes.Tuplet;
import datatypes.Voice;
import exception.ParseException;

public class Parser {
	
	private final Lexer lexer;
	private Header header;
	private Body body;
	private Voice[] voices;

	/**
	 * Creates Parser over passed Lexer.
	 * @param lexer Lexer object to parse
	 */
	public Parser(Lexer lexer) {
		this.lexer = lexer;
		parseHeader();
		parseBody();
	}
	
	//public Header getHeader() { return header; }
	//public Body getBody() { return body; }

	public Player parse()
	{
	    try
	    {
	        return new Player(header, body);
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
	private Rest readRest()
    {
	    double multiplier;
	    
	    lexer.consumeBody(Type.REST);
	    
       // Read 1/4, /3, 5/, ..
       if(lexer.peekBody().getType()==Type.NOTEMULTIPLIER)
           multiplier = readMultiplier();
       else
           multiplier = 1;
        
        return new Rest(multiplier);
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

	private Chord readChord()
	{
	    Token token;
	    List<Note> list = new ArrayList<Note>();
	    
	    lexer.consumeBody(Type.CHORDSTART);
	    
	    while((token = lexer.peekBody()).getType() != Type.CHORDEND) 
	        list.add(readNote());

	    double len = list.get(0).getNoteMultiplier();
	    for(Note note : list)
	        if(note.getNoteMultiplier() != len)
	            throw new RuntimeException("different length of nodes in a chord");

	    return new Chord(list);
	}
	private Tuplet readTuplet()
	{
	    Token token = lexer.nextBody();
	    String str = token.getValue();
	    
	    if(token.getType() != Type.TUPLET || str.length()!=2 || str.charAt(0)!='(')
	        throw new RuntimeException("A bug in program");
	    
	    if(!(str.charAt(1)>='2' && str.charAt(1)<='4'))
	        throw new RuntimeException("A wrong length of tuplet");
	    
	    List<Note> list = new ArrayList<Note>();
	    int i, n = str.charAt(1) - '0';
	    for(i=0 ; i<n; ++i) list.add(readNote());
	    return new Tuplet(list);
	    
	}
	private Voice readVoice()
	{
	    Token token = lexer.nextBody();
        String str = token.getValue();
        
        if(token.getType() != Type.VOICE)
            throw new RuntimeException("Voice expected");
        
        for(Voice v : voices)
            if(v.getVoiceName().equals(str))
                return v;
        
	    throw new RuntimeException("No voice found with such name");
	}
	private void parseHeader()
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
	
	/**
	 * Parsing body needs a header.
	 * Parse body first.
	 * 
	 * @return
	 */
	private void parseBody()
    {
	    if(header == null) throw new RuntimeException("parse header first!");

	    int i;
	    Token token;
	    Body body = new Body();
	    
	    String voiceNames[] = header.getVoiceNames();
	    Voice currentVoice;
	    
	    if(voiceNames.length > 0)
	    {
	        voices = new Voice[voiceNames.length];
	        for(Voice voice : voices) body.addVoice(voice);
	        
	        currentVoice = readVoice();
	    }
	    else
	    {
	        voices = null;
	        body.addVoice( currentVoice = new Voice("Default voice") );
	    }
	    
	    for(i=0;i<voices.length;++i)
            body.addVoice( voices[i] = new Voice(voiceNames[i]) );
	    
	    
	    while( (token=lexer.peekBody()) != null)
	    {
	        Type type = token.getType();
	        
	        if(type == Type.VOICE)
	            currentVoice = readVoice();
	        else if(type == Type.CHORDSTART)
	            currentVoice.add(readChord());
	        else if(type == Type.REST)
	            currentVoice.add(readRest());
	        else if(type == Type.ACCIDENTAL || type == Type.BASENOTE)
	            currentVoice.add(readNote());
	        else if(type == Type.TUPLET)
	            currentVoice.add(readTuplet());
	        else if(type == Type.REPEATSTART)
	        {
	            
	        }
	        else if(type == Type.REPEATSECTION)
	        {
	            
	        }
	        else if(type == Type.REPEATEND)
	        {
	            
	        }
	        else if(type == Type.BARLINE)
	        {
	            
	        }
	        else if(type == Type.ENDMAJORSECTION)
	        {
	            
	        }
	        else
	            throw new RuntimeException("What's this token?");
	    }
    }
	
	// Q. any chance Lexer is exposed to outsdie?
	public Lexer getLexer() {
		return this.lexer;
	}
}
