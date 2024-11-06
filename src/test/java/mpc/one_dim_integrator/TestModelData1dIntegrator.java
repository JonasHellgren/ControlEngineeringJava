package mpc.one_dim_integrator;

import mpc.domain.value_objects.MpcModelData;
import mpc.problems.one_dim_integrator.FactoryOneDimIntegrator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestModelData1dIntegrator {

    public static final int HORIZON = 2;
    MpcModelData model;

    @BeforeEach
    void init() {
        model = FactoryOneDimIntegrator.createModelData(HORIZON);
    }


    @Test
    void model_thenIsOk() {
        Assertions.assertTrue(model.isValid());
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
