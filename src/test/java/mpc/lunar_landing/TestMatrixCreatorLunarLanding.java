package mpc.lunar_landing;

import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.MpcModelData;
import mpc.problems.lunar_landing.FactoryLunarLanding;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.properties;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMatrixCreatorLunarLanding {

    public static final int HORIZON = 2;
    MpcModelData model;
    MpcMatrices mpcMatrices;

    @BeforeEach
    void init() {
        model = FactoryLunarLanding.createModelData(HORIZON);
        mpcMatrices = new MpcMatrixCreator(model).createMatrices();
    }

    @Test
    void whenMatrixS_thenCorrect() {
        var s=mpcMatrices.stateImpact();
        assertEquals(model.nStates() * model.horizon(),properties(s).nRows() );
        assertEquals(model.nStates(),properties(s).nColumns() );
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
