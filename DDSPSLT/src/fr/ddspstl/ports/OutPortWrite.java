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
 *
 * Classe OutPortWrite
 */
public class OutPortWrite extends AbstractOutboundPort implements WriteCI {

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
	public <T> void write(Topic<T> topic, T data) throws Exception {
		((WriteCI)getConnector()).write(topic,data);
		
	}

}
