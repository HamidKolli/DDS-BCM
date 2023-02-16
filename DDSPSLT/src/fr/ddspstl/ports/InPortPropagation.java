package fr.ddspstl.ports;



import org.omg.dds.topic.Topic;

import fr.ddspstl.components.DDSNode;
import fr.ddspstl.interfaces.Propagation;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortPropagation extends AbstractInboundPort implements Propagation{

	private static final long serialVersionUID = 1L;

	public InPortPropagation(String uri,  ComponentI owner)
			throws Exception {
		super(uri, Propagation.class, owner);
	}

	@Override
	public <T> void propager(T newObject, Topic<T> topic, String id) throws Exception {
		getOwner().runTask(new AbstractComponent.AbstractTask() {
			
			@Override
			public void run() {
				try {
					((DDSNode)getOwner()).propager(newObject,topic,id);
				} catch (Exception e) {
				}
				
			}
		});
	}
	
	

}
