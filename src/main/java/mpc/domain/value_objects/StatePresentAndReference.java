package mpc.domain.value_objects;

import org.apache.commons.math3.linear.RealVector;


/**
 * Represents a state present and reference in the MPC domain.
 * This class is a simple data container for two RealVectors, x and xRef.
 */
public record StatePresentAndReference(RealVector x, RealVector xRef) {
    public static StatePresentAndReference of(RealVector x, RealVector xRef) {
        return new StatePresentAndReference(x, xRef);
    }
}
