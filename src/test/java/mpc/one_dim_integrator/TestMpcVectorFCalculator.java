package mpc.one_dim_integrator;

import mpc.domain.MpcMatrixCalculator;
import mpc.domain.calculators.MpcVectorFCalculator;
import mpc.domain.value_objects.MPCModelData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMpcVectorFCalculator {

    MPCModelData model;
    MpcVectorFCalculator calculator;

    @BeforeEach
    void init() {
        model = MatrixDataFactoryOneDimIntegrator.createModelData(2);
        var matrixes= new MpcMatrixCalculator(model).getMpcMatrixes();
        calculator = new MpcVectorFCalculator(model, matrixes); }


    @Test
    void whenMatrixF_thenCorrect() {
        var f=calculator.vectorFSameXrefEveryStep(createZeroVector(1),createOnesVector(1));
        assertEquals( model.horizon(), properties(f).nRows());
        assertEquals( 1, properties(f).nColumns());
    }

}
