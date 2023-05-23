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
public class ConnectorPropagation extends AbstractConnector implements Propagation {

	
	
	/**
	 * 
	 * @see fr.ddspstl.interfaces.Propagation#propager(Object, TopicDescription, String, Time)
	 */
	@Override
	public <T> void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		((Propagation) this.offering).propager(newObject, topic, id, time);

	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.Propagation#consommer(TopicDescription, String, boolean)
	 */
	@Override
	public <T> Iterator<T> consommer(TopicDescription<T> topic, String id, boolean isFirst) throws Exception {
		return ((Propagation) this.offering).consommer(topic, id, isFirst);
	}

}
