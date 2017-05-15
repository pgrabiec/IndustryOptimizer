package examples;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Sensor extends Agent {
	private examples.SensorGui myGui;
	private String processName;
	private int phase = 0;

	@Override
    protected void setup() {
		myGui = new examples.SensorGui(this);
		this.processName = this.getArguments()[0].toString();
        myGui.display();

		addBehaviour(new getMessages());
    }

	protected void takeDown() { //opcjonalnie
		sendFarewell();
		myGui = null;
	}

	public void sendDataMessage(final String name, final String value, final String unit){
		sendMessage("DATA_" + name + ":" + value + ":" + unit);
	}

	public void sendNotice(){
		sendMessage(getSensorNotice());
		if(phase == 3) {
			phase = 4;
			myGui.update(phase);
		}
	}

	public void sendFarewell(){
		sendMessage("EXIT_" + this.getName());
	}

	private void sendMessage(final String message) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
		        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		        msg.addReceiver(new AID(processName, AID.ISLOCALNAME));
		        msg.setLanguage("Polish");
		        msg.setContent(message);
		        send(msg);
			}
		} );
	}

	private String getSensorNotice(){
		if(phase == 1){
			return "PR_READY";
		}
		else if(phase == 2){
			return "PR_STOP";
		}
		else if(phase == 3){
			return  "PR_FINALIZE";
		}
		return "ERROR";
	}

	public class getMessages extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				String notice = msg.getContent();
				if(notice.split("_")[0].equals("PR")){

					if(notice.split("_")[1].equals("INIT")){
						phase = 1;
					}
					else if(notice.split("_")[1].equals("START")){
						phase = 2;
					}
					else if(notice.split("_")[1].equals("STOP")){
						phase = 3;
					}
					myGui.update(phase);
				}
			}
		}
	}
}
