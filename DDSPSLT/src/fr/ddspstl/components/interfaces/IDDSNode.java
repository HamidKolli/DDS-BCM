package fr.ddspstl.components.interfaces;

import org.omg.dds.sub.Sample.Iterator;

import fr.ddspstl.exceptions.DDSTopicNotFoundException;

public interface IDDSNode {
	

	public int getDomainId();

	public void disconnectClient(String dataReader, String dataWriter);

	public String getDataReader(String topic) throws DDSTopicNotFoundException;

	public Iterator<?> read(String reader) throws DDSTopicNotFoundException;

	public String getDataWriter(String topic) throws DDSTopicNotFoundException;

	public <T> void write(String writer, T data) throws Exception;

	public <T> void propager(T newObject, String topicName, String id) throws Exception;
}
