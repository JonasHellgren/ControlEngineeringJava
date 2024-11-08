package mpc.domain.controller;

import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mpc.domain.creators.MpcVectorFCreator;
import mpc.domain.value_objects.MpcMatrices;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.StatePresentAndReference;
import org.nd4j.shade.guava.base.Preconditions;

/**
 * Helper class for Model Predictive Control (MPC) that provides methods for calculating the cost function.
 */
@AllArgsConstructor
@Getter
public class ModelQPHelper {

    MpcModelData modelData;
    MpcMatrices matrices;
    MpcVectorFCreator vectorFCreator;

    public ModelQPHelper(MpcModelData modelData, MpcMatrices matrices) {
        Preconditions.checkArgument(modelData.isValid(), "modelData not ok");
        this.modelData = modelData;
        this.matrices = matrices;
        this.vectorFCreator=new MpcVectorFCreator(modelData, matrices);
    }

    public ConvexMultivariateRealFunction getCostFunction(StatePresentAndReference statePresentAndReference) {
        double[][] h = matrices.hessian().getData();
        double[] f = vectorFCreator.vectorFSameXrefEveryStep(statePresentAndReference).toArray();
        return new PDQuadraticMultivariateRealFunction(h, f, 0);
    }



}
