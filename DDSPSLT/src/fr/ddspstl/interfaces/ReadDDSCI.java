package fr.ddspstl.interfaces;


import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ReadDDSCI<T> extends OfferedCI, RequiredCI {
	public void read(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception;

	public void take(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception;

	public void acceptResult(Iterator<T> result, String requestID) throws Exception;
}
