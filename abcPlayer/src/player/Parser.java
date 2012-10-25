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
    
    private int currentKey[]; // used when parsing 

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
            if(body == null) System.out.println("body is null in parse()");
            Player player = new Player(header, body);
            player.getBody().accept( new Duration(player) );
//            System.out.println(player.getHeader().getMeter().toString());
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
       if(lexer.peekBody()!=null && lexer.peekBody().getType()==Type.NOTEMULTIPLIER)
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
        int octave = 0;
        double multiplier;
        
        // Read accidental if exist
        // ** Double accidentals are two tokens
        if(token.getType() == Type.ACCIDENTAL)
        {
            Token second = lexer.peekBody();
            if(second.getType() == Type.ACCIDENTAL)
            {
                lexer.nextBody();
                accidental = new Accidental(token.getValue() + second.getValue()); // double sharp or flat
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
        if(note>='a' && note<='z') {octave++; note=(char)(note-'a'+'A');}
        
        if(accidental != null)
        {
            currentKey[note-'A'] = accidental.getIntRep();
        }
        else
        {
            accidental = new Accidental(currentKey[note-'A']);
        }
        
        // Read octave modifiers
        if(lexer.peekBody()!=null && lexer.peekBody().getType()==Type.OCTAVE)
        {
            token = lexer.nextBody();
            String str = token.getValue();
            
            if(str.equals(","))
            {
                --octave;
                if(lexer.peekBody()!=null && lexer.peekBody().getType()==Type.OCTAVE)
                {
                    if(lexer.nextBody().getValue().equals(","))
                        --octave;
                    else
                        throw new RuntimeException("mixed octave");
                    
                }
            }
            else if(str.equals("'"))
            {
                ++octave;
                if(lexer.peekBody()!=null && lexer.peekBody().getType()==Type.OCTAVE)
                {
                    if(lexer.nextBody().getValue().equals("'"))
                        ++octave;
                    else
                        throw new RuntimeException("mixed octave");
                    
                }
                ++octave;
            }
        }
        
        
       // Read 1/4, /3, 5/, ..
       if(lexer.peekBody()!=null && lexer.peekBody().getType()==Type.NOTEMULTIPLIER)
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
        lexer.consumeBody(Type.CHORDEND);

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
        for(i=0 ; i<n; ++i)
            list.add(readNote());
        return new Tuplet(list);
        
    }
    private Voice readVoice()
    {
        Token token = lexer.nextBody();
        String str = token.getValue();
        str = str.substring(2,  str.length());
        
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
        
        while( (token=lexer.nextHeader()).getType() != Type.KEY)
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
                String str = token.getValue();
                if(str.charAt(0)=='C')
                    if(str.equals("C|"))
                        meter = new Fraction(2,2);
                    else if(str.equals("C")) meter = new Fraction(4,4);
                    else  throw new RuntimeException("Can't understand meter");
                else
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
        if(length!=null) header.setDefaultNoteLengthFraction(length);
        if(tempo>0) header.setTempo(tempo);
        header.setVoiceNames(voiceNames.toArray(new String[]{}));
        
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
        body = new Body();
        
        String voiceNames[] = header.getVoiceNames();
        Voice currentVoice;
        
        
        //header.getKeySignature()
        
        if(voiceNames.length > 0)
        {
            voices = new Voice[voiceNames.length];
            for(i=0;i<voices.length;++i)
                body.addVoice( voices[i] = new Voice(voiceNames[i]) );
            currentVoice = readVoice();
        }
        else
        {
            
            body.addVoice( currentVoice = new Voice("Default voice") );
            voices = new Voice[] {currentVoice};
        }
        
        currentKey = KeySignature.getType(header.getKeySignature().getStringRep()).getKeyAccidentals().clone();
        

//        while( (token=lexer.nextBody()) != null)
//            System.out.println(token.getValue() + " " + token.getType().toString());

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
                lexer.nextBody();
                currentVoice.repeatStart();
            }
            else if(type == Type.REPEATSECTION) // [1 or [2
            {
                lexer.nextBody();
                currentVoice.repeatSection();
            }
            else if(type == Type.REPEATEND)
            {
                lexer.nextBody();
                currentVoice.repeatEnd();
            }
            else if(type == Type.BARLINE)
            {
                lexer.nextBody();
                // return to default keySignature
                currentKey = KeySignature.getType(header.getKeySignature().getStringRep()).getKeyAccidentals().clone();
            }
            else if(type == Type.ENDMAJORSECTION)
            {
                lexer.nextBody();
                // over! but there can be still other voices
                currentVoice.setClosed();
                currentKey = KeySignature.getType(header.getKeySignature().getStringRep()).getKeyAccidentals().clone();
            }
            else
            {
                //if(token==null)
                //    System.out.println("null in exception");
                    //System.out.println("---: "+token.getValue() + " " + type.toString());
                throw new RuntimeException("What's this token?" + " " + token.getValue() + " " + token.getType().toString());
            }
        }

        /*
         * Checking double bars -> but can't pass examples
         * for(Voice voice : voices) if(!voice.getClosed())
            throw new RuntimeException("There is a voice not closed with || or |]");
         */
        // TODO: Validate: all voices have same length?
    }
}
