package fr.ddspstl.interfaces;

import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface ConnectInClient<T> extends OfferedCI {
	public Topic<T> connect(int domainID, String topic)throws Exception;

	public String getReaderURI()throws Exception;

	public String getWriterURI()throws Exception;

	public void disconnectClient()throws Exception;
}
