package fr.ddspstl.connectors;

import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorConnectionDDS extends AbstractConnector implements ConnectDDSNode{

	@Override
	public void connect(String uri, int domainID)throws Exception {
		((ConnectDDSNode)this.offering).connect(uri, domainID);
		
	}

	@Override
	public void disconnect(String uri) throws Exception {
		((ConnectDDSNode)this.offering).disconnect(uri);
	}

}
