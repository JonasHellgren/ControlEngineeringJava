package mpc.domain.creators;

import lombok.AllArgsConstructor;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.StatePresentAndReference;
import org.apache.commons.math3.linear.RealVector;
import org.hellgren.utilities.list_arrays.ListCreator;
import org.hellgren.utilities.vector_algebra.MatrixStacking;
import org.hellgren.utilities.vector_algebra.MyMatrixUtils;

import static com.google.common.base.Preconditions.checkArgument;


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
        var t = matrices.T();
        var q = matrices.Q();
        var s = matrices.S();
        return t.transpose().multiply(q).operate(s.operate(x).subtract(xRef)).mapMultiply(2);
    }

}
