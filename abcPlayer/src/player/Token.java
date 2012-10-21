package player;

import java.util.regex.Pattern;


public class Token{
	private final Type type;
	private String value;
	
	public Token(String inp, String string) {
		this.value = string;
		this.type = getType(inp);
		
	}

	private Type getType(String type) {
		if (Pattern.matches("\\AC\\z",type)){
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
		else if(Pattern.matches("[\\\\0-9]",type)){
			return Type.NOTEMULTIPLIER;
		}
		else if(Pattern.matches("",type)){
			this.value = type.charAt(1)+"";
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
		else if(Pattern.matches(type, "\\Az\\z")){
			return Type.REST;
		}
		else if(Pattern.matches("\\A[\\^_']\\z",type)){
			return Type.ACCIDENTAL;
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
		
		else{
			throw new RuntimeException("Unexpected Sequence:" +type+type+type+type);
		}
	}
	
	public Type getType(){	return this.type;	}
	public String getValue(){	return this.value;	}

	public static enum Type {
		ACCIDENTAL, BASENOTE, CHORDSTART, CHORDEND, NOTEMULTIPLIER, 
		BARLINE, REST, REPEATSTART, REPEATSECTION, REPEATEND, TUPLET,
		COMPOSER, TITLE, INDEX, KEY, METER, TEMPO, NOTELENGTH, ENDMAJORSECTION
	}
}
