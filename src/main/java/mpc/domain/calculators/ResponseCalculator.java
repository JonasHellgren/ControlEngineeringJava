package mpc.domain.calculators;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import mpc.domain.value_objects.MPCModelData;
import mpc.domain.value_objects.MpcMatrices;
import org.apache.commons.math3.linear.RealVector;

@AllArgsConstructor
public class ResponseCalculator {

    MPCModelData model;
    MpcMatrices matrices;

    public RealVector response(RealVector x, RealVector u) {
        Preconditions.checkArgument(x.getDimension() == model.nStates());
        Preconditions.checkArgument(u.getDimension() == model.horizon());

        return matrices.S().operate(x).add(matrices.T().operate(u));
    }



}
