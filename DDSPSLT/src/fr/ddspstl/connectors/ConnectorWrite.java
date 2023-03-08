package fr.ddspstl.connectors;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.WriteCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorWrite<T> extends AbstractConnector implements WriteCI<T> {

	@SuppressWarnings("unchecked")
	public  void write(Topic<T> topic, T data) throws Exception {
		((WriteCI<T>)this.offering).write(topic,data);
		
	}

}
