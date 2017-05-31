package edu.agh.io.industryOptimizer.model.data;

import edu.agh.io.industryOptimizer.model.units.Unit;

public interface Data {
    public String getParameterName();
    public DataValue getValue();
    public Unit getUnit();
}
