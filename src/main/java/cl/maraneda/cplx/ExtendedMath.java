package cl.maraneda.cplx;

/** This class contains some mathematical functions that were not
 *  included in Java's Math class.<br>
 *  As this class contains only static methods and a sole private
 *  constructor, an instance of it cannot be created.
 *
 *  @author Marco Araneda
 *  @since 2026-06-01
 *  @version 1.0
 *  @see Math
 */
public class ExtendedMath {
    private ExtendedMath() { /* Nothing */ }

    /**
     *
     * @param num (see below)
     * @return the secant of num - sec(num) -, that is, the multiplicative
     * inverse of cos(num)
     * @see Math#cos(double) 
     */
    public static double sec(double num){
        return 1 / Math.cos(num);
    }

    /**
     *
     * @param num (see below)
     * @return the cosecant of num - csc(num) -, that is, the multiplicative
     * inverse of sin(num)
     * @see Math#sin(double)
     */
    public static double csc(double num){
        return 1 / Math.sin(num);
    }

    /**
     *
     * @param num (see below)
     * @return the cotangent of num - cot(num) -, that is, the multiplicative
     * inverse of tan(num)
     * @see Math#tan(double)
     */
    public static double cot(double num){
        return 1 / Math.tan(num);
    }

    /**
     *
     * @param num (see below)
     * @return the hyperbolic secant of num - sech(num) -, that is,
     * the multiplicative inverse of cosh(num)
     * @see Math#cosh(double)
     */
    public static double sech(double num){
        return 1 / Math.cosh(num);
    }

    /**
     *
     * @param num (see below)
     * @return the hyperbolic cosecant of num - csch(num) -, that is,
     * the multiplicative inverse of sinh(num)
     * @see Math#sinh(double)
     */
    public static double csch(double num){
        return 1 / Math.sinh(num);
    }

    /**
     *
     * @param num (see below)
     * @return the hyperbolic cotangent of num - coth(num) -, that is,
     * the multiplicative inverse of tanh(num)
     * @see Math#tanh(double)
     */
    public static double coth(double num){
        return 1 / Math.tanh(num);
    }

    /**
     *
     * @param num (see below)
     * @return the arcsecant of num - arcsec(num) -, that is,
     * the arccosine of the multiplicative inverse of num
     * @see Math#acos(double)
     */
    public static double asec(double num){
        return Math.acos(1/num);
    }

    /**
     *
     * @param num (see below)
     * @return the arccosecant of num - arccsc(num) -, that is,
     * the arcsine of the multiplicative inverse of num
     * @see Math#asin(double)
     */
    public static double acsc(double num){
        return Math.asin(1/num);
    }

    /**
     *
     * @param num (see below)
     * @return the arccotangent of num - arccot(num) -, that is,
     * the arctangent of the multiplicative inverse of num
     * @see Math#atan(double)
     */
    public static double acot(double num){
        return Math.atan(1/num);
    }

    /**
     *
     * @param num (see below)
     * @return the hyperbolic arcsin of num - arcsinh(num) -, that is,
     * the natural logarithm of the sum between num and the square root of
     * the sum between num squared and 1
     * @see Math#sqrt(double)
     * @see Math#log(double)
     * @see Math#pow(double, double)
     */
    public static double asinh(double num){
        return Math.log(num + Math.sqrt(Math.pow(num, 2) + 1));
    }

    /**
     *
     * @param num (see below)
     * @return the hyperbolic arccosine of num - arccosh(num) -, that is,
     * the natural logarithm of the sum between num and the square root of
     * the difference between num squared and 1
     * @throws ArithmeticException if num is lower than 1
     * @see Math#sqrt(double)
     * @see Math#log(double)
     * @see Math#pow(double, double))
     */
    public static double acosh(double num){
        return Math.log(num + Math.sqrt(Math.pow(num, 2) - 1));
    }

    /**
     *
     * @param num (see below)
     * @return the hyperbolic arctangent of num - arctanh(num) -, that is,
     * the half of the difference between the natural logarithm of 1 plus num
     * and the natural logarithm of 1 minus num
     * @throws ArithmeticException if the absolute value of num is greater or
     * equal than 1.
     * @see Math#log(double)
     */
    public static double atanh(double num){
        if(Math.abs(num) >= 1){
            throw new ArithmeticException("The absolute value of the argument of the hyperbolic arctangent must be lower than 1");
        }
        return (Math.log(1 + num) - Math.log(1 - num)) / 2;
    }

    /**
     *
     * @param num (see below)
     * @return the hyperbolic arcsecant of num - arcsech(num) -, that is,
     * the natural logarithm of the sum between the inverse multiplicative of
     * num and the square root of the difference between the inverse multiplicative
     * of num squared and 1
     * @see Math#sqrt(double)
     * @see Math#log(double)
     * @see Math#pow(double, double)
     */
    public static double asech(double num){
        return Math.log(Math.pow(num, -1) + Math.sqrt(Math.pow(num, -2) - 1));
    }

    /**
     *
     * @param num (see below)
     * @return the hyperbolic arccosecant of num - arccsch(num) -, that is,
     * the natural logarithm of the sum between the inverse multiplicative of
     * num and the square root of the sum between the inverse multiplicative
     * of num squared and 1
     * @see Math#sqrt(double)
     * @see Math#log(double)
     * @see Math#pow(double, double)
     */
    public static double acsch(double num){
        return Math.log(Math.pow(num, -1) + Math.sqrt(Math.pow(num, -2) + 1));
    }

    /**
     *
     * @param num (see below)
     * @return the hyperbolic arccotangent of num - arccoth(num) -, that is,
     * the half of the difference between the natural logarithm of num plus 1
     * and the natural logarithm of num minus 1
     * @see Math#log(double)
     */
    public static double acoth(double num){
        return (Math.log(1 + num) - Math.log(num - 1)) / 2;
    }

    /**
     * Obtains the argument function of a real number
     * @param num The real number whose argument function will be calculated
     * @return zero if num is positive, the half of PI otherwise.
     */
    public static double arg(double num){
        return num >= 0 ? 0 : Math.PI/2;
    }
}
