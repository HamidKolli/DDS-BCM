package fr.ddspstl.components;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.Publisher;
import org.omg.dds.sub.Subscriber;

import fr.sorbonne_u.components.AbstractComponent;

public abstract class ClientComponent extends AbstractComponent {

	protected String pluginURI;
	protected DomainParticipant domainParticipant;
	protected Publisher publisher;
	protected Subscriber subscriber;
	protected String uriDDSNode;

	protected ClientComponent(int nbThreads, int nbSchedulableThreads,String uriDDSNode,
			DomainParticipant domainParticipant) throws Exception {
		super(nbThreads, nbSchedulableThreads);
		this.domainParticipant = domainParticipant;
		this.publisher = domainParticipant.createPublisher();
		this.subscriber = domainParticipant.createSubscriber();
		this.uriDDSNode = uriDDSNode;
	}



	@Override
	public void execute() throws Exception {

		super.execute();
	}

}
