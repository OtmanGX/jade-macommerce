package sma.agents;

import entities.Produit;
import interfaceClient.MainFrame;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.ControllerException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsommateurAgent extends GuiAgent{
    
    private MainFrame mainFrame;
    List<AID> services;
    
    public List<AID> chercherServices(Agent agent,String type){
        List<AID> services=new ArrayList<>();
        DFAgentDescription agentDescription=new DFAgentDescription();
        ServiceDescription serviceDescription=new ServiceDescription();
        serviceDescription.setType(type);
        agentDescription.addServices(serviceDescription);
        try {
        DFAgentDescription[] descriptions=DFService.search(agent, agentDescription);
        for(DFAgentDescription dfad:descriptions){
        services.add(dfad.getName());
        }
        } catch (FIPAException e) {
        e.printStackTrace();
        }
        return services;
        }

	@Override
	protected void setup() {
		System.out.println("Création et l'initialisation de l'agent "+this.getAID().getName());
                /* Create and display the form */
                ConsommateurAgent agent = this;
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        mainFrame = new MainFrame();
                        mainFrame.setVisible(true);
                        mainFrame.setConsommateurAgent(agent);
                    }
                });
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
                addBehaviour(parallelBehaviour);
                parallelBehaviour.addSubBehaviour(new CyclicBehaviour(){
			@Override
			public void action() {
				MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchOntology("client"));
				ACLMessage aclMessage = receive(messageTemplate);
				MessageTemplate messageTemplate1 = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM), MessageTemplate.MatchOntology("client"))
                                        .or(MessageTemplate.MatchPerformative(ACLMessage.FAILURE), MessageTemplate.MatchOntology("client"));
				ACLMessage aclMessage1 = receive(messageTemplate1);
				if(aclMessage!=null){
                                    try {
                                        ArrayList<Produit> produit = (ArrayList<Produit>)aclMessage.getContentObject();
                                        mainFrame.setProduitItems(produit);
                                    } catch (UnreadableException ex) {
                                        Logger.getLogger(ConsommateurAgent.class.getName()).log(Level.SEVERE, null, ex);
                                    }
//					mainFrame.showMessage("Nom :"+nomp+" Prix :"+prixp+" Taille :"+taillep+" Couleur :"+couleurp);
					
				}else if(aclMessage1!=null){
                                        mainFrame.payementResult(
                                                aclMessage1.getPerformative() == ACLMessage.CONFIRM);
				}
					else{
					block();
				}
			}
    	 });
         parallelBehaviour.addSubBehaviour(new OneShotBehaviour(this) {
             @Override
             public void action() {
                 services = chercherServices(agent, "ecommerce-vendeur");
             }
         });
	}
	
	@Override
	protected void beforeMove() {
		try {
			System.out.println("Avant migration de l'agent "+this.getAID().getName());
			System.out.println("de "+this.getContainerController().getContainerName());
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void afterMove() {
		try {
			System.out.println("Apres migration de l'agent "+this.getAID().getName());
			System.out.println("vers "+this.getContainerController().getContainerName());
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void takeDown() {
		System.out.println("L'agent "+this.getAID().getName()+" va mourir");
	}

	@Override
	public void onGuiEvent(GuiEvent ev) {
		switch(ev.getType()){
		case 1:
			String produit = ev.getParameter(0).toString();
			ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
                        for(AID aid:services){
                            aclMessage.addReceiver(aid);
                        }
			aclMessage.setContent(produit);
			aclMessage.setOntology("client");
			send(aclMessage);
			break;
		case 2:
			String id_produit = ev.getParameter(0).toString();
			ACLMessage aclMessage1 = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			aclMessage1.addReceiver(new AID("vendeur1",AID.ISLOCALNAME));
			aclMessage1.setContent("acheter");
			aclMessage1.setOntology("client");
			aclMessage1.addUserDefinedParameter("id_produit",id_produit);
			send(aclMessage1);
			break;
		}
	}
}
