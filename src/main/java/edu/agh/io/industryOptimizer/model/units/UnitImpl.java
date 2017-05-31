package edu.agh.io.industryOptimizer.model.units;

public class UnitImpl implements Unit {
    public static final Unit KILOGRAM = new UnitImpl("kilograms");
    public static final Unit METER = new UnitImpl("meters");
    public static final Unit NUMBER = new UnitImpl("number");

    private final String name;

    public UnitImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
