package mpc.one_dim_integrator;

import helpers.MatrixStacking;
import mpc.domain.ModelQPData;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hellgren.utilities.vector_algebra.MyMatrixUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Test1dIntegratorModelData {


    ModelQPData model;
    RealMatrix a;
    RealVector b;

    @BeforeEach
    void init() {
        model = MatrixDataFactoryOneDimIntegrator.createModelData();
        a = ModelQPData.getRealMatrix(model.matrixA());
        b = ModelQPData.getRealVector(model.vectorB());
    }

    @Test
    void model_thenIsOk() {
        Assertions.assertTrue(model.isOk());
    }

    @Test
    void givenModel_whenHorizon_then2() {
        assertEquals(2, model.horizon());
    }

    @Test
    void givenModel_whenNStates_then1() {
        assertEquals(1, model.nStates());
    }

    @Test
    void whenMatrixS_thenCorrect() {
        var aStacked = MatrixStacking.stackVertically(List.of(a, a));
        assertEquals(aStacked, model.matrixS());
    }

    @Test
    void whenMatrixT_thenCorrect() {
        RealVector zeroVector = MyMatrixUtils.createZeroVector(model.nStates());
        var row1 = MyMatrixUtils.stackVectorsHorizontally(List.of(b, zeroVector));
        var row2 = MyMatrixUtils.stackVectorsHorizontally(List.of(a.operate(b), b));
        var m = MatrixStacking.stackVertically(List.of(row1, row2));
        System.out.println("m = " + m);
        System.out.println("MyMatrixUtils.properties(m) = " + MyMatrixUtils.properties(m));
        RealMatrix t = model.matrixT();
        System.out.println("t = " + t);
        System.out.println("MyMatrixUtils.properties(t) = " + MyMatrixUtils.properties(t));
        // assertEquals(m, t);
        assertEquals(model.nStates()* model.horizon(), MyMatrixUtils.properties(t).nRows());
        assertEquals( model.horizon(), MyMatrixUtils.properties(t).nColumns());
    }

    @Test
    void whenMatrixH_thenCorrect() {
        var h = model.getMatrixH();
        System.out.println("h = " + h);

    }

    @Test
    void whenMatrixF_thenCorrect() {
    }

}
