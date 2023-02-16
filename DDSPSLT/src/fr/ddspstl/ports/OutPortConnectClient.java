package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.connectors.ConnectorClient;
import fr.ddspstl.interfaces.ConnectOutClient;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortConnectClient<T> extends AbstractOutboundPort implements ConnectOutClient<T>{


	private static final long serialVersionUID = 1L;

	public OutPortConnectClient( ComponentI owner) throws Exception {
		super(ConnectOutClient.class, owner);
		
	}

	@Override
	public Topic<T> connect(int domainID, String topic) throws Exception {
		return ((ConnectorClient<T>)getConnector()).connect(domainID,topic);
	}

	@Override
	public String getReaderURI() throws Exception{
		return ((ConnectorClient<T>)getConnector()).getReaderURI();
	}

	@Override
	public String getWriterURI() throws Exception{
		return ((ConnectorClient<T>)getConnector()).getWriterURI();
	}

	@Override
	public void disconnectClient() throws Exception{
		((ConnectorClient<T>)getConnector()).disconnectClient();
		
	}


}
