package datatypes;

public class Meter {

	private final int numerator, denominator;
	
	/**
	 * Represents the meter of a music
	 * @param numerator >= 1
	 * @param denominator >= 1
	 */
	public Meter(int numerator, int denominator) {
	    if(numerator<1 || denominator<1) throw new IllegalArgumentException("Wrong range of meter");
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	public int getNumerator() {
		return numerator;
	}

	public int getDenominator() {
		return denominator;
	}
}
