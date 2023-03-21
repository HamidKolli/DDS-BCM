package fr.ddspstl.connectors;

import fr.ddspstl.interfaces.ConnectClient;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorClient extends AbstractConnector implements ConnectClient {

	

	@Override
	public String getReaderURI() throws Exception {
		return ((ConnectClient)this.offering).getReaderURI();
	}

	@Override
	public String getWriterURI() throws Exception {
		return ((ConnectClient)this.offering).getWriterURI();
	}


	

}
