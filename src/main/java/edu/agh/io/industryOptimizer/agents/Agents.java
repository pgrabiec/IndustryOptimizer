package edu.agh.io.industryOptimizer.agents;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Iterator;

public class Agents {
    public static AgentType agentType(DFAgentDescription description) {
        Iterator iterator = description.getAllServices();

        if (iterator.hasNext()) {
            try {
                ServiceDescription service = (ServiceDescription) iterator.next();

                return AgentType.valueOf(service.getName());

            } catch (ClassCastException ignored) {
            }
        }

        return null;
    }
}
