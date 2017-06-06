package edu.agh.io.industryOptimizer.model.batch;

import edu.agh.io.industryOptimizer.model.DefaultIdentifier;
import org.bson.types.ObjectId;

public class BatchIdentifier extends DefaultIdentifier {
    public BatchIdentifier(ObjectId id) {
        super(id);
    }
}
