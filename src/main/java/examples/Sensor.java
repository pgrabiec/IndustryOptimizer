package Examples;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Sensor extends Agent {
	private SensorGui myGui;

	@Override
    protected void setup() {
        //System.out.println("Hello World! My name is Brutus");
        myGui = new SensorGui(this);
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
