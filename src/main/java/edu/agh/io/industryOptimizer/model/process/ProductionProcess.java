package edu.agh.io.industryOptimizer.model.process;

import edu.agh.io.industryOptimizer.model.batch.BatchIdentifier;
import edu.agh.io.industryOptimizer.model.data.Data;

import java.io.Serializable;
import java.util.List;

public interface ProductionProcess extends Serializable {
    public ProductionProcessIdentifier id();
    public String name();
    public String type();

    public List<Data> controlParameters();
    public List<Data> outputParameters();

    public List<BatchIdentifier> inputBatches();
    public List<Data> otherInput();

    public List<BatchIdentifier> outputBatches();
    public List<Data> otherOutput();
}
