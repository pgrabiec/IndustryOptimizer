package edu.agh.io.industryOptimizer.agents;

public enum AgentType {
    INTERFACE,
    PROCESS,
    BATCH,
    BATCH_INPUT,    // for the process input batch
    BATCH_OUTPUT,   // for the process output batch
    PERSISTENCE,
    OPTIMIZATION,
    ANALYSIS,
    REASONING,
    QUERYING
}
