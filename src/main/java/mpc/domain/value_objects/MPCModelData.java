package mpc.domain.value_objects;

import org.apache.commons.math3.linear.MatrixUtils;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.properties;

public record MPCModelData(
        int horizon,
        double[][] matrixA,
        double[] vectorB,
        double[] statePenalty,
        double[] controlPenalty
) {

    public static final int N_INPUTS = 1;

    public boolean isOk() {
        var a = MatrixUtils.createRealMatrix(matrixA);
        int nStates = nStates();
        return properties(a).nRows() == nStates && properties(a).nColumns() == nStates &&
                statePenalty.length == nStates && controlPenalty.length == nInputs();
    }

    public int nStates() {
        return vectorB.length;
    }

    public int nInputs() {
        return N_INPUTS;
    }

}
