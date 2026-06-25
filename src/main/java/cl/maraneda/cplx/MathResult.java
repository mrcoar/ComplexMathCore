package cl.maraneda.cplx;

/** This interface is used only in order to avoid use the Object class for downcasting
 *  results of real, imaginary and complex operations
 *
 *  @author Marco Araneda
 *  @since 2026-06-01
 *  @version 1.0
 */
public interface MathResult {

    /**
     * Checks if this MathResult represents a real number. That is, a
     * MathResult is considered a real number in one and only one of the
     * following cases:<br><ul>
     * <li> It's a RealNumber instance. That is, for a MathResult object named
     * rn, the result of the boolean expression rn instanceof RealNumber is {@code true}</li>
     * <li> It's a ComplexNumber instance having no imaginary part. That is,
     * for a MathResult instance named mr, the result of the following boolean expression
     * is {@code true}:
     * {@code mr instanceof ComplexNumber cn && cn.isReal() && cn.getImaginary() == 0}
     * </li></ul>
     * This is because the set of complex numbers represents the disjoint union
     * between real and imaginary numbers.
     * @return {@code true} if this MathResult is a RealNumber instance OR is a
     * ComplexNumber instance with no imaginary part, {@code false} otherwise.
     */
    default boolean isReal(){
        return this instanceof RealNumber ||
              ( this instanceof ComplexNumber cn && cn.isReal());
    }

    /**
     * Checks if this MathResult represents an imaginary number. That is, a
     * MathResult is considered an imaginary number in one and only one of the
     * following cases:<br><ul>
     * <li> It's an ImaginaryNumber instance. That is, for a MathResult object named
     * rn, the result of the boolean expression rn instanceof ImaginaryNumber is {@code true}</li>
     * <li> It's a ComplexNumber instance having no real part. That is,
     * for a MathResult instance named mr, the result of the following boolean expression
     * is {@code true}:
     * {@code mr instanceof ComplexNumber cn && cn.isImaginary() && (cn.getReal() == null || cn.getRealAsDouble()==0)}
     * </li></ul>
     * This is because the set of complex numbers represents the disjoint union
     * between real and imaginary numbers.
     * @return {@code true} if this MathResult is an RImaginaryNumber instance OR is a
     * ComplexNumber instance with no real part, {@code false} otherwise.
     * */
    default boolean isImaginary(){
        return (this instanceof ComplexNumber cn && cn.isImaginary()) ||
                this instanceof ImaginaryNumber;
    }

    /**
     * Checks if this MathResult is a pure ComplexNumber. That is, if this
     * MathResult is instance of ComplexNumber, and it has a real AND an imaginary part.
     * @return {@code true} if this MathResult is a ComplexNumber, {@code false} otherwise
     */
    default boolean isComplex(){
        return this instanceof ComplexNumber;
    }

    /**
     * Creates a ComplexNumber representation of this MathResult. That is:
     * <ul>
     *     <li> If this MathResult is a RealNumber, it creates a ComplexNumber
     *     with real part equal to this RealNumber's double value and no
     *     imaginary part.</li>
     *     <li>If this MathResult is an ImaginaryNumber, it creates a ComplexNumber
     *     with imaginary part equal to the numerical coefficient of this
     *     ImaginaryNumber and no real part.</li>
     *     <li>If this MathResult is a ComplexNumber, no new object is created
     *     and the cast to ComplexNumber of this MathResult is returned instead.</li>
     * </ul>
     * @return A ComplexNumber representation of this MathResult
     */
    default ComplexNumber toComplex(){
        if(isReal()){
            return new ComplexNumber((RealNumber) this);
        }
        if(isImaginary()){
            return new ComplexNumber((ImaginaryNumber) this);
        }
        return (ComplexNumber) this;
    }

    /**
     * @return The RealNumber representation of this MathResult
     * @throws UnsupportedOperationException if this MathResult is not a
     * RealNumber instance OR if it's a ComplexNumber instance having an
     * imaginary part.
     */
    default RealNumber toReal(){
        if(isComplex()){
            ComplexNumber cn = toComplex();
            if(cn.isReal()) {
                return cn.getReal();
            }
            throw new UnsupportedOperationException("Complex numbers with imaginary part cannot be real");
        }
        if(isImaginary()){
            throw new UnsupportedOperationException("Imaginary numbers cannot be real");
        }
        return (RealNumber) this;
    }

    /**
     * @return The ImaginaryNumber representation of this MathResult
     * @throws UnsupportedOperationException if this MathResult is not an
     * ImaginaryNumber instance OR if it's a ComplexNumber instance having a
     * real part.
     */
    default ImaginaryNumber toImaginary(){
        if(isComplex()){
            ComplexNumber cn = toComplex();
            if(cn.isImaginary()) {
                return new ImaginaryNumber(cn.getImaginary());
            }
            throw new UnsupportedOperationException("Complex numbers with real parts cannot be imaginary");
        }
        if(isReal()){
            throw new UnsupportedOperationException("Real numbers cannot be imaginary");
        }
        return (ImaginaryNumber) this;
    }
}
