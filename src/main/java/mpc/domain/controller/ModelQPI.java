package mpc.domain.controller;

import com.joptimizer.functions.ConvexMultivariateRealFunction;
import mpc.domain.value_objects.StatePresentAndReference;
import org.apache.commons.math3.util.Pair;

public interface ModelQPI {

    /**
     * Returns the cost function for the quadratic programming problem.
     *
     * @param statePresentAndReference the current state and reference
     * @return the cost function as a ConvexMultivariateRealFunction object
     */
    ConvexMultivariateRealFunction costFunction(StatePresentAndReference statePresentAndReference);

    /**
     * Returns an array of constraints for the quadratic programming problem.
     *
     * @return an array of constraints as ConvexMultivariateRealFunction objects
     */
    ConvexMultivariateRealFunction[] constraints();

    /**
     * Sets the upper bound for the constraints.
     *
     * @param bounds the upper bound value
     */
    void setControlBounds(Pair<Double, Double> bounds);

}
