package mpc.one_dim_integrator;

import mpc.domain.ModelQPData;

public class MatrixDataFactoryOneDimIntegrator {

    private MatrixDataFactoryOneDimIntegrator() {
    }

    public static ModelQPData createModelData() {
        double[][]  matrixA = new double[][]{{1}};
        double[]   vectorB = new double[]{1};
        double[][]  matrixQ = new double[][]{{1}};
        double[]   vectorR = new double[]{1};
        return new ModelQPData(2,matrixA, vectorB, matrixQ, vectorR);
    }

}