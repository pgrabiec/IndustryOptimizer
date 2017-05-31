package edu.agh.io.industryOptimizer.model.batch;

public class BatchIdentifierImpl implements BatchIdentifier {
    private final String id;

    public BatchIdentifierImpl(String id) {
        this.id = id;
    }

    @Override
    public String getIdentifier() {
        return id;
    }
}
