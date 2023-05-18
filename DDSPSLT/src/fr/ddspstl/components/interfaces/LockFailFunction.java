package fr.ddspstl.components.interfaces;

import org.omg.dds.topic.TopicDescription;

public interface LockFailFunction<T> {
	public void lockFailFunction(TopicDescription<T> topic,String id) throws Exception;
}
