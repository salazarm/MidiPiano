package datatypes;

import exception.InvalidInputException;

public class Fraction {
	
	private final int numerator, denominator;
	
	public Fraction(int numerator, int denominator) {
		if(denominator<=0) {
			throw new InvalidInputException(
					String.format("Invalid denominator: %s", denominator));
		}
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	public int getNumerator() {
		return numerator;
	}

	public int getDenominator() {
		return denominator;
	}
	
	public double getValue() {
		return ((double)numerator)/denominator;
	}
	
	@Override
	public String toString() {
		return numerator + "/" + denominator;
	}
}
