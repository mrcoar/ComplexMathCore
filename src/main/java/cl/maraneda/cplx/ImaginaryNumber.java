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
public class ImaginaryNumber implements MathResult, Comparable<ImaginaryNumber>{
    /** Comparator used to check if one imaginary number is greater, equal or lower than other */
    public static final Comparator<ImaginaryNumber> IMAGINARY_NUMBER_COMPARATOR =
        Comparator.comparingDouble(ImaginaryNumber::getImaginary);
    protected double imaginary;

    /** Creates an imaginary number representing the imaginary unit */
    public ImaginaryNumber(){
        this(1);
    }

    /** Create an imaginary number with a numeric coefficient of num
     *
     * @param num The numeric coefficient. It cannot be zero, as zero is not an imaginary number.
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
        if(imaginary == 1){
            return "i";
        }
        if(imaginary == -1){
            return "-i";
        }
        return  imaginary + "i";
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

    /** Compares two imaginary numbers through their numeric coefficients
     *
     * @param i1 The first imaginary number used to compare
     * @param i2 The second imagianry number used to compare
     * @return 1 if i1 is greater than i2, -1 if i2 is greater than i1, 0 if both are equal
     */
    public static int compare(ImaginaryNumber i1, ImaginaryNumber i2){
        return IMAGINARY_NUMBER_COMPARATOR.compare(i1, i2);
    }

    /**
     * Compares this ImaginaryNumber with other
     * @param other The ImaginaryNumber this object is compared to
     * @return 1 if this ImaginaryNumber is greater than other, -1 if other is greater than this, 0 if both are equal
     */
    public int compareTo(ImaginaryNumber other){
        return compare(this, other);
    }

    @Override
    public boolean isReal(){
        return false;
    }

    @Override
    public boolean isImaginary(){
        return true;
    }

    public static ImaginaryNumber fromString(String str){
        if(str == null || str.isBlank()){
            throw new IllegalArgumentException("The specified string cannot be null or empty");
        }
        if(str.trim().equals("i")){
            return new ImaginaryNumber(1);
        }
        if(str.trim().equals("-i")){
            return new ImaginaryNumber(-1);
        }
        if(str.endsWith("i")) try{
            return new ImaginaryNumber(Double.parseDouble(str.substring(0, str.length() - 1)));
        }catch(NumberFormatException _){
            throw new IllegalArgumentException("The specified string (" + str + " is not a valid imaginary number");
        }else{
            throw new IllegalArgumentException("The specified string is not an imaginary number at all");
        }

    }
}
