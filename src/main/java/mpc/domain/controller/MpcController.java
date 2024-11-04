package mpc.domain.controller;

import com.joptimizer.exception.JOptimizerException;
import com.joptimizer.optimizers.JOptimizer;
import com.joptimizer.optimizers.OptimizationRequest;
import lombok.AllArgsConstructor;
import mpc.domain.ModelQP;
import mpc.domain.value_objects.MPCModelData;
import mpc.domain.value_objects.StatePresentAndReference;
import org.hellgren.utilities.list_arrays.ArrayCreator;

@AllArgsConstructor
public class MpcController {

    MPCModelData modelData;
    ModelQP modelQP;

    public double[] calculateInputSignal(StatePresentAndReference stateAndRef) throws JOptimizerException {

        //optimization problem
        var or = new OptimizationRequest();
        or.setF0(modelQP.costFunction(stateAndRef));
        double[] initialPoint = ArrayCreator.createArrayWithSameDoubleNumber(modelData.horizon(), 0.0);
        or.setInitialPoint(initialPoint);
        or.setFi(modelQP.constraints());

        //optimization
        var opt = new JOptimizer();
        opt.setOptimizationRequest(or);
        opt.optimize();
        var response = opt.getOptimizationResponse();
        return response.getSolution();

    }


}
