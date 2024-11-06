package mpc.lunar_landing;

import mpc.domain.value_objects.MpcModelData;
import mpc.problems.lunar_landing.FactoryLunarLanding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestModelDataLunarLanding {

    public static final int HORIZON = 2;
    MpcModelData model;

    @BeforeEach
    void init() {
        model = FactoryLunarLanding.createModelData(HORIZON);
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
    void givenModel_whenNStates_then2() {
        assertEquals(2, model.nStates());
    }


}
