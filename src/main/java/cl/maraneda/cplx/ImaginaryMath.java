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
     *  exp and 4 and the result of the sign function evaluated in
     *  exp - sgn(exp).
     * @param exp The exponent the imaginary unit will be raised to the power of
     * @return For exp = 4n + b with n and b being integer numbers, this method returns: <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1 if b is zero, regardless of sign, <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;i * sgn(exp) if b is 1, <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-1 if b is 2, regardless of sign, <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sgn(exp) if b is 3
     * @see Math#signum(float) 
     */
    public static MathResult pow(int exp){
        return pow(new ImaginaryNumber(1), exp);
    }

    /**
     *
     * @param in The imaginary number used as base
     * @param exp The exponent the imaginary number will be raised to the power of
     * @return The imaginary number raised to the power of exp. As this is
     * an algebraic operation, the result of this is the product between the
     * numeric coefficient of the imaginary number raised to the power of exp and
     * the value that is returned by pow(exp). As a consequence, depending on
     * the result of pow(exp), the returned value may be a real or an imaginary
     * number.
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

    public static ComplexNumber pow(double base, ImaginaryNumber exp){
        return new ComplexNumber(Math.cos(exp.getImaginary() * Math.log(base)), Math.sin(exp.getImaginary() * Math.log(base)));
    }

    public static ImaginaryNumber sum(ImaginaryNumber... ops){
        if(ops == null || ops.length < 2){
            throw new IllegalArgumentException("Se requiere al menos dos sumandos");
        }
        return new ImaginaryNumber(Arrays.stream(ops).mapToDouble(ImaginaryNumber::getImaginary).sum());
    }

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
        if(rops.length + iops.length == 0){
            throw new IllegalArgumentException("At least 2 operands must be among real and imaginary numbers");
        }
        return new ComplexNumber(
            Arrays.stream(rops).sum(),
            Arrays.stream(iops).mapToDouble(ImaginaryNumber::getImaginary).sum()
        );
    }

    public static MathResult multiply(ImaginaryNumber... ops){
        if(ops == null || ops.length < 2){
            throw new IllegalArgumentException("Se requiere al menos dos factores");
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

    public static ImaginaryNumber multiply(double r, ImaginaryNumber i){
        return new ImaginaryNumber(r * i.getImaginary());
    }

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
            return multiply(iops);
        }
        if(rops.length + iops.length == 0){
            throw new IllegalArgumentException("At least 2 operands must be among real and imaginary numbers");
        }
        da = new DoubleAccumulator(dbo, rops[0]);
        IntStream.range(1, rops.length).forEach(i -> da.accumulate(rops[i]));
        double coef = da.get();
        MathResult mr = multiply(iops);
        if(mr instanceof  RealNumber rn){
            return new RealNumber(rn.doubleValue() * coef);
        }
        return new ImaginaryNumber(mr.toImaginary().getImaginary() * coef);
    }

    public static double sgn(ImaginaryNumber in){
        return Math.signum(in.getImaginary());
    }

    public static MathResult sqrt(RealNumber rn){
        return switch((int)Math.signum(rn.doubleValue())){
            case 1 -> new RealNumber(Math.sqrt(rn.doubleValue()));
            case -1 -> new ImaginaryNumber(Math.sqrt(Math.abs(rn.doubleValue())));
            default -> new RealNumber(0);
        };
    }

    public static MathResult sqrt(double d){
        return sqrt(new RealNumber(d));
    }

    public static List<ComplexNumber> sqrt(ImaginaryNumber in){
        return nrt(in, 2);
    }

    public static List<ComplexNumber> cbrt(ImaginaryNumber in){
        return nrt(in, 3);
    }

    public static List<ComplexNumber> nrt(ImaginaryNumber in, int n){
        double num = Math.pow(Math.abs(in.getImaginary()), 1d/n);
        List<ComplexNumber> roots = new ArrayList<>();
        IntStream.range(0, n).forEach(k -> {
            double fact = 2 * Math.PI * (k + (Math.signum(in.getImaginary()) / 4)) / n;
            roots.add(ComplexMath.ponderate(exp(new ImaginaryNumber(fact)), num));
        });
        return roots;
    }

    public static ImaginaryNumber sin(ImaginaryNumber in){
        return new ImaginaryNumber(Math.sinh(in.getImaginary()));
    }

    public static double cos(ImaginaryNumber in){
        return Math.cosh(in.getImaginary());
    }

    public static ImaginaryNumber tan(ImaginaryNumber in){
        return new ImaginaryNumber(Math.tanh(in.getImaginary()));
    }

    public static double sec(ImaginaryNumber in){
        return 1/Math.cosh(in.getImaginary());
    }

    public static ImaginaryNumber csc(ImaginaryNumber in){
        return new ImaginaryNumber(-1 / Math.sinh(in.getImaginary()));
    }

    public static ImaginaryNumber cot(ImaginaryNumber in){
        return new ImaginaryNumber(-1 / Math.tanh(in.getImaginary()));
    }

    public static ImaginaryNumber sinh(ImaginaryNumber in){
        return new ImaginaryNumber(Math.sin(in.getImaginary()));
    }

    public static double cosh(ImaginaryNumber in){
        return Math.cos(in.getImaginary());
    }

    public static ImaginaryNumber tanh(ImaginaryNumber in){
        return new ImaginaryNumber(Math.tan(in.getImaginary()));
    }

    public static double sech(ImaginaryNumber in){
        return ExtendedMath.sec(in.getImaginary());
    }

    public static ImaginaryNumber csch(ImaginaryNumber in){
        return new ImaginaryNumber(ExtendedMath.csc(in.getImaginary()));
    }

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

    public static ImaginaryNumber asin(ImaginaryNumber in){
        return new ImaginaryNumber(ExtendedMath.asinh(in.getImaginary()));
    }

    public static ComplexNumber acos(ImaginaryNumber in){
        return new ComplexNumber(Math.PI / 2, -1 * ExtendedMath.asinh(in.getImaginary()));
    }

    public static ImaginaryNumber atan(ImaginaryNumber in){
        return new ImaginaryNumber(ExtendedMath.atanh(in.getImaginary()));
    }

    public static ComplexNumber asec(ImaginaryNumber in){
        return new ComplexNumber(Math.PI / 2, ExtendedMath.asinh(1 / in.getImaginary()));
    }

    public static ImaginaryNumber acsc(ImaginaryNumber in){
        return new ImaginaryNumber(-1 * ExtendedMath.asinh(1 / in.getImaginary()));
    }

    public static ImaginaryNumber acot(ImaginaryNumber in){
        return new ImaginaryNumber(-1 * ExtendedMath.atanh(1 / in.getImaginary()));
    }

    public static ImaginaryNumber asinh(ImaginaryNumber in){
        return new ImaginaryNumber(Math.asin(in.getImaginary()));
    }

    public static ImaginaryNumber acosh(ImaginaryNumber in){
        return new ImaginaryNumber(Math.acos(in.getImaginary()));
    }

    public static ImaginaryNumber atanh(ImaginaryNumber in){
        return new ImaginaryNumber(Math.atan(in.getImaginary()));
    }

    public static ComplexNumber asech(ImaginaryNumber in){
        return new ComplexNumber(ExtendedMath.asinh(1 / in.getImaginary()), Math.PI / 2);
    }

    public static ImaginaryNumber acsch(ImaginaryNumber in){
        return new ImaginaryNumber(-1 * Math.asin(1 / in.getImaginary()));
    }

    public static ImaginaryNumber acoth(ImaginaryNumber in){
        return new ImaginaryNumber(-1 * Math.atan(1 / in.getImaginary()));
    }

    public static ImaginaryNumber logI(double num){
        return new ImaginaryNumber((-2 * Math.log(num))/ Math.PI);
    }

    public static ComplexNumber logI(double num, ImaginaryNumber base, int k){
        return ComplexMath.pow(
            ComplexMath.ponderate(
                new ComplexNumber(
                    Math.log(Math.abs(base.getImaginary())),
                    Math.PI * (0.5 + (2 * k))),
                Math.log(num)),
            -1).toComplex();
    }

    public static ComplexNumber logN(ImaginaryNumber in, double base, int k){
        return ComplexMath.ponderate(new ComplexNumber(Math.log(in.getImaginary()), (2 * Math.PI * k) + (Math.PI / 2)), 1 / Math.log(base));
    }

    public static ComplexNumber logN(ImaginaryNumber in, double base){
        return logN(in, base, 0);
    }

    public static ComplexNumber log10(ImaginaryNumber in, int k){
        return logN(in, 10, k);
    }

    public static ComplexNumber log10(ImaginaryNumber in){
        return logN(in, 10, 0);
    }

    public static ComplexNumber log(ImaginaryNumber in, int k){
        return logN(in, Math.E, k);
    }

    public static ComplexNumber log(ImaginaryNumber in){
        return logN(in, Math.E, 0);
    }
}
