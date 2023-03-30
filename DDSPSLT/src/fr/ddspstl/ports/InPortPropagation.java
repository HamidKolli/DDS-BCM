package fr.ddspstl.ports;

import org.omg.dds.core.Time;
import org.omg.dds.topic.Topic;

import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortPropagation<T> extends AbstractInboundPort implements Propagation<T>{

	private static final long serialVersionUID = 1L;

	public InPortPropagation(ComponentI owner)
			throws Exception {
		super(Propagation.class, owner);
	}

	@Override
	public void propager(T newObject, Topic<T> topicName, String id,Time time) throws Exception {
		getOwner().runTask(new AbstractComponent.AbstractTask() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					((IDDSNode<T>)getOwner()).propagerIn(newObject,topicName,id,time);
				} catch (Exception e) {
				}
				
			}
		});
	}
	
	

}
