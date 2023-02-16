package fr.ddspstl.interfaces;

import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Propagation extends OfferedCI, RequiredCI{
	public <T> void propager(T newObject, Topic<T> topic,String id) throws Exception;
}
