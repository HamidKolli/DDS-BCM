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
 * Classe Connecteur pour les opérations de Write
 */
public class ConnectorWrite<T> extends AbstractConnector implements WriteCI<T> {

	/**
	 * 
	 * @see fr.ddspstl.interfaces.WriteCI#write(Topic, Object)
	 */
	@SuppressWarnings("unchecked")
	public  void write(Topic<T> topic, T data) throws Exception {
		((WriteCI<T>)this.offering).write(topic,data);
		
	}

}
