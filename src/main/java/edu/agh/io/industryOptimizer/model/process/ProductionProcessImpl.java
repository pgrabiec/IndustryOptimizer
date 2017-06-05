package edu.agh.io.industryOptimizer.model.process;

import edu.agh.io.industryOptimizer.model.batch.BatchIdentifier;
import edu.agh.io.industryOptimizer.model.data.Data;

import java.util.LinkedList;
import java.util.List;

public class ProductionProcessImpl implements ProductionProcess {
    private final ProductionProcessIdentifier id;
    private final String name;
    private final String type;

    private final List<Data> controlParameters = new LinkedList<>();
    private final List<Data> outputParameters = new LinkedList<>();

    private final List<BatchIdentifier> inputBatches = new LinkedList<>();
    private final List<Data> otherInput = new LinkedList<>();

    private final List<BatchIdentifier> outputBatches = new LinkedList<>();
    private final List<Data> otherOutput = new LinkedList<>();


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
    public List<Data> controlParameters() {
        return controlParameters;
    }

    @Override
    public List<Data> outputParameters() {
        return outputParameters;
    }

    @Override
    public List<BatchIdentifier> inputBatches() {
        return inputBatches;
    }

    @Override
    public List<Data> otherInput() {
        return otherInput;
    }

    @Override
    public List<BatchIdentifier> outputBatches() {
        return outputBatches;
    }

    @Override
    public List<Data> otherOutput() {
        return otherOutput;
    }
}
