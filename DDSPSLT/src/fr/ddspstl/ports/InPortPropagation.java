package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.Propagation;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortPropagation<T> extends AbstractInboundPort implements Propagation<T>{

	private static final long serialVersionUID = 1L;

	public InPortPropagation(ComponentI owner,String pluginURI)
			throws Exception {
		super(Propagation.class, owner,pluginURI,null);
	}

	@Override
	public void propager(T newObject, Topic<T> topicName, String id) throws Exception {
		getOwner().runTask(new AbstractComponent.AbstractTask(getPluginURI()) {
			@Override
			public void run() {
				try {
					((ConnectionPlugin)getTaskProviderReference()).propagerIn(newObject,topicName,id);
				} catch (Exception e) {
				}
				
			}
		});
	}
	
	

}
