package mpc.problems.one_dim_integrator;

import mpc.domain.value_objects.MpcModelData;

public class FactoryOneDimIntegrator {

    private FactoryOneDimIntegrator() {
    }

    public static MpcModelData createModelData(int horizon) {
        double[][]  matrixA = new double[][]{{1}};
        double[]   vectorB = new double[]{1};
        double[] statePenalty= new double[]{1};
        double[] controlPenalty=new double[]{1};
        return new MpcModelData(horizon,matrixA, vectorB, statePenalty, controlPenalty);
    }

}
