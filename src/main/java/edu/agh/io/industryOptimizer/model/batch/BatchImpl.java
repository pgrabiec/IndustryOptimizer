package edu.agh.io.industryOptimizer.model.batch;


import edu.agh.io.industryOptimizer.model.Identifier;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;

public class BatchImpl implements Batch {
    private final Identifier id;
    private final String name;
    private final String type;

    private Document quantity;

    private final List<Document> properties = new LinkedList<>();

    public BatchImpl(Identifier id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public BatchImpl(BatchIdentifier id, String name, String type, Document quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

    @Override
    public Identifier id() {
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
    public Document quantity() {
        return quantity;
    }

    @Override
    public List<Document> properties() {
        return properties;
    }

    public void setQuantity(Document quantity) {
        this.quantity = quantity;
    }
}
