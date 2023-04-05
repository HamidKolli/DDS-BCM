package fr.ddspstl.components.interfaces;

import org.omg.dds.core.Time;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;

public interface IDDSNode<T> {

	public void connect() throws Exception ;
	
	public void connectPropagation(INodeAddress address) throws Exception ;

	public void connectBack(INodeAddress address) throws Exception ;

	public void disconnectBack(INodeAddress address) throws Exception ;

	public void disconnect(INodeAddress uri) throws Exception ;
	
	public void propagerIn(T newObject, TopicDescription<T> topic, String id,Time time) throws Exception ;
	
	public void propagerOut(T newObject, TopicDescription<T> topic, String id,Time time) throws Exception;
	
	public void propager(T newObject, TopicDescription<T> topic, String id, Time modifiableTime) throws Exception;

}
