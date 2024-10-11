package helpers;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

public class MatrixStacking {

    private MatrixStacking() {
    }

    // Function to stack matrices vertically from a list
    public static RealMatrix stackVertically(List<RealMatrix> matrices) {
        // Check if the list is empty
        if (matrices.isEmpty()) {
            throw new IllegalArgumentException("The list of matrices is empty.");
        }

        // Get the number of columns from the first matrix
        int cols = matrices.get(0).getColumnDimension();

        // Calculate the total number of rows by summing the rows of each matrix
        int totalRows = 0;
        for (RealMatrix matrix : matrices) {
            if (matrix.getColumnDimension() != cols) {
                throw new IllegalArgumentException("All matrices must have the same number of columns.");
            }
            totalRows += matrix.getRowDimension();
        }

        // Create a new matrix with total rows and same number of columns
        RealMatrix stackedMatrix = MatrixUtils.createRealMatrix(totalRows, cols);

        // Copy the rows of each matrix into the new stacked matrix
        int currentRow = 0;
        for (RealMatrix matrix : matrices) {
            int rows = matrix.getRowDimension();
            for (int i = 0; i < rows; i++) {
                stackedMatrix.setRow(currentRow, matrix.getRow(i));
                currentRow++;
            }
        }

        return stackedMatrix;
    }


}
