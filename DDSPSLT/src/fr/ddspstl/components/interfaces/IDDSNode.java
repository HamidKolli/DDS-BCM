package fr.ddspstl.components.interfaces;

import java.util.concurrent.TimeoutException;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.Topic;

import fr.ddspstl.exceptions.DDSTopicNotFoundException;

public interface IDDSNode {
	

	public int getDomainId();

	public void disconnectClient(String dataReader, String dataWriter);

	public String getDataReader(String topic) throws DDSTopicNotFoundException;

	public Iterator<?> read(String reader) throws DDSTopicNotFoundException;

	public String getDataWriter(String topic) throws DDSTopicNotFoundException;

	public <T> void write(String writer, T data) throws TimeoutException,DDSTopicNotFoundException;

	public <T> void propager(T newObject, Topic<T> topic, String id);
}
