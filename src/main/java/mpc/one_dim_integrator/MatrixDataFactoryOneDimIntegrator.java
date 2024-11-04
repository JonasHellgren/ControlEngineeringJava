package mpc.one_dim_integrator;

import mpc.domain.ModelQPData;

public class MatrixDataFactoryOneDimIntegrator {

    private MatrixDataFactoryOneDimIntegrator() {
    }

    public static ModelQPData createModelData() {
        double[][]  matrixA = new double[][]{{1}};
        double[]   vectorB = new double[]{1};
     //   double[][]  matrixQ = new double[][]{{1,1},{1,1}};
     //   double[][]   matrixR = new double[][]{{1,0},{0,1}};
        double[] statePenalty= new double[]{1};
        //      double[][] matrixR
        double[] controlPenalty=new double[]{1};
        return new ModelQPData(2,matrixA, vectorB, statePenalty, controlPenalty);
    }

}
