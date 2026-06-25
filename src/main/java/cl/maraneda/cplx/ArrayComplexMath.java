package cl.maraneda.cplx;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * This class contains methods to perform mathematics operations with unidimensional
 * and bidimensional arrays containing at least one complex or imaginary number.
 * Most methods require the bidimensional arrays to be squared or rectangular.
 * That is, as a bidimensional array of ComplexNumber, RealNumber or ImaginaryNumber
 * in Java is an array of a unidimensional array of ComplexNumber, RealNumber or
 * ImaginaryNumber, it is required that all unidimensional arrays in the
 * bidimensional array have the same size.
 * @author Marco Araneda
 * @version 1.0
 * @since 2026-06-01
 */
public class ArrayComplexMath {
    private ArrayComplexMath() { /* Nothing */}

    /**
     * Checks if biarray represents a square array or matrix. That is,
     * the number of rows is equal to the number of columns and all rows must
     * have the same number of columns (the latter, because Java allows matrices
     * to have arrays with different sizes).
     * @param biarray A bidimensional array to be checked if is square.
     * @return true if biarray is square, false otherwise.
     * @throws IllegalArgumentException if biarray is null or a zero length array.
     */
    public static boolean isSquare(MathResult[][] biarray){
        if(biarray == null || biarray.length == 0){
            throw new IllegalArgumentException("Null or zero length arrays are not square");
        }
        for(MathResult[] array : biarray){
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
    public static boolean isRectangular(MathResult[][] biarray){
        if(biarray == null || biarray.length == 0){
            throw new IllegalArgumentException("Null or zero length arrays are not rectangular");
        }
        if(biarray.length == 1){
            return true;
        }
        int len = 0;
        boolean first = true;
        for(MathResult[] array : biarray){
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
     * Checks if two bidimensional arrays have the same dimensions. That is,
     * both arrays have the same number of rows (even if it's zero) and,
     * if the number of rows is greater than zero, each row in "a" must have
     * the same length as the row in b with the same index.
     * @param a The array whose dimensions will be compared with b
     * @param b The array whose dimensions will be compared with a
     * @return true if both arrays have the same dimensions, false otherwise.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static boolean sameDimensions(MathResult[][] a, MathResult[][] b){
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

    /**
     * Checks if two bidimensional arrays (one real and one that can have real,
     * imaginary or complex numbers) have the same dimensions.<br> That is,
     * both arrays have the same number of rows (even if it's zero) and,
     * if the number of rows is greater than zero, each row in "a" must have
     * the same length as the row in b with the same index.
     * @param a The array whose dimensions will be compared with b
     * @param b The array whose dimensions will be compared with a
     * @return true if both arrays have the same dimensions, false otherwise.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static boolean sameDimensions(double[][] a, MathResult[][] b){
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

    /**
     * Checks if two bidimensional arrays (one that can have real, imaginary or
     * complex numbers and one real) have the same dimensions.<br> That is,
     * both arrays have the same number of rows (even if it's zero) and,
     * if the number of rows is greater than zero, each row in "a" must have
     * the same length as the row in b with the same index.
     * @param a The array whose dimensions will be compared with b
     * @param b The array whose dimensions will be compared with a
     * @return true if both arrays have the same dimensions, false otherwise.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static boolean sameDimensions(MathResult[][] a, double[][] b){
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

    /** Checks if two real or imaginary or complex
     *  unidimensional arrays have the same length.
     *
     * @param a The array whose length will be compared with b's.
     * @param b The array whose length will be compared with a's.
     * @return true if both arrays have the same length, false otherwise.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static boolean sameDimensions(MathResult[] a, MathResult[] b){
        return a.length == b.length;
    }

    /** Checks if two unidimensional arrays (one real and one that can be
     * real, imaginary or complex) have the same length.
     *
     * @param a The array whose length will be compared with b's.
     * @param b The array whose length will be compared with a's.
     * @return true if both arrays have the same length, false otherwise.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static boolean sameDimensions(double [] a, MathResult[] b){
        return a.length == b.length;
    }

    /** Checks if two unidimensional arrays (one that can be real, imaginary
     * or complex and one real) have the same length.
     *
     * @param a The array whose length will be compared with b's.
     * @param b The array whose length will be compared with a's.
     * @return true if both arrays have the same length, false otherwise.
     * @throws NullPointerException if at least one of the arguments is null.
     */
    public static boolean sameDimensions(MathResult[] a, double[] b){
        return a.length == b.length;
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
    public static void swap(MathResult[] a, MathResult[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Arrays must not be null nor have zero length");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Cannot swap arrays with different lengths");
        }
        if(!Arrays.equals(a, b)) {
            MathResult[] aux = Arrays.copyOf(a, Math.min(a.length, b.length));
            System.arraycopy(b, 0, a, 0, aux.length);
            System.arraycopy(aux, 0, b, 0, aux.length);
        }
    }

    /**
     * Swaps the elements of the array in the index i with the array in the
     * index j of biarray.<br>
     * Calling this method is the same as calling swap(biarray[i], biarray[j])<br>
     * NOTE: If i equals j, nothing happens.
     * @param array The bidimensional array containing the arrays to be
     *                swapped.
     * @param i The index of the source array inside array whose elements
     *          will be swapped.
     * @param j The index of the target array inside array whose elements
     *          will be swapped.
     * @throws IllegalArgumentException if array is null OR it's length is
     * lower than 2.
     * @throws ArrayIndexOutOfBoundsException if i or j are outside the range
     * of elements in array (that is, i or j are lower than zero or higher
     * than array's length - 1)
     * @see #swap(MathResult[], MathResult[])
     */
    public static void swap(MathResult[][] array, int i, int j){
        if(array == null){
            throw new IllegalArgumentException("The array cannot be null");
        }
        if(array.length < 2){
            throw new IllegalArgumentException("Not enough rows to swap");
        }
        if(i != j) {
            swap(array[i], array[j]);
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
    public static void swap(MathResult[] array, int i, int j){
        if(array == null || array.length == 0){
            throw new IllegalArgumentException("Cannot swap elements in a null or zero-length array");
        }
        if(i != j) {
            MathResult aux = array[i];
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
    public static void swap(MathResult[][] array, int i, int j, int k ,int l){
        if(array == null || array.length == 0 ||
                array[i] == null || array[i].length == 0 ||
                array[j] == null || array[k].length == 0){
            throw new IllegalArgumentException("Cannot swap elements in a null or zero-length bidimensional array");
        }
        if(i != k && j != l) {
            MathResult aux = array[i][j];
            array[i][j] = array[k][l];
            array[k][l] = aux;
        }
    }

    /**
     * Calculates the determinant of a matrix<br>.
     * In order to calculate the determinant, the matrix must not be null,
     * it's length must not be zero and must be square.
     * @param array The matrix whose determinant will be calculated.
     * @return The determinant of the matrix.
     * @throws IllegalArgumentException if the conditions described above to
     * calculate the determinant are not fulfilled.
     * @see #isSquare(MathResult[][])
     * @see ComplexMath#modulus(ComplexNumber) 
     * @see ComplexMath#divide(ComplexNumber, ComplexNumber) 
     * @see ComplexMath#multiply(ComplexNumber...) 
     * @see ComplexMath#diff(ComplexNumber, ComplexNumber) 
     * @see ComplexMath#ponderate(ComplexNumber, double)
     * @see ComplexMath#sum(ComplexNumber...)
     */
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

    /**
     * Sums two unidimensional arrays.
     * @param a The array used as the first operand.
     * @param b The array used as the second operand.
     * @return The array who's each element is the sum of the element of a
     * and the element of b in the same index.
     * @throws IllegalArgumentException if a and b don't have the same length
     * @see #sameDimensions(MathResult[], MathResult[])
     */
    public static MathResult[] sum(MathResult[] a, MathResult[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot sum null or zero-length array(s)");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        MathResult[] res = new MathResult[a.length];
        IntStream.range(0, a.length).forEach(i -> res[i] = ComplexMath.sum(a[i].toComplex(), b[i].toComplex()));
        return res;
    }

    /**
     * Sums two unidimensional arrays (one real and one that can have real,
     * imaginary and/or complex numbers).
     * @param a The array used as the first operand.
     * @param b The array used as the second operand.
     * @return The array who's each element is the sum of the element of a
     * and the element of b in the same index.
     * @throws IllegalArgumentException if a and b don't have the same length
     * @see #sameDimensions(MathResult[], MathResult[])
     */
    public static MathResult[] sum(double[] a, MathResult[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot sum null or zero-length array(s)");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        MathResult[] res = new MathResult[a.length];
        IntStream.range(0, a.length).forEach(i -> res[i] = ComplexMath.sum(new ComplexNumber(a[i], 0), b[i].toComplex()));
        return res;
    }

    /**
     * Sums two unidimensional arrays (one that can have real,
     * imaginary and/or complex numbers and one real).
     * @param a The array used as the first operand.
     * @param b The array used as the second operand.
     * @return The array who's each element is the sum of the element of a
     * and the element of b in the same index.
     * @throws IllegalArgumentException if a and b don't have the same length
     * @see #sameDimensions(MathResult[], MathResult[])
     */
    public static MathResult[] sum(MathResult[] a, double[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot sum null or zero-length array(s)");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        MathResult[] res = new MathResult[a.length];
        IntStream.range(0, a.length).forEach(i -> res[i] = ComplexMath.sum(new ComplexNumber(b[i], 0), a[i].toComplex()));
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
     * @see #isSquare(MathResult[][])
     * @see #isRectangular(MathResult[][])
     * @see #sameDimensions(MathResult[][], MathResult[][])
     */
    public static void validateCanSum(MathResult[][] a, MathResult[][] b){
        Objects.requireNonNull(a, "Arrays cannot be null");
        Objects.requireNonNull(b, "Arrays cannot be null");

        if(a.length == 0 || b.length == 0){
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
     * Checks if two bidimensional arrays (one that can have real, imaginary or
     * complex numbers and one real) are valid operands to sum.<br>
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
     * @see #isSquare(MathResult[][])
     * @see #isRectangular(MathResult[][])
     * @see #sameDimensions(MathResult[][], double[][])
     * @see ArrayMath#isSquare(double[][]) 
     * @see ArrayMath#isRectangular(double[][])
     */
    public static void validateCanSum(MathResult[][] a, double[][] b){
        Objects.requireNonNull(a, "Arrays cannot be null");
        Objects.requireNonNull(b, "Arrays cannot be null");

        if(a.length == 0 || b.length == 0){
            throw new NullPointerException("Cannot sum null or zero-length arrays");
        }

        if((!isSquare(a) && !isRectangular(a)) ||
            (!ArrayMath.isSquare(b) && !ArrayMath.isRectangular(b))){
            throw new IllegalArgumentException("Only squared or rectangular arrays are allowed to sum");
        }

        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Both arrays must have the same dimensions");
        }
    }

    /**
     * Checks if two bidimensional arrays (one real and one that can have real,
     * imaginary or complex numbers) are valid operands to sum.<br>
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
     * @see #isSquare(MathResult[][])
     * @see #isRectangular(MathResult[][])
     * @see #sameDimensions(MathResult[][], double[][])
     * @see ArrayMath#isSquare(double[][])
     * @see ArrayMath#isRectangular(double[][])
     */
    public static void validateCanSum(double[][] a, MathResult[][] b){
        Objects.requireNonNull(a, "Arrays cannot be null");
        Objects.requireNonNull(b, "Arrays cannot be null");

        if(a.length == 0 || b.length == 0){
            throw new NullPointerException("Cannot sum null or zero-length arrays");
        }

        if((!ArrayMath.isSquare(a) && !ArrayMath.isRectangular(a)) ||
            (!isSquare(b) && !isRectangular(b))){
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
     * @see #validateCanSum(MathResult[][], MathResult[][])
     * @see #sum(MathResult[], double[])
     */
    public static MathResult[][] sum(MathResult[][] a, MathResult[][] b){
        validateCanSum(a, b);
        MathResult[][] res = new MathResult[a.length][];
        IntStream.range(0, a.length).forEach(i ->  res[i]=sum(a[i], b[i]));
        return res;
    }

    /**
     * Sums two bidimensional arrays or matrices (one real and one that
     * can have real, imaginary or complex numbers).<br>
     * Given 2 arrays with the same dimensions, for each pair of indexes (i,j)
     * inside the range of those arrays, the result will be an array s with the
     * same dimensions as the operands in which s<sub>ij</sub> = a<sub>ij</sub>
     * + b<sub>ij</sub>
     * @param a The first operand of the summation.
     * @param b The second operand of the substraction
     * @return A bidimensional array with the sum of a and b as described above
     * @throws IllegalArgumentException if the validation to check if a and b can
     * be used as operands for the summation fails.
     * @see #validateCanSum(double[][], MathResult[][])
     * @see #sum(double[], MathResult[])
     */
    public static MathResult[][] sum(double[][] a, MathResult[][] b){
        validateCanSum(a, b);
        MathResult[][] res = new MathResult[a.length][];
        IntStream.range(0, a.length).forEach(i ->  res[i]=sum(a[i], b[i]));
        return res;
    }

    /**
     * Sums two bidimensional arrays or matrices (one that can have real,
     * imaginary or complex numbers and one real).<br>
     * Given 2 arrays with the same dimensions, for each pair of indexes (i,j)
     * inside the range of those arrays, the result will be an array s with the
     * same dimensions as the operands in which s<sub>ij</sub> = a<sub>ij</sub>
     * + b<sub>ij</sub>
     * @param a The first operand of the summation.
     * @param b The second operand of the substraction
     * @return A bidimensional array with the sum of a and b as described above
     * @throws IllegalArgumentException if the validation to check if a and b can
     * be used as operands for the summation fails.
     * @see #validateCanSum(MathResult[][], double[][])
     * @see #sum(MathResult[], double[]) 
     */
    public static MathResult[][] sum(MathResult[][] a, double[][] b){
        validateCanSum(a, b);
        MathResult[][] res = new MathResult[a.length][];
        IntStream.range(0, a.length).forEach(i ->  res[i]=sum(a[i], b[i]));
        return res;
    }

    /**
     * Ponderates the array a by the scalar s.<br>
     * The result is an array in which each element is the result of multiply
     * the element of the array "a" in the same index by s.<br>
     * See the method with the same name in ArrayMath for an example.
     * @param a The array to be ponderated.
     * @param s The scalar used to ponderate the array.
     * @return An array that results of ponderate a by s as described above.
     * @throws IllegalArgumentException if "a" is null, or it's length is zero.
     * @see ArrayMath#ponderate(double[], double)
     * @see ComplexMath#ponderate(ComplexNumber, double)
     */
    public static MathResult[] ponderate(MathResult[] a, double s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        return Arrays.stream(a).map(x -> ComplexMath.ponderate(x.toComplex(), s)).toArray(MathResult[]::new);
    }

    /**
     * Ponderates the array a by the scalar s.<br>
     * The result is an array in which each element is the result of multiply
     * the element of the array "a" in the same index by s.<br>
     * The main difference with ponderate(MathResult[], double) is that this
     * method can use an imaginary or complex number besides a real number as
     * a scalar.
     * @param a The array to be ponderated.
     * @param s The scalar used to ponderate the array.
     * @return An array that results of ponderate a by s as described above.
     * @throws IllegalArgumentException if "a" is null, or it's length is zero OR the scalar is null.
     * @see ComplexMath#ponderate(ComplexNumber, double)
     * @see ComplexMath#multiply(ComplexNumber...)
     */
    public static MathResult[] ponderate(MathResult[] a, MathResult s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        if(s == null){
            throw new IllegalArgumentException("Cannot use a null object as a scalar");
        }
        return Arrays.stream(a).map(x -> ComplexMath.multiply(x.toComplex(), s.toComplex())).toArray(MathResult[]::new);
    }

    /**
     * Ponderates the array a by the scalar s.<br>
     * The result is an array in which each element is the result of multiply
     * the element of the array "a" in the same index by s.<br>
     * The main difference with ponderate(MathResult[], double) is that this
     * method can use an imaginary or complex number besides a real number as
     * a scalar to ponderate an array of double.
     * @param a The array to be ponderated.
     * @param s The scalar used to ponderate the array.
     * @return An array that results of ponderate a by s as described above.
     * @throws IllegalArgumentException if "a" is null, or it's length is zero OR the scalar is null.
     * @see ComplexMath#ponderate(ComplexNumber, double)
     */
    public static MathResult[] ponderate(double[] a, MathResult s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        if(s == null){
            throw new IllegalArgumentException("Cannot use a null object as a scalar");
        }
        return Arrays.stream(a).mapToObj(x -> ComplexMath.ponderate(s.toComplex(), x)).toArray(MathResult[]::new);
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
     * @see #ponderate(MathResult[], double)
     */
    public static MathResult[][] ponderate(MathResult[][] a, double s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        MathResult[][] res = new MathResult[a.length][];
        IntStream.range(0, a.length).forEach(i -> res[i] = ponderate(a[i], s));
        return res;
    }

    /**
     * Ponderates the array a by a real, imaginary or complex scalar s.<br>
     * Calling this method is the same as calling, for each sa being one row in
     * s, the method ponderate(sa, s).
     * @param a The array to be ponderated.
     * @param s The scalar used to ponderate the array.
     * @return An array that results of ponderate a by s as described above.
     * @throws IllegalArgumentException if "a" is null, or it's length is zero or
     * there is at least one row in a that is null, or it's length is zero OR
     * the scalar is null.
     * @see #ponderate(MathResult[], MathResult)
     */
    public static MathResult[][] ponderate(MathResult[][] a, MathResult s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        if(s == null){
            throw new IllegalArgumentException("Cannot use a null object as a scalar");
        }
        MathResult[][] res = new MathResult[a.length][];
        IntStream.range(0, a.length).forEach(i -> res[i] = ponderate(a[i], s));
        return res;
    }

    /**
     * Ponderates an array of double a by a real, imaginary or complex scalar s.<br>
     * Calling this method is the same as calling, for each sa being one row in
     * s, the method ponderate(sa, s).
     * @param a The array to be ponderated.
     * @param s The scalar used to ponderate the array.
     * @return An array that results of ponderate a by s as described above.
     * @throws IllegalArgumentException if "a" is null, or it's length is zero or
     * there is at least one row in a that is null, or it's length is zero OR
     * the scalar is null.
     * @see #ponderate(MathResult[], MathResult)
     */
    public static MathResult[][] ponderate(double[][] a, MathResult s){
        if(a==null || a.length == 0){
            throw new IllegalArgumentException("Cannot ponderate a zero-length or null array with a scalar");
        }
        if(s == null){
            throw new IllegalArgumentException("Cannot use a null object as a scalar");
        }
        MathResult[][] res = new MathResult[a.length][];
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
     * @see #sum(MathResult[], MathResult[])
     * @see #ponderate(MathResult[], double)
     */
    public static MathResult[] diff(MathResult[] a, MathResult[] b){
        return sum(a, ponderate(b, -1));
    }

    /**
     * Calculates the substraction between one array of double and one that
     * can have real, imaginary and/or complex numbers.<br>
     * Given 2 arrays b and d, calling this method is the same as calling
     * sum(b, ponderate(d, -1))
     * @param a The first operand of the substraction
     * @param b The second operand of the substraction
     * @return The difference between a and b as described above.
     * @throws IllegalArgumentException if the ponderation or the summation
     * fails.
     * @see #sum(double[], MathResult[])
     * @see #ponderate(MathResult[], double)
     */
    public static MathResult[] diff(double[] a, MathResult[] b){
        return sum(a, ponderate(b, -1));
    }

    /**
     * Calculates the substraction between one array that can have real,
     * imaginary and/or complex numbers and one array of double.<br>
     * Given 2 arrays b and d, calling this method is the same as calling
     * sum(b, ArrayMath.ponderate(d, -1))
     * @param a The first operand of the substraction
     * @param b The second operand of the substraction
     * @return The difference between a and b as described above.
     * @throws IllegalArgumentException if the ponderation or the summation
     * fails.
     * @see #sum(MathResult[], double[])
     * @see ArrayMath#ponderate(double[], double)
     */
    public static MathResult[] diff(MathResult[] a, double[] b){
        return sum(a, ArrayMath.ponderate(b, -1));
    }

    /**
     * Calculates the substraction between 2 bidimensional arrays.<br>
     * Given 2 arrays b and d, calling this method is the same as calling
     * sum(b, ponderate(d, -1))
     * @param a The first operand of the substraction
     * @param b The second operand of the substraction
     * @return The difference between a and b as described above.
     * @throws IllegalArgumentException if the ponderation or the summation
     * fails.
     * @see #sum(MathResult[][], MathResult[][])
     * @see #ponderate(MathResult[][], double)
     */
    public static MathResult[][] diff(MathResult[][] a, MathResult[][] b){
        return sum(a, ponderate(b, -1));
    }

    /**
     * Calculates the substraction between 2 bidimensional arrays (one array
     * of double and one that can have real, imaginary and/or complex numbers.<br>
     * Given 2 arrays b and d, calling this method is the same as calling
     * sum(b, ponderate(d, -1))
     * @param a The first operand of the substraction
     * @param b The second operand of the substraction
     * @return The difference between a and b as described above.
     * @throws IllegalArgumentException if the ponderation or the summation
     * fails.
     * @see #sum(double[][], MathResult[][])
     * @see #ponderate(MathResult[][], double)
     */
    public static MathResult[][] diff(double[][] a, MathResult[][] b){
        return sum(a, ponderate(b, -1));
    }

    /**
     * Calculates the substraction between 2 bidimensional arrays (one that
     * can have real, imaginary and/or complex numbers and one of double).<br>
     * Given 2 arrays b and d, calling this method is the same as calling
     * sum(b, ArrayMath.ponderate(d, -1))
     * @param a The first operand of the substraction
     * @param b The second operand of the substraction
     * @return The difference between a and b as described above.
     * @throws IllegalArgumentException if the ponderation or the summation
     * fails.
     * @see #sum(MathResult[][], double[][])
     * @see ArrayMath#ponderate(double[][], double)
     */
    public static MathResult[][] diff(MathResult[][] a, double[][] b){
        return sum(a, ArrayMath.ponderate(b, -1));
    }

    /**
     * Validates if two bidimensional arrays can be used as operands for an
     * array multiplication.<br>
     * Two arrays can be multiplied if, and only if, the following conditions
     * are met:
     * <ul>
     *     <li>Neither are null nor zero-length</li>
     *     <li>Both are square or rectangular</li>
     *     <li>The number of columns of the first array equals the number
     *     of rows of the second.</li>
     * </ul>
     * If all conditions are met, nothing happens. Otherwise, an exception will
     * be thrown.
     * @param a The first candidate for an array multiplication.
     * @param b The second candidate for an array multiplication.
     * @throws IllegalArgumentException if at least one of the conditions listed
     * above is not met.
     * @see #isSquare(MathResult[][]) 
     * @see #isRectangular(MathResult[][])
     */
    public static void validateCanMultiply(MathResult[][] a, MathResult[][] b){
        if(a==null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero-length arrays");
        }

        if((!isSquare(a) && !isRectangular(a)) || (!isSquare(b) && !isRectangular(b))){
            throw new IllegalArgumentException("Only squared or rectangular arrays are allowed to multiply");
        }

        if(a[0].length != b.length){
            throw new IllegalArgumentException("The number of columns of the first array must be equal to the number of rows of the second array in order to multiply them");
        }
    }

    /**
     * Validates if two bidimensional arrays (an array of double and an array
     * that can contain real, imaginary and/or complex numbers) can be used as
     * operands for an array multiplication.<br>
     * Two arrays can be multiplied if, and only if, the following conditions
     * are met:
     * <ul>
     *     <li>Neither are null nor zero-length</li>
     *     <li>Both are square or rectangular</li>
     *     <li>The number of columns of the first array equals the number
     *     of rows of the second.</li>
     * </ul>
     * If all conditions are met, nothing happens. Otherwise, an exception will
     * be thrown.
     * @param a The first candidate for an array multiplication.
     * @param b The second candidate for an array multiplication.
     * @throws IllegalArgumentException if at least one of the conditions listed
     * above is not met.
     * @see #isSquare(MathResult[][]) 
     * @see #isRectangular(MathResult[][])
     * @see ArrayMath#isSquare(double[][]) 
     * @see ArrayMath#isRectangular(double[][])
     */
    public static void validateCanMultiply(double[][] a, MathResult[][] b){
        if(a==null || b == null || a.length == 0 || b.length == 0){
            throw new NullPointerException("Cannot multiply null or zero-length arrays");
        }

        if((!ArrayMath.isSquare(a) && !ArrayMath.isRectangular(a)) ||
            (!isSquare(b) && !isRectangular(b))){
            throw new IllegalArgumentException("Only squared or rectangular arrays are allowed to multiply");
        }

        if(a[0].length != b.length){
            throw new IllegalArgumentException("The number of columns of the first array must be equal to the number of rows of the second array in order to multiply them");
        }
    }

    /**
     * Validates if two bidimensional arrays (an array that can contain real,
     * imaginary and/or complex numbers and an array of double) can be used as
     * operands for an array multiplication.<br>
     * Two arrays can be multiplied if, and only if, the following conditions
     * are met:
     * <ul>
     *     <li>Neither are null nor zero-length</li>
     *     <li>Both are square or rectangular</li>
     *     <li>The number of columns of the first array equals the number
     *     of rows of the second.</li>
     * </ul>
     * If all conditions are met, nothing happens. Otherwise, an exception will
     * be thrown.
     * @param a The first candidate for an array multiplication.
     * @param b The second candidate for an array multiplication.
     * @throws IllegalArgumentException if at least one of the conditions listed
     * above is not met.
     * @see #isSquare(MathResult[][])
     * @see #isRectangular(MathResult[][])
     * @see ArrayMath#isSquare(double[][])
     * @see ArrayMath#isRectangular(double[][])
     */
    public static void validateCanMultiply(MathResult[][] a, double[][] b){
        if(a==null || b == null || a.length == 0 || b.length == 0){
            throw new NullPointerException("Cannot multiply null or zero-length arrays");
        }

        if((!isSquare(a) && !isRectangular(a)) ||
            (!ArrayMath.isSquare(b) && !ArrayMath.isRectangular(b))){
            throw new IllegalArgumentException("Only squared or rectangular arrays are allowed to multiply");
        }

        if(a[0].length != b.length){
            throw new IllegalArgumentException("The number of columns of the first array must be equal to the number of rows of the second array in order to multiply them");
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
     * @see #validateCanMultiply(MathResult[][], MathResult[][])
     * @see ComplexMath#multiply(ComplexNumber...)
     * @see ComplexMath#sum(ComplexNumber...)
     */
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

    /** Calculates the multiplication between 2 bidimensional arrays (one
     * of double and one that can contain real, imaginary and/or complex
     * numbers).<br>
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
     * @see #validateCanMultiply(double[][], MathResult[][])
     * @see ComplexMath#multiply(ComplexNumber...)
     * @see ComplexMath#sum(ComplexNumber...)
     */
    public static MathResult[][] multiply(double[][] a, MathResult[][] b) {
        validateCanMultiply(a, b);
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        MathResult[][] result = new MathResult[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                ComplexNumber sum = new ComplexNumber(0, 0);
                for (int k = 0; k < colsA; k++) {
                    sum = ComplexMath.sum(sum, ComplexMath.multiply(new ComplexNumber(a[i][k], 0), b[k][j].toComplex())).toComplex();
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    /** Calculates the multiplication between 2 bidimensional arrays (one
     * that can contain real, imaginary and/or complex numbers and one of
     * double).<br>
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
     * @see #validateCanMultiply(MathResult[][], double[][])
     * @see ComplexMath#ponderate(ComplexNumber, double)
     * @see ComplexMath#sum(ComplexNumber...)
     */
    public static MathResult[][] multiply(MathResult[][] a, double[][] b) {
        validateCanMultiply(a, b);
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        MathResult[][] result = new MathResult[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                ComplexNumber sum = new ComplexNumber(0, 0);
                for (int k = 0; k < colsA; k++) {
                    sum = ComplexMath.sum(sum, ComplexMath.ponderate(a[i][k].toComplex(), b[k][j])).toComplex();
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    /**
     * Checks if a unidimensional array and a bidimensional array can be multiplied.<br>
     * If all checks are passed, nothing happens. Otherwise, an exception will be thrown.
     * @param a The unidimensional array.
     * @param b The bidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #validateCanMultiply(MathResult[][], MathResult[][])
     */
    public static void validateCanMultiply(MathResult[] a, MathResult[][] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        MathResult[][] bda = new MathResult[1][];
        bda[0] = Arrays.copyOf(a, a.length);
        validateCanMultiply(bda, b);
    }

    /**
     * Checks if a unidimensional array of double and a bidimensional array
     * containing real, imaginary and/or complex numbers can be multiplied.<br>
     * If all checks are passed, nothing happens. Otherwise, an exception will be
     * thrown.
     * @param a The unidimensional array.
     * @param b The bidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #validateCanMultiply(double[][], MathResult[][])
     */
    public static void validateCanMultiply(double[] a, MathResult[][] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        double[][] bda = new double[1][];
        bda[0] = Arrays.copyOf(a, a.length);
        validateCanMultiply(bda, b);
    }

    /**
     * Checks if a unidimensional array containing real, imaginary and/or complex
     * numbers and a bidimensional array of double can be multiplied.<br>
     * If all checks are passed, nothing happens. Otherwise, an exception will be
     * thrown.
     * @param a The unidimensional array.
     * @param b The bidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #validateCanMultiply(MathResult[][], double[][])
     */
    public static void validateCanMultiply(MathResult[] a, double[][] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        MathResult[][] bda = new MathResult[1][];
        bda[0] = Arrays.copyOf(a, a.length);
        validateCanMultiply(bda, b);
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
     * @see #validateCanMultiply(double[], MathResult[][])
     * @see #multiply(MathResult[][], MathResult[][])
     */
    public static MathResult[][] multiply(MathResult[] a, MathResult[][] b){
        validateCanMultiply(a, b);
        MathResult[][] bda = new MathResult[1][];
        bda[0] = Arrays.copyOf(a, a.length);
        return multiply(bda, b);
    }

    /**
     * Multiples a unidimensional array of double with a bidimensional array
     * containing real, imaginary and/or complex numbers.<br>
     * Multiplying both arrays is the same as to multiply A<sub>1, n</sub>
     * with B<sub>n, m</sub>
     * @param a The first operand of the multiplication.
     * @param b The second operand of the multiplication.
     * @return The product between a and b, a bidimensional array M<sub>1, m</sub>
     * @throws IllegalArgumentException if the validations to check if a and b
     * can be multiplied fail.
     * @see #validateCanMultiply(double[], MathResult[][]) 
     * @see #multiply(double[][], MathResult[][])
     */
    public static MathResult[][] multiply(double[] a, MathResult[][] b){
        validateCanMultiply(a, b);
        double[][] bda = new double[1][];
        bda[0] = Arrays.copyOf(a, a.length);
        return multiply(bda, b);
    }

    /**
     * Multiples a unidimensional array containing real, imaginary and/or
     * complex numbers with a bidimensional array.<br>
     * Multiplying both arrays is the same as to multiply A<sub>1, n</sub>
     * with B<sub>n, m</sub>
     * @param a The first operand of the multiplication.
     * @param b The second operand of the multiplication.
     * @return The product between a and b, a bidimensional array M<sub>1, m</sub>
     * @throws IllegalArgumentException if the validations to check if a and b
     * can be multiplied fail.
     * @see #validateCanMultiply(MathResult[], double[][])
     * @see #multiply(MathResult[][], double[][])
     */
    public static MathResult[][] multiply(MathResult[] a, double[][] b){
        validateCanMultiply(a, b);
        MathResult[][] bda = new MathResult[1][];
        bda[0] = Arrays.copyOf(a, a.length);
        return multiply(bda, b);
    }

    /**
     * Checks if a bidimensional array and a unidimensional array can be multiplied.<br>
     * If all checks are passed, nothing happens. Otherwise, an exception will be thrown.
     * @param a The bidimensional array.
     * @param b The unidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #validateCanMultiply(MathResult[][], MathResult[][])
     */
    public static void validateCanMultiply(MathResult[][] a, MathResult[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        MathResult[][] bdb = new MathResult[b.length][1];
        IntStream.range(0, b.length).forEach(i -> bdb[i][0] = b[i]);
        validateCanMultiply(a, bdb);
    }

    /**
     * Checks if a bidimensional array of double and a unidimensional array
     * having real, imaginary and/or complex numbers can be multiplied.<br>
     * If all checks are passed, nothing happens. Otherwise, an exception will be
     * thrown.
     * @param a The bidimensional array.
     * @param b The unidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #validateCanMultiply(double[][], MathResult[][])
     */
    public static void validateCanMultiply(double[][] a, MathResult[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        MathResult[][] bdb = new MathResult[b.length][1];
        IntStream.range(0, b.length).forEach(i -> bdb[i][0] = b[i]);
        validateCanMultiply(a, bdb);
    }

    /**
     * Checks if a bidimensional array having real, imaginary and/or complex
     * numbers and a unidimensional array of double can be multiplied.<br>
     * If all checks are passed, nothing happens. Otherwise, an exception will be
     * thrown.
     * @param a The bidimensional array.
     * @param b The unidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #validateCanMultiply(MathResult[][], double[][])
     */
    public static void validateCanMultiply(MathResult[][] a, double[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        double[][] bdb = new double[b.length][1];
        IntStream.range(0, b.length).forEach(i -> bdb[i][0] = b[i]);
        validateCanMultiply(a, bdb);
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
     * @see #validateCanMultiply(MathResult[], MathResult[][])
     * @see #multiply(MathResult[][], MathResult[][])
     */
    public static MathResult[][] multiply(MathResult[][] a, MathResult[] b){
        validateCanMultiply(a, b);
        MathResult[][] bdb = new MathResult[b.length][1];
        IntStream.range(0, b.length).forEach(i -> bdb[i][0] = b[i]);
        return multiply(a, bdb);
    }

    /**
     * Multiples a bidimensional array of double with a unidimensional array
     * having real, imaginary and/or complex numbers.<br>
     * Multiplying both arrays is the same as to multiply A<sub>m, n</sub>
     * with B<sub>n, 1</sub>
     * @param a The first operand of the multiplication.
     * @param b The second operand of the multiplication.
     * @return The product between a and b, a bidimensional array M<sub>m, 1</sub>
     * @throws IllegalArgumentException if the validations to check if a and b
     * can be multiplied fail.
     * @see #validateCanMultiply(MathResult[], MathResult[][])
     * @see #multiply(MathResult[][], MathResult[][])
     */
    public static MathResult[][] multiply(double[][] a, MathResult[] b){
        validateCanMultiply(a, b);
        MathResult[][] bdb = new MathResult[b.length][1];
        IntStream.range(0, b.length).forEach(i -> bdb[i][0] = b[i]);
        return multiply(a, bdb);
    }

    /**
     * Multiples a bidimensional array that can have real, imaginary and/or
     * complex numbers with a unidimensional array of double.<br>
     * Multiplying both arrays is the same as to multiply A<sub>m, n</sub>
     * with B<sub>n, 1</sub>
     * @param a The first operand of the multiplication.
     * @param b The second operand of the multiplication.
     * @return The product between a and b, a bidimensional array M<sub>m, 1</sub>
     * @throws IllegalArgumentException if the validations to check if a and b
     * can be multiplied fail.
     * @see #validateCanMultiply(MathResult[], double[][])
     * @see #multiply(MathResult[][], double[][])
     */
    public static MathResult[][] multiply(MathResult[][] a, double[] b){
        validateCanMultiply(a, b);
        double[][] bdb = new double[b.length][1];
        IntStream.range(0, b.length).forEach(i -> bdb[i][0] = b[i]);
        return multiply(a, bdb);
    }

    /**
     * Checks if two unidimensional arrays can be multiplied.<br>
     * Only non-null, non-zero length arrays with the same length can be
     * multiplied.
     * @param a The first unidimensional array.
     * @param b The second unidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #sameDimensions(MathResult[], MathResult[])
     */
    public static void validateCanMultply(MathResult[] a, MathResult[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Both arrays must have the same length");
        }
    }

    /**
     * Checks if two unidimensional arrays (one of double and one that contains
     * real, imaginary and/or complex numbers) can be multiplied.<br>
     * Only non-null, non-zero length arrays with the same length can be
     * multiplied.
     * @param a The first unidimensional array.
     * @param b The second unidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #sameDimensions(double[], MathResult[])
     */
    public static void validateCanMultply(double[] a, MathResult[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Both arrays must have the same length");
        }
    }

    /**
     * Checks if two unidimensional arrays (one that contains real, imaginary
     * and/or complex numbers and one of double) can be multiplied.<br>
     * Only non-null, non-zero length arrays with the same length can be
     * multiplied.
     * @param a The first unidimensional array.
     * @param b The second unidimensional array.
     * @throws IllegalArgumentException if at least one of the checks fail.
     * @see #sameDimensions(MathResult[], double[])
     */
    public static void validateCanMultply(MathResult[] a, double[] b){
        if(a == null || b == null || a.length == 0 || b.length == 0){
            throw new IllegalArgumentException("Cannot multiply null or zero length arrays");
        }
        if(!sameDimensions(a, b)){
            throw new IllegalArgumentException("Both arrays must have the same length");
        }
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
     * @see #validateCanMultply(MathResult[], MathResult[])
     * @see ComplexMath#multiply(ComplexNumber...) 
     * @see ComplexMath#sum(ComplexNumber...)
     */
    public static MathResult multiply(MathResult[] a, MathResult[] b){
        validateCanMultply(a, b);
        ComplexNumber[] prods = new ComplexNumber[a.length];
        IntStream.range(0, a.length).forEach(i -> prods[i] = ComplexMath.multiply(a[i].toComplex(), b[i].toComplex()));
        return ComplexMath.sum(prods);
    }

    /**
     * Multiples 2 unidimensional arrays (one of double and one having real,
     * imaginary and/or complex numbers).<br>
     * Multiplying both arrays is the same as to multiply A<sub>1, n</sub>
     * with B<sub>n, 1</sub>. As a consequence, as the result will be a 1x1 matrix,
     * a simple element will be returned instead of the matrix that element
     * belongs to.
     * @param a The first operand of the multiplication.
     * @param b The second operand of the multiplication.
     * @return The product between a and b as described above.
     * @throws IllegalArgumentException if the validations to check if both
     * arrays can be multiplied fail.
     * @see #validateCanMultply(double[], MathResult[])
     * @see ComplexMath#ponderate(ComplexNumber, double)
     * @see ComplexMath#sum
     */
    public static MathResult multiply(double[] a, MathResult[] b){
        validateCanMultply(a, b);
        ComplexNumber[] prods = new ComplexNumber[a.length];
        IntStream.range(0, a.length).forEach(i -> prods[i] = ComplexMath.ponderate(b[i].toComplex(), a[i]));
        return ComplexMath.sum(prods);
    }

    /**
     * Multiples 2 unidimensional arrays (one having real, imaginart and/or
     * complex numbers and one of double).<br>
     * Multiplying both arrays is the same as to multiply A<sub>1, n</sub>
     * with B<sub>n, 1</sub>. As a consequence, as the result will be a 1x1
     * matrix, a simple element will be returned instead of the matrix that
     * element belongs to.
     * @param a The first operand of the multiplication.
     * @param b The second operand of the multiplication.
     * @return The product between a and b as described above.
     * @throws IllegalArgumentException if the validations to check if both
     * arrays can be multiplied fail.
     * @see #validateCanMultply(MathResult[], double[])
     * @see ComplexMath#ponderate(ComplexNumber, double)
     * @see ComplexMath#sum
     */
    public static MathResult multiply(MathResult[] a, double[] b){
        validateCanMultply(a, b);
        ComplexNumber[] prods = new ComplexNumber[a.length];
        IntStream.range(0, a.length).forEach(i -> prods[i] = ComplexMath.ponderate(a[i].toComplex(), b[i]));
        return ComplexMath.sum(prods);
    }

    /**
     * Calculates the transpose of a bidimensional array or matrix.<br>
     * Given one matrix A<sub>m, n</sub>, that matrix's transpose, named
     * A<sup>T</sup> is a matrix B<sub>n, m</sub> in that for each 1 &le; j &le; n,
     * The column j of A becomes the row j of B.<br>
     * @param matrix A bidimensional array whose transpose will be calculated.
     * @return The transpose of matrix as described above.
     * @throws IllegalArgumentException if matrix is not a square or rectangular
     * bidimensional array. NOTE: This validation implies that the matrix must
     * not be null or zero-length.
     * @see #isRectangular(MathResult[][])
     * @see #isSquare(MathResult[][])
     */
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

    /**
     * Creates an identity matrix of complex numbers.<br>
     * An identity matrix is a square matrix I of n * n elements on which,
     * for each i between 1 and n, the element I<sub>ii</sub> is the complex
     * multiplicative neutral element (a complex number with real part 1 and
     * imaginary part zero) and the rest of the elements of I are the complex
     * absorbent element (a complex number with real part zero and no imaginary
     * part)
     *
     * @param len The length of the identity matrix
     * @return The identity matrix of len n as described above.
     * @throws IllegalArgumentException if n is lower or equal than zero.
     */
    public static ComplexNumber[][] identity(int len){
        if(len <= 0){
            throw new IllegalArgumentException("Cannot create identity matrix of zero or negative length");
        }
        ComplexNumber[][] a = new ComplexNumber[len][len];
        IntStream.range(0, a.length).forEach(i ->
            IntStream.range(0, a.length).forEach(j ->
                a[i][j] = new ComplexNumber(i==j ? 1 : 0, 0)
            )
        );
        return a;
    }

    /**
     * Checks if a bidimensional array is singular.<br>
     * Only square arrays having a zero determinant are singular.
     * @param ar The array to be checked if is singular.
     * @return true if ar is singular, false otherwise.
     * @throws IllegalArgumentException if ar is null or if it's length is zero.
     * @see #isSquare(MathResult[][])
     * @see #determinant(MathResult[][])
     */
    public static boolean isSingular(MathResult[][] ar){
        if (ar == null || ar.length == 0) {
            throw new IllegalArgumentException(
                    "Null or zero-length arrays cannot be singular");
        }
        if (!isSquare(ar)) {
            return false;
        }
        ComplexNumber det = determinant(ar).toComplex();
        return det.isZero() || ComplexMath.modulus(det) == 0;
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
     * @see #isSingular(MathResult[][])
     */
    public static MathResult[][] inverse(MathResult[][] ar){
        if(!isSingular(ar)){
            throw new ArithmeticException("Only non-singular arrays can be inversed");
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

    /**
     * Compares 2 unidimensional arrays if they are equal.<br>
     * Two arrays A and B are considered equal if they have the same dimensions
     * and, for each i between 1 and both array's length, A<sub>i</sub> =
     * B<sub>i</sub><br>
     * NOTE: Both arrays can also be considered equal if both are null or
     * if both are zero-length.
     * @param a The first array used to comparison.
     * @param b The second array used to comparison.
     * @return true if both arrays are equal using the criteria described
     * above, false otherwise.
     * @see ComplexNumber#equals(Object) 
     */
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

    /** Checks if both bidimensional arrays are equal.<br>
     * Both arrays are equal if they have the same number of rows m and the same
     * number of columns n and, for each
     * 1 &le; i &le; m and 1 &le; j &le; n, A<sub>i,j</sub> = B<sub>i,j</sub><br>
     * NOTE: If both arrays are null or zero-length, both are considered equal.<br>
     * @param a The first array to be compared.
     * @param b The second array to be compared.
     * @return true if both arrays are equal, false otherwise.
     * @see #equals(MathResult[], MathResult[]) 
     */
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

    /**
     * Checks if a bidimensional array is symmetric.<br>
     * An array A is symmetric if and only if its equal to its
     * transpose, that is, A = A<sup>T</sup>
     * @param a The array to check if is symmetric.
     * @return true if "a" is symmetric, false otherwise.
     * @throws IllegalArgumentException if the transpose fails.
     * @see #equals(MathResult[][], MathResult[][])
     * @see #transpose(MathResult[][])
     */
    public static boolean isSymmetric(MathResult[][] a){
        return equals(a, transpose(a));
    }

    /**
     * Checks if a bidimensional array is skew symmetric.<br>
     * An array A is skew symmetric if and only if it's additive inverse (that
     * is, the same array ponderated by -1) equals it's transpose.
     * @param a The array to check if is skew symmetric.
     * @return true if "a" is skew symmetric, false otherwise.
     * @throws IllegalArgumentException if the transpose fails.
     * @see #equals(MathResult[][], MathResult[][])
     * @see #ponderate(MathResult[][], double)
     * @see #transpose(MathResult[][])
     */
    public static boolean isSkewSymmetric(MathResult[][] a){
        return equals(ponderate(a, -1), transpose(a));
    }

    /**
     * Checks if a bidimensional array is orthogonal.<br>
     * An array A is orthogonal if and only if is square, is not singular and
     * its inverse is equal to its transpose.<br>
     * NOTE: Singular arrays are not orthogonal.
     * @param a The array to be checked if is orthogonal.
     * @return true if "a" is orthogonal, false otherwise.
     * @throws IllegalArgumentException if the validations to determinate if
     * "a" is square or singular fail OR if the transpose fails.
     * @see #isSingular(MathResult[][])
     * @see #isSquare(MathResult[][])
     * @see #inverse(MathResult[][])
     * @see #transpose(MathResult[][])
     */
    public static boolean isOrthogonal(MathResult[][] a){
        try{
            return equals(inverse(a), transpose(a));
        }catch(ArithmeticException _){
            return false;
        }
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
     * @throws ArithmeticException if b is singular.
     * @see #multiply(MathResult[][], MathResult[][])
     * @see #inverse(MathResult[][])
     */
    public static MathResult[][] divide(MathResult[][] a, MathResult[][] b){
        return multiply(a, inverse(b));
    }

    /**
     * Calculates the conjugate of a unidimensional array.
     * The conjugate of an array A having real, imaginary or complex numbers
     * is an array B with the same length in which, for each i between 1 and
     * A's length, B<sub>i</sub> is equal to A<sub>i</sub> if it's real, or to
     * the conjugate of A<sub>i</sub> if it's imaginary or complex.
     * @param a The array whose conjugate will be calculated.
     * @return a's conjugate as described above
     * @throws IllegalArgumentException if "a" is null or zero-length.
     * @see ComplexMath#conjugate(ComplexNumber)
     */
    public static MathResult[] conjugate(MathResult[] a){
        if(a == null || a.length == 0){
            throw new IllegalArgumentException("Cannot conjugate null or zero-length arrays");
        }
        return Arrays.stream(a).map(x ->
            x.isReal() ? x : ComplexMath.conjugate(x.toComplex())
        ).toArray(MathResult[]::new);
    }

    /**
     * Calculates the conjugate of a bidimensional array.
     * The conjugate of an array A of m * n elements having real, imaginary or
     * complex numbers is an array B with the same dimensions in which, for each
     * i between 1 and m, and for each j between 1 and n, B<sub>ij</sub>
     * is equal to A<sub>ij</sub> if it's real, or to the conjugate of
     * A<sub>ij</sub> if it's imaginary or complex.
     * @param a The array whose conjugate will be calculated.
     * @return a's conjugate as described above
     * @throws IllegalArgumentException if "a" is null or zero-length.
     * @see #conjugate(MathResult[])
     */
    public static MathResult[][] conjugate(MathResult[][] a){
        if(a == null || a.length == 0){
            throw new IllegalArgumentException("Cannot conjugate null or zero-length arrays");
        }
        return Arrays.stream(a).map(ArrayComplexMath::conjugate).toArray(MathResult[][]::new);
    }

    /**
     * Checks if a bidimensional array is hermitian.<br>
     * An array A is hermitian if, and only if, it's transpose is equal to
     * it's conjugate.
     * @param a The matrix to check if it's hermitian.
     * @return true if "a" is hermitian, false otherwise
     * @throws IllegalArgumentException if "a" is null or zero-length
     * @see #equals(MathResult[][], MathResult[][]) 
     * @see #transpose(MathResult[][]) 
     * @see #conjugate(MathResult[][]) 
     */
    public static boolean isHermitian(MathResult[][] a){
        return equals(transpose(a), conjugate(a));
    }

    /**
     * Checks if a bidimensional array is skew hermitian.<br>
     * An array A is skew hermitian if, and only if, it's transpose is equal to
     * the additive inverse of it's conjugate (that is, the conjugate of A 
     * ponderated by -1).
     * @param a The matrix to check if it's hermitian.
     * @return true if "a" is hermitian, false otherwise
     * @throws IllegalArgumentException if "a" is null or zero-length
     * @see #equals(MathResult[][], MathResult[][])
     * @see #transpose(MathResult[][])
     * @see #conjugate(MathResult[][])
     * @see #ponderate(MathResult[][], double) 
     */
    public static boolean isSkewHermitian(MathResult[][] a){
        return equals(transpose(a), ponderate(conjugate(a), -1));
    }

    /**
     * Checks if a bidimensional array is unitary.<br>
     * An array is unitary if and only if it's transpose is equal to the
     * inverse of it's conjugate.
     * @param a The array to be checked if is unitary.
     * @return true if "a" is unitary, false otherwise.
     * @throws IllegalArgumentException if "a" is null or zero-length.
     * @throws ArithmeticException if "a" is singular.
     */
    public static boolean isUnitary(MathResult[][] a){
        return equals(transpose(a), inverse(conjugate(a)));
    }
}
