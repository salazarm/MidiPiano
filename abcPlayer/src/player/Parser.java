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
		
		try
		{
		    parseHeader();
		    parseBody();
		}
		catch(Exception e)
		{
		    e.printStackTrace();
		    System.err.println(lexer.nextBody().getValue());
		    if(lexer.peekBody()!=null)
		    {
		        System.err.println(lexer.nextBody().getValue());
		        if(lexer.peekBody()!=null)
		            System.err.println(lexer.nextBody().getValue());
		    }
		    
		}
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
	
	private Header parseHeader() {
//	    Header header = new Header();
		List<Token> headerTokens = this.lexer.getHeader();
		if(headerTokens.get(0).getType()!=Token.Type.INDEX) {
			throw new ParseException("Header must start with index number");
		}
		String index = headerTokens.get(0).getValue();

	    // Read accidental if exist
	    // ** Double accidentals are two tokens
	    if(token.getType() == Type.ACCIDENTAL)
	    {
	        Token second = lexer.peekBody();
            if(second.getType() == Type.ACCIDENTAL)
            {
                lexer.nextBody();
                accidental = new Accidental(token.getValue() + second.getValue());
            }
            else
                accidental = new Accidental(token.getValue());

	        token = lexer.nextBody();
	    }
	    else
	        accidental = null;

	    // Read basenote
	    if(token.getType()!=Type.BASENOTE)
	        throw new RuntimeException("readNote: This is not basenote!");
	    note = token.getValue().charAt(0);
	    
	    if(accidental != null)
	        currentKey.setKeyAccidental(note-'A', accidental.getIntRep());
	    else
	        accidental = new Accidental(currentKey.getKeyAccidentals()[note-'A']);
	    
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
	    //*****
        int index, tempo = 0;
        String title, composer = null;
        Fraction length = null,meter = null;
        KeySignature keySignature = null;
        List<String> voiceNames = new ArrayList<String>();
        //*****
        
        Token token;
        
        token=lexer.nextHeader();
        if(token.getType() != Type.INDEX) throw new RuntimeException("index expected");
        index = Integer.parseInt(token.getValue());
        
        token=lexer.nextHeader();
        if(token.getType() != Type.TITLE) throw new RuntimeException("title expected");
        title = token.getValue();
        
        while( (token=lexer.nextBody()).getType() != Type.KEY)
        {
            if(token == null) throw new RuntimeException("Header ended before keySignature found");
            
            Type type = token.getType();
            
            if(type == Type.VOICE)
                voiceNames.add(token.getValue());
            else if(type == Type.COMPOSER)
            {
                if(composer != null) throw new RuntimeException("Duplicated composer");
                composer = token.getValue();
            }
            else if(type == Type.NOTELENGTH)
            {
                if(length != null) throw new RuntimeException("Duplicated Note length");
                length = parseFraction(token.getValue());
            }
            else if(type == Type.METER)
            {
                if(meter != null) throw new RuntimeException("Duplicated Meter");
                meter = parseFraction(token.getValue());
            }
            else if(type == Type.TEMPO)
            {
                if(tempo != 0) throw new RuntimeException("Duplicated tempo");
                tempo = Integer.parseInt(token.getValue());
            }
            else
            {
                throw new RuntimeException("I don't know this header token: "+token.getValue());
            }
        }
        // last is Key
        if(token.getType() != Type.KEY) throw new RuntimeException("Last should be key");
        keySignature = KeySignature.getType(token.getValue());

        header = new Header(index, title, keySignature);
        if(composer != null) header.setComposer(composer);
        if(meter != null) header.setMeter(meter);
        header.setTempo(tempo);
        header.setVoiceNames((String[]) voiceNames.toArray());
        header.setDefaultNoteLengthFraction(length);
	}
	
	private Body parseBody() {
//        Body body = new Body();
        return body;
    }
	
	// Q. any chance Lexer is exposed to outsdie?
	public Lexer getLexer() {
		return this.lexer;
	}
}
