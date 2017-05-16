package edu.agh.io.industryOptimizer;

import jade.Boot;

public class Main {
    public static void main(String[] args) {
        String[] arguments = new String[] {
                "-gui",
                "Product1:edu.agh.io.industryOptimizer.agents.ProductionProcess"
        };

        Boot.main(arguments);
    }
}
