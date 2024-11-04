package mpc.one_dim_integrator;

import mpc.domain.value_objects.MPCModelData;

public class MatrixDataFactoryOneDimIntegrator {

    private MatrixDataFactoryOneDimIntegrator() {
    }

    public static MPCModelData createModelData(int horizon) {
        double[][]  matrixA = new double[][]{{1}};
        double[]   vectorB = new double[]{1};
        double[] statePenalty= new double[]{1};
        double[] controlPenalty=new double[]{1};
        return new MPCModelData(horizon,matrixA, vectorB, statePenalty, controlPenalty);
    }

}
