package mpc.lunar_landing;

import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.creators.MpcVectorFCreator;
import mpc.domain.value_objects.MpcModelData;
import mpc.problems.lunar_landing.FactoryLunarLanding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestVectorFCreatorLunarLanding {

    public static final int HORIZON = 2;
    MpcModelData model;
    MpcVectorFCreator calculator;

    @BeforeEach
    void init() {
        model = FactoryLunarLanding.createModelData(HORIZON);
        var matrices= MpcMatrixCreator.of(model).createMatrices();
        calculator = MpcVectorFCreator.of(model, matrices);
    }


    @Test
    void whenMatrixF_thenCorrect() {
        var f=calculator.vectorFSameXrefEveryStep(createZeroVector(model.nStates()),createOnesVector(model.nStates()));
        assertEquals( model.horizon(), properties(f).nRows());
        assertEquals( 1, properties(f).nColumns());
    }

}
