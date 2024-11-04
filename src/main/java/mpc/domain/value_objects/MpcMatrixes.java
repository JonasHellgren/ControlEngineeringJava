package mpc.domain.value_objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.math3.linear.RealMatrix;

@Builder
public record MpcMatrixes(
        RealMatrix S,
        RealMatrix T,
        RealMatrix H,
        RealMatrix Q
) {


}
