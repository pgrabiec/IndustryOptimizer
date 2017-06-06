package edu.agh.io.industryOptimizer.model.process;

import edu.agh.io.industryOptimizer.model.batch.BatchIdentifier;
import org.bson.Document;

import java.io.Serializable;
import java.util.List;

public interface ProductionProcess extends Serializable {
    public ProductionProcessIdentifier id();
    public String name();
    public String type();

    public List<Document> controlParameters();
    public List<Document> outputParameters();

    public List<BatchIdentifier> inputBatches();
    public List<Document> otherInput();

    public List<BatchIdentifier> outputBatches();
    public List<Document> otherOutput();
}
