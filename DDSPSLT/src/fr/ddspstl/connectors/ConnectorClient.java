package fr.ddspstl.connectors;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ConnectClient;

import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorClient<T> extends AbstractConnector implements ConnectClient<T> {

	public DataReader<T> getReader(TopicDescription<T> topic) throws Exception {
		return ((ConnectClient<T>)this.offering).getReader(topic);
	}

	public DataWriter<T> getWriter(Topic<T> topic) throws Exception {
		return ((ConnectClient<T>)this.offering).getWriter(topic);
	}


	

}
