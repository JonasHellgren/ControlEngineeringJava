package mpc.domain;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import helpers.MatrixStacking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mpc.domain.value_objects.MPCModelData;
import mpc.domain.value_objects.MpcMatrixes;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.hellgren.utilities.list_arrays.ArrayCreator;
import org.hellgren.utilities.list_arrays.ListCreator;
import org.hellgren.utilities.vector_algebra.MyMatrixUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;

@AllArgsConstructor
@Getter
public class MPCMatrixCalculator {

    MPCModelData modelData;
    MpcMatrixes mpcMatrixes;

    public MPCMatrixCalculator(MPCModelData modelData) {
        this.modelData = modelData;
        this.mpcMatrixes = MpcMatrixes.builder()
                .S(matrixS())
                .T(matrixT())
                .H(matrixH())
                .Q(matrixQ())
                .build();
    }

    private RealMatrix matrixS() {
        var a = MatrixUtils.createRealMatrix(modelData.matrixA());
        List<RealMatrix> matrices = Lists.newArrayList();
        for (int i = 0; i < modelData.horizon(); i++) {
            matrices.add(a.power(i));
        }
        return MatrixStacking.stackVertically(matrices);
    }

    private RealMatrix matrixT() {
        var a = MatrixUtils.createRealMatrix(modelData.matrixA());
        var b = MatrixUtils.createRealVector(modelData.vectorB());
        int n = modelData.nStates();
        List<RealMatrix> rows = Lists.newArrayList();
        for (int i = 0; i < modelData.horizon(); i++) {
            List<RealVector> vectors = Lists.newArrayList();
            for (int j = 0; j < modelData.horizon(); j++) {
                RealVector m = i - j < 0
                        ? createZeroVector(n)
                        : a.power(i - j).operate(b);
                vectors.add(m);
            }
            RealMatrix row = MyMatrixUtils.stackVectorsHorizontally(vectors);
            rows.add(row);
        }
        return MatrixStacking.stackVertically(rows);
    }

    private RealMatrix matrixH() {
        var t = matrixT();
        var q = matrixQ();
        var r = matrixR();
        return (t.transpose().multiply(q).multiply(t).add(r)).scalarMultiply(2);
    }

    private RealMatrix matrixQ() {
        double[] diagonal = ArrayCreator.duplicate(modelData.statePenalty(), modelData.horizon());
        return MyMatrixUtils.createDiagonalMatrix(diagonal);
    }

    private RealMatrix matrixR() {
        double[] diagonal = ArrayCreator.duplicate(modelData.controlPenalty(), modelData.horizon());
        return MyMatrixUtils.createDiagonalMatrix(diagonal);
    }

    public RealVector vectorFSameXrefEveryStep(RealVector x, RealVector xRef0) {
        Preconditions.checkArgument(x.getDimension() == modelData.nStates());
        Preconditions.checkArgument(x.getDimension() == modelData.nStates());
        var vectors = ListCreator.nCopiesMutable(xRef0, modelData.horizon());
        var vector = MyMatrixUtils.stackVectorsHorizontallyToVector(vectors);
        return vectorF(x, vector);
    }

    public RealVector vectorF(RealVector x, RealVector xRef) {
        Preconditions.checkArgument(x.getDimension() == modelData.nStates());
        Preconditions.checkArgument(xRef.getDimension() == modelData.nStates() * modelData.horizon());
        var t = mpcMatrixes.T();
        var q = mpcMatrixes.Q();
        var s = mpcMatrixes.S();
        return t.transpose().multiply(q).operate(s.operate(x).subtract(xRef));
    }


}
