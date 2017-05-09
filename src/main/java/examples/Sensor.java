package examples;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Sensor extends Agent {
	private examples.SensorGui myGui;

	@Override
    protected void setup() {
        myGui = new examples.SensorGui(this);
        myGui.display();

    }
	
	public void sendMessage(final String name, final int value) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
		        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		        msg.addReceiver(new AID("Product1", AID.ISLOCALNAME));
		        msg.setLanguage("Polish");
		        msg.setContent(name + " " + value);
		        send(msg);
			}
		} );
	}
}
