package mpc.domain;

import com.joptimizer.functions.ConvexMultivariateRealFunction;
import mpc.domain.value_objects.MPCModelData;

/**
 *  * min   x'Qx+kx
 */

public class ModelQP {

    MPCModelData modelData;

/*
    public ConvexMultivariateRealFunction costFunction() {
        Preconditions.checkArgument(modelData.isOk(), "modelData not ok");
        return new PDQuadraticMultivariateRealFunction(
                //modelData.getMatrixH(), modelData.getArrayF(), 0);
               // modelData.getArrayF(), modelData.getArrayF(), 0);
    }
*/

    /**
     * Returns an array of constraints for the quadratic programming problem.
     * The array includes the zero power high soc constraints, the bounds constraints, and the power total constraints.
     * @return an array of constraints as ConvexMultivariateRealFunction objects
     */
    public ConvexMultivariateRealFunction[] constraints() {
        return new ConvexMultivariateRealFunction[0];
    }


}
