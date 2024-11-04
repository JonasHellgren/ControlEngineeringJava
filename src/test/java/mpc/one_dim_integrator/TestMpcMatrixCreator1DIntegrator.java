package mpc.one_dim_integrator;

import helpers.MatrixStacking;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MPCModelData;
import mpc.domain.value_objects.MpcMatrices;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.hellgren.utilities.vector_algebra.MyMatrixUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestMpcMatrixCreator1DIntegrator {

    MPCModelData model;
    RealMatrix a;
    RealVector b;
    MpcMatrices mpcMatrices;

    @BeforeEach
    void init() {
        model = MatrixDataFactoryOneDimIntegrator.createModelData(2);
        mpcMatrices = new MpcMatrixCreator(model).createMatrices();
        a = MatrixUtils.createRealMatrix(model.matrixA());
        b = MatrixUtils.createRealVector((model.vectorB()));
    }


    @Test
    void whenMatrixS_thenCorrect() {
        var aStacked = MatrixStacking.stackVertically(List.of(a, a));
        assertEquals(aStacked, mpcMatrices.S());
    }

    @Test
    void whenMatrixT_thenCorrect() {
        RealVector zeroVector = createZeroVector(model.nStates());
        var row1 = MyMatrixUtils.stackVectorsHorizontally(List.of(b, zeroVector));
        var row2 = MyMatrixUtils.stackVectorsHorizontally(List.of(a.operate(b), b));
        var m = MatrixStacking.stackVertically(List.of(row1, row2));
        RealMatrix t = mpcMatrices.T();
        assertEquals(m, t);
        assertEquals(model.nStates() * model.horizon(), properties(t).nRows());
        assertEquals(model.horizon(), properties(t).nColumns());
    }

    @Test
    void whenMatrixH_thenCorrect() {
        var h = mpcMatrices.H();
        assertEquals(model.horizon(), properties(h).nRows());
        assertEquals(model.horizon(), properties(h).nColumns());
    }

    @Test
    void whenMatrixQ_thenCorrect() {
        var q = mpcMatrices.Q();
        assertEquals(model.horizon() * model.nStates(), properties(q).nRows());
        assertEquals(model.horizon() * model.nStates(), properties(q).nColumns());
    }


}
