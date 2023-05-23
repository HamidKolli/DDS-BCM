package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.WriteCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : type de la donnée
 *
 * Classe OutPortWrite
 */
public class OutPortWrite<T> extends AbstractOutboundPort implements WriteCI<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param owner : l'owner du port
	 * @throws Exception
	 */
	public OutPortWrite( ComponentI owner) throws Exception {
		super(WriteCI.class, owner);
	}

	/**
	 * @see fr.ddspstl.interfaces.WriteCI#write(Topic, Object)
	 */
	@SuppressWarnings("unchecked")
	public  void write(Topic<T> topic, T data) throws Exception {
		((WriteCI<T>)getConnector()).write(topic,data);
		
	}

}
