package datatypes;

import static org.junit.Assert.*;

import org.junit.Test;

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
        assertEquals(f.getDenominator(), 2147);
        assertEquals(f.getValue(), 0.7626, 0.00000001);
        
        f=new Fraction(4,2);
        assertEquals(f.getValue(), 2, 0.000001);
    }
}
