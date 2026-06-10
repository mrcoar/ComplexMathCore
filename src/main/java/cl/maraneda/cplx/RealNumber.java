package cl.maraneda.cplx;

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
public class RealNumber extends Number implements MathResult{
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

    @Override
    public boolean isReal(){
        return true;
    }

    @Override
    public boolean isImaginary(){
        return false;
    }
}
