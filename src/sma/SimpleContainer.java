package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class SimpleContainer {

	public static void main(String[] args) {
		try {
			Runtime runtime = Runtime.instance();			
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			AgentContainer mainContainer = runtime.createAgentContainer(profile);
			mainContainer.start();
		} catch (ControllerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
