package fr.ddspstl.interfaces;

import java.util.Set;

import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ConnectOutClient extends RequiredCI {
	public Set<Topic<Object>> connect() throws Exception;
	public String getReaderURI() throws Exception;
	public String getWriterURI() throws Exception;
	public void disconnectClient() throws Exception;
}
