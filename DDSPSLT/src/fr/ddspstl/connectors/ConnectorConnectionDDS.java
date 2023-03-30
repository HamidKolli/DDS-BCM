package fr.ddspstl.connectors;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorConnectionDDS extends AbstractConnector implements ConnectDDSNode{



	@Override
	public void connect(INodeAddress nodeAddress) throws Exception {
		((ConnectDDSNode)this.offering).connect(nodeAddress);
		
	}

	@Override
	public void connectPropagation(INodeAddress nodeAddress) throws Exception {
		((ConnectDDSNode)this.offering).connectPropagation(nodeAddress);
		
	}

	@Override
	public void disconnect(INodeAddress nodeAddress) throws Exception {
		((ConnectDDSNode)this.offering).disconnect(nodeAddress);
		
	}

}
