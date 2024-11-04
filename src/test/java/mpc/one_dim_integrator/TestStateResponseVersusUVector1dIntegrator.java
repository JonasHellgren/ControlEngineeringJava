package mpc.one_dim_integrator;

import mpc.domain.calculators.ResponseCalculator;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MPCModelData;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createOnesVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;

class TestResponseCalculator1dIntegrator {
    MPCModelData model;
    ResponseCalculator calculator;

    @BeforeEach
    void init() {
        model = MatrixDataFactoryOneDimIntegrator.createModelData(2);
        var mpcMatrices = MpcMatrixCreator.of(model).createMatrices();
        calculator = ResponseCalculator.of(model, mpcMatrices);
    }


    @Test
    void whenControlInputIsZero_thenCorrectResponse() {
        var response=calculator.response(createZeroVector(1),createZeroVector(model.horizon()));
        Assertions.assertEquals(response,createZeroVector(model.horizon()));
    }


    @Test
    void whenControlInputIsOne_thenCorrectResponse() {
        var response=calculator.response(createZeroVector(1),createOnesVector(model.horizon()));
        Assertions.assertEquals(response, MatrixUtils.createRealVector(new double[]{1, 2}));
    }

}
