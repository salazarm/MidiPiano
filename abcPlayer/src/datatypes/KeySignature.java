package datatypes;

import java.util.HashMap;
import java.util.Map;


public enum KeySignature {
    
    /* This enum contains a representation of the different key signatures.
     * Each key signature has a property corresponding to which keys in the particular
     * key signature are sharps or flats using the following integer representation for
     * each key:
     * -1 = flat, 0 = neutral, 1 = sharp
     * This property is contained in an array of integers corresponding to each 
     * key signature with the keys in the following order: {A, B, C, D, E, F, G}
     * Each key signature also has a stringRep property corresponding to the abc String 
     * representation of the key signature.
     */
    C_MAJOR (new int[] {0,0,0,0,0,0,0}, "C"),
    A_MINOR (new int[] {0,0,0,0,0,0,0}, "Am"),
    G_MAJOR (new int[] {0,0,0,0,0,1,0}, "G"),
    E_MINOR (new int[] {0,0,0,0,0,1,0}, "Em"),
    D_MAJOR (new int[] {0,0,1,0,0,1,0}, "D"),
    B_MINOR (new int[] {0,0,1,0,0,1,0}, "Bm"),
    A_MAJOR (new int[] {0,0,1,0,0,1,1}, "A"),
    F_SHARP_MINOR (new int[] {0,0,1,0,0,1,1}, "F#m"),
    E_MAJOR (new int[] {0,0,1,1,0,1,1}, "E"),
    C_SHARP_MINOR (new int[] {0,0,1,1,0,1,1}, "C#m"),
    B_MAJOR (new int[] {1,0,1,1,0,1,1}, "B"),
    G_SHARP_MINOR (new int[] {1,0,1,1,0,1,1}, "G#m"),
    F_SHARP_MAJOR (new int[] {1,0,1,1,1,1,1}, "F#"),
    D_SHARP_MINOR (new int[] {1,0,1,1,1,1,1}, "D#m"),
    C_SHARP_MAJOR (new int[] {1,1,1,1,1,1,1}, "C#"),
    A_SHARP_MINOR (new int[] {1,1,1,1,1,1,1}, "A#m"),
    F_MAJOR (new int[] {0,-1,0,0,0,0,0}, "F"),
    D_MINOR (new int[] {0,-1,0,0,0,0,0}, "Dm"),
    B_FLAT_MAJOR (new int[] {0,-1,0,0,-1,0,0}, "Bb"),
    G_MINOR (new int[] {0,-1,0,0,-1,0,0}, "Gm"),
    E_FLAT_MAJOR (new int[] {-1,-1,0,0,-1,0,0}, "Eb"),
    C_MINOR (new int[] {-1,-1,0,0,-1,0,0}, "Cm"),
    A_FLAT_MAJOR (new int[] {-1,-1,0,-1,-1,0,0}, "Ab"),
    F_MINOR (new int[] {-1,-1,0,-1,-1,0,0}, "Fm"),
    D_FLAT_MAJOR (new int[] {-1,-1,0,-1,-1,0,-1}, "Db"),
    B_FLAT_MINOR (new int[] {-1,-1,0,-1,-1,0,-1}, "Bbm"),
    G_FLAT_MAJOR (new int[] {-1,-1,-1,-1,-1,0,-1}, "Gb"),
    E_FLAT_MINOR (new int[] {-1,-1,-1,-1,-1,0,-1}, "Ebm"),
    C_FLAT_MAJOR (new int[] {-1,-1,-1,-1,-1,-1,-1}, "Cb"),
    A_FLAT_MINOR (new int[] {-1,-1,-1,-1,-1,-1,-1}, "Abm");
    
    private int[] keyAccidentals = new int[7];
    private final String stringRep;
    private static Map<String, KeySignature> keySignatureMap = makeKeySignatureMap();
    
    KeySignature(int[] keyAccidentals, String stringRep) {
        this.keyAccidentals = keyAccidentals;
        this.stringRep = stringRep;
    }
    
    // temporary setting accidental
    public void setKeyAccidental(int index,int accidental)
    {
        keyAccidentals[index] = accidental;
    }
    
    private static Map<String, KeySignature> makeKeySignatureMap() {
        Map<String, KeySignature> keySignatureMap = new HashMap<String, KeySignature>();
        keySignatureMap.put("C", C_MAJOR);
        keySignatureMap.put("Am", A_MINOR);
        keySignatureMap.put("G", G_MAJOR);
        keySignatureMap.put("Em", E_MINOR);
        keySignatureMap.put("D", D_MAJOR);
        keySignatureMap.put("Bm", B_MINOR);
        keySignatureMap.put("A", A_MAJOR);
        keySignatureMap.put("F#m", F_SHARP_MINOR);
        keySignatureMap.put("E", E_MAJOR);
        keySignatureMap.put("C#m", C_SHARP_MINOR);
        keySignatureMap.put("B", B_MAJOR);
        keySignatureMap.put("G#m", G_SHARP_MINOR);
        keySignatureMap.put("F#", F_SHARP_MAJOR);
        keySignatureMap.put("D#m", D_SHARP_MINOR);
        keySignatureMap.put("C#", C_SHARP_MAJOR);
        keySignatureMap.put("A#m", A_SHARP_MINOR);
        keySignatureMap.put("F", F_MAJOR);
        keySignatureMap.put("Dm", D_MINOR);
        keySignatureMap.put("Bb", B_FLAT_MAJOR);
        keySignatureMap.put("Gm", G_MINOR);
        keySignatureMap.put("Eb", E_FLAT_MAJOR);
        keySignatureMap.put("Cm", C_MINOR);
        keySignatureMap.put("Ab", A_FLAT_MAJOR);
        keySignatureMap.put("Fm", F_MINOR);
        keySignatureMap.put("Db", D_FLAT_MAJOR);
        keySignatureMap.put("Bbm", B_FLAT_MINOR);
        keySignatureMap.put("Gb", G_FLAT_MAJOR);
        keySignatureMap.put("Ebm", E_FLAT_MINOR);
        keySignatureMap.put("Cb", C_FLAT_MAJOR);
        keySignatureMap.put("Abm", A_FLAT_MINOR);
        return keySignatureMap;
    }
    
    public int[] getKeyAccidentals() {
        return this.keyAccidentals;
    }   
    
    public String getStringRep() {
        return this.stringRep;
    }
    
    public static KeySignature getType(String keySignature) {
        return KeySignature.keySignatureMap.get(keySignature);
    }
}
