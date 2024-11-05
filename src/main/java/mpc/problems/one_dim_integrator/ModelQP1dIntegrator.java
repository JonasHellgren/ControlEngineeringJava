package mpc.problems.one_dim_integrator;

import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import mpc.domain.controller.ModelQPHelper;
import mpc.domain.controller.ModelQPI;
import mpc.domain.creators.MpcVectorFCreator;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.StatePresentAndReference;
import org.apache.commons.math3.util.Pair;
import org.hellgren.utilities.joptimizer.UpperBoundConstraint;
import org.nd4j.shade.guava.base.Preconditions;

/**
 * * min   x'Qx+kx
 */

@NonNull
public class ModelQP1dIntegrator implements ModelQPI {

    ModelQPHelper helper;
    @Setter
    private Pair<Double, Double> controlBounds;

    @Builder
    public ModelQP1dIntegrator(MpcModelData modelData, MpcMatrices matrices, Pair<Double, Double> bounds) {
        this.helper = new ModelQPHelper(modelData, matrices);
        this.controlBounds = bounds;
    }

    public static ModelQP1dIntegrator of(MpcModelData modelData, MpcMatrices matrices,Pair<Double, Double> bounds) {
        return ModelQP1dIntegrator.builder()
                .modelData(modelData)
                .matrices(matrices)
                .bounds(bounds)
                .build();
    }

    public ConvexMultivariateRealFunction costFunction(StatePresentAndReference statePresentAndReference) {
        return helper.getCostFunction(statePresentAndReference);
    }

    /**
     * Returns an array of constraints for the quadratic programming problem.
     * The array includes the zero power high soc constraints, the bounds constraints, and the power total constraints.
     *
     * @return an array of constraints as ConvexMultivariateRealFunction objects
     */
    public ConvexMultivariateRealFunction[] constraints() {
        var modelData = helper.getModelData();
        var inequalities = new ConvexMultivariateRealFunction[modelData.horizon()];

        for (int i = 0; i < modelData.horizon(); i++) {
            inequalities[i] = UpperBoundConstraint.builder()
                    .nDim(modelData.horizon()).variableIndex(i).upperBound(controlBounds.getSecond()).build();
        }
        return inequalities;

    }

}

