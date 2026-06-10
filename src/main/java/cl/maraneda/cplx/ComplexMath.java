package cl.maraneda.cplx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ComplexMath {
    private ComplexMath() {/* Nothing */}

    public static ComplexNumber ponderate(ComplexNumber cn, double scalar){
        return new ComplexNumber(scalar * cn.getRealAsDouble(), scalar * cn.getImaginary());
    }

    public static ComplexNumber diff(ComplexNumber cn1, ComplexNumber cn2){
        return new ComplexNumber(cn1.getRealAsDouble() - cn2.getRealAsDouble(), cn1.getImaginary() - cn2.getImaginary());
    }

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

    public static ComplexNumber conjugate(ComplexNumber cn){
        return new ComplexNumber(cn.getReal(), -1 * cn.getImaginary());
    }

    public static double modulus(ComplexNumber cn){
        return Math.sqrt(Math.pow(cn.getRealAsDouble(), 2) + Math.pow(cn.getImaginary(), 2));
    }

    public static MathResult pow(ComplexNumber cn, int exp){
        return switch(exp){
            case 0 -> {
                if(modulus(cn) == 0){
                    throw new ArithmeticException("Cannot rise zero to the power of zero");
                }
                yield new RealNumber(1);
            }
            case 1 -> cn;
            case -1 -> divide(new ComplexNumber(1, 0), cn);
            default -> {
                if(exp > 1) {
                    ComplexNumber[] ops = new ComplexNumber[exp];
                    Arrays.fill(ops, cn);
                    yield multiply(ops);
                }else{
                    if(modulus(cn) == 0){
                        throw new ArithmeticException("Cannot raise zero to any power");
                    }
                    ComplexNumber[] ops = new ComplexNumber[Math.abs(exp)];
                    Arrays.fill(ops, conjugate(cn));
                    double scalar = Math.pow(modulus(cn), -2d * exp);
                    yield ponderate(multiply(ops), scalar);
                }
            }
        };
    }

    public static ComplexNumber divide(ComplexNumber cn1, ComplexNumber cn2){
        double scalar = Math.pow(modulus(cn2), -2d);
        double real = (cn1.getRealAsDouble() * cn2.getRealAsDouble()) + (cn1.getImaginary() + cn2.getImaginary());
        double imaginary = (cn1.getRealAsDouble() * cn2.getImaginary()) - (cn1.getImaginary() + cn2.getRealAsDouble());
        return ponderate(new ComplexNumber(real, imaginary), scalar);
    }

    public static ComplexNumber polar(ComplexNumber cn, int angle){
        return polar(cn, angle / 180d);
    }

    public static ComplexNumber polar(ComplexNumber cn, double piFactor){
        double rad = piFactor * Math.PI;
        return ponderate(new ComplexNumber(Math.cos(rad), Math.sin(rad)), modulus(cn));
    }

    public static ComplexNumber polarMultiply(ComplexNumber cn1, ComplexNumber cn2, int angle1, int angle2){
        return polarMultiply(cn1, cn2, angle1 / 180d, angle2 / 180d);
    }

    public static ComplexNumber polarMultiply(ComplexNumber cn1, ComplexNumber cn2, double factor1, double factor2){
        double rad1 = factor1 * Math.PI;
        double rad2 = factor2 * Math.PI;
        return ponderate(new ComplexNumber(Math.cos(rad1 + rad2), Math.sin(rad1 + rad2)), modulus(cn1) * modulus(cn2));
    }

    public static ComplexNumber polarDivide(ComplexNumber cn1, ComplexNumber cn2, int angle1, int angle2){
        double rad1 = (angle1 * Math.PI) / 180;
        double rad2 = (angle2 * Math.PI) / 180;
        return ponderate(new ComplexNumber(Math.cos(rad1 - rad2), Math.sin(rad1 - rad2)), modulus(cn1) / modulus(cn2));
    }

    public static ComplexNumber polarPow(ComplexNumber cn, int angle, double exp){
        double rad = (angle * Math.PI) / 180;
        return ponderate(new ComplexNumber(Math.cos(exp * rad), Math.sin(exp * rad)), Math.pow(modulus(cn), exp));
    }

    public static List<ComplexNumber> polarNrt(ComplexNumber cn, int angle, int n){
        if(n == 0){
            throw new ArithmeticException("0th roots of numbers don't exist");
        }
        if(modulus(cn) == 0){
            throw new ArithmeticException("The specified complex number cannot be zero");
        }
        double rad = (angle * Math.PI) / 180;
        List<ComplexNumber> values = new ArrayList<>();
        IntStream.range(0, n).forEach(k -> {
            double trigArg = (rad + (2 * Math.PI * k)) / n;
            values.add(ponderate(new ComplexNumber(Math.cos(trigArg), Math.sin(trigArg)), Math.pow(modulus(cn), 1d / n)));
        });
        return values;
    }

    public List<MathResult> solveQuadratic(double a, double b, double c){
        if(a == 0){
            throw new ArithmeticException("The value of a in a quadratic function cannot be zero");
        }
        double delta = Math.pow(b, 2) - (4 * a * c);
        return switch((int)Math.signum(delta)){
            case 1 -> List.of(new RealNumber((-b + Math.sqrt(delta)) / (2 * a)),
                              new RealNumber((-b - Math.sqrt(delta)) / (2 * a)));
            case -1 -> {
                ComplexNumber cn = ponderate(new ComplexNumber(-b, Math.sqrt(Math.abs(delta))), 1d / (2 * a));
                yield List.of(cn, conjugate(cn));
            }
            default -> List.of(new RealNumber(-b/(2 * a)));
        };
    }

    public static ComplexNumber sin(ComplexNumber cn){
        double op1 = Math.sin(cn.getRealAsDouble()) * ImaginaryMath.cos(cn);
        ImaginaryNumber op2 = ImaginaryMath.multiply(Math.cos(cn.getRealAsDouble()), ImaginaryMath.sin(cn));
        return new ComplexNumber(op1, op2);
    }

    /** It calculates the cosine of a complex number.
     *  As a complex number is of the form a +/- bi, this calculation is the same as:<br><br>
     *  cos(a) * cos(bi) -/+ sin(a) * sin(b)<br><br>
     *  Which is the same as:<br><br>
     *  cos(a) * cosh(b) -/+ i * sin(a) * sinh(b)
     * @param cn the complex number whose cosine will be calculated
     * @return The cosine of cn as described above
     */
    public static ComplexNumber cos(ComplexNumber cn){
        double op1 = Math.cos(cn.getRealAsDouble()) * ImaginaryMath.cos(cn);
        ImaginaryNumber op2 = ImaginaryMath.multiply(-1 * Math.sin(cn.getRealAsDouble()), ImaginaryMath.sin(new ImaginaryNumber(cn.getImaginary())));
        return new ComplexNumber(op1, op2);
    }

    public static ComplexNumber tan(ComplexNumber cn){
        ComplexNumber op1 = new ComplexNumber(Math.tan(cn.getRealAsDouble()), ImaginaryMath.tan(cn));
        ComplexNumber op2 = new ComplexNumber(1d, ImaginaryMath.multiply(-1 * Math.tan(cn.getRealAsDouble()), cn));
        return divide(op1, op2);
    }

    public static ComplexNumber multiplicativeInverse(ComplexNumber cn){
        return divide(new ComplexNumber(1, 0), cn);
    }
    public static ComplexNumber sec(ComplexNumber cn){
        return multiplicativeInverse(cos(cn));
    }

    public static ComplexNumber csc(ComplexNumber cn){
        return multiplicativeInverse(sin(cn));
    }

    public static ComplexNumber cot(ComplexNumber cn){
        return multiplicativeInverse(tan(cn));
    }
    public static ComplexNumber asin(ComplexNumber cn){
        double r = Math.sqrt((Math.pow(cn.getRealAsDouble() + 1, 2)) + Math.pow(cn.getImaginary(), 2));
        double s = Math.sqrt((Math.pow(cn.getRealAsDouble() - 1, 2)) + Math.pow(cn.getImaginary(), 2));
        double u = Math.asin((r - s) / 2);
        double ss = (r + s) / 2;
        double v = Math.log(ss + Math.sqrt(Math.pow(ss, 2) - 1));
        return new ComplexNumber(u, v);
    }

    public static ComplexNumber acos(ComplexNumber cn){
        double r = Math.sqrt((Math.pow(cn.getRealAsDouble() + 1, 2)) + Math.pow(cn.getImaginary(), 2));
        double s = Math.sqrt((Math.pow(cn.getRealAsDouble() - 1, 2)) + Math.pow(cn.getImaginary(), 2));
        double u = Math.acos((r - s) / 2);
        double v = -1 * Math.signum(cn.getImaginary()) * Math.acos((r + s) / 2);
        return new ComplexNumber(u, v);
    }

    public static ComplexNumber atan(ComplexNumber cn){
        double a = cn.getRealAsDouble();
        double b = cn.getImaginary();
        double u = Math.atan2(2 * a, 1 - Math.pow(a, 2) - Math.pow(b, 2)) / 2;
        double ln1 = Math.log(Math.pow(a, 2) + Math.pow(b + 1, 2));
        double ln2 = Math.log(Math.pow(a, 2) + Math.pow(b - 1, 2));
        double v = (ln1 - ln2) / 4;
        return new ComplexNumber(u, v);
    }

    public static ComplexNumber asec(ComplexNumber cn){
        return acos(multiplicativeInverse(cn));
    }

    public static ComplexNumber acsc(ComplexNumber cn){
        return asin(multiplicativeInverse(cn));
    }

    public static ComplexNumber acot(ComplexNumber cn) {
        return sum(new ComplexNumber(new RealNumber(Math.PI / 2)), atan(multiplicativeInverse(cn))).toComplex();
    }

    public static ComplexNumber exp(ComplexNumber cn){
        return ponderate(ImaginaryMath.exp(cn), Math.exp(cn.getRealAsDouble()));
    }

    public static ComplexNumber sinh(ComplexNumber cn){
        return new ComplexNumber(Math.sinh(cn.getRealAsDouble()) * Math.cos(cn.getImaginary()),
                                 Math.cosh(cn.getRealAsDouble()) * Math.sin(cn.getImaginary()));
    }

    public static ComplexNumber cosh(ComplexNumber cn){
        return new ComplexNumber(Math.cosh(cn.getRealAsDouble()) * Math.cos(cn.getImaginary()),
                                 Math.sinh(cn.getRealAsDouble()) * Math.sin(cn.getImaginary()));
    }

    public static ComplexNumber tanh(ComplexNumber cn){
        double a = cn.getRealAsDouble();
        double b = cn.getImaginary();
        double denom = Math.cosh(2 * a) + Math.cos(2 * b);
        double r = Math.sinh(2 * a) / denom;
        double i = Math.sin(2 * b) / denom;
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber sech(ComplexNumber cn){
        return divide(new ComplexNumber(1, 0), cosh(cn));
    }

    public static ComplexNumber csch(ComplexNumber cn){
        return divide(new ComplexNumber(1, 0), sinh(cn));
    }

    public static ComplexNumber coth(ComplexNumber cn){
        return divide(new ComplexNumber(1, 0), tanh(cn));
    }

    public static double arg(ComplexNumber cn){
        return Math.atan2(cn.getImaginary(), cn.getRealAsDouble());
    }

    public static ComplexNumber log(ComplexNumber cn, int k){
        return new ComplexNumber(Math.log(modulus(cn)), arg(cn) + (2 * k * Math.PI));
    }

    public static ComplexNumber log(ComplexNumber cn){
        return log(cn, 0);
    }

    public static ComplexNumber logN(ComplexNumber cn, int k, double n){
        return ponderate(log(cn, k), 1 / Math.log(n));
    }

    public static ComplexNumber logN(ComplexNumber cn, double n){
        return logN(cn, 0, n);
    }

    public static ComplexNumber logI(ComplexNumber cn, int k, int n, ImaginaryNumber c){
        return divide(log(cn, k), new ComplexNumber(Math.log(Math.abs(c.getImaginary())), (2 * Math.PI * n) + (ImaginaryMath.sgn(c) * Math.PI / 2)));
    }

    public static ComplexNumber logI(ComplexNumber cn, ImaginaryNumber c){
        return logI(cn, 0, 0, c);
    }

    public static ComplexNumber logC(ComplexNumber cn, ComplexNumber base, double angle1, double angle2, int k, int n){
        ComplexNumber num = new ComplexNumber(modulus(cn), angle1 + (2 * Math.PI * k));
        ComplexNumber den = new ComplexNumber(modulus(base), angle2 + (2 * Math.PI * n));
        return divide(num, den);
    }

    public static ComplexNumber logC(ComplexNumber cn, ComplexNumber base){
        ComplexNumber num = new ComplexNumber(modulus(cn), arg(cn));
        ComplexNumber den = new ComplexNumber(modulus(base), arg(base));
        return divide(num, den);
    }

    public static Set<ComplexNumber> sqrt(ComplexNumber cn){
        ComplexNumber root = new ComplexNumber(
            Math.sqrt((modulus(cn) + cn.getRealAsDouble()) / 2),
            Math.signum(cn.getImaginary()) * Math.sqrt((modulus(cn) - cn.getRealAsDouble()) / 2));
        return Set.of(root, ponderate(root, -1d));
    }

    public static Set<ComplexNumber> asinh(ComplexNumber cn, int k){
        ComplexNumber s = sum(pow(cn, 2).toComplex(), new ComplexNumber(1, 0)).toComplex();
        return sqrt(s).stream().map( c -> log(sum(cn, c).toComplex(), k)).collect(Collectors.toSet());
    }

    public static Set<ComplexNumber> asinh(ComplexNumber cn){
        return asinh(cn, 0);
    }

    public static Set<ComplexNumber> acosh(ComplexNumber cn, int k){
        ComplexNumber s = diff(pow(cn, 2).toComplex(), new ComplexNumber(1, 0));
        return sqrt(s).stream().map( c -> log(sum(cn, c).toComplex(), k)).collect(Collectors.toSet());
    }

    public static Set<ComplexNumber> acosh(ComplexNumber cn){
        return acosh(cn, 0);
    }

    public static ComplexNumber atanh(ComplexNumber cn, int k){
        ComplexNumber one = new ComplexNumber(1, 0);
        return diff(log(sum(one, cn).toComplex(), k), log(diff(one, cn), k));
    }

    public static ComplexNumber atanh(ComplexNumber cn){
        return atanh(cn, 0);
    }

    public static ComplexNumber acoth(ComplexNumber cn, int k){
        ComplexNumber one = new ComplexNumber(1, 0);
        return diff(log(sum(cn, one).toComplex(), k), log(diff(cn, one), k));
    }

    public static ComplexNumber acoth(ComplexNumber cn){
        return acoth(cn, 0);
    }

    public static Set<ComplexNumber> asech(ComplexNumber cn, int k){
        return acosh(multiplicativeInverse(cn), k);
    }

    public static Set<ComplexNumber> asech(ComplexNumber cn){
        return asech(cn, 0);
    }

    public static Set<ComplexNumber> acsch(ComplexNumber cn, int k){
        return asinh(multiplicativeInverse(cn), k);
    }

    public static Set<ComplexNumber> acsch(ComplexNumber cn){
        return acsch(cn, 0);
    }
}
