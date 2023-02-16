package fr.ddspstl.connectors;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorPropagation extends AbstractConnector implements Propagation{

	@Override
	public <T> void propager(T newObject, Topic<T> topic, String id) throws Exception {
		((Propagation)this.offering).propager(newObject, topic, id);
		
	}

}
