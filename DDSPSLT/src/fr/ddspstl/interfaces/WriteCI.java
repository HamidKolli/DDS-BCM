package fr.ddspstl.interfaces;

import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : le type de la donnée
 *
 * Interface Implantée par un ConnectorWrite
 */
public interface WriteCI extends RequiredCI,OfferedCI{
	/**
	 * methode write : effectue un write dans un topic
	 * 
	 * @param topic : le topic dans lequel ecrire
	 * @param data : la donnée à écrire
	 * @throws Exception
	 */
	public <T> void write(Topic<T> topic,T data) throws Exception ;
}
