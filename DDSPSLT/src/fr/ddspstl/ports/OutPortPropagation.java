package fr.ddspstl.ports;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : type de la donnée
 *
 * Classe OutPortPropagation
 */
public class OutPortPropagation<T> extends AbstractOutboundPort implements Propagation<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param owner : l'owner du port
	 * @throws Exception
	 */
	public OutPortPropagation(ComponentI owner) throws Exception {
		super(Propagation.class, owner);
	}

	/**
	 * @see fr.ddspstl.interfaces.Propagation#propager(Object, TopicDescription, String, Time)
	 */
	@SuppressWarnings("unchecked")
	public void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		((Propagation<T>) getConnector()).propager(newObject, topic, id, time);
	}

	/**
	 * @see fr.ddspstl.interfaces.Propagation#consommer(TopicDescription, String, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> consommer(TopicDescription<T> topic, String id, boolean isFirst) throws Exception {
		return ((Propagation<T>) getConnector()).consommer(topic, id, isFirst);
	}

}
