package mpc.domain.value_objects;

import lombok.With;
import mpc.helpers.RealVectorUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.properties;

/**
 * Represents a model data for Model Predictive Control (MPC).
 *
 * This record contains the necessary data for MPC, including the horizon, state transition matrix,
 * control transition vector, state penalty, and control penalty.
 */
public record MpcModelData(
        int horizon,
        double[][] matrixA,  //stateTransitionMatrix
        double[] vectorB,  //controlTransitionVector
        @With double[] statePenalty,
        @With double[] controlPenalty
) {

    public static final int N_INPUTS = 1;

    public boolean isValid() {
        var a = MatrixUtils.createRealMatrix(matrixA);
        int nStates = nStates();
        return properties(a).nRows() == nStates &&
                properties(a).nColumns() == nStates &&
                statePenalty.length == nStates &&
                controlPenalty.length == nInputs();
    }

    public int nStates() {
        return vectorB.length;
    }

    public int nInputs() {
        return N_INPUTS;
    }

    public RealVector getStateValues(RealVector response, int stateIndex) {
        return RealVectorUtils.extractStateValues(stateIndex, nStates(),horizon(), response);
    }

}
