package fr.ddspstl.components.interfaces;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.exceptions.DDSTopicNotFoundException;

public interface IDDSNode<T> {


	public void disconnectClient(String dataReader, String dataWriter);

	public  Iterator<T> read(TopicDescription<T> reader) throws DDSTopicNotFoundException;

	public  void write(Topic<T> writer, T data) throws Exception;

	public  void propager(T newObject, Topic<T> topicName, String id) throws Exception;
}
