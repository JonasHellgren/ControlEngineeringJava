package mpc.domain.creators;

import lombok.AllArgsConstructor;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.StatePresentAndReference;
import org.apache.commons.math3.linear.RealVector;
import org.hellgren.utilities.list_arrays.ListCreator;
import org.hellgren.utilities.vector_algebra.MatrixStacking;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A creator class for generating f vector used in Model Predictive Control (MPC) algorithms.
 * Vector represents a part of the QP cost function and typically arises from the setpoints or
 * references that the system should track.
 *
 */

@AllArgsConstructor
public class MpcVectorFCreator {

    MpcModelData modelData;
    MpcMatrices matrices;

    public static MpcVectorFCreator of(MpcModelData modelData, MpcMatrices matrices) {
        return new MpcVectorFCreator(modelData, matrices);
    }

    public RealVector vectorFSameXrefEveryStep(StatePresentAndReference statePresentAndReference) {
        return vectorFSameXrefEveryStep(statePresentAndReference.x(), statePresentAndReference.xRef());
    }

    public RealVector vectorFSameXrefEveryStep(RealVector xStart, RealVector xRef0) {
        checkArgument(xStart.getDimension() == modelData.nStates());
        checkArgument(xRef0.getDimension() == modelData.nStates());
        var vectors = ListCreator.nCopiesMutable(xRef0, modelData.horizon());
        var vector = MatrixStacking.stackVectorsHorizontally(vectors);
        return vectorF(xStart, vector);
    }

    public RealVector vectorF(RealVector x, RealVector xRef) {
        checkArgument(x.getDimension() == modelData.nStates());
        checkArgument(xRef.getDimension() == modelData.nStates() * modelData.horizon());
        var t = matrices.controlAffect();
        var q = matrices.trackingPenalty();
        var s = matrices.stateImpact();
        return t.transpose().multiply(q).operate(s.operate(x).subtract(xRef)).mapMultiply(2);
    }

}
