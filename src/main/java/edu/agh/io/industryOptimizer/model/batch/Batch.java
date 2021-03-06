package edu.agh.io.industryOptimizer.model.batch;

import edu.agh.io.industryOptimizer.model.Identifier;
import org.bson.Document;

import java.io.Serializable;
import java.util.List;

public interface Batch extends Serializable {
    public Identifier id();
    public String name();
    public String type();

    public Document quantity();

    public List<Document> properties();
}
