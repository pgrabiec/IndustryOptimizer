package edu.agh.io.industryOptimizer.gui.linking;

import edu.agh.io.industryOptimizer.agents.AgentType;
import edu.agh.io.industryOptimizer.agents.Agents;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import javax.swing.*;
import java.awt.*;

public class AgentsListCellRenderer implements ListCellRenderer<jade.domain.FIPAAgentManagement.DFAgentDescription> {

    @Override
    public Component getListCellRendererComponent(JList<? extends DFAgentDescription> list, DFAgentDescription value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel container = new JPanel(new GridBagLayout());

        String address = value.getName().getName();

        AgentType type = Agents.agentType(value);

        String service = type != null ? type.toString() : "<unknown_service>";

        addComponentGridBag(container, new JLabel(service), 0, 0, 1, 1);
        addComponentGridBag(container, new JLabel(" | "), 1, 0, 1, 1);
        addComponentGridBag(container, new JLabel(address), 2, 0, 1, 1);

        if (isSelected) {
            int b = 170;
            container.setBackground(new Color(b, b, b));
        }

        return container;
    }

    private void addComponentGridBag(JComponent container, JComponent component, int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.insets = new Insets(3, 3, 3, 3);
        container.add(component, c);
    }
}
