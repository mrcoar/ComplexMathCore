package cl.maraneda.cplx;

import java.util.List;
import java.util.stream.IntStream;

/**
 * This class allows to solve linear and quadratic equations.
 *
 * @author Marco Araneda
 * @version 1.0
 * @since 2026-06-01
 */
public class EquationSolver {
    private EquationSolver() { /* Nothing */ }

    /**
     * Allows to solve a system of linear equations by using the Cramer's rule.
     * For this method, the equations system is represented by a bidimensional
     * array whose number of rows represent the equations and each column represent
     * the numeric coefficient of the variables on each equation, except the last
     * column, that represents the value of each equation.<br>
     * For example, the following array:<br><br>
     * {{1, 2, 3},{4, 5, 6}}<br><br>
     *  represents the following system:<br><br>
     *  &nbsp;&nbsp;x + 2y = 3<br>
     *  4x + 5y = 6<br><br>
     *  For an array of type double to be recognized as a valid equation system,
     *  it must fulfill all following conditions:
     *  <ul>
     *     <li>The array must <b>NOT</b> be null</li>
     *  <li>The array <b>must be rectangular</b>. That means that, as bidimensional
     *  arrays of doubles in Java are arrays of unidimensional arrays of doubles,
     *  all unidimensional arrays must have the same size.</li>
     *  <li>As each column in the bidimensional array except the last one
     *  represent variables, the number of rows must be equal to the number
     *  of columns minus one. For example, the following array: {@code double[][] eq
     *  = new double[3][4];} is a valid equation system as contains 3 rows and
     *  4 columns, that is, 3 equations, 3 variables and 3 values (one for
     *  each equation).</li>
     *  <li>The array must have at least 2 rows and 3 columns because the minimum
     *  number of equations in a system is 2 and, if there are two equations,
     *  there must be 2 variables plus the value of each equation</li>
     *  <li>If for one variable, you don't want to include it in an equation,
     *  the value of that variable's coefficient for that equation must be
     *  zero.</li>
     *  </ul>
     *  The Cramer's rule consists in a division between the determinant of two
     *  arrays in which the second determinant is a square array consisting in
     *  all columns of the system except the last one and the first one is
     *  the same array as for the second determinant, but with one column replaced
     *  by the last column in the system. For the first variable, the first
     *  column is replaced, for the second variable, the second column and so on
     *  until all variables are covered.<br>
     *  The system's solution is a unidimensional array in which the first
     *  element is the value of the first variable that satisfies all equations
     *  in the system, the second element is the value of the second variable
     *  and so on.
     * @param equations A bidimensional array representing an equation system
     *                  as described above.
     * @return A unidimensional array with the system's solution as described
     * above.
     * @throws IllegalArgumentException if equations does not meet the requirements
     * listed above to be treated as an equation system.
     * @throws ArithmeticException if the determinant of the squared array formed
     * by all columns of the equation system except the last one is zero. This
     * may cause the system to have infinite solutions if all other determinants
     * calculated by this method are zeros or to have no solution otherwise.
     * @see ArrayMath#isRectangular(double[][]) 
     * @see ArrayMath#determinant(double[][])
     */
    public static double[] solve(double[][] equations) {
        if (equations == null || equations.length < 2) {
            throw new IllegalArgumentException("The equation system must not be null and must have at least 2 equations");
        }

        int numEquations = equations.length;
        int rowLength = equations[0].length;

        if (rowLength < 2) {
            throw new IllegalArgumentException("The system must have at least two equations");
        }

        int numVariables = rowLength - 1;

        // Number of equations must equal number of variables
        if (numEquations != numVariables) {
            throw new IllegalArgumentException("The number of equations in the system must be equal to the number of variables");
        }

        // All rows must have the same length
        if(!ArrayMath.isRectangular(equations)){
            throw new IllegalArgumentException("All variables in the equation system must be present in all equations, even if it's coefficient in one equation is zero");
        }

        // Build coefficient matrix A and constants vector b
        double[][] a = new double[numEquations][numVariables];
        double[] b = new double[numEquations];

        for (int i = 0; i < numEquations; i++) {
            System.arraycopy(equations[i], 0, a[i], 0, numVariables);
            b[i] = equations[i][numVariables];
        }

        double detA = ArrayMath.determinant(a);

        if (detA == 0) {
            boolean allZero = true;
            for (int i = 0; i < numVariables && allZero; i++) {
                double[][] ai = replaceColumn(a, i, b);
                if(ArrayMath.determinant(ai) !=0){
                    allZero = false;
                }
            }
            String msg = allZero ? "The specified system has infinite solutions" :
                    "The specified system has no solution";
            throw new ArithmeticException(msg);
        }

        double[] solutions = new double[numVariables];
        IntStream.range(0, numVariables).forEach(i -> {
            double[][] ai = replaceColumn(a, i, b);
            solutions[i] = ArrayMath.determinant(ai) / detA;
        });

        return solutions;
    }

    private static double[][] replaceColumn(double[][] matrix, int colToReplace, double[] column) {
        int n = matrix.length;
        double[][] result = new double[n][n];

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                result[row][col] = (col == colToReplace) ? column[row] : matrix[row][col];
            }
        }

        return result;
    }

    /**
     * It solves a quadratic equation (ax<sup>2</sup> + bx + c = 0)<br>
     * The number and type of the solutions depend on the value of the
     * equation's delta, that is, the value of D = b<sup>2</sup> - 4ac.
     * <ul>
     *     <li>If delta > 0, it means the parabola intersects the X
     *     axis in two points. Thus, the solution consists in two
     *     real numbers whose values are
     *     <ul>
     *         <li>(-b + sqrt(D))/2a</li>
     *         <li>(-b - sqrt(D))/2a</li>
     *     </ul>
     *     </li>
     *     <li>If delta = 0, it means the parabola intersects the X axis
     *     in one point. Thus, the solution consists in one real number
     *     whose value es -b/2a</li>
     *     <li>If delta < 0, it means the parabola does not intersect the
     *     X axis. Thus, the solution consists in two complex numbers.
     *     <ul>
     *         <li>For the first complex number, the real part is -b and
     *         the imaginary part is sqrt(|D|). Then the number is ponderated
     *         by 1/2a</li>
     *         <li>The second complex number is the conjugate of the first one</li>
     *     </ul>
     *     </li>
     * </ul>
     * @param a The first variable of the equation. Indicates the direction the
     *          parabola opens. If is positive, the parabola opens downside up.
     *          If is negative, the parabola opens upside down.
     * @param b The second variable of the equation.
     * @param c The third variable of the equation. It indicates the point where
     *          the parabola intersects with the Y axis.
     * @return The solution(s) of the quadratic equation as described above.
     * @throws ArithmeticException if "a" is zero. This is because if "a" is zero,
     * the equation is no longer considered a quadratic equation.
     */
    public List<MathResult> solveQuadratic(double a, double b, double c){
        if(a == 0){
            throw new ArithmeticException("The value of a in a quadratic function cannot be zero");
        }
        double delta = Math.pow(b, 2) - (4 * a * c);
        return switch((int)Math.signum(delta)){
            case 1 -> List.of(new RealNumber((-b + Math.sqrt(delta)) / (2 * a)),
                    new RealNumber((-b - Math.sqrt(delta)) / (2 * a)));
            case -1 -> {
                ComplexNumber cn = ComplexMath.ponderate(new ComplexNumber(-b, Math.sqrt(Math.abs(delta))), 1d / (2 * a));
                yield List.of(cn, ComplexMath.conjugate(cn));
            }
            default -> List.of(new RealNumber(-b/(2 * a)));
        };
    }
}
