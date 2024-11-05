package mpc.one_dim_integrator;

import com.joptimizer.exception.JOptimizerException;
import lombok.SneakyThrows;
import mpc.domain.controller.ModelQP;
import mpc.domain.calculators.ResponseCalculator;
import mpc.domain.controller.MpcController;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.StatePresentAndReference;
import mpc.problems.one_dim_integrator.FactoryOneDimIntegrator;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createOnesVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestMpcController1dIntegrator {

    static final int HORIZON_2 = 2;
    static final double TOL = 0.01;
    static final int N_STATES = 1;
    static final double UPPER_BOUND = 0.5d;
    static final double UPPER_BOUND_HIGH = 100d;
    static final double TAR_ONE = 1d;
    static final RealVector STATE_START = createZeroVector(N_STATES);
    static final int HORIZON_4 = 4;
    static final int CONTROL_PENALTY_ZERO = 0;

    MpcModelData model;
    MpcMatrices mpcMatrices;
    MpcController controller;
    ModelQP modelQP;

    private void initWithHorizonAndInputBoundAndPentaly(int horizon, double upperBound, double pen) {
        model = FactoryOneDimIntegrator.createModelData(horizon)
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
        initWithHorizonAndInputBoundAndPentaly(HORIZON_2, UPPER_BOUND, CONTROL_PENALTY_ZERO);
        var input=controller.calculateInputSignal(getStateAndRefXero());
        assertArrayEquals(new double[]{0d, 0d},input, TOL);
    }

    @SneakyThrows
    @Test
    void givenHorizon2_whenRefIsOne_thenCorrectResponse() {
        initWithHorizonAndInputBoundAndPentaly(HORIZON_2, UPPER_BOUND, CONTROL_PENALTY_ZERO);
        var response = getResponseForRefOne();
        assertArrayEquals(new double[]{UPPER_BOUND, TAR_ONE},response.toArray(), TOL);
    }

    @SneakyThrows
    @Test
    void givenHorizon2_whenRefIsOneAndHighInputBound_thenCorrectResponse() {
        initWithHorizonAndInputBoundAndPentaly(HORIZON_2, UPPER_BOUND_HIGH, CONTROL_PENALTY_ZERO);
        var response = getResponseForRefOne();
        assertArrayEquals(new double[]{TAR_ONE, TAR_ONE},response.toArray(), TOL);
    }

    @SneakyThrows
    @Test
    void givenHorizon2AndHighControlPenalty_whenRefIsOneAndHighInputBound_thenCorrectResponse() {
        initWithHorizonAndInputBoundAndPentaly(HORIZON_2, UPPER_BOUND_HIGH, 1);
        var response = getResponseForRefOne();
        assertTrue(response.toArray()[0]<TAR_ONE);
        assertTrue(response.toArray()[1]<TAR_ONE);
    }

    @SneakyThrows
    @Test
    void givenHorizon4_whenRefIsZero_thenCorrectResponse() {
        initWithHorizonAndInputBoundAndPentaly(HORIZON_4, UPPER_BOUND, CONTROL_PENALTY_ZERO);
        var input=controller.calculateInputSignal(getStateAndRefXero());
        assertArrayEquals(new double[]{0d, 0d,0d, 0d},input, TOL);
    }

    @SneakyThrows
    @Test
    void givenHorizon4_whenRefIsOne_thenCorrectResponse() {
        initWithHorizonAndInputBoundAndPentaly(HORIZON_4, UPPER_BOUND, CONTROL_PENALTY_ZERO);
        var response = getResponseForRefOne();
        assertArrayEquals(new double[]{UPPER_BOUND, TAR_ONE,TAR_ONE,TAR_ONE},response.toArray(), TOL);
    }

    @SneakyThrows
    @Test
    void givenHorizon4_whenRefIsOneAndHighInputBound_thenCorrectResponse() {
        initWithHorizonAndInputBoundAndPentaly(HORIZON_4, UPPER_BOUND_HIGH, CONTROL_PENALTY_ZERO);
        var response = getResponseForRefOne();
        assertArrayEquals(new double[]{TAR_ONE, TAR_ONE,TAR_ONE,TAR_ONE},response.toArray(), TOL);
    }

    private RealVector getResponseForRefOne() throws JOptimizerException {
        var input=controller.calculateInputSignal(getStateAndRefOne());
        var calculator=ResponseCalculator.of(model, mpcMatrices);
        return calculator.response(STATE_START, MatrixUtils.createRealVector(input));
    }


    StatePresentAndReference getStateAndRefXero() {
        return StatePresentAndReference.of(STATE_START, createZeroVector(model.nStates()));
    }


    StatePresentAndReference getStateAndRefOne() {
        return StatePresentAndReference.of(STATE_START, createOnesVector(model.nStates()));
    }



}
