package fr.ddspstl.interfaces;

import org.omg.dds.core.Time;
import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Propagation<T> extends OfferedCI, RequiredCI{
	public void propager(T newObject, Topic<T> topic,String id,Time time) throws Exception;
}
