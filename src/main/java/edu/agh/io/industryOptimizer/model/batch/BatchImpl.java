package edu.agh.io.industryOptimizer.model.batch;

import edu.agh.io.industryOptimizer.model.data.Data;

import java.util.LinkedList;
import java.util.List;

public class BatchImpl implements Batch {
    private final BatchIdentifier id;
    private final String name;
    private final String type;

    private Data quantity;

    private final List<Data> properties = new LinkedList<>();

    public BatchImpl(BatchIdentifier id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public BatchImpl(BatchIdentifier id, String name, String type, Data quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

    @Override
    public BatchIdentifier id() {
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
    public Data quantity() {
        return quantity;
    }

    @Override
    public List<Data> properties() {
        return properties;
    }

    public void setQuantity(Data quantity) {
        this.quantity = quantity;
    }
}