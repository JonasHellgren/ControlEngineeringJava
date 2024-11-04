package mpc.domain.creators;

import lombok.AllArgsConstructor;
import mpc.domain.value_objects.MPCModelData;
import mpc.domain.value_objects.MpcMatrices;
import org.apache.commons.math3.linear.RealVector;
import org.hellgren.utilities.list_arrays.ListCreator;
import org.hellgren.utilities.vector_algebra.MyMatrixUtils;

import static com.google.common.base.Preconditions.checkArgument;


@AllArgsConstructor
public class MpcVectorFCreator {

    MPCModelData modelData;
    MpcMatrices matrices;

    public RealVector vectorFSameXrefEveryStep(RealVector x, RealVector xRef0) {
        checkArgument(x.getDimension() == modelData.nStates());
        checkArgument(x.getDimension() == modelData.nStates());
        var vectors = ListCreator.nCopiesMutable(xRef0, modelData.horizon());
        var vector = MyMatrixUtils.stackVectorsHorizontallyToVector(vectors);
        return vectorF(x, vector);
    }

    public RealVector vectorF(RealVector x, RealVector xRef) {
        checkArgument(x.getDimension() == modelData.nStates());
        checkArgument(xRef.getDimension() == modelData.nStates() * modelData.horizon());
        var t = matrices.T();
        var q = matrices.Q();
        var s = matrices.S();
        return t.transpose().multiply(q).operate(s.operate(x).subtract(xRef));
    }

}
