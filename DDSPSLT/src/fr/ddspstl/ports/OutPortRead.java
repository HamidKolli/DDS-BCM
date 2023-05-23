package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ReadCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortRead extends AbstractOutboundPort implements ReadCI {

	private static final long serialVersionUID = 1L;

	public OutPortRead( ComponentI owner) throws Exception {
		super(ReadCI.class, owner);
	}


	public <T> Iterator<T> read(TopicDescription<T> topic) throws Exception {
		return ((ReadCI)getConnector()).read(topic);
	}


	@Override
	public <T> Iterator<T> take(TopicDescription<T> topic) throws Exception {
		return ((ReadCI)getConnector()).take(topic);
	}



}
