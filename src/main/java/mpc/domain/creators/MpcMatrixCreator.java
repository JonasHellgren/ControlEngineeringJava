package mpc.domain.creators;

import com.google.common.collect.Lists;
import helpers.MatrixStacking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.MpcMatrices;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.hellgren.utilities.list_arrays.ArrayCreator;
import org.hellgren.utilities.vector_algebra.MyMatrixUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;

@AllArgsConstructor
@Getter
public class MpcMatrixCreator {

    MpcModelData modelData;

    public static MpcMatrixCreator of(MpcModelData modelData) {
        return new MpcMatrixCreator(modelData);
    }

    public MpcMatrices createMatrices() {
        return MpcMatrices.builder()
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
        List<RealMatrix> rows = Lists.newArrayList();
        for (int ri = 0; ri < modelData.horizon(); ri++) {
            RealMatrix row = MyMatrixUtils.stackVectorsHorizontally(getRowElements(ri));
            rows.add(row);
        }
        return MatrixStacking.stackVertically(rows);
    }

    @NotNull
    private List<RealVector> getRowElements(int rowIndex) {
        var a = MatrixUtils.createRealMatrix(modelData.matrixA());
        var b = MatrixUtils.createRealVector(modelData.vectorB());
        List<RealVector> vectors = Lists.newArrayList();
        for (int ei = 0; ei < modelData.horizon(); ei++) {
            RealVector m = rowIndex - ei < 0
                    ? createZeroVector(modelData.nStates())
                    : a.power(rowIndex - ei).operate(b);
            vectors.add(m);
        }
        return vectors;
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


}
