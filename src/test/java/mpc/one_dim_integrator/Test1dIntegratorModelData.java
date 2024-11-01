package mpc.one_dim_integrator;

import helpers.MatrixStacking;
import mpc.domain.ModelQPData;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
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
        model=MatrixDataFactoryOneDimIntegrator.createModelData();
        a=ModelQPData.getRealMatrix(model.matrixA());
        b=ModelQPData.getRealVector(model.vectorB());

      }

       @Test
        void givenModel_whenHorizon_then2t() {
            assertEquals(2,model.horizon());
        }

       @Test
        void whenMatrixS_thenCorrect() {
           var aStacked=MatrixStacking.stackVertically(List.of(a,a));
           assertEquals(aStacked,model.matrixS());
        }

     @Test
      void whenMatrixT_thenCorrect() {
         RealVector zeroVector = MyMatrixUtils.createZeroVector(model.nStates());
         var row1=MyMatrixUtils.stackVectorsHorizontally(List.of(b, zeroVector));
         var row2=MyMatrixUtils.stackVectorsHorizontally(List.of(a.operate(b),b));
         var m=MatrixStacking.stackVertically(List.of(row1,row2));
         assertEquals(m,model.matrixT());
      }

}
