package edu.agh.io.industryOptimizer.agents;

public class AgentIdentifierImpl implements AgentIdentifier {
    private final String id;

    public AgentIdentifierImpl(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }
}
