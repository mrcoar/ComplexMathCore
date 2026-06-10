package cl.maraneda.cplx;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ArrayComplexMath {
    private ArrayComplexMath() { /* Nothing */}

    public static boolean isSquare(MathResult[][] biarray){
        for(MathResult[] array : biarray){
           if(array.length != biarray.length){
               return false;
           }
        }
        return true;
    }

    public static boolean isRectangular(MathResult[][] biarray){
        if(biarray.length == 1){
            return true;
        }
        int len = 0;
        boolean first = true;
        for(MathResult[] array : biarray){
            if(first){
                len = array.length;
                first = false;
                continue;
            }
            if(array.length != len){
                return false;
            }
        }
        return true;
    }

    public static void swap(MathResult[] a, MathResult[] b){
        MathResult[] aux = Arrays.copyOf(a, Math.min(a.length, b.length));
        System.arraycopy(b, 0, a, 0, Math.min(a.length, b.length));
        System.arraycopy(aux, 0, b, 0, Math.min(a.length, b.length));
    }

    public static void swap(MathResult[][] array, int i, int j){
        MathResult[] tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void swap(MathResult[] array, int i, int j){
        MathResult aux = array[i];
        array[i] = array[j];
        array[j] = aux;
    }

    public static void swap(MathResult[][] array, int i, int j, int k ,int l){
        MathResult aux = array[i][j];
        array[i][j] = array[k][l];
        array[k][l] = aux;
    }

    public static MathResult determinant(MathResult[][] array){
        if(!isSquare(array)){
            throw new IllegalArgumentException("Only squared bidimensional arrays can have a determinant");
        }
        int n = array.length;
        if(n == 0){
            throw new IllegalArgumentException("Arrays of length zero have not determinant");
        }
        if(array.length == 1){
            return array[0][0];
        }
        MathResult[][] l = new MathResult[n][n];
        MathResult[][] p = new MathResult[n][n];
        MathResult[][] u = new MathResult[n][];
        AtomicInteger swaps = new AtomicInteger();

        IntStream.range(0, n).forEach(i ->
            u[i]= Arrays.copyOf(array[i], array[i].length)
        );

        IntStream.range(0, n).forEach(k -> {
            AtomicInteger pivotRow = new AtomicInteger(k);
            double[] maxValue = {ComplexMath.modulus(u[k][k].toComplex())};
            IntStream.range(k + 1, n).forEach(i -> {
                if(ComplexMath.modulus(u[i][k].toComplex()) > maxValue[0]){
                    maxValue[0] = ComplexMath.modulus(u[i][k].toComplex());
                    pivotRow.set(i);
                }
            });
            if(maxValue[0] == 0){
                throw new IllegalArgumentException("Singular bidimensional arrays have not determinant");
            }
            if(pivotRow.get() != k){
                swap(u, k, pivotRow.get());
                swap(p, k, pivotRow.get());
                IntStream.range(0, k).forEach(j->
                    swap(l, k, j, pivotRow.get(), j)
                );
                swaps.incrementAndGet();
            }
            IntStream.range(k + 1, n).forEach(i -> {
                l[i][k] = ComplexMath.divide(u[i][k].toComplex(), u[k][k].toComplex());
                IntStream.range(k, n).forEach(j ->
                    u[i][j] = ComplexMath.diff(
                            u[i][j].toComplex(),
                            ComplexMath.multiply(l[i][k].toComplex(), u[k][j].toComplex()))
                );
            });
        });
        ComplexNumber[] ops = new ComplexNumber[u.length];
        IntStream.range(0, u.length).forEach(i -> ops[i] = ComplexMath.ponderate(u[i][i].toComplex(), Math.pow(-1, i)));
        return ComplexMath.sum(ops);
    }

    public static MathResult[] sum(MathResult[] a, MathResult[] b){
        int len = Math.min(a.length, b.length);
        MathResult[] res = new MathResult[len];
        IntStream.range(0, len).forEach(i -> res[i] = ComplexMath.sum(a[i].toComplex(), b[i].toComplex()));
        return res;
    }

    public static void validateCanSum(MathResult[][] a, MathResult[][] b){
        Objects.requireNonNull(a, "Arrays cannot be null");
        Objects.requireNonNull(b, "Arrays cannot be null");

        if(a.length == 0 || b.length == 0){
            throw new NullPointerException("Cannot sum null or zero-length arrays");
        }

        if((!isSquare(a) && !isRectangular(a)) || (!isSquare(b) && !isRectangular(b))){
            throw new IllegalArgumentException("Only squared or rectangular arrays are allowed to sum");
        }

        if((isSquare(a) && isRectangular(b)) || (isRectangular(a) && isSquare(b))){
            throw new IllegalArgumentException("Cannot sum square with rectangular arrays");
        }

        boolean bothSquared = isSquare(a) && isSquare(b);
        if(bothSquared && a.length != b.length){
            throw new IllegalArgumentException("Both squared arrays must have the same dimensions");
        }
        boolean bothRectangular = isRectangular(a) && isRectangular(b);
        boolean sameDim = a.length == b.length && a[0].length == b[0].length;
        if(bothRectangular && !sameDim) {
            throw new IllegalArgumentException("Both arrays must have the same dimensions");
        }
    }

    public static MathResult[][] sum(MathResult[][] a, MathResult[][] b){
        validateCanSum(a, b);
        MathResult[][] res = new MathResult[a.length][];
        IntStream.range(0, a.length).forEach(i ->  res[i]=sum(a[i], b[i]));
        return res;
    }

    public static MathResult[] ponderate(MathResult[] a, double s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        return Arrays.stream(a).map(x -> ComplexMath.ponderate(x.toComplex(), s)).toArray(MathResult[]::new);
    }

    public static MathResult[][] ponderate(MathResult[][] a, double s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        MathResult[][] res = new MathResult[a.length][];
        IntStream.range(0, a.length).forEach(i -> res[i] = ponderate(a[i], s));
        return res;
    }

    public static MathResult[] diff(MathResult[] a, MathResult[] b){
        return sum(a, ponderate(b, -1));
    }

    public static MathResult[][] diff(MathResult[][] a, MathResult[][] b){
        return sum(a, ponderate(b, -1));
    }

    public static void validateCanMultiply(MathResult[][] a, MathResult[][] b){
        if(a==null || b == null || a.length == 0 || b.length == 0){
            throw new NullPointerException("Cannot multiply null or zero-length arrays");
        }

        if((!isSquare(a) && !isRectangular(a)) || (!isSquare(b) && !isRectangular(b))){
            throw new IllegalArgumentException("Only squared or rectangular arrays are allowed to multiply");
        }

        if(a[0].length != b.length){
            throw new IllegalArgumentException("The number of columns of the first array must be equal to the number of rows of the second array in order to multiply them");
        }
    }

    public static MathResult[][] multiply(MathResult[][] a, MathResult[][] b) {
        validateCanMultiply(a, b);
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        MathResult[][] result = new MathResult[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                ComplexNumber sum = new ComplexNumber(0, 0);
                for (int k = 0; k < colsA; k++) {
                    sum = ComplexMath.sum(sum, ComplexMath.multiply(a[i][k].toComplex(), b[k][j].toComplex())).toComplex();
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    public static MathResult[][] transpose(MathResult[][] matrix) {
        if(!isSquare(matrix) && !isRectangular(matrix)){
            throw new IllegalArgumentException("Only squared or rectangular arrays can have a transpose");
        }
        int rows = matrix.length;
        int cols = matrix[0].length;
        MathResult[][] transpose = new MathResult[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }
        return transpose;
    }

    public static ComplexNumber[][] identity(int len){
        ComplexNumber[][] a = new ComplexNumber[len][len];
        IntStream.range(0, a.length).forEach(i ->
            IntStream.range(0, a.length).forEach(j ->
                a[i][j] = new ComplexNumber(i==j ? 1 : 0, 0)
            )
        );
        return a;
    }

    public static MathResult[][] inverse(MathResult[][] ar){
        if(!isSquare(ar)){
            throw new IllegalArgumentException("Only squared arrays can be inversed");
        }

        MathResult det = determinant(ar);
        if(ComplexMath.modulus(det.toComplex()) == 0){
            throw new ArithmeticException("Only arrays with a non-zero determinant can be inversed");
        }

        int n = ar.length;
        MathResult[][] b = identity(n);
        MathResult[][] a = new MathResult[n][];
        IntStream.range(0, a.length).forEach(i ->
            a[i] = Arrays.copyOf(ar[i], ar[i].length)
        );

        IntStream.range(0, n).forEach(k -> {
            AtomicInteger pivotRow = new AtomicInteger(k);
            double[] maxValue = {ComplexMath.modulus(a[k][k].toComplex())};
            IntStream.range(k + 1, n).forEach(i -> {
                if(ComplexMath.modulus(a[i][k].toComplex()) > maxValue[0]){
                    maxValue[0] = ComplexMath.modulus(a[i][k].toComplex());
                    pivotRow.set(i);
                }
            });
            if(maxValue[0]==0){
                throw new ArithmeticException("Singular bidimensional arrays are not invertible");
            }
            if(pivotRow.get() != k){
                swap(a, k, pivotRow.get());
                swap(b, k, pivotRow.get());
            }
            MathResult pivot = a[k][k];
            IntStream.range(0, n).forEach(j -> {
                a[k][j] = ComplexMath.divide(a[k][j].toComplex(), pivot.toComplex());
                b[k][j] = ComplexMath.divide(b[k][j].toComplex(), pivot.toComplex());
            });
            IntStream.range(0, n).forEach(i -> {
                if(i != k){
                    ComplexNumber factor = a[i][k].toComplex();
                    IntStream.range(0, n).forEach(j -> {
                        a[i][j] = ComplexMath.diff(a[i][j].toComplex(), ComplexMath.multiply(factor, a[k][j].toComplex()));
                        b[i][j] = ComplexMath.diff(b[i][j].toComplex(), ComplexMath.multiply(factor, b[k][j].toComplex()));
                    });
                }
            });
        });
        return b;
    }

    public static boolean equals(MathResult[] a, MathResult[] b){
        if(a == null && b == null){
            return true;
        }
        if(a != null && b != null) {
            if (a.length == 0 && b.length == 0) {
                return true;
            }
            if(a.length != b.length){
                return false;
            }

            boolean eq = true;
            for(int i = 0; i < a.length && eq; i++){
                eq = a[i].toComplex().equals(b[i].toComplex());
            }
            return eq;
        }
        return false;
    }

    public static boolean equals(MathResult[][] a, MathResult[][] b){
        if(a == null && b == null){
            return true;
        }

        if(a != null && b != null){
            if(a.length == 0 && b.length == 0){
                return true;
            }
            if(a.length != b.length){
                return false;
            }
            boolean eq = true;
            for(int i=0; i < a.length && eq; i++){
                eq = equals(a[i], b[i]);
            }
            return eq;
        }
        return false;
    }

    public static boolean isSimetric(MathResult[][] a){
        return equals(a, transpose(a));
    }

    public static boolean isSkewSimetric(MathResult[][] a){
        return equals(ponderate(a, -1), transpose(a));
    }

    public static boolean isOrthogonal(MathResult[][] a){
        try{
            return equals(inverse(a), transpose(a));
        }catch(ArithmeticException _){
            return false;
        }
    }

    public static MathResult[][] divide(MathResult[][] a, MathResult[][] b){
        return multiply(a, inverse(b));
    }

    public static MathResult[] conjugate(MathResult[] a){
        if(a == null || a.length == 0){
            throw new IllegalArgumentException("Cannot conjugate null or zero-length arrays");
        }
        return Arrays.stream(a).map(x ->
            x.isReal() ? x : ComplexMath.conjugate(x.toComplex())
        ).toArray(MathResult[]::new);
    }

    public static MathResult[][] conjugate(MathResult[][] a){
        if(a == null || a.length == 0){
            throw new IllegalArgumentException("Cannot conjugate null or zero-length arrays");
        }
        return Arrays.stream(a).map(ArrayComplexMath::conjugate).toArray(MathResult[][]::new);
    }

    public static boolean isHermitian(MathResult[][] a){
        return equals(transpose(a), conjugate(a));
    }

    public static boolean isSkewHermitian(MathResult[][] a){
        return equals(transpose(a), ponderate(conjugate(a), -1));
    }

    public static boolean isUnitary(MathResult[][] a){
        return equals(transpose(a), inverse(conjugate(a)));
    }
}
