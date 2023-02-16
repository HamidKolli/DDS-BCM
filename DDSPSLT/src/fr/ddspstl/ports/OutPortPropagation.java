package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.connectors.ConnectorPropagation;
import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortPropagation extends AbstractOutboundPort implements Propagation{

	private static final long serialVersionUID = 1L;

	public OutPortPropagation( ComponentI owner) throws Exception {
		super(Propagation.class, owner);
	}

	@Override
	public <T> void propager(T newObject, Topic<T> topic, String id) throws Exception {
		((ConnectorPropagation)getConnector()).propager(newObject, topic, id);
	}

}
