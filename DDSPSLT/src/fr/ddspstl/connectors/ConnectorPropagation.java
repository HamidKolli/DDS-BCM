package fr.ddspstl.connectors;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorPropagation<T> extends AbstractConnector implements Propagation<T>{

	@SuppressWarnings("unchecked")
	@Override
	public void propager(T newObject, TopicDescription<T> topic, String id,Time time) throws Exception {
		((Propagation<T>)this.offering).propager(newObject, topic, id,time);
		
	}

}
