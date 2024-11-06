package mpc.domain.creators;

import com.google.common.collect.Lists;
import org.hellgren.utilities.vector_algebra.MatrixStacking;
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
import java.util.stream.IntStream;

import static org.hellgren.utilities.vector_algebra.MatrixStacking.vectorsToMatrix;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.properties;

/**
 * A class responsible for creating matrices used in Model Predictive Control (MPC) algorithm.
 */
@AllArgsConstructor
@Getter
public class MpcMatrixCreator {

    MpcModelData modelData;

    public static MpcMatrixCreator of(MpcModelData modelData) {
        return new MpcMatrixCreator(modelData);
    }

    public MpcMatrices createMatrices() {
        return MpcMatrices.builder()
                .stateImpact(matrixS())
                .controlAffect(matrixT())
                .hessian(matrixH())
                .trackingPenalty(matrixQ())
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

    /**
     *  A single matrix corresponding to for example [B 0 .. 0}
     */

    private RealMatrix matrixT() {
        List<RealMatrix> matrices = Lists.newArrayList();
        for (int ti = 0; ti < modelData.horizon(); ti++) {
            RealMatrix matrix=vectorsToMatrix(getVectors(ti));
            matrices.add(matrix);
        }
        return MatrixStacking.stackVertically(matrices);
    }


    /**
     * Creates vectors corresponding to elements in controlAffect matrix, for example B, AB, etc
     */

    private List<RealVector> getVectors(int ti) {
        return IntStream.range(0, modelData.horizon())
                .mapToObj(ei -> getVector(ti, ei))
                .toList();
    }

    private RealVector getVector(int ti, int ei) {
        var a = MatrixUtils.createRealMatrix(modelData.matrixA());
        var b = MatrixUtils.createRealVector(modelData.vectorB());
        return ti - ei < 0
                ? createZeroVector(modelData.nStates())
                : a.power(ti - ei).operate(b);
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
