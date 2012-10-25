package player;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import player.Token.Type;
import visitors.Duration;
import datatypes.Accidental;
import datatypes.Body;
import datatypes.Chord;
import datatypes.Fraction;
import datatypes.Header;
import datatypes.KeySignature;
import datatypes.Note;
import datatypes.Player;
import datatypes.Rest;
import datatypes.Tuplet;
import datatypes.Voice;

public class Parser {
	
	private final Lexer lexer;
	private Header header;
	private Body body;
	private Voice[] voices;
	
	private KeySignature currentKey; // used when parsing 

	/**
	 * Creates Parser over passed Lexer.
	 * @param lexer Lexer object to parse
	 */
	public Parser(Lexer lexer) {
		this.lexer = lexer;
		parseHeader();
		parseBody();
	}

	public Player parse()
	{
	    try
	    {
	        Player player = new Player(header, body);
	        
	        // check timing
	        player.getBody().accept( new Duration(player) );

	        return player;
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
	
	private Fraction parseFraction(String str)
	{
        int split = str.indexOf("/");
        if(split<=0 || split==str.length()-1)
            throw new RuntimeException("Not a complete fraction");
        
        return new Fraction(Integer.parseInt(str.substring(0, split)),
                            Integer.parseInt(str.substring(split+1, str.length())));
	}
	private double parseMultiplier(String str)
	{
        int split = str.indexOf('/');
        
        if(split == -1) // not fraction: A2
            return Integer.parseInt(str);
        else // fraction: A3/4
        {
            int numerator,denominator;
            
            if(split>0) numerator = Integer.parseInt(str.substring(0, split)); else numerator=1;
            if(split<str.length()-1) denominator = Integer.parseInt(str.substring(split+1, str.length())); else denominator=2;
            return ((double) numerator) / denominator;
        }
	}
	private double readMultiplier()
	{
	    Token token = lexer.nextBody();
	    if(token.getType() != Type.NOTEMULTIPLIER) throw new RuntimeException("multiplier expected");
	    return parseMultiplier(token.getValue());
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

    /**
     * Read [ (^, b) (Basenote) (Octave up/down) multiplier ] 
     * @param lastAccidental 
     * @return
     */

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
	        token = lexer.nextBody();
	    }

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
                throw new RuntimeException("I don't know this header token");
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
	    
	    
	    //header.getKeySignature()
	    
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
	            currentVoice.repeatStart();
	        else if(type == Type.REPEATSECTION) // [1. ignore "[2".
	            if(token.getValue().charAt(1)=='1') currentVoice.repeatSection(); // [1
	        else if(type == Type.REPEATEND)
	            currentVoice.repeatEnd();
	        else if(type == Type.BARLINE)
	        {
	            // return to default keySignature
	            currentKey = KeySignature.getType(header.getKeySignature().getStringRep());
	        }
	        else if(type == Type.ENDMAJORSECTION)
	        {
	            // over! but there can be still other voices
	            currentVoice.setClosed();
	            currentKey = KeySignature.getType(header.getKeySignature().getStringRep());
	        }
	        else
	            throw new RuntimeException("What's this token?");
	    }

	    // TODO: Validate: all voices have same length?
	    
    }
}
