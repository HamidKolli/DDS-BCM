package fr.ddspstl.components;

import org.omg.dds.domain.DomainParticipant;

import fr.sorbonne_u.components.AbstractComponent;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 * Composant représentant un Client
 */
public abstract class ClientComponent extends AbstractComponent {

	protected DomainParticipant domainParticipant;
	protected String uriDDSNode;
	protected String uriReader;
	protected String uriWriter;
	
	/**
	 * Constructeur
	 * 
	 * @param nbThreads : nb de threads
	 * @param nbSchedulableThreads : nb de chedulableThreads
	 * @param uriDDSNode : l'URI du noeud DDS auquel il est rattaché
	 * @param domainParticipant : Le domainParticipant
	 * @throws Exception
	 */
	protected ClientComponent(int nbThreads, int nbSchedulableThreads,String uriDDSNode,
			DomainParticipant domainParticipant) throws Exception {
		super(nbThreads, nbSchedulableThreads);
		this.domainParticipant = domainParticipant;
		this.uriDDSNode = uriDDSNode;
	}



}
