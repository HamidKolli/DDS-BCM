package fr.ddspstl.connectors;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.ConnectInClient;
import fr.ddspstl.interfaces.ConnectOutClient;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorClient<T> extends AbstractConnector implements ConnectOutClient<T> {

	@Override
	public Topic<T> connect(int domainID, String topic) throws Exception{
		return ((ConnectInClient<T>)this.offering).connect(domainID, topic);
	}

	@Override
	public String getReaderURI() throws Exception{
		return ((ConnectInClient<T>)this.offering).getReaderURI();
	}

	@Override
	public String getWriterURI() throws Exception{
		return ((ConnectInClient<T>)this.offering).getWriterURI();
	}

	@Override
	public void disconnectClient() throws Exception{
		((ConnectInClient<T>)this.offering).disconnectClient();
	}

}
