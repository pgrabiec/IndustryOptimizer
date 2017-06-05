package edu.agh.io.industryOptimizer.model.batch;

import edu.agh.io.industryOptimizer.model.data.Data;

import java.io.Serializable;
import java.util.List;

public interface Batch extends Serializable {
    public BatchIdentifier id();
    public String name();
    public String type();

    public Data quantity();

    public List<Data> properties();
}
