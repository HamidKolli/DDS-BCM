package fr.ddspstl.ports;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.PropagationLock;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe OutPortLock
 */
public class OutPortLock extends AbstractOutboundPort implements PropagationLock {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param owner : l'owner du port
	 * @throws Exception
	 */
	public OutPortLock(ComponentI owner) throws Exception {
		super(PropagationLock.class, owner);
	}

	/**
	 * @see fr.ddspstl.interfaces.PropagationLock#lock(TopicDescription, String, Time)
	 */
	@Override
	public <T> boolean lock(TopicDescription<T> topic, String idPropagation, Time timestamp) throws Exception {
		return ((PropagationLock) getConnector()).lock(topic, idPropagation,timestamp);

	}

	/**
	 * @see fr.ddspstl.interfaces.PropagationLock#unlock(TopicDescription, String, String)
	 */
	@Override
	public <T> void unlock(TopicDescription<T> topic,String idPropagation,String idPropagationUnlock) throws Exception {
		((PropagationLock) getConnector()).unlock(topic,idPropagation,idPropagationUnlock);
	}

}