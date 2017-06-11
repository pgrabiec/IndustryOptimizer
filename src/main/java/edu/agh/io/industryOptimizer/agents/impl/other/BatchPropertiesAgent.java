package edu.agh.io.industryOptimizer.agents.impl.other;

import edu.agh.io.industryOptimizer.agents.AbstractStatelessAgent;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.messaging.messages.DocumentMessage;
import edu.agh.io.industryOptimizer.messaging.messages.MessageType;
import edu.agh.io.industryOptimizer.messaging.util.CallbacksUtility;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;

public class BatchPropertiesAgent extends AbstractStatelessAgent {
    private static final Logger log = Logger.getLogger(BatchPropertiesAgent.class.getName());

    @Override
    protected void setupCallbacksStateless(CallbacksUtility utility) {
    }

    @Override
    protected void onStart() {
        addBehaviour(new QualityBehaviour());
    }

    public class QualityBehaviour extends OneShotBehaviour {
        public void action() {
            log.debug("Listing agents");

            AID[] agents = listAgents(AgentType.PROCESS.toString());

            if (agents == null) {
                log.debug("Null agents");
                return;
            }
            if (agents.length < 1) {
                log.debug("Zero agents");
                return;
            }

            log.debug(Arrays.toString(agents));
//            String aid = new StringImpl(agents[0].getLocalName());
//            Document document = new Document("quality", "9");
//            sendProperties(aid, document);

        }
    }

    private void sendProperties(String aid, Document document) {
        try {
            sendMessage(aid,
                    new DocumentMessage(
                            MessageType.BATCH_PROPERTIES,
                            getMyId(),
                            document
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AID[] listAgents(String type) {
        AID[] agents = searchDF(type);

        if (agents == null) {
            log.debug("Null service response");
            return null;
        }

        return agents;
    }

    private AID[] searchDF(String service) {
        ServiceDescription sd = new ServiceDescription();
        sd.setName(service);

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);

        SearchConstraints all = new SearchConstraints();

        try {
            DFAgentDescription[] result = DFService.search(this, dfd, all);
            AID[] agents = new AID[result.length];

            int i;
            for (i = 0; i < result.length; i++)
                agents[i] = result[i].getName();
            return agents;

        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        return null;
    }

    @Override
    protected final AgentType agentType() {
        return AgentType.OTHER;
    }
}
