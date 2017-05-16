package edu.agh.io.industryOptimizer.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

public class ProductionProcess extends Agent {
	ArrayList<AgentController> sensorsList;
	private int SENSOR_AGENTS_NUMBER = 2;

	protected void setup() {
		ContainerController cc = getContainerController();
		sensorsList = new ArrayList<AgentController>();
		String[] args = new String[1];
		try {
			args[0] = this.getName().split("@")[0];
			for(int i = 1; i <= SENSOR_AGENTS_NUMBER; i++) {
				String agentName = "UserInterface" + i;
				sensorsList.add(cc.createNewAgent(agentName, "edu.agh.io.industryOptimizer.agents.UserInterface", args));
			}
			for(AgentController ac : sensorsList){
				ac.start();
			}
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
//		pressAnyKeyToContinue();
//		sendMessage("PR_INIT");
		addBehaviour(new getMessages());
	}
	protected void takeDown() { //opcjonalnie
		// operacje wykonywane bezpo�rednio przed usuni�ciem agenta
	}
	
	public class getMessages extends CyclicBehaviour {
		public void action() {
			ACLMessage mesg = myAgent.receive();
			if (mesg != null) {
				System.out.println("Received from: " + mesg.getSender().getName());
				String notice = mesg.getContent();
				if(notice.split("_")[0].equals("PR")){
                    if(notice.split("_")[1].equals("INIT")){
                        sendMessage("PR_INIT");
                        System.out.println("Process init");
                    }
					else if(notice.split("_")[1].equals("READY")){
						sendMessage("PR_START");
                        System.out.println("Process starting");
                    }
					else if(notice.split("_")[1].equals("STOP")){
						sendMessage("PR_STOP");
                        System.out.println("Process stop");
                    }
					else if(notice.split("_")[1].equals("FINALIZE")){
                        System.out.println("Process finalize\n__________________");
                        sendMessage("PR_FINALIZE");
//						for(AgentController ac : sensorsList){
//							try {
//								ac.kill();
//							} catch (StaleProxyException e) {
//								e.printStackTrace();
//							}
//						}
					}
				}
				else if(notice.split("_")[0].equals("DATA"))
					System.out.println(notice.split("_")[1]);
				else if(notice.split("_")[0].equals("EXIT")){
                    System.out.println("Process terminated");
//					for(AgentController ac : sensorsList){
//						try {
//							if(ac.getName().equals(notice.split("_")[1])){
//								sensorsList.remove(sensorsList.indexOf(ac));
//                                System.out.println("removed");
//							}
//						} catch (StaleProxyException e) {
//							e.printStackTrace();
//						}
//					}
				}
			}
		}
	}

	public void sendMessage(final String message) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				for(AgentController ac : sensorsList) {
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					try {
						msg.addReceiver(new AID(ac.getName().split("@")[0], AID.ISLOCALNAME));
					} catch (StaleProxyException e) {
						e.printStackTrace();
					}
					msg.setLanguage("Polish");
					msg.setContent(message);
					send(msg);
				}
			}
		} );
	}

	private void pressAnyKeyToContinue()
	{
		System.out.println("Nacisnij dowolny przycisk aby zainicjowac proces produkcyjny.");
		try
		{
			System.in.read();
		}
		catch(Exception e)
		{
			// Ignore
		}
	}
}
