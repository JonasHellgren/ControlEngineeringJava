package mpc.one_dim_integrator;

import lombok.SneakyThrows;
import mpc.domain.ModelQP;
import mpc.domain.calculators.ResponseCalculator;
import mpc.domain.controller.MpcController;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.creators.MpcVectorFCreator;
import mpc.domain.value_objects.MPCModelData;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.StatePresentAndReference;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createOnesVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;

public class TestMpcController1dIntegrator {

    public static final int HORIZON = 2;
    public static final double TOL = 0.01;
    public static final int N_STATES = 1;
    public static final double UPPER_BOUND = 0.5d;
    public static final double TAR_ONE = 1d;
    public static final RealVector STATE_START = createZeroVector(N_STATES);
    MPCModelData model;
    MpcMatrices mpcMatrices;
    MpcController controller;
    ModelQP modelQP;

    @BeforeEach
    void init() {
        model = MatrixDataFactoryOneDimIntegrator.createModelData(HORIZON)
                .withControlPenalty(new double[]{0})
                .withStatePenalty(new double[]{10});
        mpcMatrices = new MpcMatrixCreator(model).getMpcMatrices();
        modelQP= ModelQP.builder()
                .modelData(model)
                .matrices(mpcMatrices)
                .vectorFCreator(new MpcVectorFCreator(model, mpcMatrices))
                .upperBound(UPPER_BOUND)
                .build();
        controller=new MpcController(model, modelQP);
    }


    @SneakyThrows
    @Test
    void whenRefIsZero_thenCorrectResponse() {
        var input=controller.calculateInputSignal(getStateAndRefXero());
        Assertions.assertArrayEquals(new double[]{0d, 0d},input, TOL);
    }

    @SneakyThrows
    @Test
    void whenRefIsOne_thenCorrectResponse() {
        var input=controller.calculateInputSignal(getStateAndRefOne());
        var calculator=new ResponseCalculator(model, mpcMatrices);
        var response=calculator.response(STATE_START, MatrixUtils.createRealVector(input));
        Assertions.assertArrayEquals(new double[]{UPPER_BOUND, TAR_ONE},response.toArray(), TOL);
    }

    @SneakyThrows
    @Test
    void whenRefIsOneAndNoInputBound_thenCorrectResponse() {
        modelQP=modelQP.withUpperBound(100);
        controller=new MpcController(model, modelQP);
        var input=controller.calculateInputSignal(getStateAndRefOne());
        var calculator=new ResponseCalculator(model, mpcMatrices);
        var response=calculator.response(STATE_START, MatrixUtils.createRealVector(input));
        Assertions.assertArrayEquals(new double[]{TAR_ONE, TAR_ONE},response.toArray(), TOL);
    }

    StatePresentAndReference getStateAndRefXero() {
        return StatePresentAndReference.of(STATE_START, createZeroVector(model.horizon()));
    }


    StatePresentAndReference getStateAndRefOne() {
        return StatePresentAndReference.of(STATE_START, createOnesVector(model.horizon()));
    }



}
