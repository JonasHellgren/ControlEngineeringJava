package mpc.one_dim_integrator;

import mpc.domain.calculators.ResponseCalculator;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MpcModelData;
import mpc.problems.one_dim_integrator.FactoryOneDimIntegrator;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createOnesVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;

class TestResponseCalculator1dIntegrator {
    MpcModelData model;
    ResponseCalculator calculator;

    @BeforeEach
    void init() {
        model = FactoryOneDimIntegrator.createModelData(2);
        var mpcMatrices = MpcMatrixCreator.of(model).createMatrices();
        calculator = ResponseCalculator.of(model, mpcMatrices);
    }


    @Test
    void whenControlInputIsZero_thenCorrectResponse() {
        var response=calculator.response(createZeroVector(model.nStates()),createZeroVector(model.horizon()));
        Assertions.assertEquals(createZeroVector(model.horizon()),response);
    }


    @Test
    void whenControlInputIsOne_thenCorrectResponse() {
        var response=calculator.response(createZeroVector(model.nStates()),createOnesVector(model.horizon()));
        Assertions.assertEquals(MatrixUtils.createRealVector(new double[]{1, 2}),response);
    }

}
