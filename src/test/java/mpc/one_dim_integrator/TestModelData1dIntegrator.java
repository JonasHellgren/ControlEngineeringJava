package mpc.one_dim_integrator;

import mpc.domain.value_objects.MPCModelData;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestModelData1dIntegrator {

    public static final int HORIZON = 2;
    MPCModelData model;
    RealMatrix a;
    RealVector b;

    @BeforeEach
    void init() {
        model = MatrixDataFactoryOneDimIntegrator.createModelData(HORIZON);
        a = MatrixUtils.createRealMatrix(model.matrixA());
        b = MatrixUtils.createRealVector(model.vectorB());
    }


    @Test
    void model_thenIsOk() {
        Assertions.assertTrue(model.isOk());
    }

    @Test
    void givenModel_whenHorizon_then2() {
        assertEquals(HORIZON, model.horizon());
    }

    @Test
    void givenModel_whenNStates_then1() {
        assertEquals(1, model.nStates());
    }



}
