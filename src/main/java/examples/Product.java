package Examples;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Product extends Agent {

	protected void setup() {
		addBehaviour(new ExampleBehaviour());
	}
	protected void takeDown() { //opcjonalnie
		// operacje wykonywane bezpo�rednio przed usuni�ciem agenta
	}
	
	public class ExampleBehaviour extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				System.out.println(msg.getContent());
			}
		}
	}
}
