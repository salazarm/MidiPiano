package player;

import java.util.regex.Pattern;


public class Token{
	private final Type type;
	private String value;
	
	// header : V voicename
	// body : V V 
	Token(String inp, String string) {
	    this.type = getType(inp);
	    
	    if(type!=Type.TITLE && type!=Type.COMPOSER && type!=Type.VOICE)
	        this.value = string.trim();
	    else
	        this.value = string;
			
	}

	private Type getType(String type) {
		if (Pattern.matches("\\ACi\\z",type)){
			return Type.COMPOSER;
		}
		else if(Pattern.matches("\\AX\\z",type)){
			return Type.INDEX;
		}
		else if (Pattern.matches("\\AK\\z",type)){
			return Type.KEY;
		}
		else if (Pattern.matches("\\AL\\z",type)){
			return Type.NOTELENGTH;
		}
		else if(Pattern.matches("\\AM\\z",type)){
			return Type.METER;
		}
		else if(Pattern.matches("\\AQ\\z",type)){
			return Type.TEMPO;
		}
		else if(Pattern.matches("\\AT\\z",type)){
			return Type.TITLE;
		}
		else if(Pattern.matches("\\A\\|\\z",type)){
			return Type.BARLINE;
		}
		else if(Pattern.matches("\\d+",type)|| Pattern.matches("\\d*/\\d*", type) ){
			return Type.NOTEMULTIPLIER;
		}
		else if(Pattern.matches("\\([2-4]",type)){
			return Type.TUPLET;
		}
		else if(Pattern.matches("\\A\\[\\z",type)){
			return Type.CHORDSTART;
		}
		else if(Pattern.matches("\\A\\]\\z",type)){
			return Type.CHORDEND;
		}
		else if(Pattern.matches("\\A[a-gA-G]\\z",type)){
			return Type.BASENOTE;
		}
		else if(Pattern.matches("\\Az\\z",type)){
			return Type.REST;
		}
		else if(Pattern.matches("\\A[\\^_=]\\z",type)){
			return Type.ACCIDENTAL;
		}
		else if(Pattern.matches("\\A[\\,']", type)){
			return Type.OCTAVE;
		}
		else if(Pattern.matches("\\|[\\|\\]]",type)){
			return Type.ENDMAJORSECTION;
		}
		else if(type.equals("|:")){
			return Type.REPEATSTART;
		}
		else if (type.equals(":|")){
			return Type.REPEATEND;
		}
		else if(Pattern.matches("\\A\\[[12]\\z",type)){
			return Type.REPEATSECTION;
		}
		else if(Pattern.matches("\\A1V:[\\s\\S]*",type) ){
			this.value = type.substring(4,type.length());
			return Type.VOICE;
		}
		else if(Pattern.matches("\\AV\\z",type) ){
			return Type.VOICE;
		}
		else{
			throw new RuntimeException("Unexpected Sequence: " +type);
		}
	}
	
	public Type getType(){	return this.type;	}
	public String getValue(){	return this.value;	}

	public static enum Type {
		ACCIDENTAL, BASENOTE, CHORDSTART, CHORDEND, NOTEMULTIPLIER, 
		BARLINE, REST, REPEATSTART, REPEATSECTION, REPEATEND, TUPLET, VOICE,
		COMPOSER, TITLE, INDEX, KEY, METER, TEMPO, NOTELENGTH, ENDMAJORSECTION, OCTAVE
	}
}
