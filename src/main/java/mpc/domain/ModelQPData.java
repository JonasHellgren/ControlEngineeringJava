package mpc.domain;

import com.google.common.collect.Lists;
import helpers.MatrixStacking;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.hellgren.utilities.list_arrays.ArrayCreator;
import org.hellgren.utilities.list_arrays.MyArrayUtil;
import org.hellgren.utilities.vector_algebra.MyMatrixUtils;

import java.util.List;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.properties;

public record ModelQPData(
        int horizon,
        double[][] matrixA,
        double[] vectorB,
        double[] statePenalty,
        double[] controlPenalty
) {
    public boolean isOk() {
        var a = getRealMatrix(matrixA);
        var q = matrixQ();
        var r = matrixR();
        int nStates = nStates();
        return properties(a).nRows() == nStates && properties(a).nColumns() == nStates &&
                properties(q).nRows() == nStates * horizon && properties(q).nColumns() == nStates * horizon &&
                properties(r).nRows() == horizon && properties(r).nColumns() == horizon
                ;
    }

    public int nStates() {
        return vectorB.length;
    }

    public static RealMatrix getRealMatrix(double[][] matrix) {
        return MatrixUtils.createRealMatrix(matrix);
    }

    public static RealVector getRealVector(double[] vector) {
        return MatrixUtils.createRealVector(vector);
    }

    public RealMatrix matrixQ() {
          double[] diagonal = ArrayCreator.duplicate(statePenalty, horizon);
           return MyMatrixUtils.createDiagonalMatrix(diagonal);
    }

    public RealMatrix matrixR() {
        double[] diagonal = ArrayCreator.duplicate(controlPenalty, horizon);
        return MyMatrixUtils.createDiagonalMatrix(diagonal);
    }

    public RealMatrix matrixS() {
        var a = getRealMatrix(matrixA);
        List<RealMatrix> matrices = Lists.newArrayList();
        for (int i = 0; i < horizon; i++) {
            matrices.add(a.power(i));
        }
        return MatrixStacking.stackVertically(matrices);
    }

    public RealMatrix matrixT() {
        var a = getRealMatrix(matrixA);
        var b = getRealVector(vectorB);
        int n = nStates();
        List<RealMatrix> rows = Lists.newArrayList();
        for (int i = 0; i < horizon; i++) {
            List<RealVector> vectors = Lists.newArrayList();
            for (int j = 0; j < horizon; j++) {
                RealVector m = i - j < 0
                        ? createZeroVector(n)
                        : a.power(i - j).operate(b);
                vectors.add(m);
            }
            RealMatrix row = MyMatrixUtils.stackVectorsHorizontally(vectors);
            rows.add(row);
        }
        return MatrixStacking.stackVertically(rows);
    }


    public RealMatrix getMatrixH() {
        var t = matrixT();
        var q = matrixQ();
        var r = matrixR();
        return (t.transpose().multiply(q).multiply(t).add(r)).scalarMultiply(2);
    }

    public double[] getArrayF() {
        return vectorB;
    }


}
