package mpc.lunar_landing;

import com.joptimizer.exception.JOptimizerException;
import lombok.SneakyThrows;
import mpc.domain.calculators.ResponseCalculator;
import mpc.domain.controller.ModelQP;
import mpc.domain.controller.MpcController;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.StatePresentAndReference;
import mpc.problems.lunar_landing.FactoryLunarLanding;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createOnesVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TestControllerLunarLanding {

    static final int HORIZON_2 = 2;
    static final double TOL = 0.01;
    static final int N_STATES = 2;
    static final double UPPER_BOUND_CONTROL = 0.5d;
    static final double UPPER_BOUND_HIGH = 100d;
    static final double TAR_ONE = 1d;
    static final RealVector STATE_START = MatrixUtils.createRealVector(new double[]{10,0});
    static final int HORIZON_4 = 4;
    static final int CONTROL_PENALTY_ZERO = 0;

    MpcModelData model;
    MpcMatrices mpcMatrices;
    MpcController controller;
    ModelQP modelQP;

    private void initWithHorizonAndInputBoundAndPenalty(int horizon, double upperBound, double pen) {
        model = FactoryLunarLanding.createModelData(horizon)
                .withControlPenalty(new double[]{pen});
        mpcMatrices = MpcMatrixCreator.of(model).createMatrices();
        modelQP= ModelQP.builder()
                .modelData(model)
                .matrices(mpcMatrices)
                .upperBound(upperBound)
                .build();
        controller=MpcController.of(model, modelQP);
    }


    @SneakyThrows
    @Test
    void givenHorizon2_whenRefIsZero_thenCorrectResponse() {
        initWithHorizonAndInputBoundAndPenalty(
                HORIZON_2,
                UPPER_BOUND_CONTROL,
                CONTROL_PENALTY_ZERO);
        var input=controller.calculateInputSignal(getStateAndRefXero());
        System.out.println("input = " + Arrays.toString(input));
        assertArrayEquals(new double[]{0d, 0d},input, TOL);
    }

    @SneakyThrows
    @Test
    void givenHorizon2_whenRefIsOne_thenCorrectResponse() {
        initWithHorizonAndInputBoundAndPenalty(HORIZON_2, UPPER_BOUND_CONTROL, CONTROL_PENALTY_ZERO);
        var response = getResponseForRefOne();
        assertArrayEquals(new double[]{UPPER_BOUND_CONTROL, TAR_ONE},response.toArray(), TOL);
    }


    StatePresentAndReference getStateAndRefXero() {
        return StatePresentAndReference.of(STATE_START, createZeroVector(model.nStates()));
    }


    StatePresentAndReference getStateAndRefOne() {
        return StatePresentAndReference.of(STATE_START, createOnesVector(model.nStates()));
    }

    private RealVector getResponseForRefOne() throws JOptimizerException {
        var input=controller.calculateInputSignal(getStateAndRefOne());
        var calculator= ResponseCalculator.of(model, mpcMatrices);
        return calculator.response(STATE_START, MatrixUtils.createRealVector(input));
    }



}
