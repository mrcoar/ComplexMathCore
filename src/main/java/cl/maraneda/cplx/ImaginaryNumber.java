package cl.maraneda.cplx;

import java.util.Comparator;

/** Represents an imaginary number
 *  An imaginary number is any number that results of calculating the
 *  squared root of a negative number and is expressed in the form "bi",
 *  where "b" is any real number and i the imaginary unit, which is the
 *  square root of -1.
 *
 * @author Marco Araneda
 * @since 2026-06-01
 * @version 1.0
 */
public class ImaginaryNumber implements MathResult{
    public static final Comparator<ImaginaryNumber> IMAGINARY_NUMBER_COMPARATOR =
            (i1, i2) -> Double.compare(i1.getImaginary(), i2.getImaginary());
    protected double imaginary;

    /** Create an imaginary number with a numeric coefficient of num
     *
     * @param num The numeric coefficient. It cannot be zero
     * @throws IllegalArgumentException if num is zero
     */
    public ImaginaryNumber(double num){
        if(num == 0){
            throw new IllegalArgumentException("Zero is not an imaginary number");
        }
        imaginary = num;
    }

    /** Retrieves the numeric coefficient of the imaginary number
     *
     * @return the numeric coefficient
     */
    public double getImaginary(){
        return imaginary;
    }

    /** Obtains the String representation of the imaginary number (see this
     *  class' description above)
     * @return the String representing the imaginary number
     */
    public String toString(){
        return imaginary + "i";
    }

    /** Compares this imaginary number with other object.
     * Assuming other is another imaginary number, this method compares
     * their numeric coefficients.
     * @param other the reference object with which to compare.
     * @return true if other is an imaginary number and it's coefficient equals this one's. Otherwise, it returns false
     */
    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(other instanceof ImaginaryNumber iot){
            return imaginary == iot.getImaginary();
        }
        return false;
    }

    /** It obtains this imaginary number's hash code using the hash code of
     * the Object representation of it.
     *
     * @return this imaginary number's hash code
     * @see Object
     */
    public int hashCode(){
        return super.hashCode();
    }

    public static int compare(ImaginaryNumber i1, ImaginaryNumber i2){
        return IMAGINARY_NUMBER_COMPARATOR.compare(i1, i2);
    }

    @Override
    public boolean isReal(){
        return false;
    }

    @Override
    public boolean isImaginary(){
        return true;
    }
}
