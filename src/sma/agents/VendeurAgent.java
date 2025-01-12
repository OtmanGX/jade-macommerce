package sma.agents;

import entities.Produit;
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
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import java.sql.SQLException;
import model.ProduitDAO;

public class VendeurAgent extends Agent{
	@Override
	protected void setup() {
		System.out.println("Création et l'initialisation de l'agent "+this.getAID().getName());
		
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
   	 addBehaviour(parallelBehaviour);
   	 parallelBehaviour.addSubBehaviour(new CyclicBehaviour(){
   		
			@Override
			public void action() {
				MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL), MessageTemplate.MatchOntology("client"));
				ACLMessage aclMessage = receive(messageTemplate);
				if(aclMessage!=null){
				 //traitement
					Produit produit=null;
					String id_produit = aclMessage.getUserDefinedParameter("id_produit");
					try {
						ProduitDAO produitDAO = new ProduitDAO();
						produit=produitDAO.GetProduitByID(Integer.parseInt(id_produit));
						System.out.println(id_produit);
					
					} catch (ClassNotFoundException | SQLException  e) {e.printStackTrace();}
					
					//send message
					ACLMessage reponse = aclMessage.createReply();
					try {
						reponse.addUserDefinedParameter("verifier", produit.getNom());
						reponse.setContent("transaction réussie");
                                                reponse.setPerformative(ACLMessage.CONFIRM);
					} catch (Exception e) {
						reponse.setContent("cette article n'existe pas désormais");
                                                reponse.setPerformative(ACLMessage.FAILURE);
					}
					reponse.setOntology("client");
					send(reponse);
				}else{
					block();
				}	
			}
   		 
   	 });
	}
	
	@Override
	protected void beforeMove() {
		try {
			System.out.println("Avant migration de l'agent "+this.getAID().getName());
			System.out.println("de "+this.getContainerController().getContainerName());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void afterMove() {
		try {
			System.out.println("Apres migration de l'agent "+this.getAID().getName());
			System.out.println("vers "+this.getContainerController().getContainerName());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void takeDown() {
		System.out.println("L'agent "+this.getAID().getName()+" va mourir");
                try {
                    DFService.deregister(this);
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
	}

}
