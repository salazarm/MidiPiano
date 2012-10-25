package datatypes;

import static org.junit.Assert.*;

import org.junit.Test;

/*
 * Our testing strategy for Fraction was very simple. We divided the test space into the following:
 * 1. Valid fractions - numerator=0, numerator and denominator are positive, numerator is negative
 * 2. Invalid fractions - denominator<=0 (should throw RuntimeException)
 */

public class FractionTest
{
    @Test
    public void testFraction()
    {
        Fraction f;
        
        f=new Fraction(0,1);
        assertEquals(f.getValue(), 0.0, 0.0000001);
        
        f=new Fraction(1,2);
        assertEquals(f.getValue(), 0.5, 0.0000001);
        
        f=new Fraction(951,1247);
        assertEquals(f.getNumerator(), 951);
        assertEquals(f.getDenominator(), 1247);
        assertEquals(f.getValue(), 0.7626303127, 0.00000001);
        
        f=new Fraction(-4,2);
        assertEquals(f.getValue(), -2, 0.000001);
    }
    
    @Test (expected = RuntimeException.class) 
    public void zeroDenominatorTest() {
    	Fraction f;
    	f=new Fraction(0,0);
    	f.getValue();
    }

    @Test (expected = RuntimeException.class) 
    public void negativeDenominatorTest() {
    	Fraction f;
    	f=new Fraction(5,-1);
    	f.getValue();
    }
}
