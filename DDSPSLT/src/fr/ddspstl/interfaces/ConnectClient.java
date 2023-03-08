package fr.ddspstl.interfaces;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ConnectClient<T> extends RequiredCI,OfferedCI {
	public DataReader<T> getReader(TopicDescription<T> topic) throws Exception;
	public DataWriter<T> getWriter(Topic<T> topic) throws Exception;
}
