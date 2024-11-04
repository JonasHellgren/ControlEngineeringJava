package mpc.domain.value_objects;

import lombok.With;
import org.apache.commons.math3.linear.MatrixUtils;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.properties;

public record MpcModelData(
        int horizon,
        double[][] matrixA,
        double[] vectorB,
        @With double[] statePenalty,
        @With double[] controlPenalty
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
