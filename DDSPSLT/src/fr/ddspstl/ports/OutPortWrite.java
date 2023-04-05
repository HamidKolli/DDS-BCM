package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.WriteCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortWrite<T> extends AbstractOutboundPort implements WriteCI<T> {

	private static final long serialVersionUID = 1L;

	public OutPortWrite( ComponentI owner) throws Exception {
		super(WriteCI.class, owner);
	}

	
	@SuppressWarnings("unchecked")
	public  void write(Topic<T> topic, T data) throws Exception {
		((WriteCI<T>)getConnector()).write(topic,data);
		
	}

}
