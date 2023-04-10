package fr.ddspstl.connectors;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.PropagationLock;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorLock extends AbstractConnector implements PropagationLock {

	@Override
	public <T> void lock(TopicDescription<T> topic, String idPropagation,Time timestamp) throws Exception {
		((PropagationLock) this.offering).lock(topic, idPropagation,timestamp);
	}

	@Override
	public <T> void unlock(TopicDescription<T> topic) throws Exception {
		((PropagationLock) this.offering).unlock(topic);
	}

}
