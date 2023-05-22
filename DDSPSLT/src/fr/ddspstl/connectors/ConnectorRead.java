package fr.ddspstl.connectors;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ReadCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 * @param <TYPE> : le type de la donnée
 * 
 * Classe Connecteur pour un read
 */
public class ConnectorRead<TYPE> extends AbstractConnector implements ReadCI<TYPE> {


	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadCI#read(TopicDescription)
	 */
	@SuppressWarnings("unchecked")
	public  Iterator<TYPE> read(TopicDescription<TYPE> topic) throws Exception {
		return ((ReadCI<TYPE> )this.offering).read(topic);

	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadCI#take(TopicDescription)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<TYPE> take(TopicDescription<TYPE> topic) throws Exception {
		return ((ReadCI<TYPE>)this.offering).take(topic);
	}

}
