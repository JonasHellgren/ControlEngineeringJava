package mpc.helpers;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class RealVectorUtils {

    private RealVectorUtils() {
    }

    public static RealVector extractStateValues(int startIndex, int numStates, int numElements, RealVector vector) {
        Preconditions.checkArgument((startIndex >= 0) && (numStates > 0) && (numElements > 0),"startIndex, numStates and numElements must be positive");
        Preconditions.checkArgument(startIndex + numStates * numElements <= vector.getDimension(),"startIndex + numStates * numElements must be smaller than vector.getDimension()");
        RealVector extractedVector = new ArrayRealVector(numElements);
        for (int i = 0; i < numElements; i++) {
            int index = startIndex + i * numStates;
            extractedVector.setEntry(i, vector.getEntry(index));
        }
        return extractedVector;
    }


    public static boolean areVectorsEqual(RealVector v1, RealVector v2, double tolerance) {
        return v1.subtract(v2).getNorm() < tolerance;
    }


    public static boolean areElementsDecreasing(RealVector vector) {
        for (int i = 1; i < vector.getDimension(); i++) {
            if (vector.getEntry(i) >= vector.getEntry(i - 1)) {
                return false;
            }
        }
        return true;
    }

}
