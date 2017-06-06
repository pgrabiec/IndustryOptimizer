package edu.agh.io.industryOptimizer.model.process;

import edu.agh.io.industryOptimizer.model.Identifier;
import org.bson.Document;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;

public class ProductionProcessImpl implements ProductionProcess {
    private final ProductionProcessIdentifier id;
    private final String name;
    private final String type;

    private final List<Document> controlParameters = new LinkedList<>();
    private final List<Document> outputParameters = new LinkedList<>();

    private final List<Identifier> inputBatches = new LinkedList<>();
    private final List<Document> otherInput = new LinkedList<>();

    private final List<Identifier> outputBatches = new LinkedList<>();
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
    public List<Identifier> inputBatches() {
        return inputBatches;
    }

    @Override
    public List<Document> otherInput() {
        return otherInput;
    }

    @Override
    public List<Identifier> outputBatches() {
        return outputBatches;
    }

    @Override
    public List<Document> otherOutput() {
        return otherOutput;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();

        document.put("_id", id.toString());
        document.put("name", name);
        document.put("type", type);

        document.put("control_parameters", controlParameters);
        document.put("output_parameters", name);

        document.put("input_batches", name);
        document.put("input_other", name);

        document.put("output_batches", name);
        document.put("output_other", name);

        return document;
    }

    public static ProductionProcess fromDocument() {
        // TODO - implement
        throw new NotImplementedException();
    }
}
