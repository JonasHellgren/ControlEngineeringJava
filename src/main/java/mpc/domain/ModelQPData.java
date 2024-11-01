package mpc.domain;

import com.google.common.collect.Lists;
import helpers.MatrixStacking;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.hellgren.utilities.vector_algebra.MyMatrixUtils;

import java.util.List;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;

public record ModelQPData(
        int horizon,
        double[][] matrixA,
        double[] vectorB,
        double[][] matrixQ,
        double[][] matrixR
) {
    public boolean isOk() {
        return matrixA != null && vectorB != null;
    }

    public static double[][] matrixSAsArray(RealMatrix m) {
        return m.getData();
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
        int n= nStates();
        List<RealMatrix> matrices = Lists.newArrayList();
        for (int i = 0; i < horizon; i++) {
            List<RealVector> vectors = Lists.newArrayList();
            for (int j = 0; j < horizon; j++) {
                RealVector m = i-j<0
                        ? createZeroVector(n)
                        : a.power(i-j).operate(b);
                vectors.add(m);
            }
            System.out.println("vectors = " + vectors);
            RealMatrix row = MyMatrixUtils.stackVectorsHorizontally(vectors);
            matrices.add(row);
        }
        return MatrixStacking.stackVertically(matrices);
    }


    public RealMatrix getMatrixH() {
        var t=matrixT();
        var q=getRealMatrix(matrixQ);
        var r=getRealMatrix(matrixR);

        System.out.println("t = " + t);
        System.out.println("q = " + q);
        System.out.println("r = " + r);

        System.out.println("t.transpose().multiply(q) = " + t.transpose().multiply(q));
        System.out.println("t.multiply(q) = " + t.multiply(q));

        return (t.transpose().multiply(q).multiply(t).add(r)).scalarMultiply(2);
    }

    public double[] getArrayF() {
        return vectorB;
    }


}
