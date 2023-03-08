package fr.ddspstl.ports;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ConnectClient;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortConnectClient<T> extends AbstractOutboundPort implements ConnectClient<T>{


	private static final long serialVersionUID = 1L;

	public OutPortConnectClient( ComponentI owner) throws Exception {
		super(ConnectClient.class, owner);	
	}
	
	@Override
	public DataReader<T> getReader(TopicDescription<T> topic) throws Exception {
		return ((ConnectClient<T>)getConnector()).getReader(topic);
	}

	@Override
	public DataWriter<T> getWriter(Topic<T> topic) throws Exception {
		return ((ConnectClient<T>)getConnector()).getWriter(topic);
	}


}
