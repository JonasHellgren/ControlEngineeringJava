package mpc.one_dim_integrator;

import helpers.MatrixStacking;
import mpc.domain.MPCMatrixCalculator;
import mpc.domain.value_objects.MPCModelData;
import mpc.domain.value_objects.MpcMatrixes;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.hellgren.utilities.vector_algebra.MyMatrixUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hellgren.utilities.vector_algebra.MyMatrixUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMPCMatrixCalculator1dIntegrator {

    MPCModelData model;
    MPCMatrixCalculator calculator;
    RealMatrix a;
    RealVector b;
    MpcMatrixes mpcMatrixes;

    @BeforeEach
    void init() {
        model = MatrixDataFactoryOneDimIntegrator.createModelData(2);
        calculator= new MPCMatrixCalculator(model);
        mpcMatrixes = calculator.getMpcMatrixes();
        a = MatrixUtils.createRealMatrix(model.matrixA());
        b = MatrixUtils.createRealVector((model.vectorB()));
    }


    @Test
    void whenMatrixS_thenCorrect() {
        var aStacked = MatrixStacking.stackVertically(List.of(a, a));
        assertEquals(aStacked, mpcMatrixes.S());
    }

    @Test
    void whenMatrixT_thenCorrect() {
        RealVector zeroVector = createZeroVector(model.nStates());
        var row1 = MyMatrixUtils.stackVectorsHorizontally(List.of(b, zeroVector));
        var row2 = MyMatrixUtils.stackVectorsHorizontally(List.of(a.operate(b), b));
        var m = MatrixStacking.stackVertically(List.of(row1, row2));
        System.out.println("m = " + m);
        System.out.println("MyMatrixUtils.properties(m) = " + properties(m));
        RealMatrix t = mpcMatrixes.T();
        System.out.println("t = " + t);
        System.out.println("MyMatrixUtils.properties(t) = " + properties(t));
        // assertEquals(m, t);
        assertEquals(model.nStates()* model.horizon(), properties(t).nRows());
        assertEquals( model.horizon(), properties(t).nColumns());
    }

    @Test
    void whenMatrixH_thenCorrect() {
        var h = mpcMatrixes.H();
        assertEquals( model.horizon(), properties(h).nRows());
        assertEquals( model.horizon(), properties(h).nColumns());
    }

    @Test
    void whenMatrixQ_thenCorrect() {
        var q = mpcMatrixes.Q();
        assertEquals( model.horizon()* model.nStates(), properties(q).nRows());
        assertEquals( model.horizon()* model.nStates(), properties(q).nColumns());
    }


    @Test
    void whenMatrixF_thenCorrect() {
        var f=calculator.vectorFSameXrefEveryStep(createZeroVector(1),createOnesVector(1));
        assertEquals( model.horizon(), properties(f).nRows());
        assertEquals( 1, properties(f).nColumns());
    }

}
