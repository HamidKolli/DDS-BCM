package fr.ddspstl.interfaces;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Propagation<T> extends OfferedCI, RequiredCI {
	public void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception;

	public Iterator<T> consommer(TopicDescription<T> topic, String id,boolean isFirst) throws Exception;
}
