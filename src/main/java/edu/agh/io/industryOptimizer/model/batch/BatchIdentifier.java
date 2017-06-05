package edu.agh.io.industryOptimizer.model.batch;

import edu.agh.io.industryOptimizer.model.AbstractIdentifier;
import org.bson.types.ObjectId;

public class BatchIdentifier extends AbstractIdentifier {
    public BatchIdentifier(ObjectId id) {
        super(id);
    }
}
