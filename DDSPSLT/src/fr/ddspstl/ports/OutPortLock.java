package fr.ddspstl.ports;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.PropagationLock;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortLock extends AbstractOutboundPort implements PropagationLock {

	private static final long serialVersionUID = 1L;

	public OutPortLock(ComponentI owner) throws Exception {
		super(PropagationLock.class, owner);
	}

	@Override
	public <T> boolean lock(TopicDescription<T> topic, String idPropagation, Time timestamp) throws Exception {
		return ((PropagationLock) getConnector()).lock(topic, idPropagation,timestamp);

	}

	@Override
	public <T> void unlock(TopicDescription<T> topic,String idPropagation,String idPropagationUnlock) throws Exception {
		((PropagationLock) getConnector()).unlock(topic,idPropagation,idPropagationUnlock);
	}

}
