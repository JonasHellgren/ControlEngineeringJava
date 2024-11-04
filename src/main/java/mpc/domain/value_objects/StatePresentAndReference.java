package mpc.domain.value_objects;

import org.apache.commons.math3.linear.RealVector;


public record StatePresentAndReference(RealVector x, RealVector xRef) {
    public static StatePresentAndReference of(RealVector x, RealVector xRef) {
        return new StatePresentAndReference(x, xRef);
    }
}
