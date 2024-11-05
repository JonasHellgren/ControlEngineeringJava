package mpc.lunar_landing;

import com.joptimizer.exception.JOptimizerException;
import lombok.SneakyThrows;
import mpc.domain.calculators.ResponseCalculator;
import mpc.domain.controller.ModelQPI;
import mpc.helpers.RealVectorUtils;
import mpc.problems.lunar_landing.ModelQPLunarLander;
import mpc.domain.controller.MpcController;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.StatePresentAndReference;
import mpc.problems.lunar_landing.FactoryLunarLanding;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static mpc.helpers.RealVectorUtils.areVectorsEqual;
import static org.apache.commons.math3.linear.MatrixUtils.createRealVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;
import static org.junit.jupiter.api.Assertions.*;

class TestControllerLunarLanding {

    static final int HORIZON_2 = 2;
    private static final int HORIZON_10 = 10 ;
    static final double TOL = 0.01;
    public static final double FORCE = 1000d;
    static final double LOWER_BOUND_CONTROL = -FORCE;
    static final double UPPER_BOUND_CONTROL = FORCE;
    public static final Pair<Double, Double> BOUNDS = Pair.create(LOWER_BOUND_CONTROL, UPPER_BOUND_CONTROL);
    static final double TAR_ONE = 1d;
    static final RealVector STATE_START = createRealVector(new double[]{5,0});
    public static final double START_POS = STATE_START.getEntry(0);
    static final double CONTROL_PENALTY_ZERO = 0d;
    public static final double MASS = 1000;
    public static final double DSPD_MAX=FORCE/MASS;

    MpcModelData model;
    MpcMatrices mpcMatrices;
    MpcController controller;
    ModelQPI modelQP;

    private void initWithHorizonAndInputBoundAndPenalty(int horizon) {
        model = FactoryLunarLanding.createModelData(horizon,1, MASS)
                .withControlPenalty(new double[]{TestControllerLunarLanding.CONTROL_PENALTY_ZERO});
        mpcMatrices = MpcMatrixCreator.of(model).createMatrices();
        modelQP= ModelQPLunarLander.builder()
                .modelData(model)
                .matrices(mpcMatrices)
                .bounds(TestControllerLunarLanding.BOUNDS)
                .build();
        controller=MpcController.of(model, modelQP);
    }

    @SneakyThrows
    @Test
    void givenHorizon2_whenRefIsZero_thenCorrectInput() {
        initWithHorizonAndInputBoundAndPenalty(HORIZON_2);
        var input=controller.calculateInputSignal(getStateAndRefXero());
        System.out.println("input = " + Arrays.toString(input));
        assertEquals(-FORCE,input[0], TOL);
    }

    @SneakyThrows
    @Test
    void givenHorizon2_whenRefIsOne_thenCorrectPositions() {
        initWithHorizonAndInputBoundAndPenalty(HORIZON_2);
        var response = getResponseForRefZero();
        var positions= model.getStateValues(response,0);
        assertTrue(areVectorsEqual(createRealVector(new double[]{START_POS,START_POS-DSPD_MAX}),positions,TOL));
    }


    @SneakyThrows
    @Test
    void givenHorizon10_whenRefIsOne_thenCorrectPositions() {
        initWithHorizonAndInputBoundAndPenalty(HORIZON_10);
        var response = getResponseForRefZero();
        var positions= model.getStateValues(response,0);
        System.out.println("positions = " + positions);
        assertTrue(RealVectorUtils.areElementsDecreasing(positions));
        assertTrue(positions.getMinValue()>0);
    }

    private RealVector getResponseForRefZero() throws JOptimizerException {
        var input=controller.calculateInputSignal(getStateAndRefXero());
        var calculator= ResponseCalculator.of(model, mpcMatrices);
        return calculator.response(STATE_START, createRealVector(input));
    }


    StatePresentAndReference getStateAndRefXero() {
        return StatePresentAndReference.of(STATE_START, createZeroVector(model.nStates()));
    }


}
