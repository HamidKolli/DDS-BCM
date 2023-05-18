package fr.ddspstl.components;

import org.omg.dds.domain.DomainParticipant;

import fr.sorbonne_u.components.AbstractComponent;

public abstract class ClientComponent extends AbstractComponent {

	protected DomainParticipant domainParticipant;
	protected String uriDDSNode;
	protected String uriReader;
	protected String uriWriter;
	protected ClientComponent(int nbThreads, int nbSchedulableThreads,String uriDDSNode,
			DomainParticipant domainParticipant) throws Exception {
		super(nbThreads, nbSchedulableThreads);
		this.domainParticipant = domainParticipant;
		this.uriDDSNode = uriDDSNode;
	}



}
