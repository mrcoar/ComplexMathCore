package cl.maraneda.cplx;

/** This interface is used only in order to avoid use the Object class for downcasting
 *  results of real, imaginary and complex operations
 *
 *  @author Marco Araneda
 *  @since 2026-06-01
 *  @version 1.0
 */
public interface MathResult {
    default boolean isReal(){
        return this instanceof RealNumber ||
              ( this instanceof ComplexNumber cn && cn.isReal());
    }

    default boolean isImaginary(){
        return (this instanceof ComplexNumber cn && cn.isImaginary()) ||
                this instanceof ImaginaryNumber;
    }

    default boolean isComplex(){
        return this instanceof ComplexNumber;
    }

    default ComplexNumber toComplex(){
        if(isReal()){
            return new ComplexNumber((RealNumber) this);
        }
        if(isImaginary()){
            return new ComplexNumber((ImaginaryNumber) this);
        }
        return (ComplexNumber) this;
    }

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
