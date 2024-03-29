package fr.ddspstl.ports;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutConnectionDDS extends AbstractOutboundPort implements ConnectDDSNode{

	private static final long serialVersionUID = 1L;

	public OutConnectionDDS( ComponentI owner) throws Exception {
		super(ConnectDDSNode.class, owner);
	}



	@Override
	public void connect(INodeAddress nodeAddress) throws Exception {
		((ConnectDDSNode)getConnector()).connect(nodeAddress);

		
	}

	@Override
	public void connectPropagation(INodeAddress nodeAddress) throws Exception {
		((ConnectDDSNode)getConnector()).connectPropagation(nodeAddress);

		
	}

	@Override
	public void disconnect(INodeAddress nodeAddress) throws Exception {
		((ConnectDDSNode)getConnector()).disconnect(nodeAddress);
		
	}



}
