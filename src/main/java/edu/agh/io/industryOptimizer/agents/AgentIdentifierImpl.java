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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgentIdentifierImpl that = (AgentIdentifierImpl) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AgentIdentifierImpl{" +
                "id='" + id + '\'' +
                '}';
    }
}
