package mpc.one_dim_integrator;

import helpers.MatrixStacking;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

public record ModelQPData(
        double[][]  matrixA,
        double[]   vectorB,
        double[][]  matrixQ,
        double[]   vectorR
) {
    public boolean isOk() {
        return matrixA != null && vectorB != null;
    }

    public double[][]  matrixS() {
        var m = getRealMatrixOfA();
        return MatrixStacking.stackVertically(List.of(m,m)).getData();
    }


    public double[][] getMatrixH() {
        return matrixA;
    }

    public double[] getArrayF() {
        return vectorB;
    }


    private RealMatrix getRealMatrixOfA() {
        return MatrixUtils.createRealMatrix(matrixA);
    }


}
