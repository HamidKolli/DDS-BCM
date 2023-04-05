package fr.ddspstl.ports;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortPropagation<T> extends AbstractOutboundPort implements Propagation<T>{

	private static final long serialVersionUID = 1L;

	public OutPortPropagation( ComponentI owner) throws Exception {
		super(Propagation.class, owner);
	}

	@SuppressWarnings("unchecked")
	public void propager(T newObject, TopicDescription<T> topic, String id,Time time) throws Exception {
		((Propagation<T>)getConnector()).propager(newObject, topic, id,time);
	}

	

	
}
