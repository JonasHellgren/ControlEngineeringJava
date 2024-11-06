package mpc.domain.value_objects;

import lombok.Builder;
import org.apache.commons.math3.linear.RealMatrix;

/**
 /**
 * Represents the matrices used in the Model Predictive Control (MPC) algorithm.
 * See mpc.md documentation for details on the MPC algorithm and the role of these matrices.
 */

@Builder
public record MpcMatrices(
        RealMatrix stateImpact,  //S
        RealMatrix controlAffect, //T
        RealMatrix hessian, //H
        RealMatrix trackingPenalty  //Q
) {


}
