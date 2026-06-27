package cl.maraneda.cplx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.IntStream;

/** This class contains most (if not all) operations that can be made with
 *  imaginary numbers.
 *  Unlike real numbers, not always the closure axiom is fulfilled, but it
 *  depends on the selected operation and the argument(s) for it.
 *
 *  @author Marco Araneda
 *  @since 2026-06-01
 *  @version 1.0
 */
public class ImaginaryMath {
    private ImaginaryMath() { /*Nothing*/ }

    /** Calculates the imaginary unit - sqrt(-1) - to an integer
     *  power. The result depends on the modulus operation between
     *  exp and 4 ({code exp % 4}) and the result of the sign function evaluated in
     *  exp - sgn(exp).
     * @param exp The exponent the imaginary unit will be raised to the power of
     * @return For exp = 4n + b with n and b being integer numbers, this method returns: <ul>
     * <li>1 if b is zero, regardless of sign, </li>
     * <li>i * sgn(exp) if b is 1, </li>
     * <li>-1 if b is 2, regardless of sign, </li>
     * <li>sgn(exp) if b is 3</li>
     * @see Math#signum(float) 
     */
    public static MathResult pow(int exp){
        return pow(new ImaginaryNumber(1), exp);
    }

    /**
     * Calculates the result of an imaginary number raised to the power of
     * exp. As this is
     * an algebraic operation, the result of this is the product between the
     * numeric coefficient of the imaginary number raised to the power of exp and
     * the value that is returned by pow(exp). As a consequence, depending on
     * the result of pow(exp), the returned value may be a real or an imaginary
     * number.
     * @param in The imaginary number used as base
     * @param exp The exponent the imaginary number will be raised to the power of
     * @return The imaginary number raised to the power of exp as described above.
     * @throws ArithmeticException if the numeric coefficient of the imaginary
     * number is zero and exp is zero, as zero raised to the power of zero does not exist.
     * @throws NullPointerException if in is null.
     * @see ImaginaryMath#pow(int)
     * @see Math#pow(double, double)
     */
    public static MathResult pow(ImaginaryNumber in, int exp){
        double num = in.getImaginary();
        if(num == 0 && exp == 0){
            throw new ArithmeticException("Cannot calculated zero raised to the power of zero");
        }
        if(num == 0){
            return new RealNumber(0);
        }
        double pwr = Math.pow(num, exp);
        return switch(exp % 4){
            case 0 -> new RealNumber(pwr);
            case 1 -> new ImaginaryNumber(pwr * Math.signum(exp));
            case 2 -> new RealNumber(-1 * pwr);
            default -> new ImaginaryNumber(-1 * Math.signum(exp) * pwr);
        };
    }

    /**
     * Calculates the base raised to the power of an imaginary number exp
     * - base^(exp * i) -.
     * The result of this will be always a complex number: <ul>
     *     <li>The real part will be the cosine of the product between
     *     exp and the natural logarithm of base - cos(exp * log(base))</li>
     *     <li>The imaginary part will have a numeric coefficient whose
     *     value is the sine of the same argument as the cosine described
     *     above - i * sin(exp * log(base)) - </li>
     * </ul>
     *
     * @param base The base of the power
     * @param exp The imaginary exponent of the power
     * @return the result of base raised to the power of exp as described above
     * @throws ArithmeticException if base is zero, as log(0) does not exist.
     * @throws NullPointerException if exp is null.
     */
    public static ComplexNumber pow(double base, ImaginaryNumber exp){
        return new ComplexNumber(Math.cos(exp.getImaginary() * Math.log(base)), Math.sin(exp.getImaginary() * Math.log(base)));
    }

    /**
     * Raises one imaginary number to the power of other imaginary number.<br>
     * The result of this operation is a complex number that results from raise
     * E to the power of the product between the exp and the natural logarithm
     * of the base.<br>
     * As the natural logarithm of an imaginary number can have an infinite number
     * of values, an integer k must be specified to indicate that the kth value
     * of that logarithm must be used for the calculation.
     * @param base The imaginary base
     * @param exp The imaginary exponent
     * @param k An integer to indicate that the kth value of the natural 
     *          logarithm of the base will be used.
     * @return the base raised to the power of the exponent as described above.
     * @throws NullPointerException if the base and/or the exponent are null.
     * @see #log(ImaginaryNumber, int) 
     * @see ComplexMath#multiply(ComplexNumber...) 
     * @see ComplexMath#exp(ComplexNumber)
     */
    public static ComplexNumber pow(ImaginaryNumber base, ImaginaryNumber exp, int k){
        return ComplexMath.exp(ComplexMath.multiply(new ComplexNumber(exp), log(base, k)));
    }

    /**
     * Raises one imaginary number to the power of other imaginary number and
     * returns its principal value.<br>
     * Calling this method is the same as calling pow(base, exp, 0)
     * @param base The imaginary base
     * @param exp The imaginary exponent
     * @return the base raised to the power of the exponent as described above.
     * @throws NullPointerException if the base and/or the exponent are null.
     * @see #pow(ImaginaryNumber, ImaginaryNumber, int)
     */
    public static ComplexNumber pow(ImaginaryNumber base, ImaginaryNumber exp){
        return pow(base, exp, 0);
    }

    /**
     * Calculates the sum of two or more imaginary numbers.
     * The result of this will be always an imaginary number whose numeric
     * coefficient will be the sum of the numeric coefficients of each
     * operand in ops.
     * @param ops The operands for the sum (min. 2)
     * @return The result of the sum between all operands.
     * @throws IllegalArgumentException if ops is null or if it has less than 2 elements or if this method
     * is called with less than one argument
     * @throws NullPointerException if at least one of the elements in ops is null
     */
    public static ImaginaryNumber sum(ImaginaryNumber... ops){
        if(ops == null || ops.length < 2){
            throw new IllegalArgumentException("At least two operands are required");
        }
        return new ImaginaryNumber(Arrays.stream(ops).mapToDouble(ImaginaryNumber::getImaginary).sum());
    }

    /**
     * Calculates the sum between zero or more double numbers and zero or more
     * imaginary numbers.
     *
     * @param rops An array of double numbers to sum
     * @param iops An array of ImaginaryNumber to sum
     * @return A MathResult object with the result of the sum between the real and the imaginary numbers as follows:
     * <ul>
     *     <li>If rops is neither null nor empty, but iops is null or empty, the result
     *     will be a RealNumber with the sum of all numbers in rops</li>
     *     <li>If rops is null or empty, but iops is neither null nor empty, the result
     *     will be an ImaginaryNumber having a numeric coefficient equal to the sum of
     *     the numeric coefficients in all elements of iops (that is, the value returned
     *     by getImaginary() for each element in iops</li>
     *     <li>If neither argument is null nor empty, the result will be a ComplexNumber with
     *     the real part equal to the sum of all elements in rops and the imaginary part
     *     equal to the sum of the numeric coefficients from all elements in iops</li>
     * </ul>
     * @throws NullPointerException if both rops and iops are null.
     * @throws IllegalArgumentException in one of the following scenarios:
     * <ul>
     *     <li>rops is null or empty and iops has exactly 1 element</li>
     *     <li>iops is null or empty and rops has exactly 1 element</li>
     *     <li>The sum of elements between rops and iops is less than 2</li>
     * </ul>
     */
    public static MathResult sum (double[] rops, ImaginaryNumber[] iops){
        if(rops == null && iops == null){
            throw new NullPointerException("Operands cannot be null");
        }
        if(rops!=null && iops == null){
            if(rops.length < 2){
                throw new IllegalArgumentException("If no imaginary operands are present, there must be at least 2 real numbers");
            }
            return new RealNumber(Arrays.stream(rops).sum());
        }
        if(rops==null){
            if(iops.length < 2){
                throw new IllegalArgumentException("If no real operands are present, there must be at least 2 imaginary numbers");
            }
            return new ImaginaryNumber(Arrays.stream(iops).mapToDouble(ImaginaryNumber::getImaginary).sum());
        }
        if(rops.length + iops.length < 2){
            throw new IllegalArgumentException("At least 2 operands must be among real and imaginary numbers");
        }
        return new ComplexNumber(
            Arrays.stream(rops).sum(),
            Arrays.stream(iops).mapToDouble(ImaginaryNumber::getImaginary).sum()
        );
    }

    /**
     * Multiplies all imaginary numbers in ops.
     * The result of this is the product of two expressions:
     * <ul>
     *     <li>The product of the numeric coefficient of each operand</li>
     *     <li>The imaginary unit raised to the power of the number of operands</li>
     * </ul>
     * @param ops The imaginary numbers to multiply
     * @return The result of multiply all operands in ops as mentioned above as
     * a MathResult, since it's type depends on the result of i^n, being n
     * the length of ops: a RealNumber if |i^n| is 1 or an ImaginaryNumber if
     * |i^n| is i, where |x| is the absolute value of x.
     * @throws IllegalArgumentException if ops is null, or it has less than 2 elements
     * @see #pow(int)
     */
    public static MathResult multiply(ImaginaryNumber... ops){
        if(ops == null || ops.length < 2){
            throw new IllegalArgumentException("At least 2 imaginary factors are required");
        }
        DoubleAccumulator da = new DoubleAccumulator((f1, f2) -> f1 * f2, ops[0].getImaginary());
        IntStream.range(1, ops.length).forEach(i ->
            da.accumulate(ops[i].getImaginary())
        );
        MathResult powRes = pow(ops.length);
        if(powRes instanceof RealNumber rn){
            return new RealNumber(rn.doubleValue() * da.get());
        }
        return new ImaginaryNumber(powRes.toImaginary().getImaginary() * da.get());
    }

    /**
     * Multiplies a real number with an imaginary number.
     * @param r the real number to multiply
     * @param i the imaginary number to multiply
     * @return The real number zero if r is zero. Otherwise, this method will return
     * an imaginary number whose numeric coefficient is the product between
     * r and the numeric coefficient in the ImaginaryNumber i.
     */
    public static MathResult multiply(double r, ImaginaryNumber i){
        if(r == 0){
            return new RealNumber(0);
        }
        return new ImaginaryNumber(r * i.getImaginary());
    }

    /** Multiplies all real numbers in rops with all imaginary numbers in iops
     * If rops is null or empty and iops has two or more elements, calling this
     * method is the same as calling multiply(iops).<br>
     * If iops is null or empty and rops has two or more elements, then the
     * result will be a real number equal to the product between all elements
     * in rops.
     * @param rops The real numbers to multiply
     * @param iops The imaginary numbers to multiply
     * @return a MathResult with the product between the elements in rops
     * and iops as described above.<br>
     * @throws NullPointerException if both rops and iops are null OR if at least
     * one element in iops is null.
     * @throws IllegalArgumentException in one of the following scenarios:
     * <ul>
     *     <li>iops is null or empty and rops has exactly 1 element</li>
     *     <li>rops is null or empty and iops has exactly 1 element</li>
     *     <li>The sum between the number of elements in rops and iops is less
     *     than 2</li>
     * </ul>
     * @see #multiply(ImaginaryNumber...) 
     */
    public static MathResult multiply(double[] rops, ImaginaryNumber[] iops){
        if(rops == null && iops == null){
            throw new NullPointerException("Operands cannot be null");
        }
        DoubleAccumulator da;
        DoubleBinaryOperator dbo = (x, y) -> x * y;
        if(rops!=null && iops == null){
            if(rops.length < 2){
                throw new IllegalArgumentException("If no imaginary operands are present, there must be at least 2 real numbers");
            }
            da = new DoubleAccumulator(dbo, rops[0]);
            IntStream.range(1, rops.length).forEach(i -> da.accumulate(rops[i]));
            return new RealNumber(da.get());
        }
        if(rops==null){
            if(iops.length < 2){
                throw new IllegalArgumentException("If no real operands are present, there must be at least 2 imaginary numbers");
            }
            return multiply(iops);
        }

        if(rops.length + iops.length < 2){
            throw new IllegalArgumentException("At least 2 operands must be among real and imaginary numbers");
        }

        if(iops.length == 1 && rops.length == 1){
            return multiply(rops[0], iops[0]);
        }

        da = new DoubleAccumulator(dbo, rops[0]);
        IntStream.range(1, rops.length).forEach(i -> da.accumulate(rops[i]));
        double coef = da.get();

        if(iops.length == 1){
            return multiply(coef, iops[0]);
        }

        MathResult mr = multiply(iops);
        if(mr instanceof RealNumber rn){
            return new RealNumber(rn.doubleValue() * coef);
        }
        return new ImaginaryNumber(mr.toImaginary().getImaginary() * coef);
    }

    /**
     * Divides two imaginary numbers and returns the result.<br>
     * Due to algebraic properties, dividing 2 imaginary numbers will always
     * result in a real number.
     * @param i1 The first operand of the division
     * @param i2 the second operand of the division
     * @return The result of the division as described above
     * @throws NullPointerException if i1 and/or i2 are null.
     */
    public static double divide(ImaginaryNumber i1, ImaginaryNumber i2){
        return i1.getImaginary() / i2.getImaginary();
    }

    /** Divides an imaginary number by a real number and returns the result.
     * Due to algebraic properties, the result will always be an imaginary
     * number
     * @param in The first operand of the division
     * @param n The second operand of the division
     * @return The result of the division as described above
     * @throws ArithmeticException if n is zero.
     */
    public static ImaginaryNumber divide(ImaginaryNumber in, double n){
        if(n == 0){
            throw new ArithmeticException("The real number cannot be zero");
        }
        return new ImaginaryNumber(in.getImaginary() / n);
    }

    /** Divides a real number by an imaginary number and returns the result.<br>
     * Due to algebraic properties, the result will always be an imaginary
     * number.
     *
     * @param n The real operand
     * @param in The imaginary operand
     * @return The result of the division as described above
     * @throws NullPointerException if in is null
     */
    public static MathResult divide(double n, ImaginaryNumber in){
        return multiply(n, pow(in, -1).toImaginary());
    }
    
    /** Obtains the absolute value of this imaginary number (|ai|). That is,
     *  if a > 0, then ai is obtained. Otherwise, -ai is obtained.
     * 
     * @param in The imaginary number whose absolute value will be obtained
     * @return The imaginary number with the absolute value of in as described
     * above
     * @throws NullPointerException if in is null
     * @see Math#signum(double) 
     */
    public static double sgn(ImaginaryNumber in){
        return Math.signum(in.getImaginary());
    }

    /** Obtains the square root of a real number.<br>
     * Unlike the sqrt method in JSE's Math class, if the value of the
     * real number is negative, an imaginary number is returned instead
     * of throwing an ArithmeticException.
     * @param rn The real number whose square root is calculated.
     * @return A real number with the square root of rn's encapsulated number
     * if that number is positive, A real number encapsulating a  
     * zero if it's zero, or an ImaginaryNumber with a numeric coefficient
     * that is equal to the squared root of the absolute value of rn's encapsulated
     * number if it's negative.
     * @throws NullPointerException if rn is null
     * @see Math#sqrt(double)
     * @see Math#signum(double)
     * @see Math#abs(double) 
     * @see ArithmeticException
     */
    public static MathResult sqrt(RealNumber rn){
        return switch((int)Math.signum(rn.doubleValue())){
            case 1 -> new RealNumber(Math.sqrt(rn.doubleValue()));
            case -1 -> new ImaginaryNumber(Math.sqrt(Math.abs(rn.doubleValue())));
            default -> new RealNumber(0);
        };
    }

    /** Obtains the square root of a real number.<br>
     * Unlike the sqrt method in JSE's Math class, if the value of the
     * real number is negative, an imaginary number is returned instead
     * of throwing an ArithmeticException.<br>
     * Also, unlike sqrt(RealNumber), this method ensures that a 
     * NullPointerException will never be thrown.
     * @param d The number whose squared root will be calculated.
     * @return A real number with the square root of d
     * if d is positive, zero if it's zero, or an ImaginaryNumber with a 
     * numeric coefficient that is equal to the squared root of the absolute 
     * value of d if it's negative.
     * @see Math#sqrt(double)
     * @see Math#signum(double)
     * @see Math#abs(double) 
     * @see ArithmeticException
     * @see #sqrt(RealNumber) 
     */
    public static MathResult sqrt(double d){
        return sqrt(new RealNumber(d));
    }

    /** Calculates the squared root of an imaginary number.
     *  See nrt(ImaginaryNumber, int) for more details
     * @param in The imaginary number whose squared root will be calculated.
     * @return A list of complex numbers having two solutions as described in
     * nrt(ImaginaryNumber, int)
     * @throws NullPointerException if in is null
     * @see #nrt(ImaginaryNumber, int)
     */
    public static List<ComplexNumber> sqrt(ImaginaryNumber in){
        return nrt(in, 2);
    }

    /** Calculates the cube root of an imaginary number.
     *  See nrt(ImaginaryNumber, int) for more details
     * @param in The imaginary number whose cube root will be calculated.
     * @return A list of complex numbers having three solutions as described in
     * nrt(ImaginaryNumber, int)
     * @throws NullPointerException if in is null
     * @see #nrt(ImaginaryNumber, int)
     */
    public static List<ComplexNumber> cbrt(ImaginaryNumber in){
        return nrt(in, 3);
    }

    /** Calculates the nth root of an imaginary number.
     *  If n is positive, this results in n solutions in which, given an
     *  imaginary number b = ai for any "a" distinct of zero,
     *  each kth solution, with 0 <= k <= n-1, will be a complex number that is
     *  the conjugate of e^(in) ponderated by the following scalar:<br><br>
     *  2 * pi * ((k + sgn(a)) / 4) / n<br><br>
     *  However, if n is negative, the nth root of the multiplicative inverse
     *  of that imaginary number will be calculated instead.
     * @param in The imaginary number whose nth square root will be calculated
     * @param n The number of the root to be calculated.
     * @return A list of complex numbers having n elements, in which each element
     * is calculated as described above.
     * @throws NullPointerException if in is null
     * @throws ArithmeticException if n is zero
     * @see #pow(ImaginaryNumber, int) 
     * @see #exp(ImaginaryNumber)
     * @see #sgn(ImaginaryNumber)
     * @see ComplexMath#conjugate(ComplexNumber)
     * @see Math#pow(double, double)
     * @see Math#PI
     */
    public static List<ComplexNumber> nrt(ImaginaryNumber in, int n){
        if(n == 0){
            throw new ArithmeticException("The number of roots cannot be zero");
        }
        if(n == 1){
            return List.of(new ComplexNumber(in));
        }
        if(n < 0){
            return nrt(pow(in, -1).toImaginary(), Math.abs(n));
        }
        double num = Math.pow(Math.abs(in.getImaginary()), 1d/n);
        double t = arg(in);
        List<ComplexNumber> roots = new ArrayList<>();
        IntStream.range(0, n).forEach(k -> {
            double fact = (t + 2 * Math.PI * k) / n;
            roots.add(ComplexMath.ponderate(exp(new ImaginaryNumber(fact)), num));
        });
        return roots;
    }

    /** Calculates the sine of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the hyperbolic sine
     * of a
     * @param in The imaginary number whose sine will be calculated.
     * @return The sine of in as described above
     * @throws NullPointerException if in is null
     * @see Math#sinh(double)
     */
    public static ImaginaryNumber sin(ImaginaryNumber in){
        return new ImaginaryNumber(Math.sinh(in.getImaginary()));
    }

    /** Calculates the cosine of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * a real number equal to the hyperbolic cosine of a
     * @param in The imaginary number whose cosine will be calculated.
     * @return The cosine of in as described above
     * @throws NullPointerException if in is null
     * @see Math#cosh(double)
     */
    public static double cos(ImaginaryNumber in){
        return Math.cosh(in.getImaginary());
    }

    /** Calculates the tangent of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the hyperbolic
     * tangent of a
     * @param in The imaginary number whose tangent will be calculated.
     * @return The tangent of in as described above
     * @throws NullPointerException if in is null
     * @see Math#tanh(double)
     */
    public static ImaginaryNumber tan(ImaginaryNumber in){
        return new ImaginaryNumber(Math.tanh(in.getImaginary()));
    }

    /**
     * Calculates the secant of an imaginary number, that is, the multiplicative
     * inverse of the cosine of that number
     * @param in The imaginary number whose secant will be calculated.
     * @return the secant of in as described above
     * @throws NullPointerException if in is null
     * @see #cos(ImaginaryNumber)
     * @see ExtendedMath#sech(double)
     */
    public static double sec(ImaginaryNumber in){
        return ExtendedMath.sech(in.getImaginary());
    }

    /**
     * Calculates the cosecant of an imaginary number, that is, the multiplicative
     * inverse of the sine of that number.<br>
     * Unlike the cosecant of a real number, the cosecant of an imaginary number
     * is multiplied by -1.
     * @param in The imaginary number whose cosecant will be calculated.
     * @return the cosecant of in as described above
     * @throws NullPointerException if in is null
     * @see #sin(ImaginaryNumber)
     */
    public static ImaginaryNumber csc(ImaginaryNumber in){
        return new ImaginaryNumber(ExtendedMath.csch(in.getImaginary()));
    }

    /**
     * Calculates the cotangent of an imaginary number, that is, the multiplicative
     * inverse of the tangent of that number.<br>
     * Unlike the cotangent of a real number, the cotangent of an imaginary
     * number is multiplied by -1.
     * @param in The imaginary number whose cotangent will be calculated.
     * @return the cotangent of in as described above
     * @throws NullPointerException if in is null
     * @see #tan(ImaginaryNumber)
     */
    public static ImaginaryNumber cot(ImaginaryNumber in){
        return new ImaginaryNumber(-1 / Math.tanh(in.getImaginary()));
    }

    /** Calculates the hyperbolic sine of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the sine of a
     * @param in The imaginary number whose hyperbolic sine will be calculated.
     * @return The hyperbolic sine of in as described above
     * @throws NullPointerException if in is null
     * @see Math#sin(double)
     */
    public static ImaginaryNumber sinh(ImaginaryNumber in){
        return new ImaginaryNumber(Math.sin(in.getImaginary()));
    }

    /** Calculates the hyperbolic cosine of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * a real number equal to the cosine of a
     * @param in The imaginary number whose hyperbolic cosine will be calculated.
     * @return The hyperbolic cosine of in as described above
     * @throws NullPointerException if in is null
     */
    public static double cosh(ImaginaryNumber in){
        return Math.cos(in.getImaginary());
    }

    /** Calculates the hyperbolic tangent of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the tangent of a
     * @param in The imaginary number whose hyperbolic tangent will be calculated.
     * @return The hyperbolic tangent of in as described above
     * @throws NullPointerException if in is null
     * @throws ArithmeticException if the hyperbolic cosine of in is zero
     * @see Math#tan(double)
     * @see #cosh(ImaginaryNumber)
     */
    public static ImaginaryNumber tanh(ImaginaryNumber in){
        return new ImaginaryNumber(Math.tan(in.getImaginary()));
    }

    /**
     * Calculates the hyperbolic secant of an imaginary number, that is, the
     * multiplicative inverse of the hyperbolic cosine of that imaginary number.
     * @param in the imaginary number whose hyperbolic secant will be calculated.
     * @return The hyperbolic secant of in as described above.
     * @throws NullPointerException if in is null.
     * @throws ArithmeticException if the absolute value of the numeric coefficient
     * of in has the form n * pi / 2 for any non-zero integer number, as it's
     * cosine is zero.
     * @see Math#cos(double)
     * @see ExtendedMath#sec(double)
     */
    public static double sech(ImaginaryNumber in){
        return ExtendedMath.sec(in.getImaginary());
    }

    /**
     * Calculates the hyperbolic cosecant of an imaginary number, that is, the
     * multiplicative inverse of the hyperbolic sine of that imaginary number.
     * @param in the imaginary number whose hyperbolic cosecant will be calculated.
     * @return The hyperbolic cosecant of in as described above.
     * @see Math#sin(double)
     * @see ExtendedMath#csc(double)
     */
    public static ImaginaryNumber csch(ImaginaryNumber in){
        return new ImaginaryNumber(ExtendedMath.csc(in.getImaginary()));
    }

    /** Calculates the hyperbolic cotangent of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the cotangent of a
     * @param in The imaginary number whose hyperbolic cotangent will be calculated.
     * @return The hyperbolic cotangent of in as described above
     * @throws NullPointerException if in is null
     * @see Math#tan(double)
     * @see ExtendedMath#cot(double) 
     */
    public static ImaginaryNumber coth(ImaginaryNumber in){
        return new ImaginaryNumber(ExtendedMath.cot(in.getImaginary()));
    }

    /** Calculates the value of e raised to the power of an imaginary number bi.
     *  The result is equal to the complex number cos(b) + i * sin(b) <br>
     *  But i * sin(b) is equal to sinh(bi), so the expression above is equivalent to
     *  cos(b) + sinh(bi)<br>
     *  Notice that, due to trigonometric properties, cos(b) = cos(-b). Thus, if b is negative,
     *  only the imaginary part will be negative.
     * @param in The imaginary used as exponent to raise e to the power of
     * @return A complex number that results from the calculations mentioned above.
     * @see Math#cos(double)
     * @see Math#sin(double)
     * @see Math#E
     * @see ImaginaryMath#sinh(ImaginaryNumber)
     * @see ComplexNumber
     */
    public static ComplexNumber exp(ImaginaryNumber in){
        return new ComplexNumber(Math.cos(in.getImaginary()), sinh(in));
    }

    /** Calculates the arcsine of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the hyperbolic arcsine
     * of a
     * @param in The imaginary number whose arcsine will be calculated.
     * @return The arcsine of in as described above
     * @throws NullPointerException if in is null
     * @see ExtendedMath#asinh(double)
     */
    public static ImaginaryNumber asin(ImaginaryNumber in){
        return new ImaginaryNumber(ExtendedMath.asinh(in.getImaginary()));
    }

    /** Calculates the arccosine of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * a complex number whose real part is the half of PI and the imaginary part
     * is the hyperbolic arcsine of the numeric coefficient of the imaginary number
     * multiplied by -1.
     * @param in The imaginary number whose arccosine will be calculated.
     * @return The arccosine of in as described above
     * @throws NullPointerException if in is null
     * @see ExtendedMath#asinh(double)
     * @see Math#PI
     */
    public static ComplexNumber acos(ImaginaryNumber in){
        return new ComplexNumber(Math.PI / 2, -1 * ExtendedMath.asinh(in.getImaginary()));
    }

    /** Calculates the arctangent of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the hyperbolic arctangent
     * of a
     * @param in The imaginary number whose arctangent will be calculated.
     * @return The arctangent of in as described above
     * @throws NullPointerException if in is null
     * @throws ArithmeticException if the value of "a" as described above causes
     * atanh(a) throw an ArithmeticException
     * @see ExtendedMath#atanh(double)
     */
    public static ImaginaryNumber atan(ImaginaryNumber in){
        return new ImaginaryNumber(ExtendedMath.atanh(in.getImaginary()));
    }

    /** Calculates the arcsecant of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * a complex number whose real part is the half of PI and the imaginary part
     * is the hyperbolic arcsine of the multiplicative inverse of the numeric
     * coefficient of the imaginary number.
     * @param in The imaginary number whose arcsecant will be calculated.
     * @return The arcsecant of in as described above
     * @throws NullPointerException if in is null
     * @see ExtendedMath#asinh(double)
     * @see Math#PI
     */
    public static ComplexNumber asec(ImaginaryNumber in){
        return new ComplexNumber(Math.PI / 2, ExtendedMath.asinh(1 / in.getImaginary()));
    }

    /** Calculates the arccosecant of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient is the product between the
     * hyperbolic arcsine of the multiplicative inverse of a and -1.
     * @param in The imaginary number whose arccosecant will be calculated.
     * @return The arccosecant of in as described above
     * @throws NullPointerException if in is null
     * @see ExtendedMath#asinh(double)
     * @see Math#PI
     */
    public static ImaginaryNumber acsc(ImaginaryNumber in){
        return new ImaginaryNumber(-1 * ExtendedMath.asinh(1 / in.getImaginary()));
    }

    /** Calculates the arccotangent of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the product between
     * the hyperbolic arctangent of the multiplicative inverse of a and -1.
     * @param in The imaginary number whose arctangent will be calculated.
     * @return The arctangent of in as described above
     * @throws NullPointerException if in is null
     * @throws ArithmeticException if the value of "a" as described above causes
     * atanh(1/a) throw an ArithmeticException
     * @see ExtendedMath#atanh(double)
     */
    public static ImaginaryNumber acot(ImaginaryNumber in){
        return new ImaginaryNumber(-1 * ExtendedMath.atanh(1 / in.getImaginary()));
    }

    /** Calculates the hyperbolic arcsine of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the arcsine of a
     * @param in The imaginary number whose hyperbolic arcsine will be calculated.
     * @return The hyperbolic arcsine of in as described above
     * @throws NullPointerException if in is null
     * @throws ArithmeticException if the absolute value of "a" as described above
     * is greater than 1.
     * @see Math#asin(double)
     */
    public static ImaginaryNumber asinh(ImaginaryNumber in){
        double coef = Math.asin(in.getImaginary());
        if(Double.valueOf(coef).isNaN()){
            throw new ArithmeticException("The numeric coefficient of the specified imaginary number is outside the range of arcsin(x)");
        }
        return new ImaginaryNumber(coef);
    }

    /** Calculates the hyperbolic arccosine of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the arccosine of a
     * @param in The imaginary number whose hyperbolic arccosine will be calculated.
     * @return The hyperbolic arccosine of in as described above
     * @throws NullPointerException if in is null
     * @throws ArithmeticException if the absolute value of "a" as described above
     * is greater than 1.
     * @see Math#acos(double)
     */
    public static ImaginaryNumber acosh(ImaginaryNumber in){
        double coef = Math.acos(in.getImaginary());
        if(Double.valueOf(coef).isNaN()){
            throw new ArithmeticException("The numeric coefficient of the specified imaginary number is outside the range of arccos(x)");
        }
        return new ImaginaryNumber(coef);
    }

    /** Calculates the hyperbolic arctangent of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the arctangent of a
     * @param in The imaginary number whose hyperbolic arctangent will be calculated.
     * @return The hyperbolic arctangent of in as described above
     * @throws NullPointerException if in is null
     * @see Math#atan(double)
     */
    public static ImaginaryNumber atanh(ImaginaryNumber in){
        return new ImaginaryNumber(Math.atan(in.getImaginary()));
    }

    /** Calculates the hyperbolic arcsecant of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * a complex number whose real part is the hyperbolic arcsine of 1/a and the
     * imaginary part is the half of PI.
     * @param in The imaginary number whose hyperbolic arcsecant will be calculated.
     * @return The hyperbolic arcsecant of in as described above
     * @throws NullPointerException if in is null
     * @see ExtendedMath#asinh(double)
     */
    public static ComplexNumber asech(ImaginaryNumber in){
        return new ComplexNumber(ExtendedMath.asinh(1 / in.getImaginary()), Math.PI / 2);
    }

    /** Calculates the hyperbolic arccosecant of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the product between
     * -1 and arcsine of 1/a
     * @param in The imaginary number whose hyperbolic arccosecant will be calculated.
     * @return The hyperbolic arccosine of in as described above
     * @throws NullPointerException if in is null
     * @throws ArithmeticException if given the value of "a" as described above,
     * the value of |1/a| is greater than 1
     * @see Math#asin(double)
     */
    public static ImaginaryNumber acsch(ImaginaryNumber in){
        double coef = Math.asin(1 / in.getImaginary());
        if(Double.valueOf(coef).isNaN()){
            throw new ArithmeticException("The numeric coefficient of the specified imaginary number is outside the range of arcsin(x)");
        }
        return new ImaginaryNumber(-1 * Math.asin(1 / in.getImaginary()));
    }

    /** Calculates the hyperbolic arccotangent of an imaginary number.<br>
     * For an imaginary number b = ai, with "a" distinct of zero, this results in
     * an imaginary number whose numeric coefficient will be the product between
     * -1 and the arctangent of 1/a
     * @param in The imaginary number whose hyperbolic arctangent will be calculated.
     * @return The hyperbolic arctangent of in as described above
     * @throws NullPointerException if in is null
     * @see Math#atan(double)
     */
    public static ImaginaryNumber acoth(ImaginaryNumber in){
        return new ImaginaryNumber(-1 * Math.atan(1 / in.getImaginary()));
    }

    /**
     * Calculates the argument function of an imaginary number
     * @param in The imaginary number whose argument will be calculated.
     * @return The half of PI if the numeric coefficient of in is positive,
     * the half of pi multiplied by -1 otherwise.
     * @throws NullPointerException if in is null
     * @see Math#PI
     * @see Math#signum(double)
     */
    public static double arg(ImaginaryNumber in){
        return Math.signum(in.getImaginary()) * Math.PI / 2;
    }

    /** Calculates the base i logarithm of a real number, where i is the imaginary
     *  unit.<br>
     *  The result is an imaginary number whose numeric coefficient is the
     *  result of the division between the negative double of ln(num) and PI.
     * @param num The real number whose base i logarithm is calculated
     * @return the base i logarithm of num as described above.
     * @throws ArithmeticException if num is not positive, as the logarithm of a
     * non-positive number (including zero) on any base does not exist.
     * @see Math#log(double)
     * @see Math#PI
     */
    public static ImaginaryNumber logI(double num){
        double coef = (-2 * Math.log(num))/ Math.PI;
        if(Double.valueOf(coef).isNaN()){
            throw new ArithmeticException("The numeric coefficient of the specified imaginary number is outside the range of log(x)");
        }
        return new ImaginaryNumber(coef);
    }

    /** Calculates the "bi" base logarithm of a real number, where i is the imaginary
     *  unit.<br>
     * This kind of logarithm has an infinite amount of solutions. For this reason,
     * a third parameter consisting in the kth value of the logarithm must be
     * specified.
     * Based on what is mentioned above, the kth value of the "bi" base logarithm
     * of a real number num is a complex number generated as follows:
     <ul>
     *     <li>The real part is the natural logarithm of the absolute value
     *     of b</li>
     *     <li>The imaginary part is the result of PI * (0,5 + (2 * k))</li>
     *     <li>The complex number that results of summing both parts is
     *    ponderated by the natural logarithm of num</li>
     *     <li>Finally, the multiplicative inverse of the resulting complex
     *     number is calculated.</li>
     *     </ul>
     * If you wish to obtain the principal value of the logarithm, make k's value
     * zero.
     * @param num The real number whose base bi logarithm is calculated
     * @param base the base used to calculate the logarithm
     * @param k an integer number to indicate that the kth value of the logarithm
     * will be obtained. It can be zero to obtain the principal value.
     * @return The kth base bi logarithm of num, as described above.
     * @throws NullPointerException if base is null.
     * @throws ArithmeticException if num is not positive, as the logarithm of a
     * non-positive number (including zero) on any base does not exist.</li>
     * @see Math#log(double)
     * @see Math#abs(double)
     * @see Math#PI
     * @see ComplexMath#ponderate(ComplexNumber, double)
     * @see ComplexMath#multiplicativeInverse(ComplexNumber)
     */
    public static ComplexNumber logI(double num, ImaginaryNumber base, int k){
        double scalar = Math.log(num);
        if(Double.valueOf(scalar).isNaN()){
            throw new ArithmeticException("The numeric coefficient of the specified imaginary number is outside the range of log(x)");
        }
        return ComplexMath.multiplicativeInverse(
            ComplexMath.ponderate(
                new ComplexNumber(
                    Math.log(Math.abs(base.getImaginary())),
                    arg(base) + (2 * k * Math.PI)),
                scalar)
        );
    }

/** Calculates the "bi" base logarithm of a real number, where i is the imaginary
    unit.<br>
    Using this method will allow to obtain the principal value of the logarithm.
  * @param num The real number whose base bi logarithm is calculated
  * @param base the base used to calculate the logarithm
  * @return The principal value of the bi logarithm of num, as described above.
  * @throws NullPointerException if base is null.
  * @throws ArithmeticException if num is not positive, as the logarithm of a
  * non-positive number (including zero) on any base does not exist.
 */
    public static ComplexNumber logI(double num, ImaginaryNumber base){
        return logI(num, base, 0);
    }

    /** Calculates the b base logarithm of an imaginary number ai.<br>
     * This kind of logarithm has an infinite amount of solutions. For this reason,
     * a third parameter consisting in the kth value of the logarithm must be
     * specified.
     * Based on what is mentioned above, the kth value of the base logarithm
     * of an imaginary number bi is represented by two complex numbers defined as
     * follows: <ul>
     <li>The real part is the natural logarithm of the absolute value
     *     of b</li>
     *     <li>The imaginary part is the result of PI * (0,5 + (2 * k))</li>
     *     <li>The complex number that results of summing both parts is
     *    ponderated by the multiplicative inverse of the natural logarithm of b
     *    </li>
     *    </ul>
     * If you wish to obtain the principal value of the logarithm, make k's value
     * zero.
     * @param in The imaginary number whose base logarithm is calculated
     * @param b the base used to calculate the logarithm
     * @param k an integer number to indicate that the kth value of the logarithm
     * will be obtained. It can be zero to obtain the principal value.
     * @return The kth value of the b base logarithm of in as described above.
     * @throws NullPointerException if base is null.
     * @throws ArithmeticException if base is not positive, as the logarithm of a
     * non-positive number (including zero) on any base does not exist.
     * @see Math#log(double)
     * @see Math#abs(double)
     * @see Math#PI
     * @see ComplexMath#ponderate(ComplexNumber, double)
     */
    public static ComplexNumber logN(ImaginaryNumber in, double b, int k){
        return ComplexMath.ponderate(
            new ComplexNumber(
                    Math.log(Math.abs(in.getImaginary())),
                    arg(in) + (2 * k * Math.PI)),
        1 / Math.log(b));
    }

    /**
     Calculates the b base logarithm of an imaginary number.<br>
     Using this method will allow to obtain the principal value of the logarithm.
     *
     * @param in The imaginary number whose base logarithm is calculated
     * @param base the base used to calculate the logarithm
     * @return The principal value of the b base logarithm of in, as described above.
     * @throws NullPointerException if base is null.
     * @throws ArithmeticException if base is not positive, as the logarithm
     * of a non-positive number (including zero) on any base does not exist.
     * @see #logN(ImaginaryNumber, double, int)
     */
    public static ComplexNumber logN(ImaginaryNumber in, double base){
        return logN(in, base, 0);
    }

    /**
     Calculates the base 10 logarithm of an imaginary number.<br>
     Calling this method is the same as calling {@code logN(in, 10, k)}
     *
     * @param in The imaginary number whose base logarithm is calculated
     * @param k an integer number to indicate that the kth value of the logarithm
     * will be obtained. It can be zero to obtain the principal value.
     * @return The kth value of the 10 base logarithm of in, as described above.
     * @throws NullPointerException if base is null.
     * @throws ArithmeticException if k is negative.
     * @see #logN(ImaginaryNumber, double, int)
     */
    public static ComplexNumber log10(ImaginaryNumber in, int k){
        return logN(in, 10, k);
    }

    /**
     Calculates the base 10 logarithm of an imaginary number.<br>
     Calling this method is the same as calling {@code logN(in, 10, 0)}
     *
     * @param in The imaginary number whose base logarithm is calculated
     * @return The principal value of the 10 base logarithm of in, as described above.
     * @throws NullPointerException if base is null.
     * @see #logN(ImaginaryNumber, double, int)
     */
    public static ComplexNumber log10(ImaginaryNumber in){
        return logN(in, 10, 0);
    }

    /**
     Calculates the natural logarithm of an imaginary number.<br>
     Calling this method is the same as calling {@code logN(in, Math.E, k)}
     *
     * @param in The imaginary number whose base logarithm is calculated
     * @param k an integer number to indicate that the kth value of the logarithm
     * will be obtained. It can be zero to obtain the principal value.
     * @return The kth value of the natural logarithm of in as described above.
     * @throws NullPointerException if base is null.
     * @throws ArithmeticException if k is negative.
     * @see #logN(ImaginaryNumber, double, int)
     * @see Math#E
     */
    public static ComplexNumber log(ImaginaryNumber in, int k){
        return logN(in, Math.E, k);
    }

    /**
     Calculates the natural logarithm of an imaginary number.<br>
     Calling this method is the same as calling {@code logN(in, Math.E, 0)}
     *
     * @param in The imaginary number whose base logarithm is calculated
     * @return The principal value of the natural logarithm of in, as described above.
     * @throws NullPointerException if base is null.
     * @see #logN(ImaginaryNumber, double, int)
     */
    public static ComplexNumber log(ImaginaryNumber in){
        return logN(in, Math.E, 0);
    }

    /**
     * Calculates the logarithm of an imaginary number in with an imaginary base. <br>
     * This calculation can have an infinite number of values, so k and n are
     * required to obtain the kth value of the logarithm of in and the nth value
     * of the logarithm of base.
     * Because of this, the result of the logarithm is the division of two
     * complex numbers generated as follows:
     * <ul>
     *     <li>For the first complex number:</li>
     *     <ul>
     *         <li>The real part is the natural logarithm of the absolute value of
     *         the numeric coefficient of in</li>
     *         <li>The imaginary part is the product between arg(in) and the
     *         double of the product between PI and k</li>
     *     </ul>
     *     <li>For the second complex number:</li>
     *     <ul>
     *         <li>The real part is the natural logarithm of the absolute value of
     *         the numeric coefficient of base</li>
     *         <li>The imaginary part is the product between arg(base) and the
     *         double of the product between PI and n</li>
     *     </ul>
     * </ul>
     * @param in The imaginary number whose logarithm will be calculated
     * @param base The imaginary base in which the logarithm will be calculated.
     * @param k The integer number to indicate that the kth value of the
     *          logarithm of in will be obtained. It can be zero to obtain
     *          the principal value of that logarithm.
     * @param n The integer number to indicate that the nth value of the
     *          logarithm of base will be obtained. It can be zero to obtain
     *          the principal value of that logarithm.
     * @return a ComplexNumber generated as described above.
     * @throws NullPointerException if in and/or base are null
     * 
     */
    public static ComplexNumber logI(ImaginaryNumber in, ImaginaryNumber base, int k, int n){
        double re1 = Math.log(Math.abs(in.getImaginary()));
        double re2 = Math.log(Math.abs(base.getImaginary()));
        double im1 = arg(in) + (2 * k * Math.PI);
        double im2 = arg(base) + (2 * n * Math.PI);
        return ComplexMath.divide(new ComplexNumber(re1, im1), new ComplexNumber(re2, im2));
    }

    /**
     * Calculates the logarithm of an imaginary number in with an imaginary base. <br>
     * This method is used to obtain the principal value of the logarithm
     * @param in The imaginary number whose logarithm will be calculated
     * @param base The imaginary base in which the logarithm will be calculated.
     * @return a ComplexNumber generated as described in 
     * logI(ImaginaryNumber, ImaginaryNumber, int, int).
     * @throws NullPointerException if in and/or base are null
     * @see #logI(ImaginaryNumber, ImaginaryNumber, int, int) 
     */
    public static ComplexNumber logI(ImaginaryNumber in, ImaginaryNumber base){
        return logI(in, base, 0, 0);
    }
}
