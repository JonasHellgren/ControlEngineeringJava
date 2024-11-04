package mpc.one_dim_integrator;

import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.creators.MpcVectorFCreator;
import mpc.domain.value_objects.MPCModelData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMpcVectorFCreator {

    MPCModelData model;
    MpcVectorFCreator calculator;

    @BeforeEach
    void init() {
        model = MatrixDataFactoryOneDimIntegrator.createModelData(2);
        var matrices= MpcMatrixCreator.of(model).createMatrices();
        calculator = MpcVectorFCreator.of(model, matrices);
    }


    @Test
    void whenMatrixF_thenCorrect() {
        var f=calculator.vectorFSameXrefEveryStep(createZeroVector(1),createOnesVector(1));
        assertEquals( model.horizon(), properties(f).nRows());
        assertEquals( 1, properties(f).nColumns());
    }

}
