package fr.ddspstl.DDS.subscribers.interfaces;

import org.omg.dds.topic.TopicDescription;

public interface Subscriber extends org.omg.dds.sub.Subscriber{
	public <TYPE> org.omg.dds.sub.DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic,String uriPortDDSNode) ;
}
