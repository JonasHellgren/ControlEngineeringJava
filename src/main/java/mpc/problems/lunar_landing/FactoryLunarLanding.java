package mpc.problems.lunar_landing;

import mpc.domain.value_objects.MpcModelData;

public class FactoryLunarLanding {

    public static final double DT = 1;
    public static final double MASS = 1000;

    private FactoryLunarLanding() {
    }

    public static MpcModelData createModelData(int horizon) {
        return createModelData(horizon, DT, MASS);
    }

        /**
         * Creates a new instance of MpcModelData for the lunar landing problem.
         *
         * @param horizon The prediction horizon for the model.
         * @param dt The time step for the model.
         * @param m The mass of the spacecraft.
         * @return A new instance of MpcModelData.
         */
    public static MpcModelData createModelData(int horizon, double dt, double m) {
        // Define the state transition matrix A
        double[][] matrixA = new double[][]{{DT, dt}, {0, DT}};

        // Define the control matrix B
        double[] vectorB = new double[]{0, dt / m};

        // Define the state penalty weights
        double[] statePenalty = new double[]{DT, DT};

        // Define the control penalty weights
        double[] controlPenalty = new double[]{DT};
        return new MpcModelData(horizon,matrixA, vectorB, statePenalty, controlPenalty);
    }

}
