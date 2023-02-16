package fr.ddspstl.interfaces;

import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ConnectOutClient<T> extends RequiredCI {
	public Topic<T> connect(int domainID, String topic) throws Exception;
	public String getReaderURI() throws Exception;
	public String getWriterURI() throws Exception;
	public void disconnectClient() throws Exception;
}
