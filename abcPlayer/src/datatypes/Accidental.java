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
    
    /**
     * Initializes an Accidental with the given integer representation
     * @param intRep int representation of desired Accidental
     */
    public Accidental(int intRep) {
        if(intRep == 2) {this.type=Type.DOUBLE_SHARP; this.stringRep = "^^";}
        else if(intRep == 1) {this.type=Type.SHARP;stringRep = "^";}
        else if(intRep == 0) {this.type=Type.NEUTRAL; stringRep = "=";}
        else if(intRep == -1) {this.type=Type.FLAT; stringRep = "_";}
        else if(intRep == -2) {this.type=Type.DOUBLE_FLAT; stringRep = "__";}
        else throw new RuntimeException("wrong Accidental");
    }
    /**
     * Initializes an Accidental with the given integer representation
     * @param stringRep String representation of desired Accidental
     */
    public Accidental(String stringRep) {
        if(stringRep.equals("^^")) this.type = Type.DOUBLE_SHARP;
        else if(stringRep.equals("^")) this.type = Type.SHARP;
        else if(stringRep.equals("=")) this.type = Type.NEUTRAL;
        else if(stringRep.equals("_")) this.type = Type.FLAT;
        else if(stringRep.equals("__")) this.type = Type.DOUBLE_FLAT;
        else
        {
            throw new RuntimeException("I can't understand this accidental! : " + stringRep);
        }
        
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
