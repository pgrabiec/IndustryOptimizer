package edu.agh.io.industryOptimizer.model.data;

public class DataValueImpl implements DataValue {
    private final String value;

    public DataValueImpl(String value) {
        this.value = value;
    }

    @Override
    public String getValueString() {
        return value;
    }
}
