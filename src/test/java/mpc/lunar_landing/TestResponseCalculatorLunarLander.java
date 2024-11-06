package mpc.lunar_landing;

import mpc.domain.calculators.ResponseCalculator;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MpcModelData;
import mpc.problems.lunar_landing.FactoryLunarLanding;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createOnesVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;

class TestResponseCalculatorLunarLander {
    public static final int HORIZON = 2;
    public static final double DT = 1;
    public static final double MASS = 1;
    MpcModelData model;
    ResponseCalculator calculator;

    @BeforeEach
    void init() {
        model = FactoryLunarLanding.createModelData(HORIZON, DT, MASS);
        var mpcMatrices = MpcMatrixCreator.of(model).createMatrices();
        calculator = ResponseCalculator.of(model, mpcMatrices);
    }


    @Test
    void whenControlInputIsZero_thenCorrectResponse() {
        var response=calculator.response(createZeroVector(model.nStates()),createZeroVector(model.horizon()));
        RealVector zeroVector = createZeroVector(model.horizon()*model.nStates());
        Assertions.assertEquals(zeroVector,response);
    }


    @Test
    void whenControlInputIsOne_thenCorrectResponse() {
        var response=calculator.response(createZeroVector(model.nStates()),createOnesVector(model.horizon()));

        System.out.println("response = " + response);

        double force = 1;
        double dSpd = MASS*force;
        double pos0 = 0d;
        double spd0 = dSpd;
        double pos1 = pos0 + spd0;
        double sdp1 = spd0 + dSpd;
        Assertions.assertEquals(MatrixUtils.createRealVector(new double[]{pos0, spd0, pos1, sdp1}),response);
    }

}
