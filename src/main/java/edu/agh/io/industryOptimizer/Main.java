package edu.agh.io.industryOptimizer;

import jade.Boot;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        a((Iterable) new LinkedList<Integer>());
//        String[] arguments = new String[] {
//                "-gui",
//                "Product1:edu.agh.io.industryOptimizer.agents.ProductionProcess"
//        };
//
//        Boot.main(arguments);
    }

    private static void a(Object o) {
        System.out.println("1");
    }
    private static void a(Collection<Integer> o) {
        System.out.println("2");
    }

}
