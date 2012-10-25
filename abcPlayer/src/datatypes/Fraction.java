package datatypes;


public class Fraction {

	private final int numerator, denominator;

	/**
	 * @param numerator int numerator of this Fraction
	 * @param denominator positive int denominator of this Fraction
	 * @throws RuntimeException if denominator <=0
	 */
	public Fraction(int numerator, int denominator) {
		if(denominator<=0) {
			throw new RuntimeException(
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
