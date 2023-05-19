package fr.ddspstl.components.interfaces;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;

public interface IDDSNode<T> {

	public void connect() throws Exception ;
	
	public void connectPropagation(INodeAddress address) throws Exception ;

	public void connectBack(INodeAddress address) throws Exception ;

	public void disconnectBack(INodeAddress address) throws Exception ;

	public void disconnect(INodeAddress uri) throws Exception ;

	public void propager(T newObject, TopicDescription<T> topic, String id, Time modifiableTime) throws Exception;

	public Iterator<T> consommer(TopicDescription<T> topic, String id, boolean b) throws Exception;
	
	public Iterator<T> read(TopicDescription<T> topic) throws Exception;

	

}
