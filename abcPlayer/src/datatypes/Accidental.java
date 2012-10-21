package datatypes;

public class Accidental {
	
	/* Type represents the different types of accidentals. Each type has an intRep
	 * property corresponding to the integer representation of that Accidental type
	 * in the Pitch class.
	 */
	
	public static enum Type {
		
		SHARP (1),
		NEUTRAL (0),
		FLAT (-1),
		DOUBLE_SHARP (2),
		DOUBLE_FLAT (-2);
		
		private final int intRep;
		Type(int intRep) {
			this.intRep = intRep;
		}
		
		public int getIntRep() {
			return this.intRep;
		}
	}
	
	private final Type type;
	private final String stringRep;
	
	public Accidental(Type type, String stringRep) {
		this.type = type;
		this.stringRep = stringRep;		
	}
	
	public Type getType() {
		// TODO: IS THIS REP EXPOSURE?
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
		return this.type.getIntRep();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Accidental)) return false;
		Accidental otherObject = (Accidental) other;
		return (this.getType()==otherObject.getType() && 
				this.getStringRep().equals(otherObject.getStringRep()));		
	}
	
	@Override
	public int hashCode() {
		return this.getType().getIntRep();
	}
}
