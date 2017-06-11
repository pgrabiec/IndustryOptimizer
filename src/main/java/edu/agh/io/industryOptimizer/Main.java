package edu.agh.io.industryOptimizer;

import edu.agh.io.industryOptimizer.launch.Launcher;
import jade.Boot;
import jade.wrapper.StaleProxyException;
import org.apache.log4j.BasicConfigurator;

public class Main {
    public static void main(String[] args) {
//        BasicConfigurator.configure();

        String[] arguments = new String[] {
                "-gui",
                "Product1:edu.agh.io.industryOptimizer.agents.impl.ProductionProcessImpl"
        };

        Boot.main(arguments);

        try {
            Launcher.launch();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
