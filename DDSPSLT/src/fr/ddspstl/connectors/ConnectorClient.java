package fr.ddspstl.connectors;

import fr.ddspstl.interfaces.ConnectInClient;
import fr.ddspstl.interfaces.ConnectOutClient;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorClient extends AbstractConnector implements ConnectOutClient {


	@Override
	public String getReaderURI() throws Exception{
		return ((ConnectInClient)this.offering).getReaderURI();
	}

	@Override
	public String getWriterURI() throws Exception{
		return ((ConnectInClient)this.offering).getWriterURI();
	}

	@Override
	public void disconnectClient(String dataReader, String dataWriter) throws Exception{
		((ConnectInClient)this.offering).disconnectClient(dataReader, dataWriter);
	}

}
