package cl.maraneda.cplx;

public class EquationSolver {
    private EquationSolver() { /* Nothing */ }

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
            throw new ArithmeticException("Cannot solve the equation system if the determinant is zero");
        }

        double[] solutions = new double[numVariables];

        for (int i = 0; i < numVariables; i++) {
            double[][] ai = replaceColumn(a, i, b);
            solutions[i] = ArrayMath.determinant(ai) / detA;
        }

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
}
