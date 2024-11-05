package mpc.domain.controller;

import com.joptimizer.exception.JOptimizerException;
import com.joptimizer.optimizers.JOptimizer;
import com.joptimizer.optimizers.OptimizationRequest;
import lombok.NonNull;
import mpc.domain.value_objects.MpcModelData;
import mpc.domain.value_objects.StatePresentAndReference;
import org.hellgren.utilities.list_arrays.ArrayCreator;

public class MpcController {

    MpcModelData modelData;
    ModelQPI modelQP;
    OptimizationRequest request;
    JOptimizer optimizer;

    public MpcController(@NonNull MpcModelData modelData, @NonNull ModelQPI modelQP) {
        this.modelData = modelData;
        this.modelQP = modelQP;
        this.request=new OptimizationRequest();
        this.optimizer = new JOptimizer();
    }

    public static MpcController of(MpcModelData modelData, ModelQPI modelQP) {
        return new MpcController(modelData, modelQP);
    }

    public double[] calculateInputSignal(StatePresentAndReference stateAndRef) throws JOptimizerException {
        setRequest(stateAndRef);
        return getOptimizationResult();
    }

    private void setRequest(StatePresentAndReference stateAndRef) {
        request.setF0(modelQP.costFunction(stateAndRef));
        double[] initialPoint = ArrayCreator.createArrayWithSameDoubleNumber(modelData.horizon(), 0.0);
        request.setInitialPoint(initialPoint);
        request.setFi(modelQP.constraints());
    }

    private double[] getOptimizationResult() throws JOptimizerException {
        optimizer.setOptimizationRequest(request);
        optimizer.optimize();
        var response = optimizer.getOptimizationResponse();
        return response.getSolution();
    }


}
