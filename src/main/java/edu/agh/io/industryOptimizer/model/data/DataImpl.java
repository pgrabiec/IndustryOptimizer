package edu.agh.io.industryOptimizer.model.data;

import org.bson.Document;

public class DataImpl implements Data {
    private final String name;
    private final Document value;

    public DataImpl(String name, Document value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public Document value() {
        return null;
    }

    public String getName() {
        return name;
    }

    public Document getValue() {
        return value;
    }
}
