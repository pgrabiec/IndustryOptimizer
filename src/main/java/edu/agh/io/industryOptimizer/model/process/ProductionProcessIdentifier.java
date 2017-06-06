package edu.agh.io.industryOptimizer.model.process;

import edu.agh.io.industryOptimizer.model.DefaultIdentifier;
import org.bson.types.ObjectId;

public class ProductionProcessIdentifier extends DefaultIdentifier {
    public ProductionProcessIdentifier(ObjectId id) {
        super(id);
    }
}
