package fr.ddspstl.ports;

import java.util.Set;

import org.omg.dds.topic.Topic;

import fr.ddspstl.connectors.ConnectorClient;
import fr.ddspstl.interfaces.ConnectOutClient;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortConnectClient extends AbstractOutboundPort implements ConnectOutClient{


	private static final long serialVersionUID = 1L;

	public OutPortConnectClient( ComponentI owner) throws Exception {
		super(ConnectOutClient.class, owner);
		
	}

	@Override
	public Set<Topic<Object>> connect() throws Exception {
		return ((ConnectorClient)getConnector()).connect();
	}

	@Override
	public String getReaderURI() throws Exception{
		return ((ConnectorClient)getConnector()).getReaderURI();
	}

	@Override
	public String getWriterURI() throws Exception{
		return ((ConnectorClient)getConnector()).getWriterURI();
	}

	@Override
	public void disconnectClient() throws Exception{
		((ConnectorClient)getConnector()).disconnectClient();
		
	}


}
