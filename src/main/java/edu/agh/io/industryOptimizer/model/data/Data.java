package edu.agh.io.industryOptimizer.model.data;

import org.bson.Document;

import java.io.Serializable;

public interface Data extends Serializable {
    public String name();
    public Document value();
}
