package datatypes;

public class Meter {

	private final int numerator, denominator;
	
	public Meter(int numerator, int denominator) {
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
