package cl.maraneda.cplx;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.IntStream;

/**
 * This class contains methods to perform mathematics operations with unidimensional
 * and bidimensional arrays.
 * Most methods require the bidimensional arrays to be squared or rectangular.
 * That is, as a bidimensional array of double in Java is an array of a unidimensional
 * array of double, it is required that all unidimensional arrays in the
 * bidimensional array have the same size.
 * @author Marco Araneda
 * @version 1.0
 * @since 2026-06-01
 */
public class ArrayMath {
    private ArrayMath() { /* Nothing */}

    /**
     * Checks if biarray represents a square array or matrix. That is,
     * the number of rows is equal to the number of columns and all rows must
     * have the same number of columns (the latter, because Java allows matrices
     * to have arrays with different sizes).
     * @param biarray A bidimensional array to be checked if is square.
     * @return true if biarray is square, false otherwise.
     * @throws IllegalArgumentException if biarray is null or a zero length array.
     */
    public static boolean isSquare(double[][] biarray){
        if(biarray == null || biarray.length == 0){
            throw new IllegalArgumentException("Null or zero length arrays are not square");
        }
        for(double[] array : biarray){
            if(array == null || array.length == 0){
                throw new IllegalArgumentException("Matrices having null or zero-length rows are not square");
            }
           if(array.length != biarray.length){
               return false;
           }
        }
        return true;
    }

    /**
     * Checks if biarray represent a rectangular bidimensional array or matrix.
     * That is, although the number of rows may be distinct of the number of
     * columns, it is required that all rows must have the same number of
     * columns (the latter, because Java allows matrices to have arrays with
     * different sizes).<br>
     * NOTE: All square matrices are rectangular, but <b>NOT ALL</b> rectangular
     * matrices are square.
     * @param biarray The bidimensional array to be checked if is rectangular.
     * @return true if biarray is rectangular, false otherwise.
     * @throws IllegalArgumentException if biarray is null or a zero length array.
     */
    public static boolean isRectangular(double[][] biarray){
        if(biarray == null || biarray.length == 0){
            throw new IllegalArgumentException("Null or zero length arrays are not rectangular");
        }
        if(biarray.length == 1){
            return true;
        }
        int len = 0;
        boolean first = true;
        for(double[] array : biarray){
            if(array == null || array.length == 0){
                throw new IllegalArgumentException("Matrices having null or zero-length rows are not rectangular");
            }
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

    /**
     * Swaps each element between a and b. That is, the element in the first
     * position of a will be the first element of b and viceversa, the element
     * in the second position of a will be the second element of b and viceversa,
     * and so on, until the last element of the array with the lower (or equal)
     * length is swapped.<br>
     * NOTE: If both arrays are equal, nothing happens.
     * @param a The source array whose elements will be swapped.
     * @param b The target array whose elements will be swapped.
     * @throws IllegalArgumentException if at least one of the arrays is null
     * or has a length of zero OR the arrays have different lengths.
     * @see Arrays#equals(double[], double[])
     */
    public static void swap(double[] a, double[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Arrays must not be null nor have zero length");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Cannot swap arrays with different lengths");
        }
        if(!Arrays.equals(a, b)) {
            IntStream.range(0, Math.min(a.length, b.length)).forEach(i -> {
                double aux = a[i];
                a[i] = b[i];
                b[i] = aux;
            });
        }
    }

    /**
     * Swaps the elements of the array in the index i with the array in the
     * index j of biarray.<br>
     * Calling this method is the same as calling swap(biarray[i], biarray[j])<br>
     * NOTE: If i equals j, nothing happens.
     * @param biarray The bidimensional array containing the arrays to be
     *                swapped.
     * @param i The index of the source array inside biarray whose elements
     *          will be swapped.
     * @param j The index of the target array inside biarray whose elements
     *          will be swapped.
     * @throws IllegalArgumentException if biarray is null OR it's length is
     * lower than 2.
     * @throws ArrayIndexOutOfBoundsException if i or j are outside the range
     * of elements in biarray (that is, i or j are lower than zero or higher
     * than biarray's length - 1)
     * @see #swap(double[], double[]) 
     */
    public static void swap(double[][] biarray, int i, int j){
        if(biarray == null){
            throw new IllegalArgumentException("The array cannot be null");
        }
        if(biarray.length < 2){
            throw new IllegalArgumentException("Not enough rows to swap");
        }
        if(i != j) {
            swap(biarray[i], biarray[j]);
        }
    }

    /**
     * Swaps one element of array with another.<br>
     * NOTE: If i equals j, nothing happens.
     * @param array The array whose elements will be swapped.
     * @param i The index of the source element to be swapped.
     * @param j The index of the target element to be swapped.
     * @throws IllegalArgumentException if array is null OR it's length is zero.
     * @throws ArrayIndexOutOfBoundsException if i or j are outside the range
     * of elements in array (that is, i or j are lower than zero or higher
     * than array's length - 1)
     */
    public static void swap(double[] array, int i, int j){
        if(array == null || array.length == 0){
            throw new IllegalArgumentException("Cannot swap elements in a null or zero-length array");
        }
        if(i != j) {
            double aux = array[i];
            array[i] = array[j];
            array[j] = aux;
        }
    }

    /**
     * Swaps one element of array with another.<br>
     * NOTE: If i equals k and j equals l, nothing happens.
     * @param array The bidimensional array whose elements will be swapped.
     * @param i The row of the source element to be swapped.
     * @param j The column of the source element to be swapped.
     * @param k The row of the target element to be swapped.
     * @param l The column of the target element to be swapped.
     * @throws IllegalArgumentException if one of the following happens:
     * <ul>
     *     <li>array is null OR it's length is zero</li>
     *     <li>array[i] is null or the length of array[i] is zero</li>
     *     <li>array[k] is null or the length of array[k] is zero.</li>
     * </ul>
     * @throws ArrayIndexOutOfBoundsException if i, j, k or l are outside the range
     * of elements in array. That is, one of the following happens:
     * <ul>
     *     <li>i or k are lower than zero or higher than array's length - 1</li>
     *     <li>For array[i] and/or array[k], j or l are lower than zero or
     *     higher than the mentioned arrays' length - 1</li>
     * </ul>
     */
    public static void swap(double[][] array, int i, int j, int k ,int l){
        if(array == null || array.length == 0 ||
           array[i] == null || array[i].length == 0 ||
           array[j] == null || array[k].length == 0){
            throw new IllegalArgumentException("Cannot swap elements in a null or zero-length bidimensional array");
        }
        if(i != k && j != l) {
            double aux = array[i][j];
            array[i][j] = array[k][l];
            array[k][l] = aux;
        }
    }

    /**
     * Calculates the determinant of a matrix<br>.
     * In order to calculate the determinant, the matrix must not be null,
     * it's length must not be zero and must be square.
     * @param matrix The matrix whose determinant will be calculated.
     * @return The determinant of the matrix.
     * @throws IllegalArgumentException if the conditions described above to
     * calculate the determinant are not fulfilled.
     * @see #isSquare(double[][])
     */
    public static double determinant(double[][] matrix) {
        if(!isSquare(matrix)){
            throw new IllegalArgumentException("Only square matrices can have a determinant");
        }

        int n = matrix.length;

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

    /**
     * Checks if two bidimensional arrays have the same dimensions. That is,
     * both arrays have the same number of rows (even if it's zero) and,
     * if the number of rows is greater than zero, each row in "a" must have
     * the same length as the row in b with the same index.
     * @param a The array whose dimensions will be compared with b
     * @param b The array whose dimensions will be compared with a
     * @return true if both arrays have the same dimensions, false otherwise.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static boolean sameDimensions(double[][] a, double[][] b){
        if(a.length != b.length){
            return false;
        }
        if(a.length != 0) for(int i = 0; i < a.length; i++){
            if(a[i].length != b[i].length){
                return false;
            }
        }
        return true;
    }

    /** Checks if two unidimensional arrays have the same length.
     *
     * @param a The array whose length will be compared with b's.
     * @param b The array whose length will be compared with a's.
     * @return true if both arrays have the same length, false otherwise.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static boolean sameDimensions(double[] a, double[] b){
        return a.length == b.length;
    }
    
    /**
     * Sums two unidimensional arrays.
     * @param a The array used as the first operand.
     * @param b The array used as the second operand.
     * @return The array who's each element is the sum of the element of a
     * and the element of b in the same index.
     * @throws IllegalArgumentException if a and b don't have the same length
     * @see #sameDimensions(double[], double[])
     */
    public static double[] sum(double[] a, double[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot sum null or zero-length array(s)");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        double[] res = new double[a.length];
        IntStream.range(0, a.length).forEach(i -> res[i] = a[i] + b[i]);
        return res;
    }

    /**
     * Checks if two bidimensional arrays are valid operands to sum.<br>
     * The check is complete if:
     * <ul>
     *     <li>Neither array is null nor their length is zero</li>
     *     <li>Both arrays are square or both are rectangular</li>
     *     <li>Both arrays have the same dimensions</li>
     * </ul>
     * If all conditions are met, nothing happens. Otherwise, an exception
     * will be thrown.
     * @param a The first candidate to be an operand for summation
     * @param b The second candidate to be an operand for summation
     * @throws IllegalArgumentException if at least one of the conditions listed
     * above is not met.
     * @see #isSquare(double[][]) 
     * @see #isRectangular(double[][]) 
     * @see #sameDimensions(double[][], double[][]) 
     */
    public static void validateCanSum(double[][] a, double[][] b){
        if(a==null || b == null || a.length == 0 || b.length == 0){
            throw new NullPointerException("Cannot sum null or zero-length arrays");
        }

        if((!isSquare(a) && !isRectangular(a)) || (!isSquare(b) && !isRectangular(b))){
            throw new IllegalArgumentException("Only squared or rectangular arrays are allowed to sum");
        }

        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Both arrays must have the same dimensions");
        }
    }

    /**
     * Sums two bidimensional arrays or matrices.<br>
     * Given 2 arrays with the same dimensions, for each pair of indexes (i,j)
     * inside the range of those arrays, the result will be an array s with the
     * same dimensions as the operands in which s<sub>ij</sub> = a<sub>ij</sub>
     * + b<sub>ij</sub>
     * @param a The first operand of the summation.
     * @param b The second operand of the substraction
     * @return A bidimensional array with the sum of a and b as described above
     * @throws IllegalArgumentException if the validation to check if a and b can
     * be used as operands for the summation fails.
     * @see #validateCanSum(double[][], double[][])
     */
    public static double[][] sum(double[][] a, double[][] b){
        validateCanSum(a, b);
        double[][] res = new double[a.length][];
        IntStream.range(0, a.length).forEach(i ->  res[i]=sum(a[i], b[i]));
        return res;
    }

    /**
     * Ponderates the array a by the scalar s.<br>
     * The result is an array in which each element is the result of multiply
     * the element of the array "a" in the same index by s.<br>
     * For example, ponderating {1, 2, 3, 4, 5} by 10 results {10, 20, 30, 40, 50}
     * @param a The array to be ponderated.
     * @param s The scalar used to ponderate the array.
     * @return An array that results of ponderate a by s as described above.
     * @throws IllegalArgumentException if "a" is null, or it's length is zero.
     */
    public static double[] ponderate(double[] a, double s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        return Arrays.stream(a).map(x -> x * s).toArray();
    }

    /**
     * Ponderates the array a by the scalar s.<br>
     * Calling this method is the same as calling, for each sa being one row in
     * s, the method ponderate(sa, s).
     * @param a The array to be ponderated.
     * @param s The scalar used to ponderate the array.
     * @return An array that results of ponderate a by s as described above.
     * @throws IllegalArgumentException if "a" is null, or it's length is zero or
     * there is at least one row in a that is null, or it's length is zero.
     * @see #ponderate(double[], double) 
     */
    public static double[][] ponderate(double[][] a, double s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }

        double[][] res = new double[a.length][];
        IntStream.range(0, a.length).forEach(i -> res[i] = ponderate(a[i], s));
        return res;
    }

    /**
     * Calculates the substraction between 2 arrays.<br>
     * Given 2 arrays b and d, calling this method is the same as calling 
     * sum(b, ponderate(d, -1))
     * @param a The first operand of the substraction
     * @param b The second operand of the substraction
     * @return The difference between a and b as described above.
     * @throws IllegalArgumentException if the ponderation or the summation
     * fails.
     * @see #sum(double[], double[]) 
     * @see #ponderate(double[], double)
     */
    public static double[] diff(double[] a, double[] b){
        return sum(a, ponderate(b, -1));
    }

    /**
     * Calculates the substraction between 2 arrays.<br>
     * Given 2 arrays b and d, calling this method is the same as calling
     * sum(b, ponderate(d, -1))
     * @param a The first operand of the substraction
     * @param b The second operand of the substraction
     * @return The difference between a and b as described above.
     * @throws IllegalArgumentException if the ponderation or the summation
     * fails.
     * @see #sum(double[][], double[][])
     * @see #ponderate(double[][], double)
     */
    public static double[][] diff(double[][] a, double[][] b){
        return sum(a, ponderate(b, -1));
    }

    /**
     * Checks if two bidimensional arrays are valid operands to multiply.<br>
     * The check is complete if:
     * <ul>
     *     <li>Neither array is null nor their length is zero</li>
     *     <li>Each array is square or rectangular</li>
     *     <li>The number of columns of the first array is equal to the
     *     number of rows in the second</li>
     * </ul>
     * If all conditions are met, nothing happens. Otherwise, an exception
     * will be thrown.
     * @param a The first candidate to be an operand for multiplication
     * @param b The second candidate to be an operand for multiplication
     * @throws IllegalArgumentException if at least one of the conditions listed
     * above is not met.
     * @see #isSquare(double[][])
     * @see #isRectangular(double[][])
     */
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

    /**
     * Checks if a unidimensional array and a bidimensional array can be multiplied.<br>
     * If all checks are passed, nothing happens. Otherwise, an exception will be thrown.
     * @param a The unidimensional array.
     * @param b The bidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #validateCanMultiply(double[][], double[][]) 
     */
    public static void validateCanMultiply(double[] a, double[][] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        double[][] bda = new double[1][];
        bda[0] = Arrays.copyOf(a, a.length);
        validateCanMultiply(bda, b);
    }

    /**
     * Checks if a bidimensional array and a unidimensional array can be multiplied.<br>
     * If all checks are passed, nothing happens. Otherwise, an exception will be thrown.
     * @param a The bidimensional array.
     * @param b The unidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #validateCanMultiply(double[][], double[][])
     */
    public static void validateCanMultiply(double[][] a, double[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        double[][] bdb = new double[b.length][1];
        IntStream.range(0, b.length).forEach(i -> bdb[i][0] = b[i]);
        validateCanMultiply(a, bdb);
    }

    /**
     * Checks if two unidimensional arrays can be multiplied.<br>
     * Only non-null, non-zero length arrays with the same length can be
     * multiplied.
     * @param a The first unidimensional array.
     * @param b The second unidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #sameDimensions(double[], double[]) 
     */
    public static void validateCanMultply(double[] a, double[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Both arrays must have the same length");
        }
    }
    /** Calculates the multiplication between 2 bidimensional arrays.<br>
     * For 2 bidimensional arrays A<sub>m,n</sub> and B<sub>n,q</sub>, the
     * product between both is a bidimensional array M<sub>m,q</sub> so that
     * for each 1 &le; i &le; m and 1 &le; j &le; q, M<sub>i,j</sub> is calculated
     * as follows:<br><br>
     * M<sub>i,j</sub> = A<sub>i,1</sub> * B<sub>1,j</sub> +
     * A<sub>i,2</sub> * B<sub>2,j</sub> + ... + A<sub>i,n</sub> * B<sub>n,j</sub><br>
     * @param a The first operand of the multiplication
     * @param b The second operand of the multiplication
     * @return The product between a and b as described above.
     * @throws IllegalArgumentException if the validation to check if both
     * arrays can be multiplied fails.
     * @see #validateCanMultiply(double[][], double[][]) 
     */
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

    /**
     * Multiples a unidimensional array with a bidimensional array.<br>
     * Multiplying both arrays is the same as to multiply A<sub>1, n</sub>
     * with B<sub>n, m</sub>
     * @param a The first operand of the multiplication.
     * @param b The second operand of the multiplication.
     * @return The product between a and b, a bidimensional array M<sub>1, m</sub>
     * @throws IllegalArgumentException if the validations to check if a and b
     * can be multiplied fail.
     * @see #multiply(double[][], double[][])
     */
    public static double[][] multiply(double[] a, double[][] b){
        validateCanMultiply(a, b);
        double[][] bda = new double[1][];
        bda[0] = Arrays.copyOf(a, a.length);
        return multiply(bda, b);
    }

    /**
     * Multiples a bidimensional array with a unidimensional array.<br>
     * Multiplying both arrays is the same as to multiply A<sub>m, n</sub>
     * with B<sub>n, 1</sub>
     * @param a The first operand of the multiplication.
     * @param b The second operand of the multiplication.
     * @return The product between a and b, a bidimensional array M<sub>m, 1</sub>
     * @throws IllegalArgumentException if the validations to check if a and b
     * can be multiplied fail.
     * @see #validateCanMultiply(double[], double[][])
     * @see #multiply(double[][], double[][])
     */
    public static double[][] multiply(double[][] a, double[] b){
        validateCanMultiply(a, b);
        double[][] bdb = new double[b.length][1];
        IntStream.range(0, b.length).forEach(i -> bdb[i][0] = b[i]);
        return multiply(a, bdb);
    }

    /**
     * Multiples 2 unidimensional arrays.<br>
     * Multiplying both arrays is the same as to multiply A<sub>1, n</sub>
     * with B<sub>n, 1</sub>. As a consequence, as the result will be a 1x1 matrix,
     * a simple element will be returned instead of the matrix that element
     * belongs to.
     * @param a The first operand of the multiplication.
     * @param b The second operand of the multiplication.
     * @return The product between a and b as described above.
     * @throws IllegalArgumentException if the validations to check if both
     * arrays can be multiplied fail.
     * @see #validateCanMultply(double[], double[])
     */
    public static double multiply(double[] a, double[] b){
        validateCanMultply(a, b);
        DoubleAdder da = new DoubleAdder();
        IntStream.range(0, a.length).forEach(i -> da.add(a[i] * b[i]));
        return da.sum();
    }

    /**
     * Calculates the transpose of a bidimensional array or matrix.<br>
     * Given one matrix A<sub>m, n</sub>, that matrix's transpose, named
     * A<sup>T</sup> is a matrix B<sub>n, m</sub> in that for each 1 &le; j &le; n,
     * The column j of A becomes the row j of B.
     * @param matrix A bidimensional array whose transpose will be calculated.
     * @return The transpose of matrix as described above.
     * @throws IllegalArgumentException if matrix is not a square or rectangular
     * bidimensional array.  NOTE: This validation implies that the matrix must
     * not be null or zero-length.
     * @see #isRectangular(double[][]) 
     * @see #isSquare(double[][])
     */
    public static double[][] transpose(double[][] matrix) {
        if(matrix == null || matrix.length == 0){
            throw new IllegalArgumentException("Cannot transpose null or zero-length arrays");
        }
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

    /**
     * Checks if a bidimensional array is singular.<br>
     * Only square arrays having a zero determinant are singular.
     * @param ar The array to be checked if is singular.
     * @return true if ar is singular, false otherwise.
     * @throws IllegalArgumentException if ar is null or if it's length is zero.
     * @see #isSquare(double[][]) 
     * @see #determinant(double[][]) 
     */
    public static boolean isSingular(double[][] ar){
        if (ar == null || ar.length == 0) {
            throw new IllegalArgumentException(
                    "Null or zero-length arrays cannot be singular");
        }
        if (!isSquare(ar)) {
            return false;
        }
        return determinant(ar) == 0;
    }

    /**
     * Calculates the inverse of a bidimensional array.<br>
     * Only arrays that fulfill the following conditions can have an inverse:
     * <ul>
     *     <li>Must not be null nor zero-length.</li>
     *     <li>Must be square.</li>
     *     <li>It's determinant must not be zero. That is, must not be singular</li>
     * </ul>
     * @param ar The array whose inverse will be calculated.
     * @return The inverse of ar as described above
     * @throws IllegalArgumentException if ar is null or zero-length or non-square.
     * @throws ArithmeticException if ar is singular.
     * @see #isSingular(double[][]) 
     */
    public static double[][] inverse(double[][] ar) {
        if(isSingular(ar)){
            throw new ArithmeticException("Singular matrix");
        }
        int n = ar.length;
        double[][] a = Arrays.stream(ar)
                .map(double[]::clone)
                .toArray(double[][]::new);

        double[][] b = new double[n][n];
        IntStream.range(0, n).forEach(i -> b[i][i] = 1);
        IntStream.range(0, n).forEach(k -> {
            AtomicInteger pivotRow = new AtomicInteger(k);
            IntStream.range(k + 1, n).forEach(i -> {
                if (Math.abs(a[i][k]) >
                        Math.abs(a[pivotRow.get()][k])) {
                    pivotRow.set(i);
                }
            });

            if (Math.abs(a[pivotRow.get()][k]) == 0) {
                throw new ArithmeticException(
                        "Singular matrix");
            }

            if (pivotRow.get() != k) {
                swap(a, k, pivotRow.get());
                swap(b, k, pivotRow.get());
            }

            double pivot = a[k][k];

            IntStream.range(0, n).forEach(j -> {
                a[k][j] /= pivot;
                b[k][j] /= pivot;
            });

            IntStream.range(0, n).forEach(i -> {
                if (i != k) {
                    double factor = a[i][k];
                    for (int j = 0; j < n; j++) {
                        a[i][j] -= factor * a[k][j];
                        b[i][j] -= factor * b[k][j];
                    }
                }
            });
        });

        return b;
    }

    /** Checks if both unidimensional arrays are equal.<br>
     * Both arrays are equal if they have the same length n and, for each
     * 1 &le; i &le; n, A<sub>i</sub> = B<sub>i</sub><br>
     * NOTE: If both arrays are null or zero-length, both are considered equal.<br>
     * @param a The first array to be compared.
     * @param b The second array to be compared.
     * @return true if both arrays are equal, false otherwise.
     */
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

    /** Checks if both bidimensional arrays are equal.<br>
     * Both arrays are equal if they have the same number of rows m and the same
     * number of columns n and, for each
     * 1 &le; i &le; m and 1 &le; j &le; n, A<sub>i,j</sub> = B<sub>i,j</sub><br>
     * NOTE: If both arrays are null or zero-length, both are considered equal.<br>
     * @param a The first array to be compared.
     * @param b The second array to be compared.
     * @return true if both arrays are equal, false otherwise.
     */
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
            return eq;
        }
        return false;
    }

    /**
     * Checks if a bidimensional array is symmetric.<br>
     * An array A is symmetric if and only if A = A<sup>T</sup>
     * @param a The array to check if is symmetric.
     * @return true if "a" is symmetric, false otherwise.
     * @throws IllegalArgumentException if the transpose fails.
     * @see #equals(double[][], double[][]) 
     * @see #transpose(double[][]) 
     */
    public static boolean isSymmetric(double[][] a){
        return equals(a, transpose(a));
    }

    /**
     * Checks if a bidimensional array is skew symmetric.<br>
     * An array A is skew symmetric if and only if it's additive inverse equals
     * it's transpose.
     * @param a The array to check if is skew symmetric.
     * @return true if "a" is skew symmetric, false otherwise.
     * @throws IllegalArgumentException if the transpose fails.
     * @see #equals(double[][], double[][]) 
     * @see #ponderate(double[][], double) 
     * @see #transpose(double[][]) 
     */
    public static boolean isSkewSimetric(double[][] a){
        return equals(ponderate(a, -1), transpose(a));
    }

    /**
     * Checks if a bidimensional array is orthogonal.<br>
     * An array A is orthogonal if and only if is square, is not singular and
     * its inverse is equal to its transpose.
     * @param a The array to be checked if is orthogonal.
     * @return true if "a" is orthogonal, false otherwise.
     * @throws IllegalArgumentException if the validations to determinate if
     * "a" is square or singular fail OR if the transpose fails.
     * @see #isSingular(double[][])
     * @see #isSquare(double[][])
     * @see #inverse(double[][])
     * @see #transpose(double[][])
     */
    public static boolean isOrthogonal(double[][] a){
        if(!isSquare(a) || isSingular(a)){
            return false;
        }
        return equals(inverse(a), transpose(a));
    }

    /**
     * Divides two matrices.<br>
     * A division between 2 matrices A and B can be made if and only if:
     * <ul>
     *     <li>Neither matrix is null or zero-length</li>
     *     <li>The number of columns in A is equal to the number of
     *     columns in B</li>
     *     <li>B is square and is not singular</li>
     * </ul>
     * Calling this method for 2 arrays b and c is the same as calling
     * multiply(b, inverse(c))
     * @param a The first operand of the division
     * @param b The second operand of the division
     * @return The division between a and b as described above
     * @throws IllegalArgumentException if the multiplication or the inverse fails.
     * @see #multiply(double[][], double[][]) 
     * @see #inverse(double[][])
     */
    public static double[][] divide(double[][] a, double[][] b){
        return multiply(a, inverse(b));
    }

    /**
     * Creates one n-length identity matrix.<br>
     * An identity matrix is a square matrix having 1 where the index for the
     * row and the index for the column are equal, and zero elsewhere.<br>
     * For example, for n = 3, the resulting matrix is {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}
     * @param n The length of the identity matrix.
     * @return The n-length identity matrix as described above.
     * @throws IllegalArgumentException if n is lower or equal than zero.
     */
    public static double[][] identity(int n){
        if(n <= 0){
            throw new IllegalArgumentException("Cannot create identity arrays with zero or negative length");
        }
        double[][] i = new double[n][n];
        IntStream.range(0, n).forEach(j -> i[j][j] = 1);
        return i;
    }
}
