package fr.ddspstl.ports;

import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutConnectionDDS extends AbstractOutboundPort implements ConnectDDSNode{

	private static final long serialVersionUID = 1L;

	public OutConnectionDDS( ComponentI owner) throws Exception {
		super(ConnectDDSNode.class, owner);
	}

	@Override
	public String connect(String uri,String uriPropagation) throws Exception {
		return ((ConnectDDSNode)getConnector()).connect(uri,uriPropagation);
	}

	@Override
	public void disconnect(String uri) throws Exception {
		((ConnectDDSNode)getConnector()).disconnect(uri);
	}

}
