package datatypes;

import java.util.HashMap;
import java.util.Map;

public enum KeySignature {
	
	/* This enum contains a representation of the different key signatures.
	 * Each key signature has a property corresponding to which keys in the particular
	 * key signature are sharps or flats using the following integer representation for
	 * each key:
	 * -1 = flat, 0 = neutral, 1 = sharp
	 * These properties are contained in an array of integers corresponding to each 
	 * key signature with the keys in the following order: {A, B, C, D, E, F, G}
	 */
	C_MAJOR (new int[] {0,0,0,0,0,0,0}),
	A_MINOR (new int[] {0,0,0,0,0,0,0}),
	G_MAJOR (new int[] {0,0,0,0,0,1,0}),
	E_MINOR (new int[] {0,0,0,0,0,1,0}),
	D_MAJOR (new int[] {0,0,1,0,0,1,0}),
	B_MINOR (new int[] {0,0,1,0,0,1,0}),
	A_MAJOR (new int[] {0,0,1,0,0,1,1}),
	F_SHARP_MINOR (new int[] {0,0,1,0,0,1,1}),
	E_MAJOR (new int[] {0,0,1,1,0,1,1}),
	C_SHARP_MINOR (new int[] {0,0,1,1,0,1,1}),
	B_MAJOR (new int[] {1,0,1,1,0,1,1}),
	G_SHARP_MINOR (new int[] {1,0,1,1,0,1,1}),
	F_SHARP_MAJOR (new int[] {1,0,1,1,1,1,1}),
	D_SHARP_MINOR (new int[] {1,0,1,1,1,1,1}),
	C_SHARP_MAJOR (new int[] {1,1,1,1,1,1,1}),
	A_SHARP_MINOR (new int[] {1,1,1,1,1,1,1}),
	F_MAJOR (new int[] {0,-1,0,0,0,0,0}),
	D_MINOR (new int[] {0,-1,0,0,0,0,0}),
	B_FLAT_MAJOR (new int[] {0,-1,0,0,-1,0,0}),
	G_MINOR (new int[] {0,-1,0,0,-1,0,0}),
	E_FLAT_MAJOR (new int[] {-1,-1,0,0,-1,0,0}),
	C_MINOR (new int[] {-1,-1,0,0,-1,0,0}),
	A_FLAT_MAJOR (new int[] {-1,-1,0,-1,-1,0,0}),
	F_MINOR (new int[] {-1,-1,0,-1,-1,0,0}),
	D_FLAT_MAJOR (new int[] {-1,-1,0,-1,-1,0,-1}),
	B_FLAT_MINOR (new int[] {-1,-1,0,-1,-1,0,-1}),
	G_FLAT_MAJOR (new int[] {-1,-1,-1,-1,-1,0,-1}),
	E_FLAT_MINOR (new int[] {-1,-1,-1,-1,-1,0,-1}),
	C_FLAT_MAJOR (new int[] {-1,-1,-1,-1,-1,-1,-1}),
	A_FLAT_MINOR (new int[] {-1,-1,-1,-1,-1,-1,-1});
	
	private int[] keyAccidentals = new int[6];
	private static Map<String, KeySignature> keySignatureMap = makeKeySignatureMap();
	
	KeySignature(int[] keyAccidentals) {
		this.keyAccidentals = keyAccidentals;
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
	
	public KeySignature getType(String keySignature) {
		return KeySignature.keySignatureMap.get(keySignature);
	}
}
