package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.connectors.ConnectorRead;
import fr.ddspstl.interfaces.ReadCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <TYPE> : type de la donnée
 *
 * Classe OutPortRead
 */
public class OutPortRead<TYPE> extends AbstractOutboundPort implements ReadCI<TYPE> {

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
	@SuppressWarnings("unchecked")
	public  Iterator<TYPE> read(TopicDescription<TYPE> topic) throws Exception {
		return ((ConnectorRead<TYPE>)getConnector()).read(topic);
	}


	/**
	 * @see fr.ddspstl.interfaces.ReadCI#take(TopicDescription)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<TYPE> take(TopicDescription<TYPE> topic) throws Exception {
		return ((ConnectorRead<TYPE>)getConnector()).take(topic);
	}



}
