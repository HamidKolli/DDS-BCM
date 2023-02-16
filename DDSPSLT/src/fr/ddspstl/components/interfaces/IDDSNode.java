package fr.ddspstl.components.interfaces;

import java.util.concurrent.TimeoutException;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;

import fr.ddspstl.exceptions.DDSTopicNotFoundException;

public interface IDDSNode {
	public <T> Topic<T> connect(int domainID, String topicName) throws TimeoutException;

	public int getDomainId();

	public void disconnectClient();

	public <T> DataReader<T> getDataReader(Topic<T> topic) throws DDSTopicNotFoundException;

	public <T> T read(DataReader<T> reader);

	public <T> DataWriter<T> getDataWriter(Topic<T> topic);

	public <T> void write(DataWriter<T> reader, T data) throws TimeoutException;

	public <T> void propager(T newObject, Topic<T> topic, String id);
}
