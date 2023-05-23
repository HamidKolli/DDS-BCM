package fr.ddspstl.connectors;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.WriteCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 * @param <T> : le type de la donnée
 * 
 *            Classe Connecteur pour les opérations de Write
 */
public class ConnectorWrite extends AbstractConnector implements WriteCI {

	/**
	 * 
	 * @see fr.ddspstl.interfaces.WriteCI#write(Topic, Object)
	 */
	public <T> void write(Topic<T> topic, T data) throws Exception {
		((WriteCI) this.offering).write(topic, data);

	}

}
