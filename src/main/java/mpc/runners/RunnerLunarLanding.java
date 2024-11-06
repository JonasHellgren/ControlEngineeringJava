package mpc.runners;

import lombok.SneakyThrows;
import mpc.domain.calculators.ResponseCalculator;
import mpc.domain.controller.MpcController;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.StatePresentAndReference;
import mpc.helpers.Plotter;
import mpc.problems.lunar_landing.FactoryLunarLanding;
import mpc.problems.lunar_landing.ModelQPLunarLander;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import java.util.Arrays;
import static org.apache.commons.math3.linear.MatrixUtils.createRealVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;

public class RunnerLunarLanding {

    public static final double START_SPD = -1d;
    public static final double CONTROL_PENALTY = 1e-4;
    public static final int HORIZON = 20;
    public static final double FORCE = 500d;
    static final double LOWER_BOUND_CONTROL = -FORCE;
    static final double UPPER_BOUND_CONTROL = FORCE;
    public static final Pair<Double, Double> BOUNDS = Pair.create(LOWER_BOUND_CONTROL, UPPER_BOUND_CONTROL);
    public static final double START_POS = 20d;
    public static final double G = 9.81;

    @SneakyThrows
    public static void main(String[] args) {
        var model = FactoryLunarLanding.createModelData(HORIZON)
                .withControlPenalty(new double[]{CONTROL_PENALTY});
        var mpcMatrices = MpcMatrixCreator.of(model).createMatrices();
        var modelQP = ModelQPLunarLander.of(model,mpcMatrices,BOUNDS);
        var controller = MpcController.of(model, modelQP);
        var startAndRefState = getStartStateAndRefState(model);
        var input = controller.calculateInputSignal(startAndRefState);
        var calculator = ResponseCalculator.of(model, mpcMatrices);
        var response = calculator.response(startAndRefState.x(), input);
        var speeds = model.getStateValues(response, 1);
        var positions = model.getStateValues(response, 0);
        doPlotting(input, speeds,positions);
    }

    static StatePresentAndReference getStartStateAndRefState(MpcModelData model) {
        RealVector stateStart = createRealVector(new double[]{START_POS,START_SPD});
        return StatePresentAndReference.of(stateStart, createZeroVector(model.nStates()));
    }

    private static void doPlotting(double[] input, RealVector speeds, RealVector positions) {
        var plotter=new Plotter();
        double mass = FactoryLunarLanding.MASS;

        double[] force =  Arrays.stream(input)
                .map(i -> i + mass * G).toArray();
        System.out.println("force = " + Arrays.toString(force));
        plotter.addPlot(input, "Control", BOUNDS);
        plotter.addPlot(force, "Force", Pair.create(mass*G-FORCE,mass*G+FORCE));
        plotter.addPlot(speeds.toArray(), "Speed",Pair.create(-5d, 5d));
        plotter.addPlot(positions.toArray(), "Position",Pair.create(0d, START_POS));
        plotter.show();
    }


}
