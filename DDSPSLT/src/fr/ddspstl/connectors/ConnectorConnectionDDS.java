package fr.ddspstl.connectors;

import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorConnectionDDS extends AbstractConnector implements ConnectDDSNode{

	@Override
	public String connect(String uri,String uriPropagation, int domainID)throws Exception {
		return ((ConnectDDSNode)this.offering).connect(uri, uriPropagation,domainID);
		
	}

	@Override
	public void disconnect(String uri) throws Exception {
		((ConnectDDSNode)this.offering).disconnect(uri);
	}

}
