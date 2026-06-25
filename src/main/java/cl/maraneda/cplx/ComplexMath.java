package cl.maraneda.cplx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.stream.IntStream;

/**
 * This class contains methods for mathematical operations with complex numbers.
 *
 * @author Marco Araneda
 * @version 1.0
 * @since 2026-06-01
 */
public class ComplexMath {
    private ComplexMath() {/* Nothing */}

    /**
     * Ponderates a complex number by a scalar. That is, for a complex number
     * a + bi and a scalar s, this method returns the complex number as +
     * bsi
     * @param cn The complex number to be ponderated
     * @param scalar The scalar the complex number will be ponderated by
     * @return The complex number resulting of the ponderation of cn by
     * the scalar as described above.
     * @throws NullPointerException if cn is null
     */
    public static ComplexNumber ponderate(ComplexNumber cn, double scalar){
        return new ComplexNumber(scalar * cn.getRealAsDouble(), scalar * cn.getImaginary());
    }

    /**
     * Calculates the difference between 2 complex numbers. That is, for a complex
     * number a + bi and a complex number c + di, the result is the complex number
     * a - c + (b - d)i
     * @param cn1 The first operand of the substraction
     * @param cn2 The second operand of the substraction
     * @return The result of cn1 - cn2 as described above
     * @throws NullPointerException if cn1 and/or cn2 are null
     */
    public static ComplexNumber diff(ComplexNumber cn1, ComplexNumber cn2){
        return new ComplexNumber(cn1.getRealAsDouble() - cn2.getRealAsDouble(), cn1.getImaginary() - cn2.getImaginary());
    }

    /**
     * Calculates the sum between all complex numbers specified as arguments.<br>
     * Given the complex numbers a<sub>1</sub> + b<sub>1</sub>i, a<sub>2</sub> +
     * b<sub>2</sub>i, ..., a<sub>n</sub> + b<sub>n</sub>i, the result of the sum
     * is the complex number a<sub>1</sub> + a<sub>2</sub> + ... + a<sub>n</sub> + (
     * b<sub>1</sub> + b<sub>2</sub> + ... + b<sub>n</sub>)i
     * @param ops The complex numbers specified as operands
     * @return The sum between all specified complex numbers. That sum will be
     * returned as a RealNumber if the sum of the imaginary part of all operands
     * is zero. Otherwise, it will be returned as a ComplexNumber
     * @throws IllegalArgumentException if less than two arguments are specified.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static MathResult sum(ComplexNumber... ops){
        if(ops == null || ops.length < 2){
            throw new IllegalArgumentException("Two operands are required for the sum");
        }
        DoubleAccumulator realPart = new DoubleAccumulator(Double::sum, ops[0].getRealAsDouble());
        DoubleAccumulator imaginaryPart = new DoubleAccumulator(Double::sum, ops[0].getImaginary());
        IntStream.range(1, ops.length).forEach(i -> {
            realPart.accumulate(ops[i].getRealAsDouble());
            imaginaryPart.accumulate(ops[i].getImaginary());
        });
        double realS = realPart.get();
        double imaginaryS = imaginaryPart.get();
        return imaginaryS == 0 ? new RealNumber(realS) : new ComplexNumber(realS, imaginaryS);
    }

    /**
     * Calculates the productory between all complex numbers specified as arguments.<br>
     * Given two complex numbers a + bi and c + di, the product between both is
     * a complex number whose real part is the result of (a * c) - (b * d) and it's
     * imaginary part is the result of (a * d) + (b * c).<br>
     * As this is an algebraic operation, two things will happen:
     * <ul>
     *    <li>The result of multiply 2 complex numbers will always be a
     *    binomial expression</li>
     *    <li>Two or more complex numbers can be multiplied by applying the
     *    associative axiom. For example: c<sub>1</sub> * c<sub>2</sub> *
     *    c<sub>3</sub> = (c<sub>1</sub> * c<sub>2</sub>) * c<sub>3</sub></li>
     * </ul>
     * @param ops The complex numbers specified as operands
     * @return The product of all specified arguments.
     * @throws IllegalArgumentException if less than two arguments are specified.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static ComplexNumber multiply(ComplexNumber... ops) {
        if (ops == null || ops.length < 2) {
            throw new IllegalArgumentException(
                    "At least two operands are required");
        }
        double real = ops[0].getRealAsDouble();
        double imag = ops[0].getImaginary();
        for (int i = 1; i < ops.length; i++) {
            double nextReal = ops[i].getRealAsDouble();
            double nextImag = ops[i].getImaginary();
            double newReal = real * nextReal - imag * nextImag;
            double newImag = real * nextImag + imag * nextReal;
            real = newReal;
            imag = newImag;
        }
        return new ComplexNumber(real, imag);
    }

    /**
     * Obtains the conjugate of a complex number. That is, given a complex
     * number a + bi, this method returns a - bi and viceversa.
     * @param cn The complex number whose conjugate will be calculated.
     * @return The conjugate of cn as described above
     * @throws NullPointerException if cn is null.
     */
    public static ComplexNumber conjugate(ComplexNumber cn){
        return new ComplexNumber(cn.getReal(), -1 * cn.getImaginary());
    }

    public static ComplexNumber additiveInverse(ComplexNumber cn){
        return ponderate(cn, -1);
    }
    /**
     * Calculates the modulus of a complex number.<br>
     * Given one complex number a + bi, the modulus will be always a positive or
     * zero real number that results of the square root of a<sup>2</sup> +
     * b<sup>2</sup>.<br>
     * NOTE: This method is required for other methods from this class that may
     * throw an ArithmeticException if the returned value of this method for a
     * complex number is zero, which is possible if, and only if, both the real and
     * the imaginary part of cn are zero.
     * @param cn The complex number whose modulus will be calculated.
     * @return The modulus of cn as described above
     * @throws NullPointerException if cn is null.
     */
    public static double modulus(ComplexNumber cn){
        return Math.sqrt(Math.pow(cn.getRealAsDouble(), 2) + Math.pow(cn.getImaginary(), 2));
    }

    /**
     * Calculates a complex number raised to the power of an integer exponent.<br>
     * The result depends on the value of exp
     * <ul>
     *     <li>The real number zero if the base is the complex number zero
     *     (no real nor imaginary part) and the exponent is not zero</li>
     *     <li>The same cn if exp is 1</li>
     *     <li>cn multiplied by itself (exp - 1) times if exp > 1</li>
     *     <li>The multiplicative inverse of cn if exp is -1</li>
     *     <li>If exp < 1, let m be the modulus of cn and cj the conjugate of cn. Then,
     *     cj is multiplied by itself (|exp| - 1) times and the
     *     result of that productory will be ponderated by m<sup>-2</sup></li>
     * </ul>
     * @param cn The complex number used as a base
     * @param exp The exponent cn will be raised to the power of
     * @return The result of cn<sup>exp</sup> as mentioned above
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if one of the following happens:
     * <ul>
     *     <li>cn represents the complex number zero and exp is zero, as it's no
     *     possible to calculate 0<sup>0</sup></li>
     *     <li>exp is negative and cn's modulus is zero, as the denominator
     *     of the resulting scalar cannot be zero.</li>
     * </ul>
     * @see #multiply(ComplexNumber...)
     * @see #multiplicativeInverse(ComplexNumber)
     */
    public static MathResult pow(ComplexNumber cn, int exp){
        return switch(exp){
            case 0 -> {
                if(modulus(cn) == 0){
                    throw new ArithmeticException("Cannot rise zero to the power of zero");
                }
                yield new RealNumber(1);
            }
            case 1 -> cn;
            case -1 -> multiplicativeInverse(cn);
            default -> {
                if(exp > 1) {
                    ComplexNumber[] ops = new ComplexNumber[exp];
                    Arrays.fill(ops, cn);
                    yield multiply(ops);
                }else{
                    if(modulus(cn) == 0){
                        throw new ArithmeticException("Cannot use a complex number with a modulus of zero");
                    }
                    ComplexNumber[] ops = new ComplexNumber[Math.abs(exp)];
                    Arrays.fill(ops, conjugate(cn));
                    double scalar = Math.pow(modulus(cn), -2d * exp);
                    yield ponderate(multiply(ops), scalar);
                }
            }
        };
    }

    /**
     * Divides two complex numbers and calculates the result.<br>
     * Given 2 complex numbers a + bi and c + di, the division between both
     * numbers is a complex number ponderated by a scalar.
     * Both expressions are calculated as follows.
     * <ul>
     *     <li>The real part is the result of (a * c) + (b * d)</li>
     *     <li>The imaginary part is the result of (a * d) - (b * c)</li>
     *     <li>The scalar is "the multiplicative inverse of the modulus of
     *     the second complex number" squared</li>
     * </ul>
     * @param cn1 The first operand of the division
     * @param cn2 The second operand of the division
     * @return The result of cn1 / cn2
     * @throws NullPointerException if cn1 and/or cn2 are null
     * @throws ArithmeticException if the modulus of cn2 is zero, as numbers
     * cannot be divided by zero.
     * @see #modulus(ComplexNumber)
     * @see #ponderate(ComplexNumber, double)
     */
    public static ComplexNumber divide(ComplexNumber cn1, ComplexNumber cn2){
        double scalar = Math.pow(modulus(cn2), -2d);
        if(scalar == 0){
            throw new ArithmeticException("Cannot divide 2 complex numbers when the second has a modulus of zero");
        }
        double real = (cn1.getRealAsDouble() * cn2.getRealAsDouble()) + (cn1.getImaginary() * cn2.getImaginary());
        double imaginary = (cn1.getRealAsDouble() * cn2.getImaginary()) - (cn1.getImaginary() * cn2.getRealAsDouble());
        return ponderate(new ComplexNumber(real, imaginary), scalar);
    }

    /**
     * Obtains the polar representation of a complex number given one angle
     * expressed in degrees.<br>
     * Calling this method is the same as calling polar(cn, angle / 180)
     * @param cn The complex number whose polar representation will be calculated
     * @param angle The angle expressed in degrees used for the polar representation.
     * @return The polar representation of cn.
     * @throws NullPointerException if cn is null
     * @see #polar(ComplexNumber, double)
     */
    public static ComplexNumber polarDeg(ComplexNumber cn, double angle){
        return polar(cn, angle / 180);
    }

    /**
     * Obtains the polar representation of a complex number given one angle
     * factor.<br>
     * Given a complex number cn and a factor, let f = factor * &pi; an angle
     * expressed in radians. The polar representation of the complex number is
     * the complex number cos(f) + i * sin(f) ponderated by the modulus of cn.
     * @param cn The complex number whose polar representation will be calculated
     * @param piFactor The factor to be multiplied with &pi; to obtain the angle
     * expressed in radians.
     * @return The polar representation of cn as described above
     * @throws NullPointerException if cn is null.
     * @see Math#cos(double)
     * @see Math#sin(double)
     * @see #modulus(ComplexNumber)
     * @see #ponderate(ComplexNumber, double)
     */
    public static ComplexNumber polar(ComplexNumber cn, double piFactor){
        double rad = piFactor * Math.PI;
        return ponderate(new ComplexNumber(Math.cos(rad), Math.sin(rad)), modulus(cn));
    }

    /**
     * Calculates the polar multiplication between 2 complex numbers given one
     * angle expressed in degrees for each one.<br>
     * Calling this method is the same as calling 
     * polarMultiply(cn1, cn2, angle1 / 180, angle2 / 180)
     * @param cn1 The first operand of the multiplication.
     * @param cn2 The second operand of the multiplication.
     * @param angle1 The angle associated to cn1 expressed in degrees
     * @param angle2 The angle associated to cn2 expressed in degrees
     * @return The polar multiplication between cn1 and cn2
     * @throws NullPointerException if cn1 and/or cn2 are null
     * @see #polarMultiply(ComplexNumber, ComplexNumber, double, double) 
     */
    public static ComplexNumber polarMultiplyDeg(ComplexNumber cn1, ComplexNumber cn2, double angle1, double angle2){
        return polarMultiply(cn1, cn2, angle1 / 180, angle2 / 180);
    }

    /**
     * Calculates the polar multiplication between 2 complex numbers given one
     * angle factor for each one<br>.
     * Given two complex numbers cn<sub>1</sub> and cn<sub>2</sub> and
     * two factors f<sub>1</sub> and f<sub>2</sub>, let a<sub>1</sub> =
     * f<sub>1</sub> * &pi; and a<sub>2</sub> = f<sub>2</sub> * &pi; angles
     * expressed in radians. The polar multiplication
     * of cn<sub>1</sub> and cn<sub>2</sub> is the complex number
     * cos(a<sub>1</sub> + a<sub>2</sub>) +
     * i * sin(a<sub>1</sub> + a<sub>2</sub>) ponderated by the product of
     * the modulus of each complex number.
     * @param cn1 The first operand of the multiplication.
     * @param cn2 The second operand of the multiplication.
     * @param factor1 The factor to be multiplied by &pi; in order to obtain the
     * angle associated to cn1 expressed in radians.
     * @param factor2 The factor to be multiplied by &pi; in order to obtain the
     * angle associated to cn2 expressed in radians.
     * @return The polar multiplication between cn1 and cn2 as described above.
     * @throws NullPointerException if cn1 and/or cn2 are null.
     * @see Math#cos(double)
     * @see Math#sin(double)
     * @see #modulus(ComplexNumber)
     * @see #ponderate(ComplexNumber, double)
     */
    public static ComplexNumber polarMultiply(ComplexNumber cn1, ComplexNumber cn2, double factor1, double factor2){
        double rad1 = factor1 * Math.PI;
        double rad2 = factor2 * Math.PI;
        return ponderate(new ComplexNumber(Math.cos(rad1 + rad2), Math.sin(rad1 + rad2)), modulus(cn1) * modulus(cn2));
    }

    /**
     * Calculates the polar division between 2 complex numbers given one
     * angle factor for each one<br>.
     * Given two complex numbers cn<sub>1</sub> and cn<sub>2</sub> and
     * two factors f<sub>1</sub> and f<sub>2</sub>, let a<sub>1</sub> =
     * f<sub>1</sub> * &pi; and a<sub>2</sub> = f<sub>2</sub> * &pi; angles
     * expressed in radians. The polar division
     * of cn<sub>1</sub> and cn<sub>2</sub> is the complex number
     * cos(a<sub>1</sub> - a<sub>2</sub>) +
     * i * sin(a<sub>1</sub> - a<sub>2</sub>) ponderated by the division between
     * the modulus of each complex number.
     * @param cn1 The first operand of the division.
     * @param cn2 The second operand of the division.
     * @param factor1 The factor to be multiplied by &pi; in order to obtain the
     * angle associated to cn1 expressed in radians.
     * @param factor2 The factor to be multiplied by &pi; in order to obtain the
     * angle associated to cn2 expressed in radians.
     * @return The polar division between cn1 and cn2 as described above.
     * @throws NullPointerException if cn1 and/or cn2 are null.
     * @throws ArithmeticException if the modulus of cn2 is zero, as the division
     * by zero is not allowed.
     * @see Math#cos(double)
     * @see Math#sin(double)
     * @see #modulus(ComplexNumber)
     * @see #ponderate(ComplexNumber, double)
     */
    public static ComplexNumber polarDivide(ComplexNumber cn1, ComplexNumber cn2, double factor1, double factor2){
        double denom = modulus(cn2);
        if(denom == 0){
            throw new ArithmeticException("Cannot polar divide 2 complex numbers if the modulus of the second is zero");
        }
        double scalar = modulus(cn1) / denom;
        double rad1 = factor1 * Math.PI;
        double rad2 = factor2 * Math.PI;
        return ponderate(new ComplexNumber(Math.cos(rad1 - rad2), Math.sin(rad1 - rad2)), scalar);
    }

    /**
     * Calculates the polar division between 2 complex numbers given one
     * angle expressed in degrees for each one.<br>
     * Calling this method is the same as calling
     * polarDivide(cn1, cn2, angle1 / 180, angle2 / 180)
     * @param cn1 The first operand of the division.
     * @param cn2 The second operand of the division.
     * @param angle1 The angle associated to cn1 expressed in degrees
     * @param angle2 The angle associated to cn2 expressed in degrees
     * @return The polar division between cn1 and cn2
     * @throws NullPointerException if cn1 and/or cn2 are null
     * @throws ArithmeticException if the modulus of cn2 is zero, as the division
     * by zero is not allowed.
     * @see #polarMultiply(ComplexNumber, ComplexNumber, double, double)
     */
    public static ComplexNumber polarDivideDeg(ComplexNumber cn1, ComplexNumber cn2, double angle1, double angle2){
        return polarDivide(cn1, cn2, angle1 / 180, angle2 / 180);
    }

    /**
     * Calculates the polar power of cn<sup>exp</sup> given an angle factor.<br>
     * Given a complex number cn, a factor f and an exponent b, let a = f * &pi;
     * an angle expressed in radians.
     * Then, the polar power of cn is the complex number
     * cos(a * b) + i * sin(a * b) ponderated by the modulus of cn raised to
     * the power of exp.
     * @param cn The complex number whose polar power will be calculated
     * @param factor The factor to be used to obtain the angle expressed in
     * radians
     * @param exp The exponent used to calculate the power.
     * @return cn raised to the polar power of exp given the factor as described
     * above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if both the modulus of cn and the value of exp are zero,
     * as calculate 0<sup>0</sup> is not allowed.
     * @see Math#cos(double)
     * @see Math#sin(double)
     * @see Math#pow(double, double)
     * @see #modulus(ComplexNumber)
     * @see #ponderate(ComplexNumber, double)

     */
    public static ComplexNumber polarPow(ComplexNumber cn, double factor, double exp){
        double mod = modulus(cn);
        if(exp == 0 && mod == 0){
            throw new ArithmeticException("Cannot raise a modulus to the power of zero if the modulus is zero");
        }
        double rad = factor * Math.PI;
        return ponderate(new ComplexNumber(Math.cos(exp * rad), Math.sin(exp * rad)), Math.pow(mod, exp));
    }

    /**
     * Calculates the polar power of cn<sup>exp</sup> given an angle
     * expressed in degrees.<br>
     * Calling this method is the same as calling
     * polarPow(cn, angle / 180, exp)
     * @param cn The complex number whose polar power will be calculated
     * @param angle The angle expressed in degrees.
     * @param exp The exponent used to calculate the power.
     * @return cn raised to the polar power of exp given the angle.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if both the modulus of cn and the value of exp are zero,
     * as calculate 0<sup>0</sup> is not allowed.
     * @see #polarPow(ComplexNumber, double, double)
     */
    public static ComplexNumber polarPowDeg(ComplexNumber cn, double angle, double exp){
        return polarPow(cn, angle / 180, exp);
    }

    /**
     * Calculates the polar nth root of a complex number given an angle factor.<br>
     * Given a complex number cn, an integer n and a factor f, let a = f * &pi;
     * an angle expressed in radians. The result is a list of n complex numbers
     * where, for each kth complex number, with 0 <= k < n:
     * <ul>
     *     <li>Let b =  (a + (2 * &pi; * k)) / n</li>
     *     <li>The real part is cos(b)</li>
     *     <li>The imaginary part is sin(b)</li>
     *     <li>The complex number that results of summing the real and the
     *     imaginary part is ponderated by the nth root of the modulus of cn</li>
     * </ul>
     * @param cn The complex number whose nth root is calculated
     * @param factor The factor used to obtain the angle expressed in radians
     * @param n The root to be calculated of cn
     * @return A list of complex numbers generated as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if one of the following happens:
     * <ul>
     *     <li>n is zero, as the 0th root of a number does not exist</li>
     *     <li>The modulus of cn is zero, as the nth root of zero is zero
     *     and the division by zero is not allowed.</li>
     * </ul>
     */
    public static List<ComplexNumber> polarNrt(ComplexNumber cn, double factor, int n){
        if(n == 0){
            throw new ArithmeticException("0th roots of numbers don't exist");
        }
        if(modulus(cn) == 0){
            throw new ArithmeticException("The specified complex number cannot be zero");
        }
        double rad = factor * Math.PI;
        List<ComplexNumber> values = new ArrayList<>();
        IntStream.range(0, n).forEach(k -> {
            double trigArg = (rad + (2 * Math.PI * k)) / n;
            values.add(ponderate(new ComplexNumber(Math.cos(trigArg), Math.sin(trigArg)), Math.pow(modulus(cn), 1d / n)));
        });
        return values;
    }

    /**
     * Calculates the polar nth root of a complex number given an angle 
     * expressed in degrees.<br>
     * Calling this method is the same as calling polarNrt(cn, angle / 180, n)
     * @param cn The complex number whose nth root is calculated
     * @param angle The angle expressed in degrees
     * @param n The root to be calculated of cn
     * @return A list of complex numbers generated as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if one of the following happens:
     * <ul>
     *     <li>n is zero, as the 0th root of a number does not exist</li>
     *     <li>The modulus of cn is zero, as the nth root of zero is zero
     *     and the division by zero is not allowed.</li>
     * </ul>
     * @see #polarNrt(ComplexNumber, double, int)
     */
    public static List<ComplexNumber> polarNrtDeg(ComplexNumber cn, double angle, int n){
        return polarNrt(cn, angle / 180, n);
    }

    /** It calculates the sine of a complex number.
     *  As a complex number is of the form a &pm; bi, due to trigonometric
     *  properties, this calculation is the same as:<br><br>
     *  sin(a) * cos(bi) &pm; cos(a) * sin(bi)<br><br>
     *  Which is the same as:<br><br>
     *  sin(a) * cosh(b) &pm; i * cos(a) * sinh(b)
     * @param cn the complex number whose sine will be calculated
     * @return The sine of cn as described above
     * @throws NullPointerException if cn is null
     * @see Math#sin(double)
     * @see Math#cos(double)
     */
    public static ComplexNumber sin(ComplexNumber cn){
        double op1 = Math.sin(cn.getRealAsDouble()) * ImaginaryMath.cos(cn);
        ImaginaryNumber op2 = ImaginaryMath.multiply(Math.cos(cn.getRealAsDouble()), ImaginaryMath.sin(cn)).toImaginary();
        return new ComplexNumber(op1, op2);
    }

    /** It calculates the cosine of a complex number.<br>
     *  As a complex number is of the form a &pm; bi, due to trigonometric
     *  properties, this calculation is the same as:<br><br>
     *  cos(a) * cos(bi) &mp; sin(a) * sin(bi)<br><br>
     *  Which is the same as:<br><br>
     *  cos(a) * cosh(b) &mp; i * sin(a) * sinh(b)
     * @param cn the complex number whose cosine will be calculated
     * @return The cosine of cn as described above
     * @throws NullPointerException if cn is null
     * @see Math#sin(double)
     * @see Math#cos(double)
     */
    public static ComplexNumber cos(ComplexNumber cn){
        double op1 = Math.cos(cn.getRealAsDouble()) * ImaginaryMath.cos(cn);
        ImaginaryNumber op2 = ImaginaryMath.multiply(-1 * Math.sin(cn.getRealAsDouble()), ImaginaryMath.sin(new ImaginaryNumber(cn.getImaginary()))).toImaginary();
        return new ComplexNumber(op1, op2);
    }

    /** It calculates the tangent of a complex number.<br>
     *  As a complex number is of the form a &pm; bi, due to trigonometric
     *  properties, this calculation is the same as the result of the division
     *  of the following 2 complex numbers:
     *  <ul>
     *      <li>tan(a) &pm; tan(bi), which is the same as tan(a) &pm; i * tanh(b)</li>
     *      <li>1 &mp; tan(a)tan(bi), which is the same as 1 &mp; i * tan(a)tanh(b)</li>
     *  </ul>
     * @param cn the complex number whose tangent will be calculated
     * @return The tangent of cn as described above
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cos(cn) is zero, which is posible if cn
     * meets all the following conditions:
     * <ul>
     *     <li>cn is a real number (no imaginary part)</li>
     *     <li>The real part is equal to (2n + 1) * &pi;/2 for any integer number
     *     n, regardless of sign</li>
     * </ul>
     * @see Math#tan(double)
     * @see #cos(ComplexNumber)
     * @see #divide(ComplexNumber, ComplexNumber) 
     */
    public static ComplexNumber tan(ComplexNumber cn){
        ComplexNumber cosine = cos(cn);
        if(cosine.isReal() && cosine.getRealAsDouble() == 0){
            throw new ArithmeticException("The cosine of the specified complex number must not be zero");
        }
        ComplexNumber op1 = new ComplexNumber(Math.tan(cn.getRealAsDouble()), ImaginaryMath.tan(cn));
        ComplexNumber op2 = new ComplexNumber(1d, ImaginaryMath.multiply(-1 * Math.tan(cn.getRealAsDouble()), ImaginaryMath.tan(cn)).toImaginary());
        return divide(op1, op2);
    }

    /**
     * Obtains the multiplicative inverse of a complex number.<br>
     * Calling this method is equivalent to making any of the following calls:
     * <ul>
     *     <li>pow(cn, -1)</li>
     *     <li>divide(1, cn)</li>
     * </ul>
     * @param cn The complex number whose multiplicative inverse will be calculated
     * @return The multiplicative inverse of cn as described above
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if the modulus of cn is zero
     * @see #pow(ComplexNumber, int)
     * @see #divide(ComplexNumber, ComplexNumber)
     * @see #modulus(ComplexNumber)
     */
    public static ComplexNumber multiplicativeInverse(ComplexNumber cn){
        return divide(new ComplexNumber(1, 0), cn);
    }

    /**
     * Obtains the secant of a complex number.<br>
     * Due to trigonometric properties, calling this method for a complex number
     * cn is the same as calling multiplicativeInverse(cos(cn))
     * @param cn The complex number whose secant will be calculated
     * @return The secant of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cos(cn) is zero, which is posible if cn
     * meets all the following conditions:
     * <ul>
     *     <li>cn is a real number (no imaginary part)</li>
     *     <li>The real part is equal to (2n + 1) * &pi;/2 for any integer number
     *     n, regardless of sign</li>
     * </ul>
     * @see #multiplicativeInverse(ComplexNumber) 
     * @see #cos(ComplexNumber)
     */
    public static ComplexNumber sec(ComplexNumber cn){
        ComplexNumber cosine = cos(cn);
        if(cosine.isReal() && cosine.getRealAsDouble() == 0){
            throw new ArithmeticException("The cosine of the specified complex number must not be zero");
        }
        return multiplicativeInverse(cosine);
    }

    /**
     * Obtains the cosecant of a complex number.<br>
     * Due to trigonometric properties, calling this method for a complex number
     * cn is the same as calling multiplicativeInverse(sin(cn))
     * @param cn The complex number whose cosecant will be calculated
     * @return The cosecant of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if sin(cn) is zero, which is posible if cn
     * meets all the following conditions:
     * <ul>
     *     <li>cn is a real number (no imaginary part)</li>
     *     <li>The real part is equal to n * &pi; for any integer number
     *     n, regardless of sign</li>
     * </ul>
     * @see #multiplicativeInverse(ComplexNumber)
     * @see #sin(ComplexNumber) 
     */
    public static ComplexNumber csc(ComplexNumber cn){
        ComplexNumber sine = sin(cn);
        if(sine.isReal() && sine.getRealAsDouble() == 0){
            throw new ArithmeticException("The sine of the specified complex number must not be zero");
        }
        return multiplicativeInverse(sin(cn));
    }

    /**
     * Obtains the cotangent of a complex number.<br>
     * Due to trigonometric properties, calling this method for a complex number
     * cn is the same as calling multiplicativeInverse(tan(cn))
     * @param cn The complex number whose cotangent will be calculated
     * @return The cotangent of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if sin(cn) is zero, which is posible if cn
     * meets all the following conditions:
     * <ul>
     *     <li>cn is a real number (no imaginary part)</li>
     *     <li>The real part is equal to n * &pi; for any integer number
     *     n, regardless of sign</li>
     * </ul>
     * @see #multiplicativeInverse(ComplexNumber)
     * @see #sin(ComplexNumber)
     */
    public static ComplexNumber cot(ComplexNumber cn){
        return multiplicativeInverse(tan(cn));
    }

    /**
     * Calculates the arcsine of a complex number.<br>
     * For a complex number a + bi, the arcsine of that number is a complex
     * number generated as follows:
     * <ul>
     *     <li>Let r = sqrt((a + 1)<sup>2</sup> + b<sup>2</sup>)</li>
     *     <li>Let s = sqrt((a - 1)<sup>2</sup> + b<sup>2</sup>)</li>
     *     <li>The real part is the arcsine of the half of the difference
     *     between r and s</li>
     *     <li>The imaginary part is the hyperbolic arcsine of the half of
     *     the sum between r and s</li>
     * </ul>
     * @param cn The complex number whose arcsine will be calculated
     * @return The arcsine of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn is outside the domain of the arcsine,
     * which is possible if and only if cn is real (no imaginary part) and the
     * absolute value of its real part is greater than 1.
     * @see Math#hypot(double, double)
     * @see Math#asin(double)
     * @see Math#abs(double)
     * @see Math#signum(double)
     * @see Math#hypot(double, double) 
     * @see ExtendedMath#asinh(double)
     */
    public static ComplexNumber asin(ComplexNumber cn){
        if(cn.isReal() && (Math.abs(cn.getRealAsDouble())) > 1){
            throw new ArithmeticException("Cannot calculate arcsin of real numbers whose absolute value is greater than 1");
        }
        double r = Math.hypot(cn.getRealAsDouble() + 1, cn.getImaginary());
        double s = Math.hypot(cn.getRealAsDouble() -1, cn.getImaginary());
        double u = Math.asin((r - s) / 2);
        double ss = (r + s) / 2;
        double v = ExtendedMath.asinh(ss);
        return new ComplexNumber(u, v);
    }

    /**
     * Calculates the arccosine of a complex number.<br>
     * For a complex number a + bi, the arccosine of that number is a complex
     * number generated as follows:
     * <ul>
     *     <li>Let r = sqrt((a + 1)<sup>2</sup> + b<sup>2</sup>)</li>
     *     <li>Let s = sqrt((a - 1)<sup>2</sup> + b<sup>2</sup>)</li>
     *     <li>The real part is the arccosine of the half of the difference
     *     between r and s</li>
     *     <li>The imaginary part is the product between sgn(b) and the hyperbolic
     *     arcsine of the half of the sum between r and s</li>
     * </ul>
     * @param cn The complex number whose arccosine will be calculated
     * @return The arccosine of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn is outside the domain of the arccosine,
     * which is possible if and only if cn is real (no imaginary part) and the
     * absolute value of its real part is greater than 1.
     * @see Math#hypot(double, double)
     * @see Math#asin(double)
     * @see Math#abs(double) 
     * @see Math#signum(double)
     * @see Math#hypot(double, double)
     * @see ExtendedMath#asinh(double)
     */
    public static ComplexNumber acos(ComplexNumber cn) {
        double x = cn.getRealAsDouble();
        double y = cn.getImaginary();
        double r = Math.hypot(x + 1, y);
        double s = Math.hypot(x - 1, y);
        double real = Math.acos((r - s) / 2.0);
        double t = (r + s) / 2.0;
        double imag = -Math.signum(y) * ExtendedMath.asinh(t);
        return new ComplexNumber(real, imag);
    }

    /**
     * Calculates the arctangent of a complex number.<br>
     * Given a complex number a + bi, it's arctangent is a complex number
     * that is calculated as follows:
     * <ul>
     *     <li>The real part is half of the arctangent of the cuocient
     *     between 1 - a<sup>2</sup> - b<sup>2</sup> and 2a. NOTE: Due to
     *     the behaviour of Java's implementation of the atan2 method,
     *     which is to calculate the arctangent of a division between 2 numbers,
     *     no ArithmeticException will be thrown if a = 0</li>
     *     <li>The imaginary part is the difference, divided by four, between two
     *     natural logarithms</li>
     *     <ul>
     *         <li>The argument of the first natural logarithm is a<sup>2</sup>
     *         + (b + 1)<sup>2</sup></li>
     *         <li>The argument of the second natural logarithm is a<sup>2</sup>
     *         + (b - 1)<sup>2</sup></li>
     *     </ul>
     * </ul>
     * @param cn The complex number whose arctangent will be calculated
     * @return The arctangent of cn as described above
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn represents the positive or negative 
     * imaginary unit (no real part and a numeric coefficient whose absolute
     * value equals 1), as the argument of one of the natural logarithms 
     * described above will be zero and the logarithm of zero on any base
     * is not allowed.
     * @see Math#abs(double)
     * @see Math#atan(double) 
     * @see Math#atan2(double, double)
     * @see Math#pow(double, double)
     * @see Math#log(double)
     */
    public static ComplexNumber atan(ComplexNumber cn){
        double a = cn.getRealAsDouble();
        double b = cn.getImaginary();
        if(cn.isImaginary() && Math.abs(b) == 1){
            throw new ArithmeticException("Cannot calculate the arctangent of the imaginary unit");
        }
        double u = Math.atan2(2 * a, 1 - Math.pow(a, 2) - Math.pow(b, 2)) / 2;
        double ln1 = Math.log(Math.pow(a, 2) + Math.pow(b + 1, 2));
        double ln2 = Math.log(Math.pow(a, 2) + Math.pow(b - 1, 2));
        double v = (ln1 - ln2) / 4;
        return new ComplexNumber(u, v);
    }

    /**
     * Calculates the arcsecant of a complex number.<br>
     * Due to the arcsecant's definition, calling this method is the same as
     * calling the arccosine of the multiplicative inverse of the complex number.
     * @param cn The complex number whose arcsecant will be calculated.
     * @return The arcsecant of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero.
     * @see #acos(ComplexNumber)
     * @see #multiplicativeInverse(ComplexNumber)
     */
    public static ComplexNumber asec(ComplexNumber cn){
        return acos(multiplicativeInverse(cn));
    }

    /**
     * Calculates the arccosecant of a complex number.<br>
     * Due to the arccosecant's definition, calling this method is the same as
     * calling the arcsine of the multiplicative inverse of the complex number.
     * @param cn The complex number whose arccosecant will be calculated.
     * @return The arccosecant of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero.
     * @see #asin(ComplexNumber)
     * @see #multiplicativeInverse(ComplexNumber)
     */
    public static ComplexNumber acsc(ComplexNumber cn){
        return asin(multiplicativeInverse(cn));
    }

    /**
     * Calculates the arctangent of a complex number.<br>
     * Due to the arctangent's definition, calling this method is the same as
     * performing a sum between the half of &pi; and the arctangent of the
     * multiplicative inverse of the complex number.
     * @param cn The complex number whose arctangent will be calculated.
     * @return The arctangent of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero OR if cn's value
     * causes the call to atan(ComplexNumber) throw an ArithmeticException.
     * @see #atan(ComplexNumber)
     * @see #multiplicativeInverse(ComplexNumber)
     */
    public static ComplexNumber acot(ComplexNumber cn) {
        return sum(new ComplexNumber(new RealNumber(Math.PI / 2)), atan(multiplicativeInverse(cn))).toComplex();
    }

    /** Calculates e<sup>cn</sup> for a complex number cn.
     * For a complex number a + bi, the result is a complex number that results 
     * from calculate e<sup>bi</sup>, ponderated by e<sup>a</sup>
     * @param cn The complex number used to raise e to the power of
     * @return e raised to the power of cn as described above.
     * @throws NullPointerException if cn is null
     * @see Math#exp(double) 
     * @see ImaginaryMath#exp(ImaginaryNumber) 
     * @see #ponderate(ComplexNumber, double)
     */
    public static ComplexNumber exp(ComplexNumber cn){
        return ponderate(ImaginaryMath.exp(cn), Math.exp(cn.getRealAsDouble()));
    }

    /**
     * Calculates the hyperbolic sine of a complex number.<br>
     * For a complex number a + bi, this is equivalent to calculate<br>
     * sinh(a)cos(b) + cosh(a)sin(b)
     * @param cn The complex number whose hyperbolic arcsine will be calculated
     * @return The arcsine of cn as described above
     * @throws NullPointerException if cn is null
     */
    public static ComplexNumber sinh(ComplexNumber cn){
        return new ComplexNumber(Math.sinh(cn.getRealAsDouble()) * Math.cos(cn.getImaginary()),
                                 Math.cosh(cn.getRealAsDouble()) * Math.sin(cn.getImaginary()));
    }

    /**
     * Calculates the hyperbolic cosine of a complex number.<br>
     * For a complex number a + bi, this is equivalent to calculate<br>
     * cosh(a)cos(b) + sinh(a)sin(b)
     * @param cn The complex number whose hyperbolic arcsine will be calculated
     * @return The arcsine of cn as described above
     * @throws NullPointerException if cn is null
     */
    public static ComplexNumber cosh(ComplexNumber cn){
        return new ComplexNumber(Math.cosh(cn.getRealAsDouble()) * Math.cos(cn.getImaginary()),
                                 Math.sinh(cn.getRealAsDouble()) * Math.sin(cn.getImaginary()));
    }

    /**
     * Calculates the hyperbolic tangent of a complex number.<br>
     * For a complex number a + bi, the result will be a complex number that is
     * calculated as follows:
     * <ul>
     *     <li>The real part is sinh(2a)</li>
     *     <li>The imaginary part is sin(2b)</li>
     *     <li>The sum of both parts is ponderated by the multiplicative
     *     inverse of cosh(2a) + cos(2b)</li>
     * </ul>
     * @param cn The complex number whose hyperbolic arctangent will be calculated.
     * @return The hyperbolic tangent of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cosh(2a) + cos(2b) is zero, which is possible
     * if cn represents an imaginary number (no real part) and the numeric
     * coefficient of the imaginary part is (0.5 + n)&pi;, for any integer n.
     * @see Math#cos(double)
     * @see Math#cosh(double)
     * @see Math#sin(double)
     * @see Math#sinh(double)
     */
    public static ComplexNumber tanh(ComplexNumber cn){
        double a = cn.getRealAsDouble();
        double b = cn.getImaginary();
        double denom = Math.cosh(2 * a) + Math.cos(2 * b);
        if(Math.abs(denom) == 0){
            throw new ArithmeticException("Cannot calculate the hyperbolic tangent of a complex number a + bi if cosh(2a) + cos(2b) equals zero");
        }
        double r = Math.sinh(2 * a);
        double i = Math.sin(2 * b);
        return ponderate(new ComplexNumber(r, i), 1 / denom);
    }

    /**
     * Calculates the hyperbolic secant of a complex number, that is,
     * the multiplicative inverse of the hyperbolic cosine of the complex
     * number.
     * @param cn The complex number whose hyperbolic secant will be calculated
     * @return The hyperbolic secant of cn as described above
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero OR if cosh(cn) is zero.
     * @see #multiplicativeInverse(ComplexNumber) 
     * @see #cosh(ComplexNumber)
     */
    public static ComplexNumber sech(ComplexNumber cn){
        return multiplicativeInverse(cosh(cn));
    }

    /**
     * Calculates the hyperbolic cosecant of a complex number, that is,
     * the multiplicative inverse of the hyperbolic sine of the complex
     * number.
     * @param cn The complex number whose hyperbolic cosecant will be calculated
     * @return The hyperbolic cosecant of cn as described above
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero OR sinh(cn) is zero.
     * @see #multiplicativeInverse(ComplexNumber)
     * @see #sinh(ComplexNumber)
     */
    public static ComplexNumber csch(ComplexNumber cn){
        return multiplicativeInverse(sinh(cn));
    }

    /**
     * Calculates the hyperbolic cotangent of a complex number, that is,
     * the multiplicative inverse of the hyperbolic tangent of the complex
     * number.
     * @param cn The complex number whose hyperbolic cotangent will be calculated
     * @return The hyperbolic cotangent of cn as described above
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero OR sinh(cn) is zero.
     * @see #multiplicativeInverse(ComplexNumber)
     * @see #sinh(ComplexNumber)
     * @see #tanh(ComplexNumber)
     */
    public static ComplexNumber coth(ComplexNumber cn){
        return multiplicativeInverse(tanh(cn));
    }

    /**
     * Calculates the argument of a complex number, that is, the
     * arctangent of the division between the real and the imaginary part
     * of the complex number.<br>
     * NOTE: Due to Java's implementation of the atan2 method in the
     * Math class, which calculates the tangent of the division between
     * its second and its first argument, this method will never throw
     * an ArithmeticException when cn has no real part.
     * @param cn The complex number whose argument will be calculated.
     * @return The argument of cn as described above.
     * @throws NullPointerException if cn is null
     * @see Math#atan2(double, double) 
     */
    public static double arg(ComplexNumber cn){
        return Math.atan2(cn.getImaginary(), cn.getRealAsDouble());
    }

    /**
     * Calculates the natural logarithm of a complex number.<br>
     * The natural logarithm of a complex number has an infinite number
     * of values. Because of this, an integer k must be specified to 
     * indicate that the kth value must be returned.<br>
     * Given a complex number a + bi and an integer k, the kth natural 
     * logarithm of that complex number is another complex number calculated
     * as follows:
     * <ul>
     *     <li>The real part is the natural logarithm of cn's modulus</li>
     *     <li>The imaginary part is the sum between cn's argument and
     *     the double of &pi;k</li>
     * </ul>
     * NOTE: To obtain the principal value of the logarithm, call this method
     * with k = 0.
     * @param cn The complex number whose natural logarithm will be calculated.
     * @param k The integer number to indicate that the kth value must be 
     *          obtained. Can be negative or zero.
     * @return The kth natural logarithm of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero
     * @see Math#log(double) 
     * @see #modulus(ComplexNumber) 
     * @see #arg(ComplexNumber) 
     */
    public static ComplexNumber log(ComplexNumber cn, int k){
        double mod = modulus(cn);
        if(mod == 0){
            throw new ArithmeticException("Cannot calculate the natural logarithm of a complex number whose modulus is zero");
        }
        return new ComplexNumber(Math.log(mod), arg(cn) + (2 * k * Math.PI));
    }

    /**
     * Calculates the principal value of the natural logarithm of a complex number.<br>
     * Calling this method is the same as calling log(cn, 0).
     * @param cn The complex number whose natural logarithm will be calculated.
     * @return The principal value of the natural logarithm of cn as described above
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero
     * @see #log(ComplexNumber, int) 
     */
    public static ComplexNumber log(ComplexNumber cn){
        return log(cn, 0);
    }

    /**
     * Calculates the base-n logarithm of a complex number.<br>
     * Due to logarithms properties, calling this method is the same as
     * ponderate the result of calling log(cn, k) by the natural logarithm
     * of n
     * @param cn The complex number whose n-base logarithm will be calculated.
     * @param k The integer number to indicate that the kth value must be 
     *          obtained. Can be negative or zero.
     * @param n The base used to calculate the logarithm.
     * @return The kth n-base logarithm of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if one of the following happens:
     * <ul>
     *     <li>cn's modulus is zero</li>
     *     <li>n is 1, zero or negative as the logarithm of 1 on any
     *     base is zero and the logarithm of any number that is lower
     *     or equal to zero is not allowed</li>
     * </ul>
     * @see Math#log(double)
     * @see #ponderate(ComplexNumber, double) 
     * @see #arg(ComplexNumber)
     * @see #log(ComplexNumber, int)
     */
    public static ComplexNumber logN(ComplexNumber cn, int k, double n){
        if(n == 1 || n <= 0){
            throw new ArithmeticException("The base cannot be 1 nor a negative number");
        }
        return ponderate(log(cn, k), 1 / Math.log(n));
    }

    /**
     * Calculates the principal value of the base-n logarithm of a complex number.<br>
     * Calling this method is the same as calling logN(cn, 0).
     * @param cn The complex number whose n-base logarithm will be calculated.
     * @param n The base used to calculate the logarithm.
     * @return The principal value of the  base-n logarithm of cn as described
     * above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if one of the following happens:
     * <ul>
     *     <li>cn's modulus is zero</li>
     *     <li>n is 1, zero or negative as the logarithm of 1 on any
     *     base is zero and the logarithm of any number that is lower
     *     or equal to zero is not allowed</li>
     * </ul>
     * @see #logN(ComplexNumber, int, double)
     */
    public static ComplexNumber logN(ComplexNumber cn, double n){
        return logN(cn, 0, n);
    }

    /**
     * Calculates the logarithm of a complex number on an imaginary base.<br>
     * This result in a division of two logarithms of a complex number for each
     * logarithm. Thus, it can result in an infinite number of solutions for each
     * logarithm in the division. For this reason, two integers k and n are
     * required to specify the kth value of the first logarithm and the nth value
     * of the second.<br>
     * Given a complex number a + bi, an imaginary number ci and two integers
     * k and n, the result of log<sub>ci</sub>(a + bi) is a division of two
     * complex numbers calculated as follows:
     * <ul>
     *     <li>The first complex number is the kth value of the natural
     *     logarithm of cn</li>
     *     <li>For the second complex number:</li>
     *     <ul>
     *         <li>The real part is ln(|c|), where ln(x) is the natural logarithm of
     *         x and |x| is the absolute value of x</li>
     *         <li>The imaginary part is &pi;(2n + sgn(ci)/2)</li>
     *     </ul>
     * </ul>
     * @param cn The complex number whose logarithm on an imaginary base will be
     *           calculated
     * @param k An integer to indicate the kth value of the first logarithm in
     *          the division will be obtained. Can be zero or negative
     * @param n An integer to indicate the nth value of the second logarithm in
     *          the division will be obtained. Can be zero or negative
     * @param c The imaginary number used as base for the logarithm of cn
     * @return The logarithm on a c base of cn as described above
     * @throws NullPointerException if cn and/or c are null
     * @throws ArithmeticException if cn's modulus is zero
     * @see Math#log(double) 
     * @see Math#abs(double) 
     * @see ImaginaryMath#sgn(ImaginaryNumber) 
     * @see #log(ComplexNumber, int) 
     * @see #divide(ComplexNumber, ComplexNumber) 
     */
    public static ComplexNumber logI(ComplexNumber cn, int k, int n, ImaginaryNumber c){
        return divide(log(cn, k), new ComplexNumber(Math.log(Math.abs(c.getImaginary())), Math.PI * ((2 * n) + (ImaginaryMath.sgn(c) / 2))));
    }

    /**
     * Calculates the principal value of the logarithm on an imaginary base of a
     * complex number.<br>
     * Calling this method is the same as calling logI(cn, 0, 0, c)
     * @param cn The complex number whose logarithm on an imaginary base will be
     *           calculated
     * @param c The imaginary number used as base for the logarithm of cn
     * @return The principal value of the logarithm on a c base of cn as described
     * above.
     * @throws NullPointerException if cn and/or c are null
     * @throws ArithmeticException if cn's modulus is zero
     * @see #logI(ComplexNumber, int, int, ImaginaryNumber) 
     */
    public static ComplexNumber logI(ComplexNumber cn, ImaginaryNumber c){
        return logI(cn, 0, 0, c);
    }

    /**
     * Calculates the logarithm of a complex number on a base that is another
     * complex number.<br>
     * As the result is a division of two complex numbers implying natural
     * logarithms, each operand in the division can have an infinite number of
     * values. For this reason, 2 integers k and n are required to obtain the
     * kth value of the first operand and the nth operand of the second.<br>
     * Given 2 complex numbers c and d, f(x) is the
     * modulus for any complex number x and g(x, k) is arg(x) + (2&pi;k)
     * for any complex number x and any integer k, the value of
     * log<sub>d</sub>c is (f(c) + i * g(c, k)) / (f(d) + i * g(d, n))
     * @param cn The complex number whose logarithm in a complex base will be
     *           calculated.
     * @param base The base in which the logarithm of cn will be calculated.
     * @param k An integer number to indicate that the kth value of the first
     *          operand in the resulting division will be used.
     * @param n An integer number to indicate that the nth value of the second
     *          operand in the resulting division will be used.
     * @return The logarithm in the complex base of cn as described above.
     * @throws NullPointerException if cn and/or base are null
     * @throws ArithmeticException if one of the following happens:
     * <ul>
     *     <li>cn's and/or base's modulus are zero as the logarithm of zero on any base
     *     is not allowed</li>
     *     <li>base represents the real number 1 (no imaginary part) and n is zero,
     *     as the resulting denominator will be zero and the division by zero
     *     is not allowed</li>
     * </ul>
     * @see Math#log(double) 
     * @see #modulus(ComplexNumber) 
     * @see #arg(ComplexNumber)
     */
    public static ComplexNumber logC(ComplexNumber cn, ComplexNumber base, int k, int n){
        ComplexNumber num = new ComplexNumber(Math.log(modulus(cn)), arg(cn) + (2 * Math.PI * k));
        ComplexNumber den = new ComplexNumber(Math.log(modulus(base)), arg(base) + (2 * Math.PI * n));
        return divide(num, den);
    }

    /**
     * Calculates the principal value of the logarithm of a complex number on
     * a base that is another complex number.<br>
     * Calling this method is the same as calling logC(cn, base, 0, 0)
     * @param cn The complex number whose logarithm in a complex base will be
     *           calculated.
     * @param base The base in which the logarithm of cn will be calculated.
     * @return The logarithm in the complex base of cn as described above.
     * @throws NullPointerException if cn and/or base are null
     * @throws ArithmeticException if one of the following happens:
     * <ul>
     *     <li>cn's and/or base's modulus are zero as the logarithm of zero on
     *     any base is not allowed</li>
     *     <li>base represents the real number 1 (no imaginary part),
     *     as the resulting denominator will be zero and the division by zero
     *     is not allowed</li>
     * </ul>
     */
    public static ComplexNumber logC(ComplexNumber cn, ComplexNumber base){
        return logC(cn, base, 0, 0);
    }

    /** Calculates the square root of a complex number.<br>
     * Given a complex number c = a + bi, its square root results in two complex 
     * numbers.
     * <ul>
     *     <li>For the first complex number
     *      <ul>
     *      <li>The real part will be the square root of the average
     *      between a and the modulus of c</li>
     *      <li>The imaginary part will be sgn(b) times the square
     *      root of half of the difference between a and the modulus
     *      of c</li>
     *      </ul>
     *     </li>
     *     <li>The second complex number will be the additive inverse of
     *     the first one, as raising a number to the power of two will be
     *     a positive number regardless of the sign of the base</li>
     * </ul>
     * @param cn The complex number whose square root will be calculated
     * @return The square root of cn as described above
     * @throws NullPointerException if cn is null
     * @see Math#sqrt(double) 
     * @see #additiveInverse(ComplexNumber) 
     * @see #modulus(ComplexNumber)
     */
    public static List<ComplexNumber> sqrt(ComplexNumber cn){
        ComplexNumber root = new ComplexNumber(
            Math.sqrt((modulus(cn) + cn.getRealAsDouble()) / 2),
            Math.signum(cn.getImaginary()) * Math.sqrt((modulus(cn) - cn.getRealAsDouble()) / 2));
        return List.of(root, additiveInverse(root));
    }

    /**
     * Calculates the nth root of a complex number.<br>
     * For a complex number c, this results on n complex numbers.
     * The value of each k<sup>th</sup> complex number c<sub>k</sub> (1 <= k <= n) is calculated as follows:
     * <ul>
     *     <li>Let a be the half of the sum between arg(c)
     *     and 2&pi;k</li>
     *     <li>The real part will be the cosine of a</li>
     *     <li>The imaginary part will be the sine of a</li>
     *     <li>The result of summing the real and the imaginary parts
     *     will be ponderated by the modulus of c</li>
     * </ul>
     * @param cn The complex number whose nth root will be calculated
     * @param n The integer number used to determine what root of cn will
     *          be calculated
     * @return A list with each value of the nth root of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if n is zero, as the division by zero is not
     * allowed.
     */
    public static List<ComplexNumber> nrt(ComplexNumber cn, int n){
        if(n == 0){
            throw new ArithmeticException("Cannot calculate the nrt of a number if n is zero");
        }
        double scalar = Math.pow(modulus(cn), 1d / n);
        List<ComplexNumber> roots = new ArrayList<>();
        IntStream.rangeClosed(1, n).forEach(k -> {
            double a = (arg(cn) + (2 * Math.PI * k)) / 2;
            roots.add(ponderate(new ComplexNumber(Math.cos(a), Math.sin(a)), scalar));
        });
        return roots;
    }

    /**
     * Calculates the hyperbolic arcsin of a complex number. <br>
     * As this implies to calculate the square root of a complex number,
     * the result will be two complex numbers. Also, as this implies to
     * calculate the natural logarithm of a complex number, each resulting
     * complex number can have an
     * infinite number of values. For this reason, an integer k is required
     * to obtain the k<sup>th</sup> value of each complex number.<br>
     * Given a complex number a + bi and an integer k, the k<sup>th</sup>
     * hyperbolic arcsine of a + bi is calculated as follows:
     * <ul>
     *     <li>Let s be a complex number that is the sum of other 2 complex numbers:
     *     <ul>
     *         <li>The result of raise cn to the power of two</li>
     *         <li>The real number 1 (no imaginary part)</li>
     *     </ul>
     *     </li>
     *     <li>Let s<sub>1</sub> and s<sub>2</sub> the values of the square
     *     root of s</li>
     *     <li>For each i in {1, 2}, the resulting i<sup>th</sup> complex number
     *     is the k<sup>th</sup> natural logarithm of the sum between cn and s<sub>i</sub></li>
     * </ul>
     * @param cn The complex number whose hyperbolic arcsine will be calculated
     * @param k An integer number to indicate that the kth values of the 
     *          hyperbolic arcsine of cn will be calculated.
     * @return A list containing 2 complex numbers representing the kth hyperbolic
     * arcsine of cn as described above
     * @throws NullPointerException if k is null
     * @throws ArithmeticException if exists one s<sub>i</sub> with i in {1, 2}
     * so the modulus of cn + s<sub>i</sub> is zero.
     * @see #sum(ComplexNumber...) 
     * @see #pow(ComplexNumber, int)
     * @see #log(ComplexNumber, int) 
     * @see #sqrt(ComplexNumber)
     */
    public static List<ComplexNumber> asinh(ComplexNumber cn, int k){
        ComplexNumber s = sum(pow(cn, 2).toComplex(), new ComplexNumber(1, 0)).toComplex();
        return sqrt(s).stream().map( c -> log(sum(cn, c).toComplex(), k)).toList();
    }

    /**
     * Calculates the principal values of the hyperbolic arcsine of a complex
     * number.<br>
     * Calling this method is the same as calling asinh(cn, 0)
     * @param cn The complex number whose hyperbolic arcsine will be calculated
     * @return A list containing 2 complex numbers representing the hyperbolic
     * arcsine of cn as described above
     * @throws NullPointerException if k is null
     * @throws ArithmeticException if exists one s<sub>i</sub> with i in {1, 2}
     * so the modulus of cn + s<sub>i</sub> is zero.
     * @see #asinh(ComplexNumber, int) 
     */
    public static List<ComplexNumber> asinh(ComplexNumber cn){
        return asinh(cn, 0);
    }

    /**
     * Calculates the hyperbolic arccosine of a complex number. <br>
     * As this implies to calculate the square root of a complex number,
     * the result will be two complex numbers. Also, as this implies to
     * calculate the natural logarithm of a complex number, each resulting
     * complex number can have an
     * infinite number of values. For this reason, an integer k is required
     * to obtain the k<sup>th</sup> value of each complex number.<br>
     * Given a complex number a + bi and an integer k, the k<sup>th</sup>
     * hyperbolic arccosine of a + bi is calculated as follows:
     * <ul>
     *     <li>Let s be a complex number that is the difference between other
     *     2 complex numbers:
     *     <ul>
     *         <li>The result of raise cn to the power of two</li>
     *         <li>The real number 1 (no imaginary part)</li>
     *     </ul>
     *     </li>
     *     <li>Let s<sub>1</sub> and s<sub>2</sub> the values of the square
     *     root of s</li>
     *     <li>For each i in {1, 2}, the resulting i<sup>th</sup> complex number
     *     is the k<sup>th</sup> natural logarithm of the sum between cn and s<sub>i</sub></li>
     * </ul>
     * @param cn The complex number whose hyperbolic arccosine will be calculated
     * @param k An integer number to indicate that the kth values of the
     *          hyperbolic arccosine of cn will be calculated.
     * @return A list containing 2 complex numbers representing the kth hyperbolic
     * arccosine of cn as described above
     * @throws NullPointerException if k is null
     * @throws ArithmeticException if exists one s<sub>i</sub> with i in {1, 2}
     * so the modulus of cn + s<sub>i</sub> is zero.
     * @see #sum(ComplexNumber...)
     * @see #pow(ComplexNumber, int)
     * @see #log(ComplexNumber, int)
     * @see #sqrt(ComplexNumber)
     */
    public static List<ComplexNumber> acosh(ComplexNumber cn, int k){
        ComplexNumber s = diff(pow(cn, 2).toComplex(), new ComplexNumber(1, 0));
        return sqrt(s).stream().map( c -> log(sum(cn, c).toComplex(), k)).toList();
    }

    /**
     * Calculates the principal values of the hyperbolic arccosine of a complex
     * number.<br>
     * Calling this method is the same as calling acosh(cn, 0)
     * @param cn The complex number whose hyperbolic arccosine will be calculated
     * @return A list containing 2 complex numbers representing the hyperbolic
     * arccosine of cn as described above
     * @throws NullPointerException if k is null
     * @throws ArithmeticException if exists one s<sub>i</sub> with i in {1, 2}
     * so the modulus of cn + c is zero.
     * @see #acosh(ComplexNumber, int)
     */
    public static List<ComplexNumber> acosh(ComplexNumber cn){
        return acosh(cn, 0);
    }

    /**
     * Calculates the hyperbolic arctangent of a complex number.<br>
     * as this implies to calculate the natural logarithm of a complex number,
     * the resulting complex number can have an infinite number of values. For
     * this reason, an integer k is required to obtain the k<sup>th</sup> value
     * of that logarithm.<br>
     * The hyperbolic arctangent of a complex number c is the difference between
     * "the natural logarithm of the sum between the real number one (no
     * imaginary part) and c" and "the natural logarithm of the difference
     * between those same numbers".
     * @param cn The complex number whose hyperbolic arctangent will be
     *           calculated.
     * @param k An integer number used to indicate that the k<sup>th</sup>
     *          value of the hyperbolic arctangent will be calculated.
     * @return The k<sup>th</sup> hyperbolic arctangent of cn as described
     * above.
     * @throws NullPointerException if cn is null.
     * @throws ArithmeticException if the modulus of the sum or the difference
     * between 1 and cn is zero.
     */
    public static ComplexNumber atanh(ComplexNumber cn, int k){
        ComplexNumber one = new ComplexNumber(1, 0);
        return diff(log(sum(one, cn).toComplex(), k), log(diff(one, cn), k));
    }

    /**
     * Calculates the principal value of the hyperbolic arctangent of a
     * complex number.<br>
     * Calling this method is the same as calling atanh(cn, 0)
     * @param cn The complex number whose hyperbolic arctangent will be
     *           calculated.
     * @return The principal value of the hyperbolic arctangent of cn as described
     * above.
     * @throws NullPointerException if cn is null.
     * @throws ArithmeticException if the modulus of the sum or the difference
     * between 1 and cn is zero.
     */
    public static ComplexNumber atanh(ComplexNumber cn){
        return atanh(cn, 0);
    }

    /**
     * Calculates the hyperbolic arccotangent of a complex number.<br>
     * as this implies to calculate the natural logarithm of a complex number,
     * the resulting complex number can have an infinite number of values.
     * For this reason, an integer k is required to obtain the k<sup>th</sup>
     * value of that logarithm.<br>
     * The hyperbolic arccotangent of a complex number c is the difference between
     * "the natural logarithm of the sum between c and the real number one (no
     * imaginary part)" and "the natural logarithm of the difference
     * between those same numbers".
     * @param cn The complex number whose hyperbolic arccotangent will be
     *           calculated.
     * @param k An integer number used to indicate that the k<sup>th</sup>
     *          value of the hyperbolic arccotangent will be calculated.
     * @return The k<sup>th</sup> hyperbolic arccotangent of cn as described
     * above.
     * @throws NullPointerException if cn is null.
     * @throws ArithmeticException if the modulus of the sum or the difference
     * between cn and 1 is zero.
     */
    public static ComplexNumber acoth(ComplexNumber cn, int k){
        ComplexNumber one = new ComplexNumber(1, 0);
        return diff(log(sum(cn, one).toComplex(), k), log(diff(cn, one), k));
    }

    /**
     * Calculates the principal value of the hyperbolic arccotangent of a
     * complex number.<br>
     * Calling this method is the same as calling acoth(cn, 0)
     * @param cn The complex number whose hyperbolic arccotangent will be
     *           calculated.
     * @return The principal value of the hyperbolic arccotangent of cn as
     * described above.
     * @throws NullPointerException if cn is null.
     * @throws ArithmeticException if the modulus of the sum or the difference
     * between cn and 1 is zero.
     */
    public static ComplexNumber acoth(ComplexNumber cn){
        return acoth(cn, 0);
    }

    /**
     * Calculates the k<sup>th</sup> hyperbolic arcsecant of a complex number.<br>
     * Calling this method is the same as calling asech(multiplicativeInverse(cn), k)
     * @param cn The complex number whose hyperbolic arcsecant will be calculated
     * @param k An integer number to indicate that the k<sup>th</sup> number will
     *          be calculated.
     * @return The k<sup>th</sup> hyperbolic arcsecant of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero OR if exists one
     * s<sub>i</sub> with i in {1, 2} so the modulus of cn<sup>-1</sup> +
     * s<sub>i</sub> is zero.
     * @see #acosh(ComplexNumber, int) 
     * @see #multiplicativeInverse(ComplexNumber) 
     */
    public static List<ComplexNumber> asech(ComplexNumber cn, int k){
        return acosh(multiplicativeInverse(cn), k);
    }

    /**
     * Calculates the principal values of the hyperbolic secant of a complex
     * number.<br>
     * Calling this method is the same as calling asech(cn, 0)
     * @param cn The complex number whose hyperbolic arcsecant will be calculated
     * @return The principal value of the hyperbolic arcsecant of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero OR if exists one
     * s<sub>i</sub> with i in {1, 2} so the modulus of cn<sup>-1</sup> +
     * s<sub>i</sub> is zero.
     * @see #asech(ComplexNumber, int) 
     */
    public static List<ComplexNumber> asech(ComplexNumber cn){
        return asech(cn, 0);
    }

    /**
     * Calculates the k<sup>th</sup> hyperbolic arccosecant of a complex number.<br>
     * Calling this method is the same as calling acsch(multiplicativeInverse(cn), k)
     * @param cn The complex number whose hyperbolic arccosecant will be calculated
     * @param k An integer number to indicate that the k<sup>th</sup> value will
     *          be calculated.
     * @return The k<sup>th</sup> hyperbolic arccosecant of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero OR if exists one
     * s<sub>i</sub> with i in {1, 2} so the modulus of cn<sup>-1</sup> +
     * s<sub>i</sub> is zero.
     * @see #asinh(ComplexNumber, int)
     * @see #multiplicativeInverse(ComplexNumber)
     */
    public static List<ComplexNumber> acsch(ComplexNumber cn, int k){
        return asinh(multiplicativeInverse(cn), k);
    }

    /**
     * Calculates the principal values of the hyperbolic cosecant of a complex
     * number.<br>
     * Calling this method is the same as calling acsch(cn, 0)
     * @param cn The complex number whose hyperbolic arccosecant will be calculated
     * @return The principal value of the hyperbolic arccosecant of cn as described above.
     * @throws NullPointerException if cn is null
     * @throws ArithmeticException if cn's modulus is zero OR if exists one
     * s<sub>i</sub> with i in {1, 2} so the modulus of cn<sup>-1</sup> +
     * s<sub>i</sub> is zero.
     * @see #acsch(ComplexNumber, int)
     */
    public static List<ComplexNumber> acsch(ComplexNumber cn){
        return acsch(cn, 0);
    }
}
