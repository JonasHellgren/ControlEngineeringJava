package helpers;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.hellgren.utilities.vector_algebra.MatrixStacking;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


 class TestMatrixStacking {

    @Test
     void testStackVertically_EmptyList() {
        List<RealMatrix> matrices = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> MatrixStacking.stackVertically(matrices));
    }

    @Test
     void testStackVertically_SingleMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        List<RealMatrix> matrices = new ArrayList<>();
        matrices.add(matrix);
        RealMatrix stackedMatrix = MatrixStacking.stackVertically(matrices);
        assertEquals(matrix, stackedMatrix);
    }

    @Test
     void testStackVertically_MultipleMatrices() {
        RealMatrix matrix1 = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{5, 6}, {7, 8}});
        List<RealMatrix> matrices = new ArrayList<>();
        matrices.add(matrix1);
        matrices.add(matrix2);
        RealMatrix stackedMatrix = MatrixStacking.stackVertically(matrices);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}, {7, 8}});
        assertEquals(expectedMatrix, stackedMatrix);
    }

    @Test
     void testStackVertically_DifferentColumnDimensions() {
        RealMatrix matrix1 = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{5, 6, 7}, {8, 9, 10}});
        List<RealMatrix> matrices = new ArrayList<>();
        matrices.add(matrix1);
        matrices.add(matrix2);
        assertThrows(IllegalArgumentException.class, () -> MatrixStacking.stackVertically(matrices));
    }
    
}
