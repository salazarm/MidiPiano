package datatypes;

public class Meter {

	private final Fraction meter;
	
	public Meter(int numerator, int denominator) {
		this.meter = new Fraction(numerator, denominator);
	}
	
	public Meter(Fraction fraction) {
		this.meter = fraction;
	}
	
	public Fraction getMeter() {
		return this.meter;
	}
	
	@Override
	public String toString() {
		return this.meter.toString();
	}
}
