package edu.agh.io.industryOptimizer.agents.impl.other;

import edu.agh.io.industryOptimizer.agents.AbstractAgent;
import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.agents.Agents;
import edu.agh.io.industryOptimizer.gui.linking.LinkingView;
import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigEntry;
import edu.agh.io.industryOptimizer.messaging.messages.LinkConfigMessage;
import edu.agh.io.industryOptimizer.messaging.messages.MessageType;
import edu.agh.io.industryOptimizer.messaging.messages.OperationType;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class LinkingAgent extends AbstractAgent {
    private static final Logger log = Logger.getLogger(LinkingAgent.class.getName());

    private final LinkingView view = new LinkingView();

    @Override
    protected void onStart() {
        view.getUpdateButton().addActionListener(e -> addBehaviour(new UpdateAgentsBehaviour()));

        view.getLinkButton().addActionListener(e -> addBehaviour(new AgentsLinkBehaviour(
                view.getAgentsToLink().getSelectedValuesList(),
                view.getTargetAgents().getSelectedValuesList(),
                OperationType.LINK
        )));

        view.getUnlinkButton().addActionListener(e -> addBehaviour(new AgentsLinkBehaviour(
                view.getAgentsToLink().getSelectedValuesList(),
                view.getTargetAgents().getSelectedValuesList(),
                OperationType.UNLINK
        )));

        addBehaviour(new UpdateAgentsBehaviour());
    }

    @Override
    protected final void executeCallbacks(Message message) {}

    @Override
    protected void setupCallbacks() {}

    private DFAgentDescription[] searchDF(String service) throws FIPAException {
        ServiceDescription sd = new ServiceDescription();
        sd.setName(service);

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);

        SearchConstraints all = new SearchConstraints();
        all.setMaxResults((long) -1);

        return DFService.search(this, dfd, all);
    }

    @Override
    protected AgentType agentType() {
        return AgentType.OTHER;
    }

    private class UpdateAgentsBehaviour extends OneShotBehaviour {
        public void action() {
            log.debug("Listing agents");

            List<DFAgentDescription> agents = new ArrayList<>();

            for (AgentType agentType : AgentType.values()) {

                try {
                    DFAgentDescription[] descriptions = searchDF(agentType.toString());

                    if (descriptions == null) {
                        continue;
                    }

                    if (descriptions.length < 1) {
                        continue;
                    }

                    Collections.addAll(agents, descriptions);

                } catch (FIPAException e) {
                    continue; // Something went wrong - try get all remaining types of agents
                }
            }

            DFAgentDescription[] allAgents = agents.toArray(new DFAgentDescription[0]);

            Arrays.sort(allAgents, Comparator.comparing(o -> o.getName().getName()));

            log.debug("AGENTS FOUND: " + Arrays.toString(allAgents));

            SwingUtilities.invokeLater(() -> {
                ListModel<DFAgentDescription> descriptions = new AbstractListModel<DFAgentDescription>() {
                    @Override
                    public int getSize() {
                        return allAgents.length;
                    }

                    @Override
                    public DFAgentDescription getElementAt(int index) {
                        if (index > allAgents.length) {
                            return null;
                        }
                        return allAgents[index];
                    }
                };


                view.getAgentsToLink().setModel(descriptions);

                view.getTargetAgents().setModel(descriptions);
            });
        }
    }

    private class AgentsLinkBehaviour extends OneShotBehaviour {
        private final Collection<DFAgentDescription> diffAgents;
        private final Collection<DFAgentDescription> targetAgents;
        private final OperationType operation;

        private AgentsLinkBehaviour(
                Collection<DFAgentDescription> toLink,
                Collection<DFAgentDescription> targetAgents,
                OperationType operation) {
            this.diffAgents = toLink;
            this.targetAgents = targetAgents;
            this.operation = operation;
        }

        @Override
        public void action() {
            if (diffAgents.size() < 1 || targetAgents.size() < 1) {
                return;
            }

            Collection<LinkConfigEntry> conf = new ArrayList<>(diffAgents.size());

            diffAgents.forEach(toLink -> {
                AgentType type = Agents.agentType(toLink);

                if (type != null) {
                    conf.add(new LinkConfigEntry(
                            operation,
                            type,
                            toLink.getName().getLocalName()

                    ));
                }
            });

            targetAgents.forEach(target -> {
                try {
                    sendMessage(
                            target.getName().getLocalName(),
                            new LinkConfigMessage(
                                    conf,
                                    MessageType.LINK_CONFIG,
                                    getMyId())
                    );
                } catch (IOException e) {
                    log.error("Failed to send link config to agent " + target.getName() + "\n" + e.toString());
                }
            });
        }
    }
}


















