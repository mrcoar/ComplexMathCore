package cl.maraneda.cplx;

import java.util.Comparator;

/** It represents a real number.
 *  This is a convenience class as the wrapper classes that extend Number
 *  has no common interfaces and is not possible to parse an imaginary
 *  number to an int, float, double or long.
 *
 *  @author Marco Araneda
 *  @since 2026-06-01
 *  @version 1.0
 *  @see Number
 */
public class RealNumber extends Number implements MathResult, Comparable<RealNumber>{
    public static final Comparator<RealNumber> REAL_NUMBER_COMPARATOR =
        Comparator.comparingDouble(RealNumber::doubleValue);

    private final double real;

    /** Creates a Real number from a double */
    public RealNumber(double num){
        real = num;
    }

    /** Obtains an integer representation of this real number
     *
     * @return the real number as an integer
     */
    @Override
    public int intValue() {
        return (int) real;
    }

    /** Obtains a long representation of this real number
     *
     * @return The real number as a long
     */
    @Override
    public long longValue() {
        return (long) real;
    }

    /** Obtains a float representation of this real number
     *
     * @return the real number as a float
     */
    @Override
    public float floatValue() {
        return (float) real;
    }

    /** Obtains the double number encapsulated in this real number
     *
     * @return The double number.
     */
    @Override
    public double doubleValue() {
        return real;
    }

    /** Compares this Real number with another object.
     *  If other is a Real number, this method compares the
     *  numbers encapsulated in this object and in the other.
     * @param other the reference object with which to compare.
     * @return true if the other is a RealNumber and the encapsulated number
     * in this and the other object are equal. Otherwise, it returns false.
     */
    public boolean equals(Object other){
        return switch(other){
            case RealNumber rn -> doubleValue() == rn.doubleValue();
            case ComplexNumber cn -> doubleValue() == cn.getRealAsDouble() && cn.isReal();
            default -> false;
        };
    }

    /** It obtains this real number's hash code using the hash code of
     * the Object representation of it.
     *
     * @return this real number's hash code
     * @see Object
     */
    public int hashCode(){
        return super.hashCode();
    }

    /**
     * @return always {@code true} as this object is a RealNumber
     */
    @Override
    public boolean isReal(){
        return true;
    }

    /**
     * @return always {@code false} as this object is a RealNumber
     */
    @Override
    public boolean isImaginary(){
        return false;
    }

    /** Compares two real numbers if one is greater, equal or lower than other
     * through their encapsulated double representations.
     * @param r1 One of the real numbers to compare
     * @param r2 One of the real numbers to compare
     * @return 1 if r1 is greater than r2, -1 if r2 is greater than r1, 0 if both are equal.
     */
    public static int compare(RealNumber r1, RealNumber r2){
        return REAL_NUMBER_COMPARATOR.compare(r1, r2);
    }

    /** Compares two real numbers if one is greater, equal or lower than other
     * through their encapsulated double representations.
     * @param r1 One of the real numbers to compare
     * @param r2 One of the real numbers to compare
     * @return 1 if r1 is greater than r2, -1 if r2 is greater than r1, 0 if both are equal.
     */
    public static int compare(double r1, RealNumber r2){
        return Double.compare(r1, r2.doubleValue());
    }

    /** Compares two real numbers if one is greater, equal or lower than other
     * through their encapsulated double representations.
     * @param r1 One of the real numbers to compare
     * @param r2 One of the real numbers to compare
     * @return 1 if r1 is greater than r2, -1 if r2 is greater than r1, 0 if both are equal.
     */
    public static int compare(RealNumber r1, double r2){
        return Double.compare(r1.doubleValue(), r2);
    }

    /** Compares this real number with another to check if this RealNumber is greater, equal or lower than other
     * through their encapsulated double representations.
     * @param r The real number to be compared with this RealNumber
     * @return 1 if this is greater than r, -1 if r is greater than this, 0 if both are equal.
     */
    public int compareTo(RealNumber r){
        return compare(this, r);
    }
}
