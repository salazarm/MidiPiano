package datatypes;

public class Accidental {
	
	// Type represents the different types of accidentals
	public static enum Type {
		SHARP,
		NEUTRAL,
		FLAT,
		DOUBLE_SHARP,
		DOUBLE_FLAT,
	}
	
	private final Type type;
	private final String stringRep;
	private final Type[] types = {Type.SHARP, Type.NEUTRAL, Type.FLAT, Type.DOUBLE_SHARP, Type.DOUBLE_FLAT};
	private final int[] intReps = {1, 0, -1, 2, -2};
	
	public Accidental(Type type, String stringRep) {
		this.type = type;
		this.stringRep = stringRep;		
	}
	
	public Type getType() {
		return type;
	}

	public String getStringRep() {
		return stringRep;
	}
	
	/**
	 * Determines and returns the integer representation corresponding to this Accidental, as determined 
	 * in the Pitch class.
	 * @return int representation of this Accidental
	 * @throws RuntimeException in case of unexpected Type
	 */
	public int getIntRep() {
		for (int i = 0; i < types.length; i++) {
			if (this.getType()==types[i]) {
				return intReps[i];
			}
		}
		throw new RuntimeException("Invalid type");
	}

	public boolean equals(Accidental other) {
		return (this.getType()==other.getType() && 
				this.getStringRep().equals(other.getStringRep()));		
	}
}
