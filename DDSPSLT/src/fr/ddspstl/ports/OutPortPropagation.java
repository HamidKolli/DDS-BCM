package fr.ddspstl.ports;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortPropagation extends AbstractOutboundPort implements Propagation {

	private static final long serialVersionUID = 1L;

	public OutPortPropagation(ComponentI owner) throws Exception {
		super(Propagation.class, owner);
	}

	public <T> void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		((Propagation) getConnector()).propager(newObject, topic, id, time);
	}

	@Override
	public <T> Iterator<T> consommer(TopicDescription<T> topic, String id, boolean isFirst) throws Exception {
		return ((Propagation) getConnector()).consommer(topic, id, isFirst);
	}

}
