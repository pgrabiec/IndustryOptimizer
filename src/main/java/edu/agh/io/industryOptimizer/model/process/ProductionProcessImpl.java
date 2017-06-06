package edu.agh.io.industryOptimizer.model.process;

import edu.agh.io.industryOptimizer.model.batch.BatchIdentifier;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;

public class ProductionProcessImpl implements ProductionProcess {
    private final ProductionProcessIdentifier id;
    private final String name;
    private final String type;

    private final List<Document> controlParameters = new LinkedList<>();
    private final List<Document> outputParameters = new LinkedList<>();

    private final List<BatchIdentifier> inputBatches = new LinkedList<>();
    private final List<Document> otherInput = new LinkedList<>();

    private final List<BatchIdentifier> outputBatches = new LinkedList<>();
    private final List<Document> otherOutput = new LinkedList<>();


    public ProductionProcessImpl(ProductionProcessIdentifier identifier, String name, String type) {
        this.id = identifier;
        this.name = name;
        this.type = type;
    }

    @Override
    public ProductionProcessIdentifier id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public List<Document> controlParameters() {
        return controlParameters;
    }

    @Override
    public List<Document> outputParameters() {
        return outputParameters;
    }

    @Override
    public List<BatchIdentifier> inputBatches() {
        return inputBatches;
    }

    @Override
    public List<Document> otherInput() {
        return otherInput;
    }

    @Override
    public List<BatchIdentifier> outputBatches() {
        return outputBatches;
    }

    @Override
    public List<Document> otherOutput() {
        return otherOutput;
    }
}
