package edu.agh.io.industryOptimizer.model.process;

import edu.agh.io.industryOptimizer.model.BSONConvertable;
import edu.agh.io.industryOptimizer.model.Identifier;
import org.bson.Document;

import java.io.Serializable;
import java.util.List;

public interface ProductionProcess extends Serializable, BSONConvertable {
    public ProductionProcessIdentifier id();
    public String name();
    public String type();

    public List<Document> controlParameters();
    public List<Document> outputParameters();

    public List<Identifier> inputBatches();
    public List<Document> otherInput();

    public List<Identifier> outputBatches();
    public List<Document> otherOutput();
}
