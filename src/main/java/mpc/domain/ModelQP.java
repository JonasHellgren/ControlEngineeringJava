package mpc.domain;

import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import lombok.Builder;
import lombok.NonNull;
import lombok.With;
import mpc.domain.creators.MpcVectorFCreator;
import mpc.domain.value_objects.MPCModelData;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.StatePresentAndReference;
import org.hellgren.utilities.joptimizer.UpperBoundConstraint;
import org.nd4j.shade.guava.base.Preconditions;

/**
 * * min   x'Qx+kx
 */

@Builder
@NonNull
public class ModelQP {

    MPCModelData modelData;
    MpcMatrices matrices;
    MpcVectorFCreator vectorFCreator;
    @With private double upperBound;

    public ConvexMultivariateRealFunction costFunction(StatePresentAndReference statePresentAndReference) {
        Preconditions.checkArgument(modelData.isOk(), "modelData not ok");
        double[][] H = matrices.H().getData();
        double[] f = vectorFCreator.vectorFSameXrefEveryStep(statePresentAndReference).toArray();
        return new PDQuadraticMultivariateRealFunction(H, f, 0);
    }

    /**
     * Returns an array of constraints for the quadratic programming problem.
     * The array includes the zero power high soc constraints, the bounds constraints, and the power total constraints.
     *
     * @return an array of constraints as ConvexMultivariateRealFunction objects
     */
    public ConvexMultivariateRealFunction[] constraints() {
        var inequalities = new ConvexMultivariateRealFunction[modelData.horizon()];

        for (int i = 0; i < modelData.horizon(); i++) {
            inequalities[i] = UpperBoundConstraint.builder()
                    .nDim(modelData.horizon()).variableIndex(i).upperBound(upperBound).build();
        }
        return inequalities;

    }

}

