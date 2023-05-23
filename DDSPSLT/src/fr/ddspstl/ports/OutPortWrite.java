package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.WriteCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortWrite extends AbstractOutboundPort implements WriteCI {

	private static final long serialVersionUID = 1L;

	public OutPortWrite( ComponentI owner) throws Exception {
		super(WriteCI.class, owner);
	}

	
	public <T> void write(Topic<T> topic, T data) throws Exception {
		((WriteCI)getConnector()).write(topic,data);
		
	}

}
