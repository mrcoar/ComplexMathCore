package cl.maraneda.cplx;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;

/** This class represents a complex number.
 *  According to the numeric sets theory, the complex numbers set represent
 *  the union between the real numbers and the imaginary numbers, whose
 *  intersection is an empty set.
 *  Complex numbers can be represented in two ways:<br><br>
 *  1. An ordered pair: (a, b)<br>
 *  2. Binomial form: a + bi<br><br>
 *
 * @author Marco Araneda
 * @since 2026-06-01
 * @version 1.0
 */
public class ComplexNumber extends ImaginaryNumber implements MathResult{
    public static final Comparator<ComplexNumber> COMPLEX_NUMBER_COMPARATOR =
        (c1, c2) -> Double.compare(ComplexMath.modulus(c1), ComplexMath.modulus(c2));

    private final RealNumber real;

    /** Create a complex number from a real number and the numeric coefficient
     *  of an imaginary number
     * @param real the real part of the new complex number
     * @param imaginary the imaginary part of the new complex number
     */
    public ComplexNumber(RealNumber real, double imaginary){
        super(imaginary);
        this.real = real;
    }

    /** Create a complex number from a real number and the numeric coefficient
     *  of an imaginary number
     * @param real the real part of the new complex number
     * @param imaginary the imaginary part of the new complex number
     */
    public ComplexNumber(double real, double imaginary){
        this(new RealNumber(real), imaginary);
    }

    /** Create a complex number from a real number and an imaginary number
     * @param real the real part of the new complex number
     * @param imaginary the imaginary part of the new complex number
     */
    public ComplexNumber(RealNumber real, ImaginaryNumber imaginary){
        this(real, imaginary.getImaginary());
    }

    /** Create a complex number from a real number and an imaginary number
     * @param real the real part of the new complex number
     * @param imaginary the imaginary part of the new complex number
     */
    public ComplexNumber(double real, ImaginaryNumber imaginary){
        this(new RealNumber(real), imaginary.getImaginary());
    }

    /** Create a complex number from an imaginary number having a real part of 0
     * @param in the imaginary part of the new complex number
     */
    public ComplexNumber(ImaginaryNumber in){
        this(0, in);
    }

    /** Create a complex number from a real number having an imaginary part of 0
     * @param rn the real part of the new complex number
     */
    public ComplexNumber(RealNumber rn){
        this(rn, 0);
    }

    /** Obtains the real part of the complex number
     *
     * @return The real part of the complex number
     */
    public RealNumber getReal(){
        return real;
    }

    /** Creates a complex number from an ordered pair of doubles.
     *  The first element will always be the real part and the second will
     *  always be the imaginary part
     * @param pair A double array of 2 elements representing an ordered pair
     * @return The complex number that results from the ordered pair
     * @throws NullPointerException if pair is null
     * @throws IllegalArgumentException if pair's length is not 2
     */
    public static ComplexNumber fromOrderedPair(double[] pair){
        Objects.requireNonNull(pair, "El par ordenado es obligatorio");
        if(pair.length != 2){
            throw new IllegalArgumentException("El par ordenado debe tener exactamente dos elementos");
        }
        return new ComplexNumber(pair[0], pair[1]);
    }

    /** Obtains this complex number's real part as a double.
     * @return The encapsulated double number from the real part.
     */
    public double getRealAsDouble(){
        return real.doubleValue();
    }

    /** Obtains the string representation of this complex number in binomial
     *  form. That is, for a real part a and an imaginary part b, this method
     *  returns the expression a + bi, providing that both values are not zeros
     * @return The expression "a + bi" if a and b are not zeros and b > 0,<br>
     * the expression "a - bi" if a and b are not zeros and b < 0,<br>
     * the expression "a" if "a" is not zero but b is zero,<br>
     * the expression "bi" if "a" is zero but b is not zero,<br>
     * the expression "0" in other cases.
     */
    @Override
    public String toString(){
        if(real.doubleValue() == 0 && imaginary == 0){
            return "0";
        }
        if(real.doubleValue() == 0 && imaginary != 0){
            return super.toString();
        }
        if(real.doubleValue() != 0 && imaginary == 0){
            return Double.toString(real.doubleValue());
        }
        return String.format(
            "%s %s %si",
            BigDecimal.valueOf(real.doubleValue()).stripTrailingZeros().toPlainString(),
            Math.signum(imaginary) < 0 ? "-" : "+",
            BigDecimal.valueOf(Math.abs(imaginary)).stripTrailingZeros().toPlainString());
    }

    /** Compares this complex number with other object.
     *  If other is another complex number, both numbers are
     *  equal if and only if their respective real parts are equal
     *  and their respective imaginary parts are equal.
     *  If other is a real number, both numbers are equal if and only if
     *  their respective real parts are equal and this complex number has no
     *  imaginary part.
     *  If other is an imaginary number, both numbers are equal if and only if
     *  their respective imaginary parts are equal and this complex number has
     *  no real part.
     *  For all other cases, including other being null, both objets are false.
     * @param other the reference object with which to compare.
     * @return true if other is another complex number and both are equal as described above, false otherwise.
     */
    @Override
    public boolean equals(Object other){
        return switch(other){
            case ComplexNumber cplx -> getRealAsDouble() == cplx.getRealAsDouble() && imaginary == cplx.getImaginary();
            case RealNumber rn -> getRealAsDouble() == rn.doubleValue() && getImaginary() == 0;
            case ImaginaryNumber in -> getImaginary() == in.getImaginary() && getRealAsDouble() == 0;
            default -> false;
        };
    }

    /** Obtains this complex number's hash code
     *
     * @return the resulting hash code from the real and imaginary parts of this complex number
     */
    @Override
    public int hashCode(){
        return Objects.hash(real, imaginary);
    }

    @Override
    public boolean isReal(){
        return imaginary == 0;
    }

    @Override
    public boolean isImaginary(){
        return imaginary != 0 && real.doubleValue() == 0;
    }

    public static int compare(ComplexNumber c1, ComplexNumber c2){
        return COMPLEX_NUMBER_COMPARATOR.compare(c1, c2);
    }

    public boolean isZero(){
        return getRealAsDouble() == 0 && getImaginary() == 0;
    }

    public static ComplexNumber fromString(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException(
                    "Cannot create complex number from null or empty string"
            );
        }

        str = str.replaceAll("\\s+", "");

        if (str.startsWith("+")) {
            str = str.substring(1);
        }

        String regex =
                "^(?:-?\\d+(?:\\.\\d+)?i|-?\\d+(?:\\.\\d+)?(?:[+-]\\d+(?:\\.\\d+)?i)?)$";

        if (!Pattern.matches(regex, str)) {
            throw new IllegalArgumentException(
                    "The specified String does not represent a valid complex number"
            );
        }

        // solo real
        if (!str.contains("i")) {
            return new ComplexNumber(
                    Double.parseDouble(str),
                    0
            );
        }

        // solo imaginaria
        if (!str.substring(1).contains("+")
                && !str.substring(1).contains("-")) {

            return new ComplexNumber(
                    0,
                    Double.parseDouble(
                            str.substring(0, str.length() - 1)
                    )
            );
        }

        // buscar separador real/imaginaria
        int split = -1;

        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c == '+' || c == '-') {
                split = i;
            }
        }

        double real =
                Double.parseDouble(
                        str.substring(0, split)
                );

        double imag =
                Double.parseDouble(
                        str.substring(split, str.length() - 1)
                );

        return new ComplexNumber(real, imag);
    }

    public static ComplexNumber fromOrderedPair(String str){
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException(
                    "Cannot create complex number from null or empty string"
            );
        }
        Pattern p = Pattern.compile("^\\([+-]?\\d+(?:\\.\\d+)?,\\s*[+-]?\\d+(?:\\.\\d+)?\\)$");
        if(!p.matcher(str).matches()){
            throw new IllegalArgumentException("The specified String does not represent a valid ordered pair");
        }
        String[] nums = str.trim().substring(1, str.length() - 1).split(",", 2);
        return new ComplexNumber(Double.parseDouble(nums[0].trim()), Double.parseDouble(nums[1].trim()));
    }
}
