package mpc.one_dim_integrator;

import org.hellgren.utilities.vector_algebra.MatrixStacking;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.MpcMatrices;
import mpc.problems.one_dim_integrator.FactoryOneDimIntegrator;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestMpcMatrixCreator1DIntegrator {

    MpcModelData model;
    RealMatrix a;
    RealVector b;
    MpcMatrices mpcMatrices;

    @BeforeEach
    void init() {
        model = FactoryOneDimIntegrator.createModelData(2);
        mpcMatrices = new MpcMatrixCreator(model).createMatrices();
        a = MatrixUtils.createRealMatrix(model.matrixA());
        b = MatrixUtils.createRealVector((model.vectorB()));
    }


    @Test
    void whenMatrixS_thenCorrect() {
        var aStacked = MatrixStacking.stackVertically(List.of(a, a));
        assertEquals(aStacked, mpcMatrices.stateImpact());
    }

    @Test
    void whenMatrixT_thenCorrect() {
        RealMatrix t = mpcMatrices.controlAffect();
        assertEquals(model.nStates() * model.horizon(), properties(t).nRows());
        assertEquals(model.horizon(), properties(t).nColumns());
    }

    @Test
    void whenMatrixH_thenCorrect() {
        var h = mpcMatrices.hessian();
        assertEquals(model.horizon(), properties(h).nRows());
        assertEquals(model.horizon(), properties(h).nColumns());
    }

    @Test
    void whenMatrixQ_thenCorrect() {
        var q = mpcMatrices.trackingPenalty();
        assertEquals(model.horizon() * model.nStates(), properties(q).nRows());
        assertEquals(model.horizon() * model.nStates(), properties(q).nColumns());
    }


}
