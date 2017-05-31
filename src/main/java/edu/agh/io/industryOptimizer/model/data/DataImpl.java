package edu.agh.io.industryOptimizer.model.data;

import edu.agh.io.industryOptimizer.model.units.Unit;

public class DataImpl implements Data {
    private final String parameterName;
    private final DataValue value;
    private final Unit unit;

    public DataImpl(String parameterName, DataValue value, Unit unit) {
        this.parameterName = parameterName;
        this.value = value;
        this.unit = unit;
    }

    @Override
    public DataValue getValue() {
        return value;
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

    @Override
    public Unit getUnit() {
        return unit;
    }
}
