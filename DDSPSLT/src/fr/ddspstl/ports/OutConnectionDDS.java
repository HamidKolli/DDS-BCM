package fr.ddspstl.ports;

import fr.ddspstl.connectors.ConnectorConnectionDDS;
import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutConnectionDDS extends AbstractOutboundPort implements ConnectDDSNode{

	private static final long serialVersionUID = 1L;

	public OutConnectionDDS( ComponentI owner) throws Exception {
		super(ConnectDDSNode.class, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String connect(String uri,String uriPropagation, int domainID) throws Exception {
		return ((ConnectorConnectionDDS)getConnector()).connect(uri,uriPropagation, domainID);
		
	}

	@Override
	public void disconnect(String uri) throws Exception {
		((ConnectorConnectionDDS)getConnector()).disconnect(uri);
		
	}

}
