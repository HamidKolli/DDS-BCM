package fr.ddspstl.connectors;

import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorPropagation extends AbstractConnector implements Propagation{

	@Override
	public <T> void propager(T newObject, String topic, String id) throws Exception {
		((Propagation)this.offering).propager(newObject, topic, id);
		
	}

}
