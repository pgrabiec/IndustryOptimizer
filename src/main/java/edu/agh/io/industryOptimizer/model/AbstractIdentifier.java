package edu.agh.io.industryOptimizer.model;

import org.bson.types.ObjectId;

public abstract class AbstractIdentifier implements Identifier {
    private final ObjectId id;

    public AbstractIdentifier(ObjectId id) {
        this.id = id;
    }

    @Override
    public ObjectId id() {
        return id;
    }
}
