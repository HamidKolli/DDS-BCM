package fr.ddspstl.interfaces;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface PropagationLock extends OfferedCI, RequiredCI {
	
	public <T> boolean lock(TopicDescription<T> topic, String idPropagation, Time timestamp) throws Exception;

	public <T> void unlock(TopicDescription<T> topic, String idPropagation,String idPropagationUnlock) throws Exception;
}
