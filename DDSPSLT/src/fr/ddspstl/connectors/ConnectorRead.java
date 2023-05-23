package fr.ddspstl.connectors;

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
public class ConnectorRead extends AbstractConnector implements ReadCI {


	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadCI#read(TopicDescription)
	 */
	public <TYPE> Iterator<TYPE> read(TopicDescription<TYPE> topic) throws Exception {
		return ((ReadCI)this.offering).read(topic);

	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadCI#take(TopicDescription)
	 */
	@Override
	public <TYPE> Iterator<TYPE> take(TopicDescription<TYPE> topic) throws Exception {
		return ((ReadCI)this.offering).take(topic);
	}

}
