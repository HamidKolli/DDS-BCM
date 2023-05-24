package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ReadCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe OutPortRead
 */
public class OutPortRead extends AbstractOutboundPort implements ReadCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param owner : l'owner du port
	 * @throws Exception
	 */
	public OutPortRead( ComponentI owner) throws Exception {
		super(ReadCI.class, owner);
	}


	/**
	 * @see fr.ddspstl.interfaces.ReadCI#read(TopicDescription)
	 */
	public <T> Iterator<T> read(TopicDescription<T> topic) throws Exception {
		return ((ReadCI)getConnector()).read(topic);
	}


	/**
	 * @see fr.ddspstl.interfaces.ReadCI#take(TopicDescription)
	 */
	@Override
	public <T> Iterator<T> take(TopicDescription<T> topic) throws Exception {
		return ((ReadCI)getConnector()).take(topic);
	}



}
