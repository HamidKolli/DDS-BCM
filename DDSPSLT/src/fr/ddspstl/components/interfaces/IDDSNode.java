package fr.ddspstl.components.interfaces;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

public interface IDDSNode<T> {


	public void propager(T newObject, TopicDescription<T> topic, String id, Time modifiableTime) throws Exception;

	public Iterator<T> consommer(TopicDescription<T> topic, String id, boolean b) throws Exception;

	public Iterator<T> read(TopicDescription<T> topic) throws Exception;

}
