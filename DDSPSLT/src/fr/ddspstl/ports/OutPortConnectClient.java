package fr.ddspstl.ports;

import fr.ddspstl.interfaces.ConnectClient;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortConnectClient extends AbstractOutboundPort implements ConnectClient{


	private static final long serialVersionUID = 1L;

	public OutPortConnectClient( ComponentI owner) throws Exception {
		super(ConnectClient.class, owner);	
	}
	

	@Override
	public String getReaderURI() throws Exception {
		return ((ConnectClient)getConnector()).getReaderURI();
	}

	@Override
	public String getWriterURI() throws Exception {
		return ((ConnectClient)getConnector()).getWriterURI();
	}


}
