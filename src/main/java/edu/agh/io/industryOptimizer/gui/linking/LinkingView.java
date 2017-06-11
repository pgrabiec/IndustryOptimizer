package edu.agh.io.industryOptimizer.gui.linking;


import jade.domain.FIPAAgentManagement.DFAgentDescription;

import javax.swing.*;
import java.awt.*;

public class LinkingView extends JFrame {
    private final JPanel contentPane = new JPanel(new GridBagLayout());

    private final JLabel labelAgentsToLink = new JLabel("Select agents to link");
    private final JList<DFAgentDescription> agentsToLink = new JList<>();

    private final JLabel labelTargetAgents = new JLabel("Select agents to link to");
    private final JList<DFAgentDescription> targetAgents = new JList<>();

    private final JButton linkButton = new JButton("Link agents");
    private final JButton unlinkButton = new JButton("Unlink agents");

    private final JButton updateButton = new JButton("Update agents list");

    public LinkingView() throws HeadlessException {
        super("Link management");

        setContentPane(contentPane);

        JScrollPane agentsToLinkScrollPane = new JScrollPane(agentsToLink);
        JScrollPane targetAgentsScrollPane = new JScrollPane(targetAgents);

        addComponentGridBag(labelAgentsToLink, 0, 0, 1, 1);
        addComponentGridBag(labelTargetAgents, 1, 0, 1, 1);

        addComponentGridBag(agentsToLinkScrollPane, 0, 1, 1, 1);
        addComponentGridBag(targetAgentsScrollPane, 1, 1, 1, 1);

        addComponentGridBag(linkButton, 0, 2, 1, 1);
        addComponentGridBag(unlinkButton, 1, 2, 1, 1);

        addComponentGridBag(updateButton, 0, 3, 2, 1);


        agentsToLinkScrollPane.setPreferredSize(new Dimension(400, 400));
        targetAgentsScrollPane.setPreferredSize(new Dimension(400, 400));

        targetAgents.setCellRenderer(new AgentsListCellRenderer());
        agentsToLink.setCellRenderer(new AgentsListCellRenderer());

        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setEnabled(true);
    }

    private void addComponentGridBag(JComponent component, int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.insets = new Insets(3, 3, 3, 3);
        contentPane.add(component, c);
    }

    public JList<DFAgentDescription> getAgentsToLink() {
        return agentsToLink;
    }

    public JList<DFAgentDescription> getTargetAgents() {
        return targetAgents;
    }

    public JButton getLinkButton() {
        return linkButton;
    }

    public JButton getUnlinkButton() {
        return unlinkButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }
}
