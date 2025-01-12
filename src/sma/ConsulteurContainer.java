package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class ConsulteurContainer {

	public static void main(String[] args) {
		try {
			Runtime runtime = Runtime.instance();			
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			AgentContainer agentContainer = runtime.createAgentContainer(profile);
			AgentController agentController = agentContainer.
					createNewAgent("consulteur1", "jadeTest.agents.ConsulteurAgent", new Object[]{});
			agentController.start();
		} catch (ControllerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
