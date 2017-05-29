package edu.agh.io.industryOptimizer.agents;

import edu.agh.io.industryOptimizer.messaging.CallbacksUtility;
import edu.agh.io.industryOptimizer.messaging.CallbacksUtilityImpl;
import edu.agh.io.industryOptimizer.messaging.DefaultMessage;
import edu.agh.io.industryOptimizer.messaging.MessageType;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ProductionProcess extends Agent {
	private ArrayList<String> sensorsList;
	private String batchAgent;
	private CallbacksUtility utility = new CallbacksUtilityImpl();
	private ProductionProcessState state;
	private int agentMessagesCounter;

	protected void setup() {
		agentMessagesCounter = 0;
		batchAgent = "test";
		state = ProductionProcessState.WAITING;
		sensorsList = new ArrayList<String>();

		utility.addCallback(ProductionProcessState.WAITING, MessageType.PROCESS_INIT, message -> {
			sendToAllSensors(MessageType.PROCESS_INIT, null);
			state = ProductionProcessState.INITIALIZING;
		});
		utility.addCallback(ProductionProcessState.WAITING, MessageType.LINK_CONFIG, message -> {

		});
		utility.addCallback(ProductionProcessState.WAITING, MessageType.LINK_CONFIG, message -> {

		});
		utility.addCallback(ProductionProcessState.INITIALIZING, MessageType.PROCESS_READY, message -> {

		});
		utility.addCallback(ProductionProcessState.INITIALIZING, MessageType.PROCESS_DATA, message -> {
			//DATA
		});
		utility.addCallback(ProductionProcessState.INITIALIZING, MessageType.PROCESS_START, message -> {
			state = ProductionProcessState.EXECUTING;
		});
		utility.addCallback(ProductionProcessState.INITIALIZING, MessageType.BATCH_LAST, message -> {

		});
		utility.addCallback(ProductionProcessState.INITIALIZING, MessageType.BATCH_LAST, message -> {

		});
		utility.addCallback(ProductionProcessState.EXECUTING, MessageType.PROCESS_DATA, message -> {
			//DATA
		});
		utility.addCallback(ProductionProcessState.EXECUTING, MessageType.PROCESS_FINISHED, message -> {

		});
		utility.addCallback(ProductionProcessState.EXECUTING, MessageType.PROCESS_STOP, message -> {
			sendToAllSensors(MessageType.PROCESS_STOP, null);
			state = ProductionProcessState.FINALIZING;
		});
		utility.addCallback(ProductionProcessState.FINALIZING, MessageType.PROCESS_DATA, message -> {
			//DATA
		});
		utility.addCallback(ProductionProcessState.FINALIZING, MessageType.PROCESS_FINALIZE, message -> {
			agentMessagesCounter++;
			if(agentMessagesCounter == sensorsList.size()){
				agentMessagesCounter = 0;
				sendToAllSensors(MessageType.PROCESS_FINALIZE, null);
				state = ProductionProcessState.EXPANDING;
				System.out.println("Sending data to Product Batch Agent");
				sendMessage(batchAgent, MessageType.BATCH_PRODUCED, "!!!!!!!!!!!!!!!!!!!!!!TODO");
				state = ProductionProcessState.WAITING;
				checkLinkConfigs();
			}
		});
		utility.addCallback(ProductionProcessState.FINALIZING, MessageType.PROCESS_FORCE_FINALIZE, message -> {
			sendToAllSensors(MessageType.PROCESS_FINALIZE, null);
			state = ProductionProcessState.EXPANDING;
			System.out.println("Sending data to Product Batch Agent");
			sendMessage(batchAgent, MessageType.BATCH_PRODUCED, "!!!!!!!!!!!!!!!!!!!!!!TODO");
			state = ProductionProcessState.WAITING;
			checkLinkConfigs();
		});

		addBehaviour(new getMessages());

	}

	private void checkLinkConfigs() {

	}

	protected void takeDown() {

	}
	
	public class getMessages extends CyclicBehaviour {
		public void action() {
			ACLMessage mesg = myAgent.receive();
			if (mesg != null) {

			}
		}
	}

	private void sendToAllSensors(MessageType type, String content) {
		addBehaviour((new OneShotBehaviour() {
			@Override
			public void action() {
				for(String sensorName : sensorsList){
					sendMessage(sensorName, type, content);
				}
			}
		}));

	}

	private void sendMessage(String receiver, MessageType type, String content) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
				msg.setLanguage("Polish");
				try {
					msg.setContentObject(new DefaultMessage(type, content));
				} catch (IOException e) {
					e.printStackTrace();
				}
				send(msg);
			}
		} );
	}
}
