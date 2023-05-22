package fr.ddspstl.connectors;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.connectors.AbstractConnector;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe Connecteur pour une propagation
 */
public class ConnectorPropagation<T> extends AbstractConnector implements Propagation<T> {

	
	
	/**
	 * 
	 * @see fr.ddspstl.interfaces.Propagation#propager(Object, TopicDescription, String, Time)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		((Propagation<T>) this.offering).propager(newObject, topic, id, time);

	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.Propagation#consommer(TopicDescription, String, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> consommer(TopicDescription<T> topic, String id, boolean isFirst) throws Exception {
		return ((Propagation<T>) this.offering).consommer(topic, id, isFirst);
	}

}
