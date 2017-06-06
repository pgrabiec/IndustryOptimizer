package edu.agh.io.industryOptimizer.model;

import org.bson.types.ObjectId;

public interface Identifier extends BSONConvertable {
    public ObjectId id();
}
