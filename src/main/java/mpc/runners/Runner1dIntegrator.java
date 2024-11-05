package mpc.runners;

import lombok.SneakyThrows;
import mpc.domain.calculators.ResponseCalculator;
import mpc.domain.controller.MpcController;
import mpc.domain.creators.MpcMatrixCreator;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.StatePresentAndReference;
import mpc.helpers.Plotter;
import mpc.problems.one_dim_integrator.FactoryOneDimIntegrator;
import mpc.problems.one_dim_integrator.ModelQP1dIntegrator;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createOnesVector;
import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.createZeroVector;

public class Runner1dIntegrator {

    public static final double CONTROL_PENALTY = 1d;
    public static final int HORIZON = 10;
    public static final Pair<Double, Double> BOUNDS = Pair.create(0d, 0.2d);

    @SneakyThrows
    public static void main(String[] args) {
        var model = FactoryOneDimIntegrator.createModelData(HORIZON)
                .withControlPenalty(new double[]{CONTROL_PENALTY});
        var mpcMatrices = MpcMatrixCreator.of(model).createMatrices();
        var modelQP = ModelQP1dIntegrator.builder()
                .modelData(model)
                .matrices(mpcMatrices)
                .bounds(BOUNDS)
                .build();
        var controller = MpcController.of(model, modelQP);

        var startAndRefState = getStateAndRefOne(model);
        var input = controller.calculateInputSignal(startAndRefState);
        var calculator = ResponseCalculator.of(model, mpcMatrices);
        var response = calculator.response(startAndRefState.x(), MatrixUtils.createRealVector(input));

        var positions = model.getStateValues(response, 0);
        System.out.println("positions = " + positions);

        var plotter=new Plotter();
        plotter.addPlot(input, "Input",Pair.create(-1d,1d));
        plotter.addPlot(positions.toArray(), "Position",Pair.create(-1d,1d));
        plotter.show();
    }

    static StatePresentAndReference getStateAndRefOne(MpcModelData model) {
        RealVector stateStart = createZeroVector(model.nStates());
        return StatePresentAndReference.of(stateStart, createOnesVector(model.nStates()));
    }


}
