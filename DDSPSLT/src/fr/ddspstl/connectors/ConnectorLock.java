package fr.ddspstl.connectors;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.PropagationLock;
import fr.sorbonne_u.components.connectors.AbstractConnector;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe s'occupant de lock et unlock pour la propagation
 */
public class ConnectorLock extends AbstractConnector implements PropagationLock {

	/**
	 * 
	 * @see fr.ddspstl.interfaces.PropagationLock#lock(TopicDescription, String, Time)
	 */
	@Override
	public <T> boolean lock(TopicDescription<T> topic, String idPropagation, Time timestamp) throws Exception {
		return ((PropagationLock) this.offering).lock(topic, idPropagation, timestamp);
	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.PropagationLock#unlock(TopicDescription, String, String)
	 */
	@Override
	public <T> void unlock(TopicDescription<T> topic, String idPropagation, String idPropagationUnlock)
			throws Exception {
		((PropagationLock) this.offering).unlock(topic, idPropagation, idPropagationUnlock);
	}

}
