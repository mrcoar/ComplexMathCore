package cl.maraneda.cplx;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.stream.IntStream;

public class ArrayMath {
    private ArrayMath() { /* Nothing */}

    public static boolean isSquare(double[][] biarray){
        for(double[] array : biarray){
           if(array.length != biarray.length){
               return false;
           }
        }
        return true;
    }

    public static boolean isRectangular(double[][] biarray){
        if(biarray.length == 1){
            return true;
        }
        int len = 0;
        boolean first = true;
        for(double[] array : biarray){
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

    public static void swap(double[] a, double[] b){
        IntStream.range(0, Math.min(a.length, b.length)).forEach(i -> {
            double aux = a[i];
            a[i] = b[i];
            b[i] = aux;
        });
    }

    public static void swap(double[][] array, int i, int j){
        swap(array[i], array[j]);
    }

    public static void swap(double[] array, int i, int j){
        double aux = array[i];
        array[i] = array[j];
        array[j] = aux;
    }

    public static void swap(double[][] array, int i, int j, int k ,int l){
        double aux = array[i][j];
        array[i][j] = array[k][l];
        array[k][l] = aux;
    }

    public static double determinant(double[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            throw new IllegalArgumentException("Matrix must be square");
        }

        int n = matrix.length;

        if(!isSquare(matrix)){
            throw new IllegalArgumentException("Matrix must be square");
        }

        if (n == 1) {
            return matrix[0][0];
        }

        // Work on a copy so the input matrix is not modified
        double[][] a = new double[n][n];
        IntStream.range(0, n).forEach(i ->
            System.arraycopy(matrix[i], 0, a[i], 0, n)
        );

        double[] det = {1.0};

        IntStream.range(0, n).forEach(col -> {
            // Partial pivoting: find the row with the largest value in this column
            AtomicInteger pivotRow = new AtomicInteger(col);
            IntStream.range(col + 1, n).forEach(row -> {
                if (Math.abs(a[row][col]) > Math.abs(a[pivotRow.get()][col])) {
                    pivotRow.set(row);
                }
            });

            if (a[pivotRow.get()][col] == 0) {
                det[0] = 0;
                return;
            }

            // Row swap flips the sign of the determinant
            if (pivotRow.get() != col) {
                swap(a, col, pivotRow.get());
                det[0] *= -1;
            }

            det[0] *= a[col][col];

            // Eliminate entries below the pivot
            IntStream.range(col + 1, n).forEach(row -> {
                double factor = a[row][col] / a[col][col];
                IntStream.range(col + 1, n).forEach(c ->
                    a[row][c] -= factor * a[col][c]
                );
            });
        });

        return det[0];
    }

    public static double[] sum(double[] a, double[] b){
        int len = Math.min(a.length, b.length);
        double[] res = new double[len];
        IntStream.range(0, len).forEach(i -> res[i] = a[i] + b[i]);
        return res;
    }

    public static void validateCanSum(double[][] a, double[][] b){
        if(a==null || b == null || a.length == 0 || b.length == 0){
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

    public static double[][] sum(double[][] a, double[][] b){
        validateCanSum(a, b);
        double[][] res = new double[a.length][];
        IntStream.range(0, a.length).forEach(i ->  res[i]=sum(a[i], b[i]));
        return res;
    }

    public static double[] ponderate(double[] a, double s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        return Arrays.stream(a).map(x -> x * s).toArray();
    }

    public static double[][] ponderate(double[][] a, double s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        double[][] res = new double[a.length][];
        IntStream.range(0, a.length).forEach(i -> res[i] = ponderate(a[i], s));
        return res;
    }

    public static double[] diff(double[] a, double[] b){
        return sum(a, ponderate(b, -1));
    }

    public static double[][] diff(double[][] a, double[][] b){
        return sum(a, ponderate(b, -1));
    }

    public static void validateCanMultiply(double[][] a, double[][] b){
        if(a==null || b == null || a.length == 0 || b.length == 0){
            throw new NullPointerException("Cannot sum null or zero-length arrays");
        }

        if((!isSquare(a) && !isRectangular(a)) || (!isSquare(b) && !isRectangular(b))){
            throw new IllegalArgumentException("Only squared or rectangular arrays are allowed to multiply");
        }

        if(a[0].length != b.length){
            throw new IllegalArgumentException("The number of columns of the first array must be equal to the number of rows of the second array in order to multiply them");
        }
    }

    public static double[][] multiply(double[][] a, double[][] b) {
        validateCanMultiply(a, b);
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                double sum = 0;
                for (int k = 0; k < colsA; k++) {
                    sum += a[i][k] * b[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    public static double[][] transpose(double[][] matrix) {
        if(!isSquare(matrix) && !isRectangular(matrix)){
            throw new IllegalArgumentException("Only squared or rectangular arrays can have a transpose");
        }
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] transpose = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }
        return transpose;
    }

    public static double[][] inverse(double[][] ar){
        if(!isSquare(ar)){
            throw new IllegalArgumentException("Only squared arrays can be inversed");
        }

        double det = determinant(ar);
        if(det == 0){
            throw new ArithmeticException("Only arrays with a non-zero determinant can be inversed");
        }

        int n = ar.length;
        double[][] b = new double[ar.length][ar.length];
        double[][] a = Arrays.copyOf(ar, ar.length);
        IntStream.range(0, n).forEach(k -> {
            AtomicInteger pivotRow = new AtomicInteger(k);
            double[] maxValue = {Math.abs(a[k][k])};
            IntStream.range(k + 1, n).forEach(i -> {
                if(Math.abs(a[i][k]) > maxValue[0]){
                    maxValue[0] = Math.abs(a[i][k]);
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
            double pivot = a[k][k];
            IntStream.range(0, n).forEach(j -> {
                a[k][j] /= pivot;
                b[k][j] /= pivot;
            });
            IntStream.range(0, n).forEach(i -> {
                if(i != k){
                    double factor = a[i][k];
                    IntStream.range(0, n).forEach(j -> {
                        a[i][j] -= factor * a[k][j];
                        b[i][j] -= factor * b[k][j];
                    });
                }
            });
        });
        return b;
    }

    public static boolean equals(double[] a, double[] b){
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
            for(int i=0; i<a.length; i++){
                if(a[i] != b[i]){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(double[][] a, double[][] b){
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
        }
        return false;
    }

    public static boolean isSimetric(double[][] a){
        return equals(a, transpose(a));
    }

    public static boolean isSkewSimetric(double[][] a){
        return equals(ponderate(a, -1), transpose(a));
    }

    public static boolean isOrthogonal(double[][] a){
        try{
            return equals(inverse(a), transpose(a));
        }catch(ArithmeticException _){
            return false;
        }
    }

    public static double[][] divide(double[][] a, double[][] b){
        return multiply(a, inverse(b));
    }
}
