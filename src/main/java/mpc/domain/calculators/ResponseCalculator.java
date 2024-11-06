package mpc.domain.calculators;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.MpcMatrices;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

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

    public RealVector response(RealVector x, RealVector u) {
        Preconditions.checkArgument(x.getDimension() == model.nStates());
        Preconditions.checkArgument(u.getDimension() == model.horizon());

        return matrices.S().operate(x).add(matrices.T().operate(u));
    }



}
