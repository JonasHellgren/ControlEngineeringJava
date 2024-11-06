package mpc.domain.calculators;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.MpcMatrices;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

/**
 * A calculator for computing the response of a system to a given input.
 */
@AllArgsConstructor
public class ResponseCalculator {

    MpcModelData model;
    MpcMatrices matrices;

    public static ResponseCalculator of(MpcModelData model, MpcMatrices matrices) {
        return new ResponseCalculator(model, matrices);
    }


    public RealVector response(RealVector x, double[] u) {
        return response(x, MatrixUtils.createRealVector(u));
    }


    /**
     * Computes the response of the system to the given input.
     *
     * @param x the current state of the system
     * @param u the input to the system
     * @return the response of the system
     */
    public RealVector response(RealVector x, RealVector u) {
        Preconditions.checkArgument(x.getDimension() == model.nStates());
        Preconditions.checkArgument(u.getDimension() == model.horizon());

        return matrices.stateImpact().operate(x).add(matrices.controlAffect().operate(u));
    }

}
