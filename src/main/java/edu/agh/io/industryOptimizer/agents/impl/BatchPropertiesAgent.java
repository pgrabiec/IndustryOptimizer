package edu.agh.io.industryOptimizer.agents.impl;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.agents.AgentIdentifier;
import edu.agh.io.industryOptimizer.agents.AgentIdentifierImpl;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.bson.Document;

import java.io.IOException;

/**
 * Created by Tomasz on 06.06.2017.
 */
public class BatchPropertiesAgent extends AbstractAgent {
    @Override
    protected void setupImpl(CallbacksUtility utility) {
        addBehaviour(new QualityBehaviour());
    }



    public class QualityBehaviour extends OneShotBehaviour {
        public void action() {

            AID[] agents = listAgents("batch");
            AgentIdentifier aid = new AgentIdentifierImpl(agents[0].getLocalName());
            Document document = new Document("quality", "9");
            sendProperties(aid, document);

        }
    }

    private void sendProperties(AgentIdentifier aid, Document document){
        try {
            sendMessage(aid,
                new DocumentMessage(
                    DocumentMessage.MessageType.BATCH_PROPERTIES,
                    getMyId(),
                    document
                )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AID[] listAgents(String type){
        AID[] agents = searchDF(type);

        for (int i = 0; i < agents.length; i++)
            System.out.println(i+1 + ". " + agents[i].getLocalName());
        return agents;
    }

    private AID[] searchDF(String service )
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(service);
        dfd.addServices(sd);

        SearchConstraints ALL = new SearchConstraints();
        ALL.setMaxResults(new Long(-1));

        try
        {
            DFAgentDescription[] result = DFService.search(this, dfd, ALL);
            AID[] agents = new AID[result.length];
            int i;
            for (i=0; i<result.length; i++)
                agents[i] = result[i].getName();
            return agents;

        }
        catch (FIPAException fe) { fe.printStackTrace(); }

        return null;
    }
}
