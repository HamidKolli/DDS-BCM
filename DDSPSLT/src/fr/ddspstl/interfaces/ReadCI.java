package fr.ddspstl.interfaces;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <TYPE> : le type de la donnée
 *
 * Interface Implantée par un ConnectorRead
 */
public interface ReadCI extends RequiredCI,OfferedCI{
	/**
	 * methode read: effectue un read
	 * 
	 * @param topic : le topic dans lequel faire le read
	 * @return Iterator<?> : la donnée
	 * @throws Exception
	 */
	public <T>  Iterator<T> read(TopicDescription<T> topic)throws Exception ;
	/**
	 * methode take : effectue un take
	 * 
	 * @param topic : le topic dans lequel faire le take
	 * @return Iterator<?> : la donnée
	 * @throws Exception
	 */
	public <T> Iterator<T> take(TopicDescription<T> topic)throws Exception ;
}
