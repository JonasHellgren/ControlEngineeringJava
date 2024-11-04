package mpc.domain.value_objects;

import lombok.Builder;
import org.apache.commons.math3.linear.RealMatrix;

@Builder
public record MpcMatrices(
        RealMatrix S,
        RealMatrix T,
        RealMatrix H,
        RealMatrix Q
) {


}
