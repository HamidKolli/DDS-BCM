package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortPropagation<T> extends AbstractOutboundPort implements Propagation<T>{

	private static final long serialVersionUID = 1L;

	public OutPortPropagation( ComponentI owner) throws Exception {
		super(Propagation.class, owner);
	}

	public void propager(T newObject, Topic<T> topic, String id) throws Exception {
		((Propagation)getConnector()).propager(newObject, topic, id);
	}

	

	
}
